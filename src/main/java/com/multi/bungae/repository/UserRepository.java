package com.multi.bungae.repository;

import com.multi.bungae.domain.UserVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/*
    * Optional
    - 하나의 객체가 존재할 수도 있고, 존재하지 않을 수도 있는 상황에 적합
    - 데이터베이스에서 특정 조건에 일치하는 단일 엔티티를 검색할 때 주로 사용
    - ex. 특정 userId로 사용자 프로필을 검색하고 싶을 때,
      해당 ID에 해당하는 정확히 하나의 결과만 있을 것으로 예상되는 경우 사용
      (null 처리를 안전하게 하도록 도와줌)

    * List
    - 여러 객체를 반환할 수 있는 상황에 적합
    - 주로 하나 이상의 결과가 예상되거나, 여러 결과를 동시에 처리해야 할 때 사용
    - ex. 같은 이름을 가진 여러 사용자의 데이터를 조회하거나, 특정 조건에 해당하는 모든 리뷰나 블랙리스트 항목을 가져올 때 사용
      (이 경우 조회 결과가 여러 개일 수 있기 때문에 리스트를 사용하는 것이 적절)

    * 레퍼지토리 이름
    - findBy, existsBy, countBy, deleteBy 등으로 시작
        findByUsername: username 필드를 기반으로 데이터를 검색
        countByActiveTrue: active 필드가 true인 엔티티의 수를 세는 쿼리를 생성
        findByUsernameAndAgeGreaterThan: username이 일치하고 age가 주어진 값보다 큰 엔티티 검색
        findByUser_UserId: User 객체 내의 userId 필드를 사용하여 검색
 */
@Repository
public interface UserRepository extends JpaRepository<UserVO, Integer> {
    boolean existsUserByUserId(String userId);
    List<UserVO> findByEmailAndUserBirthAndUsername(String email, String birth, String name);
    Optional<UserVO> findByUserId(String userId);
    Optional<UserVO> findById(Integer id);
}