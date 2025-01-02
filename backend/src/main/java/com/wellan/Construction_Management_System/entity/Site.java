package com.wellan.Construction_Management_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wellan.Construction_Management_System.entity.baseEntity.BaseFullDateBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Site Entity - 代表建設管理系統中的工地。
 * 包含工地名稱、位置（緯度和經度）、地址、狀態、描述以及與原物料的關聯。
 */
@Entity
@Table(name = "sites")
public class Site extends BaseFullDateBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 工地名稱，必須不可為空。
     */
    @NotBlank
    @Column(name = "name", nullable = false)
    private String siteName;

    /**
     * 緯度，精度為 10，範圍在 -90 到 90 之間。
     */
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    /**
     * 經度，精度為 11，範圍在 -180 到 180 之間。
     */
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    /**
     * 工地地址，必須不可為空。
     */
    @NotBlank
    @Column(name = "address", nullable = false, length = 255)
    private String address;

    /**
     * 工地狀態，必須不可為空。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private SiteStatus status;

    /**
     * 工地描述，可選。
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * 與原物料的關聯，表示工地中使用的原物料。
     */
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SiteMaterial> siteMaterials = new ArrayList<>();

    /**
     * 預設建構方法，供 JPA 使用。
     */
    protected Site() {
    }

    /**
     * 帶參建構方法，用於創建 Site 實例。
     *
     * @param siteName    工地名稱，必須不可為空
     * @param latitude    緯度，範圍在 -90 到 90 之間
     * @param longitude   經度，範圍在 -180 到 180 之間
     * @param address     工地地址，必須不可為空
     * @param status      工地狀態，必須不可為空
     * @param description 工地描述
     */
    public Site(String siteName, BigDecimal latitude, BigDecimal longitude, String address, SiteStatus status, String description) {
        this.siteName = Objects.requireNonNull(siteName, "工地名稱不可為空");
        this.address = Objects.requireNonNull(address, "工地地址不可為空");
        this.status = Objects.requireNonNull(status, "工地狀態不可為空");
        if (latitude != null && (latitude.compareTo(new BigDecimal("-90.0")) < 0 || latitude.compareTo(new BigDecimal("90.0")) > 0)) {
            throw new IllegalArgumentException("緯度必須在 -90 到 90 之間");
        }
        if (longitude != null && (longitude.compareTo(new BigDecimal("-180.0")) < 0 || longitude.compareTo(new BigDecimal("180.0")) > 0)) {
            throw new IllegalArgumentException("經度必須在 -180 到 180 之間");
        }
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    /**
     * 簡化建構方法，用於創建僅有名稱和地址的 Site 實例。
     *
     * @param siteName    工地名稱，必須不可為空
     * @param address     工地地址，必須不可為空
     * @param status      工地狀態，必須不可為空
     * @param description 工地描述
     */
    public Site(String siteName, String address, SiteStatus status, String description) {
        this(siteName, null, null, address, status, description); // 調用主建構式
    }

    /**
     * 取得工地名稱。
     *
     * @return 工地名稱
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * 設定工地名稱。
     *
     * @param siteName 工地名稱，必須不可為空
     */
    public void setSiteName(String siteName) {
        this.siteName = Objects.requireNonNull(siteName, "工地名稱不可為空");
    }

    /**
     * 取得緯度。
     *
     * @return 緯度
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * 設定緯度。
     *
     * @param latitude 緯度，範圍在 -90 到 90 之間
     */
    public void setLatitude(BigDecimal latitude) {
        if (latitude != null && (latitude.compareTo(new BigDecimal("-90.0")) < 0 || latitude.compareTo(new BigDecimal("90.0")) > 0)) {
            throw new IllegalArgumentException("緯度必須在 -90 到 90 之間");
        }
        this.latitude = latitude;
    }

    /**
     * 取得經度。
     *
     * @return 經度
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * 設定經度。
     *
     * @param longitude 經度，範圍在 -180 到 180 之間
     */
    public void setLongitude(BigDecimal longitude) {
        if (longitude != null && (longitude.compareTo(new BigDecimal("-180.0")) < 0 || longitude.compareTo(new BigDecimal("180.0")) > 0)) {
            throw new IllegalArgumentException("經度必須在 -180 到 180 之間");
        }
        this.longitude = longitude;
    }

    /**
     * 取得工地地址。
     *
     * @return 工地地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 設定工地地址。
     *
     * @param address 工地地址，必須不可為空
     */
    public void setAddress(String address) {
        this.address = Objects.requireNonNull(address, "工地地址不可為空");
    }

    /**
     * 取得工地狀態。
     *
     * @return 工地狀態
     */
    public SiteStatus getStatus() {
        return this.status;
    }

    /**
     * 設定工地狀態。
     *
     * @param status 工地狀態，必須不可為空
     */
    public void setStatus(SiteStatus status) {
        this.status = Objects.requireNonNull(status, "工地狀態不可為空");
    }

    /**
     * 取得工地描述。
     *
     * @return 工地描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 設定工地描述。
     *
     * @param description 工地描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 取得工地與原物料的關聯。
     *
     * @return 工地與原物料的關聯列表
     */
    public List<SiteMaterial> getSiteMaterials() {
        return siteMaterials;
    }

    /**
     * 設定工地與原物料的關聯。
     *
     * @param siteMaterials 工地與原物料的關聯列表
     */
    public void setSiteMaterials(List<SiteMaterial> siteMaterials) {
        this.siteMaterials = siteMaterials;
    }

    @Override
    public String toString() {
        return "Site{" +
                "id=" + id +
                ", siteName='" + siteName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return id == site.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
