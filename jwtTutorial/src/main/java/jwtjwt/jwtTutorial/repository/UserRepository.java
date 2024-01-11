package jwtjwt.jwtTutorial.repository;

import jwtjwt.jwtTutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // User Entity에 매핑되는 repository

    /**
     * 1. JpaRepository를 extends하면서 save, findAll과 같은 메소드를 사용할 수 있다.
     * 2. findOneWithAuthoritiesByUsername(String username): 유저명을 기준으로 User 가져오고 이 때, 권한정보(authorities)도 같이 가져온다.
     * 3. @EntityGraph 어노테이션: query가 수행될 때 Lazy조회가 아닌 Eager조회로 authorities정보를 가져온다.
     */
    // username을 기준으로 User정보를 가져올 권한정보도 같이 가져옴
    @EntityGraph(attributePaths = "authorities") // 쿼리 수행 시 Lazy 조회가 아닌, Eager 조회로 authorities 정보를 같이가져옴
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}