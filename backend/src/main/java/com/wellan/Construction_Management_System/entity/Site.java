package com.wellan.Construction_Management_System.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sites")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name",nullable = false)
    private String siteName;
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "status",nullable = false,length = 16)
    private String status;
    @Column(name = "description",length = 255)
    private String description;

    public void setStatus(SiteStatus status){
        this.status = status.getFriendlyName();
    }
    public SiteStatus getStatus(){
        return SiteStatus.fromFriendlyName(this.status);
    }

    public Site() {
    }

    // 主建構式
    public Site(String siteName, BigDecimal latitude, BigDecimal longitude, String address, SiteStatus status, String description) {
        if (siteName == null || siteName.isEmpty()) {
            throw new IllegalArgumentException("工地名稱不能為空");
        }
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("工地地址不能為空");
        }
        if (status == null) {
            throw new IllegalArgumentException("工地狀態不能為空");
        }
        if (latitude != null && (latitude.compareTo(new BigDecimal("-90.0")) < 0 || latitude.compareTo(new BigDecimal("90.0")) > 0)) {
            throw new IllegalArgumentException("緯度必須在 -90 到 90 之間");
        }
        if (longitude != null && (longitude.compareTo(new BigDecimal("-180.0")) < 0 || longitude.compareTo(new BigDecimal("180.0")) > 0)) {
            throw new IllegalArgumentException("經度必須在 -180 到 180 之間");
        }
        this.siteName = siteName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.status = status.getFriendlyName();
        this.description = description;
    }

    // 簡化建構式
    public Site(String siteName, String address, SiteStatus status, String description) {
        this(siteName, null, null, address, status, description); // 調用主建構式
    }


    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }



    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
