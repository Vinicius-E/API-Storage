package com.wester.storage.controller;

import com.wester.storage.dto.EstoquePosicaoDTO;
import com.wester.storage.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
@Tag(name = "Estoque", description = "Operações relacionadas ao mapa e itens do estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @Operation(
            summary = "Retorna o mapa completo do estoque por área",
            description = "Retorna fileira, grade e nível (com item/produto quando existir). Níveis sem item retornam quantidade 0."
    )
    @GetMapping("/posicoes/area/{areaId}")
    public List<EstoquePosicaoDTO> buscarMapaPorArea(
            @Parameter(description = "ID da área de estoque", example = "1")
            @PathVariable Long areaId) {
        return estoqueService.buscarMapaPorArea(areaId);
    }
}