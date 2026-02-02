package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "ItemEstoqueUpsertRequest", description = "Payload para criar/atualizar item de estoque em um nível")
public class ItemEstoqueUpsertRequestDTO {

    @Schema(description = "Quantidade do item no nível", example = "3300", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Min(0)
    private Integer quantidade;

    @Schema(description = "Dados do produto", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Valid
    private ProdutoDTO produto;
}
