package com.novatronic.masivas.backoffice.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class ModelAudit<U> {

    @CreatedBy
    @Size(max = 50)
    @Column(name = "USU_CREACION")
    private String usuCreacion;
    @CreatedDate
    @Column(name = "FEC_CREACION")
    private LocalDateTime fecCreacion;
    @LastModifiedBy
    @Size(max = 500)
    @Column(name = "USU_MODIFICACION")
    private String usuModificacion;
    @LastModifiedDate
    @Column(name = "FEC_MODIFICACION")
    private LocalDateTime fecModificacion;

}
