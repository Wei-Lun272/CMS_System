package com.wellan.Construction_Management_System.entity.baseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@EntityListeners(BaseFullDateBean.class)
public class BaseFullDateBean extends BaseCreatedDateBean{
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;
}
