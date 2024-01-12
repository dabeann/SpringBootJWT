package jwtjwt.jwtTutorial.service;

import java.util.Collections;
import jwtjwt.jwtTutorial.dto.UserDto;
import jwtjwt.jwtTutorial.entity.Authority;
import jwtjwt.jwtTutorial.entity.User;
import jwtjwt.jwtTutorial.exception.DuplicateMemberException;
import jwtjwt.jwtTutorial.exception.NotFoundMemberException;
import jwtjwt.jwtTutorial.repository.UserRepository;
import jwtjwt.jwtTutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    /**
     * UserService class: 회원가입, 유저정보 조회 등 메서드 구현 클래스
     *
     * UserRepository, PasswordEncoder를 주입받는다.
     */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * signup() 메서드는 회원가입 로직을 수행하는 메서드이다.
     * 1. username이 DB에 저장돼있는지 확인한다.
     * 2. Authority와 User정보를 생성해서 UserRepository의 save()메서드를 통해 DB에 저장 후 User를 반환한다.
     * 3. 회원가입된 User는 "ROLE_USER"권한정보를 가지고 있다.
     *      - data.sql에 생성되는 admin 계정은 "USER", "ADMIN_ROLE" 두 개의 권한 정보를 가지고 있다.
     */
    // 회원가입 로직 수행
    @Transactional
    public UserDto signup(UserDto userDto) {

        // UserDto의 username을 이용해 DB에 존재하는지 확인
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // DB에 존재하지 않으면 권한정보 생성
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        // 권한정보를 포함한 User 정보를 생성
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        // 최종 설정한 User 정보를 DB에 저장
        return UserDto.from(userRepository.save(user));
    }

    /**
     * getUserWithAuthorities(String username), getMyUserWithAuthorities(): 권한 정보를 가져오는 메서드
     * - getUserWithAuthorities(String username): 어떠한 username이든 원하는 username 기준으로 정보(User, authorities)를 가져온다.
     * - getMyUserWithAuthorities(): 현재 SecurityContext에 저장된 username에 해당하는 정보(User, authorities)만 가져온다.
     */
    // 유저, 권한정보를 가져오는 메서드 1
    // username을 기준으로 정보를 가져옴
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    // 유저, 권한정보를 가져오는 메서드 2
    // SecurityContext에 저장된 username 정보만 가져옴
    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}