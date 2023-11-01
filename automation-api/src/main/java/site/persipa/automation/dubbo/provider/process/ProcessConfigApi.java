package site.persipa.automation.dubbo.provider.process;

/**
 * @author persipa
 */
public interface ProcessConfigApi {

    /**
     * 执行配置
     *
     * @param configId 配置id
     * @return 返回执行id
     */
    String execute(String configId);

    /**
     * 验证配置可用
     * @param configId 配置id
     * @return 验证成功与否
     */
    Boolean verify(String configId);
}
