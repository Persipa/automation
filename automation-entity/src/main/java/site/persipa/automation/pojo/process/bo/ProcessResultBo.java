package site.persipa.automation.pojo.process.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author persipa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResultBo {

    private boolean success;

    private Object result;

    private String message;


    public static ProcessResultBo success(Object result) {
        return new ProcessResultBo(true, result, null);
    }

    public static ProcessResultBo fail() {
        return new ProcessResultBo();
    }

    public static ProcessResultBo fail(String message) {
        return new ProcessResultBo(false, null, message);
    }
}
