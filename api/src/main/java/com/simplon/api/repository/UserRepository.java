package com.simplon.api.repository;

import com.simplon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

     Optional<User> findByEmail(String email);

     Boolean existsByEmail(String email);

     /**
      * Delete user.
      *
      * @param email the email
      */
     @Transactional
     @Modifying
     @Query(value="DELETE FROM User u WHERE u.email = :email")
     void deleteUser(@Param("email")String email);

     /**
      * Update current user password.
      *
      * @param email       the email
      * @param newPassword the new password
      */
     @Transactional
     @Modifying
     @Query(value="UPDATE User u SET u.password = :password WHERE u.email =:email ")
     void updateCurrentUserPassword(@Param("email")String email,@Param("password")String newPassword);
}
