package site.persipa.btbtt.spider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.BtbttSpiderResult;
import site.persipa.btbtt.spider.mapper.mybatis.BtbttSpiderResultMapper;
import site.persipa.btbtt.spider.service.BtbttSpiderResultService;

/**
 * @author persipa
 */
@Service
public class BtbttSpiderResultServiceImpl extends ServiceImpl<BtbttSpiderResultMapper, BtbttSpiderResult>
        implements BtbttSpiderResultService {
}
