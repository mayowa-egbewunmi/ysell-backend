package com.ysell.modules.synchronisation.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Timestamp;
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
    private Timestamp createdAt;

    @JsonProperty("updated_at")
    private Timestamp updatedAt;
}
