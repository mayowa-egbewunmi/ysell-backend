package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ysell.jpa.entities.base.ClientAuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * @author tchineke
 * @since 13 February, 2021
 */
@AllArgsConstructor
@Getter
public class UpdatedSyncResponseDto {

    private UUID id;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;


    public static UpdatedSyncResponseDto from(ClientAuditableEntity entity) {
        return new UpdatedSyncResponseDto(entity.getId(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
