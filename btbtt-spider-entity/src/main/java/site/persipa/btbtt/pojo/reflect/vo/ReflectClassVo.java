package site.persipa.btbtt.pojo.reflect.vo;

import lombok.Data;
import site.persipa.btbtt.enums.ReflectClassType;

/**
 * @author persipa
 */
@Data
public class ReflectClassVo {

    private String id;

    private String packageName;

    private String className;

    private ReflectClassType classType;

    private Boolean iterable;

}
