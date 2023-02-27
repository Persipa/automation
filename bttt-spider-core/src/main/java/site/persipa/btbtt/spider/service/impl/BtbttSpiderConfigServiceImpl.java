package site.persipa.btbtt.spider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.BtbttSpiderConfig;
import site.persipa.btbtt.spider.mapper.mybatis.BtbttSpiderConfigMapper;
import site.persipa.btbtt.spider.service.BtbttSpiderConfigService;

/**
 * @author persipa
 */
@Service
public class BtbttSpiderConfigServiceImpl extends ServiceImpl<BtbttSpiderConfigMapper, BtbttSpiderConfig>
        implements BtbttSpiderConfigService {
}
