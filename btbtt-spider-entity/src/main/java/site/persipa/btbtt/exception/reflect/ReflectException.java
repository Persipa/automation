package site.persipa.btbtt.exception.reflect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.cloud.exception.PersipaCustomException;

/**
 * @author persipa
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ReflectException extends PersipaCustomException {

    public static ReflectException expected(ProcessingExceptionEnum exceptionType) {
        return new ReflectException(exceptionType);
    }

    public static ReflectException expected(ProcessingExceptionEnum exceptionType, String description) {
        return new ReflectException(exceptionType, description);
    }

    private ReflectException(ProcessingExceptionEnum exceptionType) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.level = exceptionType.getLevel();
    }

    private ReflectException(ProcessingExceptionEnum exceptionType, String description) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.level = exceptionType.getLevel();
        this.description = description;
    }

}
