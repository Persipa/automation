package site.persipa.automation.pojo.process.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author persipa
 */
@Data
public class ProcessResultVo implements Serializable {

    private static final long serialVersionUID = 8425461818683948830L;

    private String id;

    private String configId;

    private String result;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
