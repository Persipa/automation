package site.persipa.automation.template.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.template.TemplateConfig;

/**
 * @author persipa
 */
public interface TemplateConfigService extends IService<TemplateConfig> {

    /**
     * 根据模板代码精确查询模板
     *
     * @param code 模板代码
     * @return 模板
     */
    TemplateConfig findOneByCode(String code);
}