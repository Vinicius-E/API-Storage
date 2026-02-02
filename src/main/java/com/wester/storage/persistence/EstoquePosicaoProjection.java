package com.wester.storage.persistence;

public interface EstoquePosicaoProjection {

        Long getFileiraId();

        String getFileiraIdentificador();

        Integer getFileiraOrdem();

        Long getGradeId();

        String getGradeIdentificador();

        Integer getGradeOrdem();

        Long getNivelId();

        String getNivelIdentificador();

        Integer getNivelOrdem();

        Long getItemEstoqueId();

        Integer getQuantidade();

        Long getProdutoId();

        String getCodigoSistemaWester();

        String getNomeModelo();

        String getCor();

        String getDescricao();
}
