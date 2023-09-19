# PT_Service
--------------------------
## 2개로 나눔


:hammer: 사용기술
--------------------------
*개발환경*
- 운영체제 : Mac OS
- IDE : IntelliJ
--------------------------
*Back-End*

- JAVA11
- Spring boot 2.7.5
- DB: My SQL ,Redis Cache
- Docker
- Gradle
- Spring boot Batch
- Spring JPA


*Front-end*
- Thymeleaf
------------------------------

*CI/CO
- Git Action
- AWS S3,Code Deploy,Ec2,Docker 활용(지속적인 배포와 통합이 가능하게됐다.)

--------------------------------
* 설명
- Redis Cache 를 통해 DB접근을 최소화 하고,보다 빠른데이터를 전송하게 하려고 했습니다.
- Transation 를 통해 동시성 해결(예약 및 일반적인기능 트래픽 최소화) 할려고했습니다
- 만료기간이 짧은 AccessToken 과 일회성 RefreshToke 을 사용해 로그인 유지를 안전하게 유지
