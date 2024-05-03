package com.multi.bungae.repository;

import com.multi.bungae.domain.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserVO,Long> {
    boolean existsUserById(String id);
    List<UserVO> findByEmail(String email);
}