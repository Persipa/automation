package site.persipa.btbtt.reflect.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.btbtt.reflect.mapper.ReflectMethodArgMapper;
import site.persipa.btbtt.reflect.service.ReflectMethodArgService;
import site.persipa.btbtt.pojo.reflect.ReflectMethodArg;

import java.util.List;

/**
 * @author persipa
 */
@Service
public class ReflectMethodArgServiceImpl extends ServiceImpl<ReflectMethodArgMapper, ReflectMethodArg>
        implements ReflectMethodArgService {

    @Override
    public List<ReflectMethodArg> listByMethodId(String methodId, Boolean argSortAsc) {
        return this.list(Wrappers.lambdaQuery(ReflectMethodArg.class)
                .eq(ReflectMethodArg::getMethodId, methodId)
                .orderBy(argSortAsc != null, Boolean.TRUE.equals(argSortAsc), ReflectMethodArg::getSort));
    }

}
