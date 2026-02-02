package com.wester.storage.service;

import com.wester.storage.dto.GradeCreateRequestDTO;
import com.wester.storage.dto.GradeResponseDTO;
import com.wester.storage.model.Fileira;
import com.wester.storage.model.Grade;
import com.wester.storage.repository.FileiraRepository;
import com.wester.storage.repository.GradeRepository;
import com.wester.storage.repository.NivelRepository; // Needed for deletion check
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private FileiraRepository fileiraRepository; // To fetch Fileira

    @Autowired
    private NivelRepository nivelRepository; // To check for dependencies

    @Transactional(readOnly = true)
    public List<Grade> listarGradesPorFileira(Long fileiraId) {
        return gradeRepository.findByFileiraIdOrderByOrdemAscIdentificadorAsc(fileiraId);
    }

    @Transactional(readOnly = true)
    public Optional<Grade> buscarGradePorId(Long id) {
        return gradeRepository.findById(id);
    }

    @Transactional
    public Grade salvarGrade(GradeCreateRequestDTO request, Long fileiraId) {
        Fileira fileira = fileiraRepository.findById(fileiraId)
                .orElseThrow(() -> new RuntimeException("Fileira não encontrada com id: " + fileiraId));

        Grade grade = new Grade();
        grade.setId(null);
        grade.setFileira(fileira);
        grade.setIdentificador(request.getIdentificador());
        grade.setOrdem(request.getOrdem());

        if (gradeRepository.findByFileiraAndIdentificador(fileira, grade.getIdentificador()).isPresent()) {
            throw new IllegalArgumentException("Identificador de Grade já existe nesta Fileira: " + grade.getIdentificador());
        }

        return gradeRepository.save(grade);
    }

    @Transactional
    public Grade atualizarGrade(Long id, Grade gradeAtualizada) {
        return gradeRepository.findById(id).map(gradeExistente -> {
            // Ensure Fileira isn't changed, or handle it explicitly if allowed
            if (!gradeExistente.getFileira().getId().equals(gradeAtualizada.getFileira().getId())) {
                 throw new IllegalArgumentException("Não é permitido alterar a Fileira de uma Grade existente.");
            }

            // Check for duplicate identifier within the same Fileira
            Optional<Grade> gradeComMesmoId = gradeRepository.findByFileiraAndIdentificador(
                gradeExistente.getFileira(), gradeAtualizada.getIdentificador());

            if (!gradeExistente.getIdentificador().equals(gradeAtualizada.getIdentificador()) &&
                gradeComMesmoId.isPresent() && !gradeComMesmoId.get().getId().equals(id)) {
                throw new IllegalArgumentException("Identificador de Grade já existe nesta Fileira: " + gradeAtualizada.getIdentificador());
            }

            gradeExistente.setIdentificador(gradeAtualizada.getIdentificador());
            gradeExistente.setOrdem(gradeAtualizada.getOrdem());
            return gradeRepository.save(gradeExistente);
        }).orElseThrow(() -> new RuntimeException("Grade não encontrada com id: " + id));
    }

    @Transactional
    public void deletarGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade não encontrada com id: " + id));

        // Check for dependencies (Niveis)
        if (!nivelRepository.findByGradeIdOrderByOrdemAscIdentificadorAsc(id).isEmpty()) {
            throw new IllegalStateException("Não é possível deletar a Grade pois ela contém Níveis associados.");
        }

        gradeRepository.deleteById(id);
    }

    @Transactional
    public GradeResponseDTO criarGrade(Long fileiraId, GradeCreateRequestDTO request) {
        Fileira fileira = fileiraRepository.findById(fileiraId)
                .orElseThrow(() -> new IllegalArgumentException("Fileira não encontrada: " + fileiraId));

        Grade grade = new Grade();
        grade.setIdentificador(request.getIdentificador());
        grade.setOrdem(request.getOrdem());
        grade.setFileira(fileira);

        Grade saved = gradeRepository.save(grade);

        return new GradeResponseDTO(
                saved.getId(),
                saved.getIdentificador(),
                saved.getOrdem(),
                fileiraId
        );
    }
}

