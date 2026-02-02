package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "Produto", description = "Dados do produto para criação/atualização no item de estoque")
public class ProdutoDTO {

    @Schema(description = "Código do sistema Wester (não-único). Pode ser vazio/nulo.", example = "")
    @Size(max = 100)
    private String codigoSistemaWester;

    @Schema(description = "Nome/modelo do produto", example = "NEW MODEL TEST")
    @Size(max = 255)
    private String nomeModelo;

    @Schema(description = "Cor do produto", example = "BLACK")
    @Size(max = 100)
    private String cor;

    @Schema(description = "Descrição do produto", example = "Test Validação Adição Grade_Nível_Item_Estoque_Produto")
    private String descricao;
}
