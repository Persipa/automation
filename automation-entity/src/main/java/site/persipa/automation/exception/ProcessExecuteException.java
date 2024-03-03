package site.persipa.automation.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;
import site.persipa.common.entity.exception.PersipaCustomException;

import java.io.Serial;

/**
 * @author persipa
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ProcessExecuteException extends PersipaCustomException {

    @Serial
    private static final long serialVersionUID = 123584156598253795L;

    public String detail() {
        return !StringUtils.hasLength(this.getDescription()) ? this.getMsg() : this.getMsg() + ":" + this.getDescription();
    }

}
