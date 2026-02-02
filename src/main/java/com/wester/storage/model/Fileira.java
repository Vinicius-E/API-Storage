package com.wester.storage.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "fileira")
@Schema(name = "Fileira", description = "Representa uma fileira do armazém vinculada a uma área de estoque")
public class Fileira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da fileira", example = "10")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "Identificador da fileira", example = "F10")
    private String identificador;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "Ordem da fileira", example = "10")
    private Integer ordem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "area_estoque_id", nullable = false)
    @Schema(description = "Área de estoque vinculada")
    private AreaEstoque areaEstoque;
}
