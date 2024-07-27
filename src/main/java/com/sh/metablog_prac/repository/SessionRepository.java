package com.sh.metablog_prac.repository;

import com.sh.metablog_prac.domain.Session;
import com.sh.metablog_prac.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByAccessToken(String accessToken);
}
