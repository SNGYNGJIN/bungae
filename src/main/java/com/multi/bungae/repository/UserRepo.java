package com.multi.bungae.repository;

import com.multi.bungae.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    boolean existsUserByUserId(String userId);
    List<User> findByEmail(String email);

}