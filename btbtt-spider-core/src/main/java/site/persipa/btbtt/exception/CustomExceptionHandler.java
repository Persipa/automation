package site.persipa.btbtt.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import site.persipa.cloud.exception.PersipaBaseException;
import site.persipa.cloud.pojo.model.Result;

/**
 * @author persipa
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler({PersipaBaseException.class})
    public Result<String> handle(PersipaBaseException exception) {
        return Result.exception(exception);
    }
}
