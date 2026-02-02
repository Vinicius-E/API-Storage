package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(name = "GradeCreateRequestDTO", description = "Payload para criar uma grade vinculada a uma fileira")
public class GradeCreateRequestDTO {

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Identificador da grade (ex: A1, J1)", example = "J1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String identificador;

    @NotNull
    @Min(1)
    @Schema(description = "Ordem de exibição dentro da fileira", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer ordem;

}
