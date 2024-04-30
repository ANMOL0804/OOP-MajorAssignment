package com.example.oops.Repository;
import com.example.oops.models.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<user, Long> {

   

    Optional<user> findByEmailAndPassword(String name, String password);

    Optional<user> findByEmail(String email);

    boolean existsByEmail(String email);

    
}
