package com.multi.bungae.repository;

import com.multi.bungae.domain.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserVO, String> {
    boolean existsUserByUserId(String userId);
    List<UserVO> findByEmail(String email);
}