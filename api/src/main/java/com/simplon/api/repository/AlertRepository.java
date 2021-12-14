package com.simplon.api.repository;

import com.simplon.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * The interface Alert repository.
 */
@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {

    /**
     * Fix it integer.
     *
     * @param name   the name
     * @param now    the now
     * @param id     the id
     * @param status the status
     * @return the integer
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE Alert alert SET alert.fix= :status, alert.updatedBy= :name, alert.updatedAt = :now WHERE alert.id = :id ")
    Integer fixIt(String name, LocalDateTime now, String id, Boolean status);



}
