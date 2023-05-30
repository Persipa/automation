package site.persipa.automation.reflect.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.reflect.mapper.ReflectMethodArgMapper;
import site.persipa.automation.reflect.service.ReflectMethodArgService;
import site.persipa.automation.pojo.reflect.ReflectMethodArg;

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
