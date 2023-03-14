package site.persipa.btbtt.spider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.btbtt.BtbttSpiderProcessingEntity;
import site.persipa.btbtt.spider.mapper.mybatis.BtbttSpiderProcessingEntityMapper;
import site.persipa.btbtt.spider.service.BtbttSpiderProcessingEntityService;

/**
 * @author persipa
 */
@Service
public class BtbttSpiderProcessingEntityServiceImpl extends ServiceImpl<BtbttSpiderProcessingEntityMapper, BtbttSpiderProcessingEntity>
        implements BtbttSpiderProcessingEntityService {
}
