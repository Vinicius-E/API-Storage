package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa a posição de um item no estoque (Fileira → Grade → Nível)")
public record EstoquePosicaoDTO(

        @Schema(description = "ID da fileira", example = "1")
        Long fileiraId,

        @Schema(description = "Identificador da fileira", example = "A")
        String fileiraIdentificador,

        @Schema(description = "ID da grade", example = "10")
        Long gradeId,

        @Schema(description = "Identificador da grade", example = "A1")
        String gradeIdentificador,

        @Schema(description = "ID do nível", example = "100")
        Long nivelId,

        @Schema(description = "Identificador do nível", example = "N1")
        String nivelIdentificador,

        @Schema(description = "Ordem numérica do nível", example = "1")
        Integer nivelOrdem,

        @Schema(description = "ID do item em estoque (pode ser nulo quando vazio)", example = "500")
        Long itemEstoqueId,

        @Schema(description = "Quantidade armazenada no nível", example = "35")
        Integer quantidade,

        @Schema(description = "ID do produto associado", example = "2000")
        Long produtoId,

        @Schema(description = "Código do produto no sistema Wester", example = "WST-ABC-123")
        String codigoSistemaWester,

        @Schema(description = "Nome ou modelo do produto", example = "Caixa Laranja Picking")
        String nomeModelo,

        @Schema(description = "Cor do produto", example = "Laranja")
        String cor,

        @Schema(description = "Descrição do produto", example = "Caixa plástica para separação de pedidos")
        String descricao

) { }
