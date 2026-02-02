package com.wester.storage.service;

import com.wester.storage.dto.CriarFileiraRequestDTO;
import com.wester.storage.dto.FileiraResponseDTO;
import com.wester.storage.model.AreaEstoque;
import com.wester.storage.model.Fileira;
import com.wester.storage.repository.AreaEstoqueRepository;
import com.wester.storage.repository.FileiraRepository;
import com.wester.storage.repository.GradeRepository; // Needed for deletion check
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FileiraService {

    @Autowired
    private FileiraRepository fileiraRepository;

    @Autowired
    private AreaEstoqueRepository areaEstoqueRepository; // To fetch AreaEstoque

    @Autowired
    private GradeRepository gradeRepository; // To check for dependencies

    @Transactional(readOnly = true)
    public List<Fileira> listarFileirasPorArea(Long areaId) {
        return fileiraRepository.findByAreaEstoqueIdOrderByOrdemAscIdentificadorAsc(areaId);
    }

    @Transactional(readOnly = true)
    public Optional<Fileira> buscarFileiraPorId(Long id) {
        return fileiraRepository.findById(id);
    }

    @Transactional
    public FileiraResponseDTO criar(Long areaId, CriarFileiraRequestDTO request) {
        AreaEstoque area = areaEstoqueRepository.findById(areaId)
                .orElseThrow(() -> new EntityNotFoundException("Área não encontrada: " + areaId));

        Fileira entity = new Fileira();

        // CRÍTICO: garante que o Hibernate vai gerar o ID e nunca tentar inserir 1/2/...
        entity.setId(null);

        entity.setIdentificador(request.getIdentificador());
        entity.setOrdem(request.getOrdem());
        entity.setAreaEstoque(area);

        try {
            Fileira saved = fileiraRepository.save(entity);
            return FileiraResponseDTO.of(
                    saved.getId(),
                    saved.getIdentificador(),
                    saved.getOrdem(),
                    saved.getAreaEstoque().getId()
            );
        } catch (DataIntegrityViolationException ex) {
            // deixa subir como 409 via @ControllerAdvice se você tiver, ou trate aqui se preferir
            throw ex;
        }
    }

    @Transactional
    public Fileira atualizarFileira(Long id, Fileira fileiraAtualizada) {
        return fileiraRepository.findById(id).map(fileiraExistente -> {
            // Ensure AreaEstoque isn't changed, or handle it explicitly if allowed
            if (!fileiraExistente.getAreaEstoque().getId().equals(fileiraAtualizada.getAreaEstoque().getId())) {
                 throw new IllegalArgumentException("Não é permitido alterar a Área de Estoque de uma Fileira existente.");
            }

            // Check for duplicate identifier within the same AreaEstoque
            Optional<Fileira> fileiraComMesmoId = fileiraRepository.findByAreaEstoqueAndIdentificador(
                fileiraExistente.getAreaEstoque(), fileiraAtualizada.getIdentificador());

            if (!fileiraExistente.getIdentificador().equals(fileiraAtualizada.getIdentificador()) &&
                fileiraComMesmoId.isPresent() && !fileiraComMesmoId.get().getId().equals(id)) {
                throw new IllegalArgumentException("Identificador de Fileira já existe nesta Área: " + fileiraAtualizada.getIdentificador());
            }

            fileiraExistente.setIdentificador(fileiraAtualizada.getIdentificador());
            fileiraExistente.setOrdem(fileiraAtualizada.getOrdem());
            return fileiraRepository.save(fileiraExistente);
        }).orElseThrow(() -> new RuntimeException("Fileira não encontrada com id: " + id));
    }

    @Transactional
    public void deletarFileira(Long id) {
        Fileira fileira = fileiraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fileira não encontrada com id: " + id));

        // Check for dependencies (Grades)
        if (!gradeRepository.findByFileiraIdOrderByOrdemAscIdentificadorAsc(id).isEmpty()) {
            throw new IllegalStateException("Não é possível deletar a Fileira pois ela contém Grades associadas.");
        }

        fileiraRepository.deleteById(id);
    }
}

