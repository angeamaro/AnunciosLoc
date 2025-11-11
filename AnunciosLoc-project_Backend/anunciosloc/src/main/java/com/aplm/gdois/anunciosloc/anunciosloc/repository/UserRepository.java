package com.aplm.gdois.anunciosloc.anunciosloc.repository;


import com.aplm.gdois.anunciosloc.anunciosloc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}