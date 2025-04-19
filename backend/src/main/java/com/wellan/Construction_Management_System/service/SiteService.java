package com.wellan.Construction_Management_System.service;

import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.repository.SiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class SiteService {
    private final SiteRepository siteRepository;

    private static final Logger logger = LoggerFactory.getLogger(SiteService.class);

    @Autowired
    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }
    //value = "siteList" 表示清除這個快取命名空間（cache name）
    //allEntries = true 表示「清除此命名空間下的所有 key」
    @CacheEvict(value = "siteList", allEntries = true)
    public Site addSite(Site newSite){
        Site savedSite = siteRepository.save(newSite);
        logger.info("已新增新工地資訊:{}",newSite.getSiteName());
        return savedSite;
    }
    @CacheEvict(value = "siteList", allEntries = true)
    public Site updateSiteById(int id, Site updateSite) {
        Site originSite = siteRepository.findById(id).orElseThrow(
                () -> {
                    logger.warn("此id：{} 對應的工地並不存在",id);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "此id:" + id + " 對應的工地並不存在");
                }
        );

        // 確認經緯度是否已存在（排除當前正在更新的 Site）
        if (siteRepository.existsByLatitudeAndLongitudeAndIdNot(updateSite.getLatitude(), updateSite.getLongitude(), id)&&(updateSite.getLatitude().equals(originSite.getLatitude())&&updateSite.getLongitude().equals(originSite.getLongitude()))) {
            logger.warn("經緯度 (latitude: " + updateSite.getLatitude() + ", longitude: " + updateSite.getLongitude() + ") 已存在，無法更新");
            throw new ResponseStatusException(HttpStatus.CONFLICT, "經緯度已存在，無法更新");
        }

        // 更新資料
        originSite.setSiteName(updateSite.getSiteName());
        originSite.setStatus(updateSite.getStatus());
        originSite.setLatitude(updateSite.getLatitude());
        originSite.setLongitude(updateSite.getLongitude());
        originSite.setAddress(updateSite.getAddress());
        originSite.setDescription(updateSite.getDescription());

        // 保存更新後的 Site
        Site updatedSite = siteRepository.save(originSite);
        logger.info("對應id為：{}的工地已被更新",id);

        return updatedSite;
    }

    @Cacheable(value = "siteList")
    public List<Site> getAllSite(){
        return siteRepository.findAll();
    }
    //啟動快取
    //#id為key
    //value的siteCache表示快取內的特定命名範圍
    //綜合來說對應到Redis內就是key為siteCache::#id，比如siteCache::3就是找到siteCache中#id為3的資料
    @Cacheable(value = "siteCache", key = "#id")
    public Site getSiteById(int id){
        return siteRepository.findById(id).orElseThrow(
                ()->{
                    logger.warn("此ID:{}並不存在對應的工地",id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,"此ID:"+id+"並不存在對應的工地");
                }
        );

    }
    @Caching(evict = {
            @CacheEvict(value = "siteCache", key = "#id"),
            @CacheEvict(value = "siteList", allEntries = true)
    })
    public void deleteSite(int id){
        if(siteRepository.existsById(id)){
            siteRepository.deleteById(id);
            logger.info("已刪除id：{}的工地資料",id);
        }else{
            logger.warn("此ID: {} 並不存在對應的工地，無法刪除",id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"此ID:"+id+"並不存在對應的工地，無法刪除");
        }
    }

}
