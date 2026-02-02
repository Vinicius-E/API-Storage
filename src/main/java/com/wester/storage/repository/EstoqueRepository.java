package com.wester.storage.repository;

import java.util.List;

import com.wester.storage.model.Nivel;
import com.wester.storage.persistence.EstoquePosicaoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstoqueRepository extends JpaRepository<Nivel, Long> {

    @Query(value = """
    SELECT
        f.id AS fileiraId,
        f.identificador AS fileiraIdentificador,
        f.ordem AS fileiraOrdem,

        g.id AS gradeId,
        g.identificador AS gradeIdentificador,
        g.ordem AS gradeOrdem,

        n.id AS nivelId,
        n.identificador AS nivelIdentificador,
        n.ordem AS nivelOrdem,

        ie.id AS itemEstoqueId,
        COALESCE(ie.quantidade, 0) AS quantidade,

        p.id AS produtoId,
        p.codigo_sistema_wester AS codigoSistemaWester,
        p.nome_modelo AS nomeModelo,
        p.cor AS cor,
        p.descricao AS descricao

    FROM fileira f
    LEFT JOIN grade g ON g.fileira_id = f.id
    LEFT JOIN nivel n ON n.grade_id = g.id
    LEFT JOIN item_estoque ie ON ie.nivel_id = n.id
    LEFT JOIN produto p ON p.id = ie.produto_id

    WHERE f.area_estoque_id = :areaId

    ORDER BY
        f.ordem, f.id,
        g.ordem NULLS LAST, g.id,
        n.ordem NULLS LAST, n.id
    """, nativeQuery = true)
    List<EstoquePosicaoProjection> buscarMapaPorArea(@Param("areaId") Long areaId);
}