package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.base.ActiveAuditableEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * @author tchineke
 * @since 13 February, 2021
 */
@Getter
@Setter
public abstract class BaseSyncResponseDto {

    private UUID id;

    @JsonProperty("organisation_id")
    private UUID organisationId;

    @JsonProperty("created_by")
    private UUID createdBy;

    @JsonProperty("updated_by")
    private UUID updatedBy;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @JsonProperty("client_created_at")
    private Instant clientCreatedAt;

    @JsonProperty("client_updated_at")
    private Instant clientUpdatedAt;

    private Boolean deleted;


    protected void setBaseFields(ActiveAuditableEntity entity, UUID organisationId) {
        id = entity.getId();
        this.organisationId = organisationId;
        
        createdBy = entity.getCreatedBy();
        createdAt = entity.getCreatedAt();
        updatedBy = entity.getUpdatedBy();
        updatedAt = entity.getUpdatedAt();

        clientCreatedAt = entity.getClientCreatedAt();
        clientUpdatedAt = entity.getClientUpdatedAt();

        deleted = !entity.isActive();
    }
}