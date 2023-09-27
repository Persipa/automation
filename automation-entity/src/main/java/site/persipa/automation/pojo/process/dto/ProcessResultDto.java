package site.persipa.automation.pojo.process.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author persipa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResultDto implements Serializable {

    private static final long serialVersionUID = 7373586670820095426L;

    private String configId;

    private String processId;

    private Boolean isNew;

    private Boolean used;
}
