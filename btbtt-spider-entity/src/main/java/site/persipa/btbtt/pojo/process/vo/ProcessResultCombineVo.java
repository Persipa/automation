package site.persipa.btbtt.pojo.process.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author persipa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessResultCombineVo {

    private String configId;

    private List<String> resultList;
}
