package site.persipa.btbtt.reflect.manager;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
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
public class ReflectMethodManager {

    @Autowired
    private ReflectClassService reflectClassService;
    @Autowired
    private ReflectMethodService reflectMethodService;
    @Autowired
    private ReflectMethodArgService reflectMethodArgService;

    public Method parseMethod(String methodId) throws PersipaCustomException {
        ReflectMethod reflectMethod = reflectMethodService.getById(methodId);
        if (reflectMethod == null) {
            return null;
        }
        String className = reflectMethod.getClassName();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ReflectiveOperationException e) {
            throw new PersipaCustomException(ProcessingExceptionEnum.CLASS_NOT_FOUND, className);
        }
        int argCount = reflectMethod.getArgCount() != null ? reflectMethod.getArgCount() : 0;

        // 无参方法
        if (argCount == 0) {
            return ReflectUtil.getMethod(clazz, reflectMethod.getMethodName());
        }
        List<ReflectMethodArg> methodArgList = reflectMethodArgService.list(Wrappers.lambdaQuery(ReflectMethodArg.class)
                .eq(ReflectMethodArg::getMethodId, reflectMethod.getId())
                .orderByAsc(ReflectMethodArg::getSort));
        List<String> argClassIdList = methodArgList.stream()
                .map(ReflectMethodArg::getClassId)
                .collect(Collectors.toList());
        /// 验证参数数量
        Assert.equals(argCount, argClassIdList.size(), () -> new PersipaCustomException(ProcessingExceptionEnum.METHOD_ARGS_COUNT_INCORRECT));
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
        Boolean staticMethod = reflectMethod.getStaticMethod();
        if (Boolean.TRUE.equals(staticMethod)) {
            return ReflectUtil.invokeStatic(method, args);
        }
        Assert.notEmpty(args, () -> new PersipaCustomException(ProcessingExceptionEnum.METHOD_ARGS_COUNT_INCORRECT));
        Object obj = args[0];
        if (args.length > 1) {
            Object[] argArr = new Object[args.length - 1];
            for (int i = 0; i < args.length; i++) {
                if (i == 0) {
                    continue;
                }
                argArr[i - 1] = args[i];
            }
            return ReflectUtil.invoke(obj, method, argArr);
        } else {
            return ReflectUtil.invoke(obj, method);
        }
    }
}
