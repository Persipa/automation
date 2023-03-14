package site.persipa.btbtt.jsoup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import site.persipa.btbtt.pojo.jsoup.JsoupEntity;

import java.util.List;

/**
 * @author persipa
 */
public interface JsoupEntityService extends IService<JsoupEntity> {

    List<JsoupEntity> subEntityList(String entityId);
}
