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
}
