package site.persipa.automation.dubbo.consumer;

import cn.hutool.core.util.StrUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.persipa.bark.dubbo.provider.client.BarkDeviceApi;
import site.persipa.bark.dubbo.provider.client.BarkMessageApi;
import site.persipa.bark.pojo.client.dto.BarkMessageSendSimpleDto;
import site.persipa.bark.pojo.client.vo.BarkDeviceVo;

/**
 * @author persipa
 */
@Component
public class BarkClientConsumer {

    private static final String DEFAULT_DEVICE_NAME = "iphone12mini";

    @Value("${spring.application.name}")
    private String springAppName;

    @DubboReference(version = "1.0")
    private BarkMessageApi barkMessageApi;

    @DubboReference(version = "1.0")
    private BarkDeviceApi barkDeviceApi;

    public void sendMessage(String title, String body) {
        BarkDeviceVo barkDevice = barkDeviceApi.findByDeviceName(DEFAULT_DEVICE_NAME);
        if (barkDevice != null) {
            title = StrUtil.isBlank(title) ? "Automation 通知" : title;

            BarkMessageSendSimpleDto sendSimpleDto = new BarkMessageSendSimpleDto();
            sendSimpleDto.setTitle(title);
            sendSimpleDto.setBody(body);
            sendSimpleDto.setDeviceId(barkDevice.getId());
            sendSimpleDto.setGroup(springAppName);

            barkMessageApi.push(sendSimpleDto);
        }
    }
}