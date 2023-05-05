package site.persipa.btbtt.exception.reflect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;
import site.persipa.cloud.exception.PersipaBaseException;

/**
 * @author persipa
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProcessingException extends PersipaBaseException {

    public static ProcessingException expected(ProcessingExceptionEnum exceptionType) {
        return new ProcessingException(exceptionType);
    }

    public static ProcessingException expected(ProcessingExceptionEnum exceptionType, String description) {
        return new ProcessingException(exceptionType, description);
    }

    private ProcessingException(ProcessingExceptionEnum exceptionType) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.level = exceptionType.getLevel();
    }

    private ProcessingException(ProcessingExceptionEnum exceptionType, String description) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.level = exceptionType.getLevel();
        this.description = description;
    }

}
