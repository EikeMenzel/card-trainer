package com.service.databaseservice.repository.cards;

import com.service.databaseservice.model.cards.ChoiceAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChoiceAnswerRepository extends JpaRepository<ChoiceAnswer, Long> {

    List<ChoiceAnswer> getAllByMultipleChoiceCardId(Long multipleChoiceCardId);
    @Modifying
    @Transactional
    @Query("DELETE FROM ChoiceAnswer c WHERE c.id IN :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);
}
