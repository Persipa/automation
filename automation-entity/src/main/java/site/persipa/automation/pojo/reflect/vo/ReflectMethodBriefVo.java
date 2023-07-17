package site.persipa.automation.pojo.reflect.vo;

import lombok.Data;

/**
 * 反射方法基础信息 vo
 */
@Data
public class ReflectMethodBriefVo {

    /**
     * 方法id
     */
    private String id;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法
     */
    private String methodName;

}