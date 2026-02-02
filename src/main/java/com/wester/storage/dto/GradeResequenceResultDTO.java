package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Resultado do processo de remoção e resequenciamento de níveis de uma grade")
@AllArgsConstructor
@Data
public class GradeResequenceResultDTO {

    @Schema(description = "ID da grade que teve os níveis resequenciados", example = "10")
    private Long gradeId;

    @Schema(
            description = "Lista de níveis após o resequenciamento",
            implementation = NivelDTO.class
    )
    private List<NivelDTO> niveis;

}
