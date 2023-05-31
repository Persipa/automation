package site.persipa.automation.dubbo.consumer;

import cn.hutool.core.util.StrUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import site.persipa.bark.dubbo.provider.client.BarkDeviceApi;
import site.persipa.bark.dubbo.provider.client.BarkMessageApi;
import site.persipa.bark.pojo.client.dto.BarkMessageSendSimpleDto;
import site.persipa.bark.pojo.client.vo.BarkDeviceVo;

/**
 * @author persipa
 */
@Component
public class BarkClientManager {

    private static final String DEFAULT_DEVICE_NAME = "iphone12mini";

    @DubboReference
    private BarkMessageApi barkMessageApi;

    @DubboReference
    private BarkDeviceApi barkDeviceApi;

    public void sendMessage(String title, String body) {
        BarkDeviceVo barkDevice = barkDeviceApi.findByDeviceName(DEFAULT_DEVICE_NAME);
        if (barkDevice != null) {
            title = StrUtil.isBlank(title) ? "Automation 通知" : title;

            BarkMessageSendSimpleDto sendSimpleDto = new BarkMessageSendSimpleDto();
            sendSimpleDto.setTitle(title);
            sendSimpleDto.setBody(body);
            sendSimpleDto.setDeviceId(barkDevice.getId());

            barkMessageApi.push(sendSimpleDto);
        }
    }
}