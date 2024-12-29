package com.wellan.Construction_Management_System.dto;

import com.wellan.Construction_Management_System.entity.Site;
import com.wellan.Construction_Management_System.entity.SiteMaterial;

import java.util.List;

public class SiteDetailDTO {
    private Integer siteId;
    private String siteName;
    private String address;
    private String status;
    private String description;
    private List<SiteMaterialDetailDTO> materials;

    protected SiteDetailDTO() {
    }

    private SiteDetailDTO(Integer siteId, String siteName, String address, String status, String description, List<SiteMaterialDetailDTO> materials) {
        this.siteId = siteId;
        this.siteName = siteName;
        this.address = address;
        this.status = status;
        this.description = description;
        this.materials = materials;
    }

    public static SiteDetailDTO createFromSite(Site site, List<SiteMaterial> materials) {
        List<SiteMaterialDetailDTO> materialDetails = materials.stream()
                .map(SiteMaterialDetailDTO::createFromSiteMaterial)
                .toList();

        return new SiteDetailDTO(
                site.getId(),
                site.getSiteName(),
                site.getAddress(),
                site.getStatus().name(),
                site.getDescription(),
                materialDetails
        );
    }


    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SiteMaterialDetailDTO> getMaterials() {
        return materials;
    }

    public void setMaterials(List<SiteMaterialDetailDTO> materials) {
        this.materials = materials;
    }

    @Override
    public String toString() {
        return "SiteDetailDTO{" +
                "siteId=" + siteId +
                ", siteName='" + siteName + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", materials=" + materials +
                '}';
    }
}




