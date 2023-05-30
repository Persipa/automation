package site.persipa.automation.reflect.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.persipa.automation.enums.exception.ReflectExceptionEnum;
import site.persipa.automation.pojo.reflect.ReflectMethod;
import site.persipa.automation.pojo.reflect.ReflectMethodArg;
import site.persipa.automation.pojo.reflect.dto.ReflectMethodDto;
import site.persipa.automation.reflect.service.ReflectClassService;
import site.persipa.automation.reflect.service.ReflectMethodArgService;
import site.persipa.automation.reflect.service.ReflectMethodService;
import site.persipa.cloud.exception.PersipaCustomException;
import site.persipa.cloud.exception.PersipaRuntimeException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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

    public String add(ReflectMethodDto dto) {
        String classId = dto.getClassId();
        Class<?> clazz = reflectClassService.getClazz(classId);
        Assert.notNull(clazz, () -> new PersipaRuntimeException(ReflectExceptionEnum.REFLECT_CLASS_NOT_FOUND));

        Class<?>[] paramTypes = null;
        if (CollUtil.isNotEmpty(dto.getArgClassIds())) {
            List<Class<?>> argClassList = reflectClassService.getClazz(dto.getArgClassIds());
            paramTypes = argClassList.toArray(new Class[0]);
        }
        Method method;
        try {
            method = clazz.getDeclaredMethod(dto.getMethodName(), paramTypes);
        } catch (NoSuchMethodException e) {
            throw new PersipaRuntimeException(ReflectExceptionEnum.NO_SUCH_METHOD_EXCEPTION, dto.getMethodName());
        }
        ReflectMethod reflectMethod = new ReflectMethod();
        reflectMethod.setClassId(classId);
        reflectMethod.setClassName(clazz.getName());
        reflectMethod.setMethodName(method.getName());
        reflectMethod.setStaticMethod(Modifier.isStatic(method.getModifiers()));

        int argCount = paramTypes == null ? 0 : paramTypes.length;
        reflectMethod.setArgCount(argCount);
        reflectMethod.setReturnType(method.getReturnType().getName());

        reflectMethodService.save(reflectMethod);
        return reflectMethod.getId();
    }

    /**
     * 将方法id 转换为反射的方法
     * @param methodId 方法id
     * @return 某个可执行的方法
     * @throws PersipaCustomException 转换失败抛出异常
     */
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

    public Object invokeMethod(String methodId, Object... args) throws PersipaCustomException {
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
        try {
            Class<?> returnClass = Class.forName(returnType);
            if (!returnClass.isInstance(result)) {
                throw new PersipaCustomException(ReflectExceptionEnum.REFLECT_METHOD_RETURN_TYPE_INCORRECT);
            }
        } catch (ClassNotFoundException e) {
            throw new PersipaCustomException(ReflectExceptionEnum.CLASS_NOT_FOUND_EXCEPTION, returnType);
        }
        return result;
    }
}
