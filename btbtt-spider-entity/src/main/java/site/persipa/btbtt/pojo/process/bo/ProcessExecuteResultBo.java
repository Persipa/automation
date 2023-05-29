package site.persipa.btbtt.pojo.process.bo;

import lombok.Data;

/**
 * @author persipa
 */
@Data
public class ProcessExecuteResultBo {

    private String configId;

    private boolean executeCompleted;

    private boolean executeSuccess;

    private String message;

    private Integer resultCount;

    private Object result;

}
