package site.persipa.automation.reflect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.reflect.mapper.ReflectMethodMapper;
import site.persipa.automation.reflect.service.ReflectMethodService;
import site.persipa.automation.pojo.reflect.ReflectMethod;

/**
 * @author persipa
 */
@Service
public class ReflectMethodServiceImpl extends ServiceImpl<ReflectMethodMapper, ReflectMethod> implements ReflectMethodService {
}
