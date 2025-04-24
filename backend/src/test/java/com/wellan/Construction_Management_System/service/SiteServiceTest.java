package com.wellan.Construction_Management_System.service;

import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.entity.SiteStatus;
import com.wellan.Construction_Management_System.repository.SiteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SiteServiceTest {

    private SiteRepository siteRepository;
    private SiteService siteService;
    private RedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        siteRepository = mock(SiteRepository.class);
        redisTemplate = mock(RedisTemplate.class);
        siteService = new SiteService(siteRepository, redisTemplate);
    }


    // ✅ 建立 Site 實體（反射）
    private Site createSiteInstance() {
        try {
            Constructor<Site> constructor = Site.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("無法建立 Site 實體", e);
        }
    }

    // ✅ 設定私有欄位 id（反射）
    private void setPrivateId(Site site, int id) {
        try {
            Field idField = Site.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(site, id);
        } catch (Exception e) {
            throw new RuntimeException("無法設定 id", e);
        }
    }

    @Test
    void testGetSiteById_Found() {
        Site site = createSiteInstance();
        setPrivateId(site, 1);
        site.setSiteName("工程現場 A");

        when(siteRepository.findById(1)).thenReturn(Optional.of(site));

        Site result = siteService.getSiteById(1);
        assertEquals("工程現場 A", result.getSiteName());
    }

    @Test
    void testGetSiteById_NotFound() {
        when(siteRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> siteService.getSiteById(99));
    }

    @Test
    void testGetAllSite() {
        List<Site> mockList = List.of(createSiteInstance(), createSiteInstance());
        when(siteRepository.findAll()).thenReturn(mockList);

        List<Site> result = siteService.getAllSite();
        assertEquals(2, result.size());
    }

    @Test
    void testAddSite() {
        Site newSite = createSiteInstance();
        newSite.setSiteName("新工地");

        when(siteRepository.save(newSite)).thenReturn(newSite);

        Site result = siteService.addSite(newSite);
        assertEquals("新工地", result.getSiteName());
    }

    @Test
    void testDeleteSite_Success() {
        when(siteRepository.existsById(1)).thenReturn(true);
        doNothing().when(siteRepository).deleteById(1);

        assertDoesNotThrow(() -> siteService.deleteSite(1));
        verify(siteRepository).deleteById(1);
    }

    @Test
    void testDeleteSite_NotFound() {
        when(siteRepository.existsById(999)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> siteService.deleteSite(999));
    }

    @Test
    void testUpdateSiteById_Success() {
        Site oldSite = createSiteInstance();
        setPrivateId(oldSite, 1);
        oldSite.setLatitude(new BigDecimal("25.0"));
        oldSite.setLongitude(new BigDecimal("121.0"));

        Site updated = createSiteInstance();
        updated.setSiteName("更新工地");
        updated.setLatitude(new BigDecimal("26.0"));
        updated.setLongitude(new BigDecimal("122.0"));
        updated.setAddress("地址");
        updated.setDescription("描述");
        updated.setStatus(SiteStatus.PLANNING);

        when(siteRepository.findById(1)).thenReturn(Optional.of(oldSite));
        when(siteRepository.existsByLatitudeAndLongitudeAndIdNot(
                updated.getLatitude(), updated.getLongitude(), 1)).thenReturn(false);
        when(siteRepository.save(oldSite)).thenReturn(oldSite);

        Site result = siteService.updateSiteById(1, updated);
        assertEquals("更新工地", result.getSiteName());
    }

    @Test
    void testUpdateSiteById_LatLngConflict() {
        Site oldSite = createSiteInstance();
        setPrivateId(oldSite, 1);
        oldSite.setLatitude(new BigDecimal("25.0"));
        oldSite.setLongitude(new BigDecimal("121.0"));

        Site updated = createSiteInstance();
        updated.setLatitude(new BigDecimal("25.0"));
        updated.setLongitude(new BigDecimal("121.0"));

        when(siteRepository.findById(1)).thenReturn(Optional.of(oldSite));
        when(siteRepository.existsByLatitudeAndLongitudeAndIdNot(
                updated.getLatitude(), updated.getLongitude(), 1)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> siteService.updateSiteById(1, updated));
    }
}