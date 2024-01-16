package security1.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import security1.security1.config.auth.PrincipalDetails;
import security1.security1.model.User;
import security1.security1.repository.UserRepository;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(@AuthenticationPrincipal PrincipalDetails principal) { // DI (의존성 주입)
        System.out.println("Principal : " + principal);
        System.out.println("OAuth2 : "+principal.getUser().getProvider());

        // 순차 출력 해보기
        for (GrantedAuthority auth : principal.getAuthorities()) {
            System.out.println(auth.getAuthority());
        }

        return "유저 페이지입니다.";
    }

    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴터 src/main/resources/
        // 뷰리졸버 설정: templates (prefix), .mustache (suffix) 생략가능
        return "index"; // src/main/resources/templates/index.mustache
    }

    @GetMapping("/user")
    public @ResponseBody String user() {
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "manager";
    }

    // Spring Security가 해당 주소를 낚아챔 - SecurityConfig 파일 생성 후 작동 안함
    @GetMapping("/loginForm")
    public String login() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/joinProc")
    public String joinProc(User user) {
        System.out.println("회원가입 진행 : " + user);

        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword); // 인코딩된 pwd

        user.setRole("ROLE_USER");

        userRepository.save(user);
        return "redirect:/loginForm";
    }

    // ROLE_ADMIN일 때만 접근 가능
    // @Secured: 권한 인증만 할 때 편하게 사용
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    // @PreAuthorize: 메서드가 실행되기 전에 인증을 거친다.
    // @PostAuthorize: 메서드가 실행되고 나서 응답을 보내기 전에 인증을 거친다.
    // 위 두 어노테이션: 복잡한 인증과정이 필요할 때 사용
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
