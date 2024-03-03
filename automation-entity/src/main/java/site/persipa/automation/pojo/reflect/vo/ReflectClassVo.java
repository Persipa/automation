package site.persipa.automation.pojo.reflect.vo;

import lombok.Data;
import site.persipa.automation.enums.reflect.ReflectClassTypeEnum;

/**
 * @author persipa
 */
@Data
public class ReflectClassVo {

    private String id;

    private String packageName;

    private String className;

    private ReflectClassTypeEnum classType;

    private Boolean iterable;

}
