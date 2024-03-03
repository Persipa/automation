package site.persipa.automation.pojo.process.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.persipa.automation.enums.process.ProcessTypeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author persipa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessExecuteBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 4482151059657233259L;

    /**
     * 要执行的配置
     */
    private String configId;

    /**
     * 调用方式
     */
    private ProcessTypeEnum processType;

    /**
     * 结果票据
     */
    private String ticket;
}
