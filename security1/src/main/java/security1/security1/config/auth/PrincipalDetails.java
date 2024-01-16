package security1.security1.config.auth;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Data;
import security1.security1.model.User;

// Security가 /loginProc 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 진행 완료가 되면 Security session을 만들어준다. (Security ContextHolder)
// Authentication 객체에 저장할 수 있는 유일한 타입
// Authentication 안에 User 정보가 있어야 함
// User 오브젝트 타입 -> UserDetails 타입 객체

// Security Session -> Authentication -> UserDetails(PrincipalDetails) or OAuth2User(PrincipalDetails)
// 일반유저 로그인 -> UserDetails
// OAuth 로그인 -> OAuth2User
public class PrincipalDetails implements UserDetails, OAuth2User{

    private static final long serialVersionUID = 1L;
    private User user;
    private Map<String, Object> attributes;

    // 일반 시큐리티 로그인시 사용
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth2.0 로그인시 사용
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되었는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겼는지
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 비밀번호 기간이 지났는지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화되어있는지
    @Override
    public boolean isEnabled() {

        // 1년동안 회원이 로그인 안하면 휴먼 계정으로 하기로 함
        // 현재시간 - 로긴시간 -> 1년을 초과하면 return false;

        return true;
    }

    // 해당 User의 권한을 return
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
        collect.add(()->{ return user.getRole();});
        return collect;
    }

    // 리소스 서버로 부터 받는 회원정보
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // User의 PrimaryKey
    @Override
    public String getName() {
        return user.getId()+"";
    }

}