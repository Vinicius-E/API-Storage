package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "ProdutoResumo", description = "Resumo do produto associado ao item de estoque")
public class ProdutoResumoDTO {

    @Schema(description = "ID do produto", example = "5")
    private Long id;

    @Schema(description = "Código Wester (pode ser null)", example = "ABC123", nullable = true)
    private String codigoSistemaWester;

    @Schema(description = "Nome/Modelo", example = "Caixa Plástica Verde")
    private String nomeModelo;

    @Schema(description = "Cor", example = "VERDE")
    private String cor;

    @Schema(description = "Descrição", example = "Produto de teste", nullable = true)
    private String descricao;
}
