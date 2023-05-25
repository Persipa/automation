package site.persipa.btbtt.reflect.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.exception.ReflectExceptionEnum;
import site.persipa.btbtt.pojo.reflect.ReflectMethod;
import site.persipa.btbtt.pojo.reflect.ReflectMethodArg;
import site.persipa.btbtt.reflect.service.ReflectClassService;
import site.persipa.btbtt.reflect.service.ReflectMethodArgService;
import site.persipa.btbtt.reflect.service.ReflectMethodService;
import site.persipa.cloud.exception.PersipaCustomException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
@RequiredArgsConstructor
public class ReflectMethodManager {

    private final ReflectClassService reflectClassService;

    private final ReflectMethodService reflectMethodService;

    private final ReflectMethodArgService reflectMethodArgService;

    private Method parseMethod(String methodId) throws PersipaCustomException {
        ReflectMethod reflectMethod = reflectMethodService.getById(methodId);
        if (reflectMethod == null) {
            return null;
        }
        String className = reflectMethod.getClassName();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ReflectiveOperationException e) {
            throw new PersipaCustomException(ReflectExceptionEnum.CLASS_NOT_FOUND_EXCEPTION, className);
        }
        int argCount = reflectMethod.getArgCount() != null ? reflectMethod.getArgCount() : 0;

        // 无参方法
        if (argCount == 0) {
            return ReflectUtil.getMethod(clazz, reflectMethod.getMethodName());
        }
        List<ReflectMethodArg> methodArgList = reflectMethodArgService.listByMethodId(reflectMethod.getId(), true);
        List<String> argClassIdList = methodArgList.stream()
                .map(ReflectMethodArg::getClassId)
                .collect(Collectors.toList());
        /// 验证参数数量
        Assert.equals(argCount, argClassIdList.size(), () -> new PersipaCustomException(ReflectExceptionEnum.REFLECT_METHOD_ARGS_COUNT_INCORRECT));
        Class<?>[] argClassArr = new Class[argCount];
        for (int i = 0; i < reflectMethod.getArgCount(); i++) {
            String argClassId = argClassIdList.get(i);
            Class<?> argClass = reflectClassService.getClazz(argClassId);
            argClassArr[i] = argClass;
        }
        return ReflectUtil.getMethod(clazz, reflectMethod.getMethodName(), argClassArr);
    }

    public Object invokeMethod(String methodId, Object... args) throws PersipaCustomException, ClassNotFoundException {
        ReflectMethod reflectMethod = reflectMethodService.getById(methodId);
        Method method = this.parseMethod(methodId);
        Assert.notNull(method, () -> new PersipaCustomException(ReflectExceptionEnum.REFLECT_METHOD_NOT_FOUND));
        Object result;
        if (Boolean.TRUE.equals(reflectMethod.getStaticMethod())) {
            result = ReflectUtil.invokeStatic(method, args);
        } else {
            // 对于非静态方法，args 第一位为方法的调用者 剩下的为方法的参数
            Assert.notEmpty(args, () -> new PersipaCustomException(ReflectExceptionEnum.REFLECT_METHOD_ARGS_COUNT_INCORRECT));
            // 如果args 长度大于1，说明方法有参数，需要将参数和调用实例分离
            if (args.length > 1) {
                Object[] argArr = new Object[args.length - 1];
                for (int i = 0; i < args.length; i++) {
                    if (i == 0) {
                        continue;
                    }
                    argArr[i - 1] = args[i];
                }
                result = ReflectUtil.invoke(args[0], method, argArr);
            } else {
                result = ReflectUtil.invoke(args[0], method);
            }
        }

        // 校验返回值
        String returnType = reflectMethod.getReturnType();
        Class<?> returnClass = Class.forName(returnType);
        if (!returnClass.isInstance(result)) {
            throw new PersipaCustomException(ReflectExceptionEnum.REFLECT_METHOD_RETURN_TYPE_INCORRECT);
        }
        return result;
    }
}
