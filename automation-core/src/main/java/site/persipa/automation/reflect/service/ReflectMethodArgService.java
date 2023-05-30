package site.persipa.automation.reflect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.automation.pojo.reflect.ReflectMethodArg;

import java.util.List;

/**
 * @author persipa
 */
public interface ReflectMethodArgService extends IService<ReflectMethodArg> {

    List<ReflectMethodArg> listByMethodId(String methodId, Boolean argSortAsc);
}
