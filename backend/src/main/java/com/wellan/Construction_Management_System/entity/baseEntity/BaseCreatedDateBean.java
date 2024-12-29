package com.wellan.Construction_Management_System.entity.baseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@EntityListeners(BaseCreatedDateBean.class)
@MappedSuperclass
public class BaseCreatedDateBean {
    @CreatedDate
    @Column(name = "created_date",updatable = false)
    private Timestamp createdDate;

    public Timestamp getCreatedDate() {
        return createdDate;
    }
}
