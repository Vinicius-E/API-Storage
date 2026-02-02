package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "GradeResponseDTO", description = "Resposta de grade criada")
public class GradeResponseDTO {

    @Schema(example = "29")
    private Long id;

    @Schema(example = "J1")
    private String identificador;

    @Schema(example = "1")
    private Integer ordem;

    @Schema(example = "13")
    private Long fileiraId;

}
