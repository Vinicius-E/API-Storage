package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "ItemEstoqueResponse", description = "Resposta do item de estoque por nível")
public class ItemEstoqueResponseDTO {

    @Schema(description = "ID do item de estoque (null quando não existir)", example = "10", nullable = true)
    private Long id;

    @Schema(description = "ID do nível", example = "115")
    private Long nivelId;

    @Schema(description = "Quantidade (0 quando não existir)", example = "0")
    private Integer quantidade;

    @Schema(description = "Produto (null quando não existir)", nullable = true)
    private ProdutoResumoDTO produto;
}
