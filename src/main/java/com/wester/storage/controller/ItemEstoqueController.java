package com.wester.storage.controller;

import com.wester.storage.dto.ItemEstoqueDTO;
import com.wester.storage.dto.ItemEstoqueUpsertRequestDTO;
import com.wester.storage.model.ItemEstoque;
import com.wester.storage.service.ItemEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ItemEstoque", description = "CRUD for ItemEstoque")
@RestController
@RequestMapping("/api/itens-estoque")
public class ItemEstoqueController {

    private final ItemEstoqueService itemEstoqueService;

    public ItemEstoqueController(ItemEstoqueService itemEstoqueService) {
        this.itemEstoqueService = itemEstoqueService;
    }

    @Operation(summary = "Get ItemEstoque by nivelId")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "204", description = "No content (nivel has no item)"),
            @ApiResponse(responseCode = "404", description = "Nivel not found")
    })
    @GetMapping("/nivel/{nivelId}")
    public ResponseEntity<ItemEstoqueDTO> buscarPorNivel(@PathVariable Long nivelId) {
        Optional<ItemEstoque> opt = itemEstoqueService.buscarPorNivel(nivelId);

        if (opt.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ItemEstoqueDTO.fromEntity(opt.get()));
    }

    @Operation(summary = "Create or update ItemEstoque by nivelId (UPSERT)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated or created"),
            @ApiResponse(responseCode = "404", description = "Nivel not found")
    })
    @PutMapping("/nivel/{nivelId}")
    public ResponseEntity<ItemEstoqueDTO> upsertPorNivel(
            @PathVariable Long nivelId,
            @Valid @RequestBody ItemEstoqueUpsertRequestDTO request
    ) {
        Long usuarioId = 1L;

        ItemEstoque saved = itemEstoqueService.salvarPorNivel(nivelId, request, usuarioId);
        ItemEstoqueDTO dto = ItemEstoqueDTO.fromEntity(saved);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Create ItemEstoque by nivelId (optional - you can keep using PUT only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "404", description = "Nivel not found")
    })
    @PostMapping("/nivel/{nivelId}")
    public ResponseEntity<ItemEstoqueDTO> criarPorNivel(
            @PathVariable Long nivelId,
            @Valid @RequestBody ItemEstoqueUpsertRequestDTO request
    ) {
        Long usuarioId = 1L;

        ItemEstoque saved = itemEstoqueService.salvarPorNivel(nivelId, request, usuarioId);
        ItemEstoqueDTO dto = ItemEstoqueDTO.fromEntity(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Remove item de estoque do nível")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item removido"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    @DeleteMapping("/nivel/{nivelId}")
    public ResponseEntity<Void> deletePorNivel(@PathVariable Long nivelId) {
        itemEstoqueService.removerPorNivel(nivelId);
        return ResponseEntity.noContent().build();
    }
}
