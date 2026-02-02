package com.wester.storage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(name = "MovimentacaoRequestDTO", description = "Request payload for stock movements (add/remove/move)")
public class MovimentacaoRequestDTO {

    @Schema(description = "Origin nivel ID (only for move)", example = "101", nullable = true)
    private Long nivelOrigemId;

    @Schema(description = "Destination nivel ID (only for move)", example = "102", nullable = true)
    private Long nivelDestinoId;

    @Schema(description = "Nivel ID (used for add/remove)", example = "137", nullable = true)
    private Long nivelId;

    @Schema(description = "Product ID", example = "15")
    @NotNull
    private Long produtoId;

    @Schema(description = "Quantity to move/add/remove", example = "10", minimum = "1")
    @NotNull
    @Min(1)
    private Integer quantidade;

    public Long getNivelOrigemId() {
        return nivelOrigemId;
    }

    public void setNivelOrigemId(Long nivelOrigemId) {
        this.nivelOrigemId = nivelOrigemId;
    }

    public Long getNivelDestinoId() {
        return nivelDestinoId;
    }

    public void setNivelDestinoId(Long nivelDestinoId) {
        this.nivelDestinoId = nivelDestinoId;
    }

    public Long getNivelId() {
        return nivelId;
    }

    public void setNivelId(Long nivelId) {
        this.nivelId = nivelId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
