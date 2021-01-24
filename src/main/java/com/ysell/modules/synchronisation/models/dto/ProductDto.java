package com.ysell.modules.synchronisation.models.dto;

import com.ysell.modules.common.dto.LookupDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductDto {

    @NotNull
    private UUID id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private BigDecimal costPrice;

    @NotNull
    private BigDecimal sellingPrice;

    @NotNull
    @Valid
    private LookupDto organisation;

    @NotNull
    @Valid
    private LookupDto productCategory;

    @NotNull
    private LocalDate clientCreatedAt;

    private LocalDate clientUpdatedAt;
}
