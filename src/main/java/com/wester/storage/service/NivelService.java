package com.wester.storage.service;

import com.wester.storage.dto.GradeResequenceResultDTO;
import com.wester.storage.dto.NivelCreateRequestDTO;
import com.wester.storage.dto.NivelDTO;
import com.wester.storage.dto.NivelResponseDTO;
import com.wester.storage.model.Grade;
import com.wester.storage.model.Nivel;
import com.wester.storage.repository.GradeRepository;
import com.wester.storage.repository.ItemEstoqueRepository; // Needed for deletion check
import com.wester.storage.repository.NivelRepository;
import com.wester.storage.service.exception.ResourceConflictException;
import com.wester.storage.service.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NivelService {

    private final EntityManager entityManager;

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private GradeRepository gradeRepository; // To fetch Grade

    @Autowired
    private ItemEstoqueRepository itemEstoqueRepository; // To check for dependencies

    public NivelService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<Nivel> listarNiveisPorGrade(Long gradeId) {
        return nivelRepository.findByGradeIdOrderByOrdemAscIdentificadorAsc(gradeId);
    }

    @Transactional(readOnly = true)
    public Optional<Nivel> buscarNivelPorId(Long id) {
        return nivelRepository.findById(id);
    }

    @Transactional
    public NivelResponseDTO criarNivel(Long gradeId, NivelCreateRequestDTO request) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Grade não encontrada: " + gradeId));

        Nivel nivel = new Nivel();
        nivel.setIdentificador(request.getIdentificador());
        nivel.setOrdem(request.getOrdem());
        nivel.setGrade(grade);

        Nivel saved = nivelRepository.save(nivel);

        return new NivelResponseDTO(
                saved.getId(),
                saved.getIdentificador(),
                saved.getOrdem(),
                gradeId
        );
    }

    @Transactional
    public Nivel atualizarNivel(Long id, Nivel nivelAtualizado) {
        return nivelRepository.findById(id).map(nivelExistente -> {
            // Ensure Grade isn't changed, or handle it explicitly if allowed
            if (!nivelExistente.getGrade().getId().equals(nivelAtualizado.getGrade().getId())) {
                 throw new IllegalArgumentException("Não é permitido alterar a Grade de um Nível existente.");
            }

            // Check for duplicate identifier within the same Grade
            Optional<Nivel> nivelComMesmoId = nivelRepository.findByGradeAndIdentificador(
                nivelExistente.getGrade(), nivelAtualizado.getIdentificador());

            if (!nivelExistente.getIdentificador().equals(nivelAtualizado.getIdentificador()) &&
                nivelComMesmoId.isPresent() && !nivelComMesmoId.get().getId().equals(id)) {
                throw new IllegalArgumentException("Identificador de Nível já existe nesta Grade: " + nivelAtualizado.getIdentificador());
            }

            nivelExistente.setIdentificador(nivelAtualizado.getIdentificador());
            nivelExistente.setOrdem(nivelAtualizado.getOrdem());

            // TODO: Update identificadorCompletoLegivel if necessary
            // nivelExistente.setIdentificadorCompletoLegivel(generateIdentifier(nivelExistente));

            return nivelRepository.save(nivelExistente);
        }).orElseThrow(() -> new RuntimeException("Nível não encontrado com id: " + id));
    }

    @Transactional
    public GradeResequenceResultDTO deleteAndResequence(Long nivelId) {
        Long gradeId = nivelRepository.findGradeIdByNivelId(nivelId)
                .orElseThrow(() -> new EntityNotFoundException("Nível não encontrado: " + nivelId));

        long total = nivelRepository.countByGradeId(gradeId);
        if (total <= 1) {
            throw new IllegalStateException("Não é possível remover o último nível da grade.");
        }

        nivelRepository.deleteById(nivelId);
        nivelRepository.flush();

        List<Nivel> restantes = nivelRepository.findByGradeIdOrderByOrdemAsc(gradeId);

        try {
            applyTemporarySequence(restantes);
            nivelRepository.saveAll(restantes);
            nivelRepository.flush();

            applyFinalSequence(restantes);
            List<Nivel> saved = nivelRepository.saveAll(restantes);
            nivelRepository.flush();

            return toResultDTO(gradeId, saved);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("Erro de integridade ao resequenciar níveis. Verifique constraints de ordem/identificador.", ex);
        }
    }

    private void applyTemporarySequence(List<Nivel> niveis) {
        int i = 1;
        for (Nivel n : niveis) {
            n.setIdentificador("TMP_" + n.getId());
            n.setOrdem(100000 + i);
            i++;
        }
        entityManager.flush();
    }

    private void applyFinalSequence(List<Nivel> niveis) {
        int ordem = 1;
        for (Nivel n : niveis) {
            n.setIdentificador("N" + ordem);
            n.setOrdem(ordem);
            ordem++;
        }
        entityManager.flush();
    }

    private GradeResequenceResultDTO toResultDTO(Long gradeId, List<Nivel> saved) {
        List<NivelDTO> dtos = new ArrayList<>();
        for (Nivel n : saved) {
            dtos.add(new NivelDTO(n.getId(), n.getIdentificador(), n.getOrdem()));
        }
        return new GradeResequenceResultDTO(gradeId, dtos);
    }

    @Transactional
    public void deleteNivelOnly(Long nivelId) {
        boolean exists = nivelRepository.existsById(nivelId);
        if (!exists) {
            throw new EntityNotFoundException("Nível não encontrado: " + nivelId);
        }

        if (itemEstoqueRepository.existsByNivelId(nivelId)) {
            itemEstoqueRepository.deleteByNivelId(nivelId);
        }

        try {
            nivelRepository.deleteById(nivelId);
            nivelRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException(
                    "Não foi possível remover o nível porque existem registros vinculados a ele (FK/constraint).",
                    ex
            );
        }
    }
}

