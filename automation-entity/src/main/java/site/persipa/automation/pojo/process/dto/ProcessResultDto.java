package site.persipa.automation.pojo.process.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author persipa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResultDto {

    private String configId;

    private Boolean used;
}
