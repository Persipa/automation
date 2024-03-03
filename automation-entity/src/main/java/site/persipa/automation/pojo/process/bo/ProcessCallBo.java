package site.persipa.automation.pojo.process.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author persipa
 */
@Data
public class ProcessCallBo implements Serializable {

    @Serial
    private static final long serialVersionUID = -879513846567148338L;
    /**
     * 执行的配置id
     */
    private String configId;

    /**
     * 源系统代码
     */
    private String caller;
}
