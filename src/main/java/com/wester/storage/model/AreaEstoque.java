// File: src/main/java/com/wester/storage/model/AreaEstoque.java
package com.wester.storage.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "area_estoque")
@Schema(name = "AreaEstoque", description = "Área física/lógica de estoque")
public class AreaEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da área", example = "1")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "descricao", nullable = false)
    @Schema(description = "Descrição da área", example = "Área principal de armazenagem de produtos acabados")
    private String descricao;

    @Size(max = 255)
    @Column(name = "nome")
    @Schema(description = "Nome curto da área", example = "Principal")
    private String nome;
}
