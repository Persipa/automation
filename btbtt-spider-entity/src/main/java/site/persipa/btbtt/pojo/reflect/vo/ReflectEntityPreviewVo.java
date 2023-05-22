package site.persipa.btbtt.pojo.reflect.vo;

import lombok.Data;
import site.persipa.btbtt.enums.reflect.ReflectClassType;
import site.persipa.btbtt.enums.reflect.ReflectEntityConstructorType;

/**
 * @author persipa
 */
@Data
public class ReflectEntityPreviewVo {

    private String id;

    private String classId;

    private String packageName;

    private String className;

    private ReflectClassType classType;

    private String constructorId;

    private ReflectEntityConstructorType constructType;

    private Boolean constructSuccess;

    private Object entityValue;

}
