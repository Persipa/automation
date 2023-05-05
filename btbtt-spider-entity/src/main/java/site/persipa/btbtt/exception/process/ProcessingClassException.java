package site.persipa.btbtt.exception.process;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.persipa.btbtt.enums.exception.ProcessingClassExceptionEnum;
import site.persipa.cloud.exception.PersipaBaseException;

/**
 * @author persipa
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProcessingClassException extends PersipaBaseException {

    public static ProcessingClassException excepted(ProcessingClassExceptionEnum exceptionType) {
        return new ProcessingClassException(exceptionType);
    }

    public static ProcessingClassException excepted(ProcessingClassExceptionEnum exceptionType, String description) {
        return new ProcessingClassException(exceptionType, description);
    }

    private ProcessingClassException(ProcessingClassExceptionEnum exceptionType) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.level = exceptionType.getLevel();
    }

    private ProcessingClassException(ProcessingClassExceptionEnum exceptionType, String description) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.level = exceptionType.getLevel();
        this.description = description;
    }
}
