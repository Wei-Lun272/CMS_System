package com.wellan.Construction_Management_System.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "contact_info")
    private String contactInfo;

    public Vendor() {
    }

    public Vendor(String name, String contactInfo) {
        this.setName(name);  // 使用 setter 來設置屬性
        this.setContactInfo(contactInfo);  // 使用 setter 來設置屬性
    }


    public Vendor(String name) {
        this(name,null);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "廠商名稱不可為null").trim();
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException("廠商名稱不可為空");
        }
    }


    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = (contactInfo != null) ? contactInfo.trim() : null;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }
}
