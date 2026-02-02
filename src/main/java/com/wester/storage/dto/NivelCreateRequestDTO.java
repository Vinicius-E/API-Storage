package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para criação de um nível dentro de uma grade")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelCreateRequestDTO {

    @Schema(description = "Identificador do nível (ex: N1, N2)", example = "N1")
    @NotBlank
    private String identificador;

    @Schema(description = "Ordem do nível dentro da grade", example = "1")
    @NotNull
    @Min(1)
    private Integer ordem;
}