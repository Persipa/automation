package site.persipa.btbtt.exception.jsoup;

import lombok.Getter;
import site.persipa.btbtt.enums.exception.ProcessingExceptionEnum;

/**
 * @author persipa
 */
@Getter
public class EntityConstructException extends ProcessingException {

    public static EntityConstructException expected(ProcessingExceptionEnum exceptionType) {
        return new EntityConstructException(exceptionType);
    }

    public static EntityConstructException expected(ProcessingExceptionEnum exceptionType, String description) {
        return new EntityConstructException(exceptionType, description);
    }

    private EntityConstructException(ProcessingExceptionEnum exceptionType) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
    }

    private EntityConstructException(ProcessingExceptionEnum exceptionType, String description) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.description = description;
    }
}
