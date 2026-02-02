package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(name = "CriarFileiraRequest", description = "Payload para criação de fileira")
public class CriarFileiraRequestDTO {

    @NotBlank
    @Schema(description = "Identificador da fileira", example = "F10")
    private String identificador;

    @NotNull
    @Schema(description = "Ordem da fileira", example = "10")
    private Integer ordem;
}
