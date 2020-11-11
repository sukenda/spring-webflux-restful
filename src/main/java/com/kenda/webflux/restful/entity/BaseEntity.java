package com.kenda.webflux.restful.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Document
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1905122041950251207L;

    @Id
    private String id;

    /**
     * Not supported yet
     * See {@link com.kenda.webflux.restful.config.UserAuditor}
     */
    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedBy
    private String updatedBy;

    @LastModifiedDate
    private LocalDateTime updatedDate;

    @Version
    private Integer version;

}
