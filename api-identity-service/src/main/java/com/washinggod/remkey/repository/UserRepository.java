package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public boolean existsByUsername(String username);

    public boolean existsByEmail(String email);


}
