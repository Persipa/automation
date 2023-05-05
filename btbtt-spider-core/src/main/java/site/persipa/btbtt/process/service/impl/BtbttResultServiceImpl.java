package site.persipa.btbtt.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.pojo.process.BtbttResult;
import site.persipa.btbtt.process.mapper.mybatis.BtbttResultMapper;
import site.persipa.btbtt.process.service.BtbttResultService;

/**
 * @author persipa
 */
@Service
public class BtbttResultServiceImpl extends ServiceImpl<BtbttResultMapper, BtbttResult>
        implements BtbttResultService {
}
