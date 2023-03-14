package site.persipa.btbtt.exception.jsoup;

import lombok.Getter;
import site.persipa.btbtt.enums.exception.EntityConstrucExceptionTypeEnum;

/**
 * @author persipa
 */
@Getter
public class EntityConstructException extends RuntimeException {

    private final Integer code;

    private final String msg;

    private String description;

    public EntityConstructException(EntityConstrucExceptionTypeEnum exceptionType) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
    }

    public EntityConstructException(EntityConstrucExceptionTypeEnum exceptionType, String description) {
        this.code = exceptionType.getCode();
        this.msg = exceptionType.getMsg();
        this.description = description;
    }
}
