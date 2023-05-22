package site.persipa.btbtt.pojo.reflect.dto;

import lombok.Data;

import java.util.List;

/**
 * @author persipa
 */
@Data
public class ReflectEntityDto {

    private String id;

    private String classId;

    private String constructorId;

    private String entityValue;

    private Integer sort;

    private List<ReflectEntityDto> subEntities;

}
