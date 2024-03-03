package site.persipa.automation.pojo.reflect.vo;

import lombok.Data;
import site.persipa.automation.enums.reflect.ReflectClassTypeEnum;
import site.persipa.automation.enums.reflect.ReflectEntityConstructorType;

/**
 * @author persipa
 */
@Data
public class ReflectEntityPreviewVo {

    private String id;

    private String classId;

    private String packageName;

    private String className;

    private ReflectClassTypeEnum classType;

    private String constructorId;

    private ReflectEntityConstructorType constructType;

    private Boolean constructSuccess;

    private Object entityValue;

}
