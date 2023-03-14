package site.persipa.btbtt.spider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.btbtt.BtbttSpiderProcessing;
import site.persipa.btbtt.spider.mapper.mybatis.BtbttSpiderProcessingMapper;
import site.persipa.btbtt.spider.service.BtbttSpiderProcessingService;

/**
 * @author persipa
 */
@Service
public class BtbttSpiderProcessingServiceImpl extends ServiceImpl<BtbttSpiderProcessingMapper, BtbttSpiderProcessing>
        implements BtbttSpiderProcessingService {
}
