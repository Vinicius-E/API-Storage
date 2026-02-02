package com.wester.storage.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
        name = "produto",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "codigo_sistema_wester")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "codigo_sistema_wester", nullable = true)
    private String codigoSistemaWester;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nome_modelo", nullable = false, length = 255)
    private String nomeModelo;

    @NotBlank
    @Size(max = 100)
    @Column(name = "cor", nullable = false, length = 100)
    private String cor;

    @JdbcTypeCode(SqlTypes.LONGVARCHAR)
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
}
