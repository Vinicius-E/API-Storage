package com.wester.storage.repository;

import com.wester.storage.model.Grade;
import com.wester.storage.model.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {

    Optional<Nivel> findByGradeAndIdentificador(Grade grade, String identificador);

    List<Nivel> findByGradeIdOrderByOrdemAscIdentificadorAsc(Long gradeId);

    Optional<Nivel> findByIdentificadorCompletoLegivel(String identificadorCompletoLegivel);

    List<Nivel> findByGradeIdOrderByOrdemAsc(Long gradeId);

    long countByGradeId(Long gradeId);

    @Query("select n.grade.id from Nivel n where n.id = :nivelId")
    Optional<Long> findGradeIdByNivelId(Long nivelId);

    // Add other custom queries as needed
}

