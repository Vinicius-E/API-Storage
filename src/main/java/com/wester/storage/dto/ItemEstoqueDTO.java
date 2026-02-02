package com.wester.storage.dto;

import com.wester.storage.model.ItemEstoque;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ItemEstoqueDTO", description = "Stock item representation")
public class ItemEstoqueDTO {

    @Schema(description = "Stock item ID", example = "62")
    private Long id;

    @Schema(description = "Nivel ID", example = "137")
    private Long nivelId;

    @Schema(description = "Nivel identifier", example = "N2")
    private String nivelIdentificador;

    @Schema(description = "Full identifier of nivel (if applicable)", example = "Fileira D - Grade D2 - N2", nullable = true)
    private String nivelCompletoIdentificador;

    @Schema(description = "Product ID", example = "15")
    private Long produtoId;

    @Schema(description = "Product code", example = "P006")
    private String produtoCodigoWester;

    @Schema(description = "Product model name", example = "Caixa Cinza ESD")
    private String produtoNomeModelo;

    @Schema(description = "Product color", example = "Cinza")
    private String produtoCor;

    @Schema(description = "Product description", example = "Caixa ESD para eletrônicos.")
    private String produtoDescricao;

    @Schema(description = "Quantity", example = "30")
    private Integer quantidade;

    @Schema(description = "Last update date/time", example = "2026-01-31T19:01:16.432934")
    private LocalDateTime dataAtualizacao;

    public static ItemEstoqueDTO fromEntity(ItemEstoque item) {
        ItemEstoqueDTO dto = new ItemEstoqueDTO();
        dto.setId(item.getId());

        if (item.getNivel() != null) {
            dto.setNivelId(item.getNivel().getId());
            dto.setNivelIdentificador(item.getNivel().getIdentificador());
        }

        if (item.getProduto() != null) {
            dto.setProdutoId(item.getProduto().getId());
            dto.setProdutoCodigoWester(item.getProduto().getCodigoSistemaWester());
            dto.setProdutoNomeModelo(item.getProduto().getNomeModelo());
            dto.setProdutoCor(item.getProduto().getCor());
            dto.setProdutoDescricao(item.getProduto().getDescricao());
        }

        dto.setQuantidade(item.getQuantidade());
        dto.setDataAtualizacao(item.getDataAtualizacao());
        dto.setNivelCompletoIdentificador(null);

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNivelId() {
        return nivelId;
    }

    public void setNivelId(Long nivelId) {
        this.nivelId = nivelId;
    }

    public String getNivelIdentificador() {
        return nivelIdentificador;
    }

    public void setNivelIdentificador(String nivelIdentificador) {
        this.nivelIdentificador = nivelIdentificador;
    }

    public String getNivelCompletoIdentificador() {
        return nivelCompletoIdentificador;
    }

    public void setNivelCompletoIdentificador(String nivelCompletoIdentificador) {
        this.nivelCompletoIdentificador = nivelCompletoIdentificador;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getProdutoCodigoWester() {
        return produtoCodigoWester;
    }

    public void setProdutoCodigoWester(String produtoCodigoWester) {
        this.produtoCodigoWester = produtoCodigoWester;
    }

    public String getProdutoNomeModelo() {
        return produtoNomeModelo;
    }

    public void setProdutoNomeModelo(String produtoNomeModelo) {
        this.produtoNomeModelo = produtoNomeModelo;
    }

    public String getProdutoCor() {
        return produtoCor;
    }

    public void setProdutoCor(String produtoCor) {
        this.produtoCor = produtoCor;
    }

    public String getProdutoDescricao() {
        return produtoDescricao;
    }

    public void setProdutoDescricao(String produtoDescricao) {
        this.produtoDescricao = produtoDescricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
