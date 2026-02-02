package com.wester.storage.controller;

import com.wester.storage.dto.GradeCreateRequestDTO;
import com.wester.storage.dto.GradeResponseDTO;
import com.wester.storage.model.Grade;
import com.wester.storage.service.GradeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.net.URI;
import java.util.List;

@Tag(name = "Grade", description = "CRUD for Grade")
@RestController
@RequestMapping("/api/grades") // Base path for grade endpoints
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // Endpoint to list grades within a specific fileira
    @Operation(summary = "Get resource(s)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    @GetMapping("/fileira/{fileiraId}")
    public ResponseEntity<List<Grade>> listarGradesPorFileira(@PathVariable Long fileiraId) {
        List<Grade> grades = gradeService.listarGradesPorFileira(fileiraId);
        return ResponseEntity.ok(grades);
    }

    @Operation(summary = "Get resource(s)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Success")})
    @GetMapping("/{id}")
    public ResponseEntity<Grade> buscarGradePorId(@PathVariable Long id) {
        return gradeService.buscarGradePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint to create a grade within a specific fileira
    @Operation(summary = "Criar grade em uma fileira")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Criado"),
            @ApiResponse(responseCode = "404", description = "Fileira não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/fileira/{fileiraId}")
    public ResponseEntity<GradeResponseDTO> criarGrade(
            @PathVariable Long fileiraId,
            @Valid @RequestBody GradeCreateRequestDTO request
    ) {
        GradeResponseDTO response = gradeService.criarGrade(fileiraId, request);

        return ResponseEntity
                .created(URI.create("/api/grades/" + response.getId()))
                .body(response);
    }

    @Operation(summary = "Update resource")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Updated")})
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarGrade(@PathVariable Long id, @Valid @RequestBody Grade grade) {
         // Add security check: Only ADMIN?
         // Note: The service prevents changing the Fileira ID.
         // The request body should ideally contain the Fileira ID to match the existing one or be omitted.
         try {
            Grade gradeAtualizada = gradeService.atualizarGrade(id, grade);
            return ResponseEntity.ok(gradeAtualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
             if (e.getMessage().contains("não encontrada")) {
                 return ResponseEntity.notFound().build();
             }
             // Log the exception e
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar Grade.");
         } catch (Exception e) {
            // Log the exception e
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado ao atualizar Grade.");
        }
    }

    @Operation(summary = "Delete resource")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Deleted")})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarGrade(@PathVariable Long id) {
        // Add security check: Only ADMIN?
        try {
            gradeService.deletarGrade(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) { // Catch dependency error
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (RuntimeException e) {
             if (e.getMessage().contains("não encontrada")) {
                 return ResponseEntity.notFound().build();
             }
             // Log the exception e
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar Grade.");
         } catch (Exception e) {
            // Log the exception e
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado ao deletar Grade.");
        }
    }
}

