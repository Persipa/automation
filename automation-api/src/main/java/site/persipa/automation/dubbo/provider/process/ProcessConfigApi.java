package site.persipa.automation.dubbo.provider.process;

import site.persipa.automation.pojo.process.bo.ProcessCallBo;

/**
 * @author persipa
 */
public interface ProcessConfigApi {

    /**
     * 执行配置
     *
     * @param callBo 执行相关信息
     * @return 返回结果票据
     */
    String execute(ProcessCallBo callBo);

    /**
     * 删除配置
     *
     * @param configId 配置id
     * @return 是否成功
     */
    Boolean remove(String configId);

    /**
     * 验证配置可用
     *
     * @param configId 配置id
     * @return 验证成功与否
     */
    Boolean verify(String configId);
}
