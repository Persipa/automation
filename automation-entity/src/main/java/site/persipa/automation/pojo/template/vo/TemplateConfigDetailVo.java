package site.persipa.automation.pojo.template.vo;

import lombok.Data;

import java.util.List;

/**
 * 配置模板 vo
 *
 * @author persipa
 */
@Data
public class TemplateConfigDetailVo {

    /**
     * 配置模板id
     */
    private String id;

    /**
     * 配置模板名
     */
    private String templateName;

    /**
     * 配置模板代码
     */
    private String templateCode;

    /**
     * 配置模板节点
     */
    private List<TemplateNodeVo> nodes;
}