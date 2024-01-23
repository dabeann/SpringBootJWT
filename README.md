# Spring Boot Security & JWT

jwtTutorial
- [Spring Boot JWT Tutorial](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-jwt/dashboard)

security1 : Spring Boot Security
- [스프링부트 시큐리티 & JWT 강의](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0/dashboard)

### google login
> **받아오는 정보**  
> ```
> sub = 23129847382,  
> name = 최다빈,  
> given_name = 다빈,  
> family_name = 최,  
> picture = https://lh3.googleusercontent.com/salkdhgas/AAAAAA/AAAAAI,  
> email = dabin6469@gmail.com,  
> email_verified = true,  
> locale = ko  
> ```
> 
> **저장 정보**  
> ```
> username = "google_23129847382",  
> password = "암호화(겟인데어)",  
> email = "dabin6469@gmail.com",  
> role = "ROLE_USER",  
> provider = "google",  
> providerId = "23129847382"
> ```

### naver login
> **받아오는 정보**
> ```
> response =  
> {  resultcode = 00,  
>    message = success,  
>    response = {id = 215465456, email = dabin6469@naver.com, name = 최다빈}
> }
> ```
