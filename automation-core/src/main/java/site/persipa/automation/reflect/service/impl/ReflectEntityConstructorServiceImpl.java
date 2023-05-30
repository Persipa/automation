package site.persipa.automation.reflect.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import site.persipa.automation.reflect.mapper.ReflectEntityConstructorMapper;
import site.persipa.automation.reflect.service.ReflectEntityConstructorService;
import site.persipa.automation.pojo.reflect.ReflectEntityConstructor;

/**
 * @author persipa
 */
@Service
public class ReflectEntityConstructorServiceImpl extends ServiceImpl<ReflectEntityConstructorMapper, ReflectEntityConstructor>
        implements ReflectEntityConstructorService {
}
