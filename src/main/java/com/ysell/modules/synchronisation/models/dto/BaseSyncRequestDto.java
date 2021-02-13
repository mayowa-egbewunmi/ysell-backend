package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * @author tchineke
 * @since 13 February, 2021
 */
@Getter
@Setter
public abstract class BaseSyncRequestDto {

    @NotNull
    private UUID id;

    @JsonProperty("organisation_id")
    private UUID organisationId;

    @NotNull
    @JsonProperty("created_by")
    private UUID createdBy;

    @JsonProperty("updated_by")
    private UUID updatedBy;

    @NotNull
    @JsonProperty("client_created_at")
    private Timestamp clientCreatedAt;

    @NotNull
    @JsonProperty("client_updated_at")
    private Timestamp clientUpdatedAt;

    @NotNull
    private Boolean deleted;
}
