package com.washinggod.remkey.repository;

import com.washinggod.remkey.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {

    @Modifying
    @Query("""
            DELETE InvalidToken i
            WHERE i.expiredTime < :now
            """)
    public void deleteExpiredTokens(@Param("now") LocalDateTime now);

}
