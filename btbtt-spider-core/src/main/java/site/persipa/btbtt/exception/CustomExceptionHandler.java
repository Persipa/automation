package site.persipa.btbtt.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import site.persipa.cloud.exception.PersipaCustomException;
import site.persipa.cloud.exception.PersipaRuntimeException;
import site.persipa.cloud.pojo.rest.model.Result;

import java.util.List;

/**
 * @author persipa
 */
@ControllerAdvice
public class CustomExceptionHandler {

    @ResponseBody
    @ExceptionHandler({PersipaCustomException.class})
    public Result<String> handle(PersipaCustomException exception) {
        return Result.exception(exception, exception.getMsg(), exception.getDescription());
    }

    @ResponseBody
    @ExceptionHandler({PersipaRuntimeException.class})
    public Result<String> handle(PersipaRuntimeException exception) {
        return Result.exception(exception, exception.getMsg(), exception.getDescription());
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<String> handle(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(fieldError -> sb.append(fieldError.getField())
                .append(": ")
                .append(fieldError.getDefaultMessage())
                .append(";"));
        return Result.success("参数验证失败" + sb);
        //        return Result.fail("参数验证失败", sb.toString());
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public Result<Void> handle(Exception exception) {
        exception.printStackTrace();
        return Result.fail();
    }
}
