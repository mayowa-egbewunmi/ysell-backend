package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

/**
 * @author tchineke
 * @since 13th February, 2021
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
    private Instant clientCreatedAt;

    @NotNull
    @JsonProperty("client_updated_at")
    private Instant clientUpdatedAt;

    @NotNull
    private Boolean deleted;
}
