package com.sogoeslight.instructly.audit

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(
    value = ["createdAt", "modifiedAt"]
)
abstract class AuditModel(
    @Column(name = "created_at")
    @CreatedDate
    open var createdAt: Instant? = null,

    @Column(name = "modified_at")
    @LastModifiedDate
    open var modifiedAt: Instant? = null
)