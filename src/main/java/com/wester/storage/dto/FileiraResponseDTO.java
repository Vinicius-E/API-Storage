package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "FileiraResponse", description = "Resposta da criação/consulta de fileira")
public class FileiraResponseDTO {

    @Schema(description = "ID da fileira", example = "10")
    private Long id;

    @Schema(description = "Identificador da fileira", example = "F10")
    private String identificador;

    @Schema(description = "Ordem da fileira", example = "10")
    private Integer ordem;

    @Schema(description = "ID da área de estoque", example = "1")
    private Long areaEstoqueId;

    public static FileiraResponseDTO of(Long id, String identificador, Integer ordem, Long areaEstoqueId) {
        FileiraResponseDTO r = new FileiraResponseDTO();
        r.setId(id);
        r.setIdentificador(identificador);
        r.setOrdem(ordem);
        r.setAreaEstoqueId(areaEstoqueId);
        return r;
    }
}
