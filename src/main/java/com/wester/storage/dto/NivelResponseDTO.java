package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO de resposta após criação ou consulta de um nível")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelResponseDTO {

    @Schema(description = "ID do nível", example = "45")
    private Long id;

    @Schema(description = "Identificador do nível (ex: N1, N2)", example = "N1")
    private String identificador;

    @Schema(description = "Ordem do nível dentro da grade", example = "1")
    private Integer ordem;

    @Schema(description = "ID da grade à qual o nível pertence", example = "12")
    private Long gradeId;
}