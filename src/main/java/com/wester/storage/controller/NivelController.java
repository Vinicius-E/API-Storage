package com.wester.storage.controller;

import com.wester.storage.dto.ApiErrorDTO;
import com.wester.storage.dto.GradeResequenceResultDTO;
import com.wester.storage.dto.NivelCreateRequestDTO;
import com.wester.storage.dto.NivelResponseDTO;
import com.wester.storage.model.Nivel;
import com.wester.storage.service.NivelService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.net.URI;
import java.util.List;

@Tag(name = "Nivel", description = "CRUD for Nivel")
@RestController
@RequestMapping("/api/niveis") // Base path for nivel endpoints
public class NivelController {

    @Autowired
    private NivelService nivelService;

    // Endpoint to list niveis within a specific grade
    @Operation(summary = "Get resource(s)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    @GetMapping("/grade/{gradeId}")
    public ResponseEntity<List<Nivel>> listarNiveisPorGrade(@PathVariable Long gradeId) {
        List<Nivel> niveis = nivelService.listarNiveisPorGrade(gradeId);
        return ResponseEntity.ok(niveis);
    }

    @Operation(summary = "Get resource(s)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    @GetMapping("/{id}")
    public ResponseEntity<Nivel> buscarNivelPorId(@PathVariable Long id) {
        return nivelService.buscarNivelPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a nivel within a specific grade
    @Operation(summary = "Criar nível em uma grade")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado"),
            @ApiResponse(responseCode = "404", description = "Grade não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/grade/{gradeId}")
    public ResponseEntity<NivelResponseDTO> criarNivel(
            @PathVariable Long gradeId,
            @Valid @RequestBody NivelCreateRequestDTO request
    ) {
        NivelResponseDTO
                response = nivelService.criarNivel(gradeId, request);
        return ResponseEntity
                .created(URI.create("/api/niveis/" + response.getId()))
                .body(response);
    }

    @Operation(summary = "Update resource")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Updated")})
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarNivel(@PathVariable Long id, @Valid @RequestBody Nivel nivel) {
         // Add security check: Only ADMIN?
         // Note: The service prevents changing the Grade ID.
         // The request body should ideally contain the Grade ID to match the existing one or be omitted.
         try {
            Nivel nivelAtualizado = nivelService.atualizarNivel(id, nivel);
            return ResponseEntity.ok(nivelAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
             if (e.getMessage().contains("não encontrado")) {
                 return ResponseEntity.notFound().build();
             }
             // Log the exception e
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar Nível.");
         } catch (Exception e) {
            // Log the exception e
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado ao atualizar Nível.");
        }
    }

    @DeleteMapping("/{nivelId}/resequence")
    @Operation(summary = "Remove um nível e resequencia níveis da grade")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = GradeResequenceResultDTO.class)))
    @ApiResponse(responseCode = "404", description = "Nível não encontrado",
            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "409", description = "Regra de negócio / integridade",
            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public ResponseEntity<?> deleteAndResequence(@PathVariable Long nivelId) {
        try {
            GradeResequenceResultDTO result = nivelService.deleteAndResequence(nivelId);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorDTO(ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorDTO(ex.getMessage()));
        }
    }

    @DeleteMapping("/{nivelId}")
    @Operation(summary = "Remove um nível (sem resequenciar)")
    @ApiResponse(responseCode = "204", description = "Removido")
    @ApiResponse(responseCode = "404", description = "Nível não encontrado",
            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    @ApiResponse(responseCode = "409", description = "Regra de negócio / integridade",
            content = @Content(schema = @Schema(implementation = ApiErrorDTO.class)))
    public ResponseEntity<?> deleteNivelOnly(@PathVariable Long nivelId) {
        try {
            nivelService.deleteNivelOnly(nivelId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorDTO(ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorDTO(ex.getMessage()));
        }
    }
}

