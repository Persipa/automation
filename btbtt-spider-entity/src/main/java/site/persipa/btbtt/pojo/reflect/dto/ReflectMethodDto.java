package site.persipa.btbtt.pojo.reflect.dto;

import lombok.Data;

import java.util.List;

/**
 * @author persipa
 */
@Data
public class ReflectMethodDto {

    private String classId;

    private String methodName;

    private List<String> argClassIds;
}
