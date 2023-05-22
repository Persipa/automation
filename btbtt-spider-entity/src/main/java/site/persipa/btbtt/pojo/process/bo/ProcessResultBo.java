package site.persipa.btbtt.pojo.process.bo;

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


    public static ProcessResultBo success(Object result) {
        return new ProcessResultBo(true, result);
    }

    public static ProcessResultBo fail() {
        return new ProcessResultBo();
    }
}
