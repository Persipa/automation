package site.persipa.btbtt.spider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.btbtt.BtbttResult;
import site.persipa.btbtt.spider.mapper.mybatis.BtbttResultMapper;
import site.persipa.btbtt.spider.service.BtbttResultService;

/**
 * @author persipa
 */
@Service
public class BtbttResultServiceImpl extends ServiceImpl<BtbttResultMapper, BtbttResult>
        implements BtbttResultService {
}
