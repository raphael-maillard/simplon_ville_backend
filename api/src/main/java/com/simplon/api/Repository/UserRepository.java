package com.simplon.api.Repository;

import com.simplon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User,String> {

     User findByEmail(String email);

     @Transactional
     @Modifying
     @Query(value="DELETE FROM User u WHERE u.email = :email")
     void deleteUser(@Param("email")String email);
}
