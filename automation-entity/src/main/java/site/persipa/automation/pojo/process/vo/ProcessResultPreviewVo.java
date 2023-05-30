package site.persipa.automation.pojo.process.vo;

import lombok.Data;

/**
 * @author persipa
 */
@Data
public class ProcessResultPreviewVo {

    private String configId;

    private Boolean success;

    private String message;

    private Object result;

}
