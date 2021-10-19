package com.simplon.api.Repository;

import com.simplon.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {

    @Modifying
    @Query(value = "UPDATE Alert alert SET alert.fix=true, alert.updatedBy= :name, alert.updatedAt = :now WHERE alert.id = :id ")
    Boolean fixIt(String name, LocalDateTime now, String id);

}
