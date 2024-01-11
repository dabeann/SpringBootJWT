package jwtjwt.jwtTutorial.service;

import jwtjwt.jwtTutorial.entity.User;
import jwtjwt.jwtTutorial.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    // SpringSecurity의 UserDetailsService를 구현한 구현체
    /**
     * 1. UserDetailsService를 상속받고, loadUserByUsername() 메서드를 override해 로그인 시 DB에 유저정보와 권한정보를 가져오도록 구현한다.
     * 2. 앞서 정의한 UserRepository를 주입받는다.
     * 3. 로그인 시 DB에서 유저정보와 권한정보를 가져오도록 userRepository.findOneWithAuthritiesByUsername() 메서드를 이용한다.
     * 4. Stream map을 통해 파라미터로 받은 username과 DB에서 조회된 유저정보 User를 createUser() 메서드를 이용해 User 엔티티를 생성한다.
     */

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override // UserDetailsService 클래스의 loadUserByUsername 오버라이딩
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username) // 로그인 시 DB 유저정보와 권한정보를 가져옴
                .map(user -> createUser(username, user)) // 데이터베이스에서 가져온 정보를 기준으로 createUser 메서드 수행
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String username, User user) {

        // DB 에서 가져온 유저가 활성화 상태가 아니라면
        if (!user.isActivated()) {
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }

        // 해당 유저가 활성화 상태라면
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream() // getAuthorities() : 유저의 권한정보
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), // 유저명
                user.getPassword(), // 비밀번호를 가진
                grantedAuthorities); // 유저 객체를 리턴
    }
}