package site.persipa.btbtt.jsoup.manager;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.btbtt.exception.jsoup.ProcessingException;
import site.persipa.btbtt.jsoup.service.JsoupClassService;
import site.persipa.btbtt.jsoup.service.JsoupMethodArgService;
import site.persipa.btbtt.jsoup.service.JsoupMethodService;
import site.persipa.btbtt.pojo.jsoup.JsoupMethod;
import site.persipa.btbtt.pojo.jsoup.JsoupMethodArg;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author persipa
 */
@Component
public class JsoupMethodManager {

    @Autowired
    private JsoupClassService jsoupClassService;
    @Autowired
    private JsoupMethodService jsoupMethodService;
    @Autowired
    private JsoupMethodArgService jsoupMethodArgService;

    public Method parseMethod(String methodId) throws ProcessingException {
        JsoupMethod jsoupMethod = jsoupMethodService.getById(methodId);
        if (jsoupMethod == null) {
            return null;
        }
        String className = jsoupMethod.getClassName();
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ReflectiveOperationException e) {
            throw ProcessingException.expected(ProcessingExceptionEnum.CLASS_NOT_FOUND);
        }
        int argCount = jsoupMethod.getArgCount() != null ? jsoupMethod.getArgCount() : 0;

        // 无参方法
        if (argCount == 0) {
            return ReflectUtil.getMethod(clazz, jsoupMethod.getMethodName());
        }
        List<JsoupMethodArg> methodArgList = jsoupMethodArgService.list(Wrappers.lambdaQuery(JsoupMethodArg.class)
                .eq(JsoupMethodArg::getMethodId, jsoupMethod.getId())
                .orderByAsc(JsoupMethodArg::getSort));
        List<String> argClassIdList = methodArgList.stream()
                .map(JsoupMethodArg::getClassId)
                .collect(Collectors.toList());
        /// 验证参数数量
        if (argCount != argClassIdList.size()) {
            throw ProcessingException.expected(ProcessingExceptionEnum.METHOD_ARGS_COUNT_INCORRECT);
        }
        Class<?>[] argClassArr = new Class[argCount];
        for (int i = 0; i < jsoupMethod.getArgCount(); i++) {
            String argClassId = argClassIdList.get(i);
            Class<?> argClass = jsoupClassService.getClazz(argClassId);
            argClassArr[i] = argClass;
        }
        return ReflectUtil.getMethod(clazz, jsoupMethod.getMethodName(), argClassArr);
    }

    public Object invokeMethod(String methodId, Object... args) throws ProcessingException {
        JsoupMethod jsoupMethod = jsoupMethodService.getById(methodId);
        Method method = this.parseMethod(methodId);
        Boolean staticMethod = jsoupMethod.getStaticMethod();
        if (Boolean.TRUE.equals(staticMethod)) {
            return ReflectUtil.invokeStatic(method, args);
        }
        if (args == null || args.length == 0) {
            throw ProcessingException.expected(ProcessingExceptionEnum.METHOD_ARGS_COUNT_INCORRECT);
        } else {
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
}
