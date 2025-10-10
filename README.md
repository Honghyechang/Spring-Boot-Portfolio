## [Spring-Boot-Portfolio] JPA & AWS 기반 게시판 프로젝트 & 심층 학습 기록

안녕하세요. 이 레포지토리는 **Spring Boot**를 활용하여 백엔드 개발 역량을 쌓고, 그 과정에서 얻은 기술적 이해를 상세히 기록한 **지식 포트폴리오**입니다. 단순히 코드를 넘어, 프레임워크의 원리와 설계 과정에 대한 깊이 있는 고민을 담았습니다.

---

## 1. 프로젝트 개요 (Overview)

책 『이것이 스프링 부트다 with 자바』를 기반으로, 데이터베이스 연동부터 클라우드 배포까지 백엔드 개발의 전 과정을 실습했습니다. 핵심 게시판 애플리케이션을 완성하며 실무에 필요한 기술 스택을 통합적으로 익히는 데 중점을 두었습니다.

### 주요 기능
- **게시글 관리 (CRUD)**: Spring Data JPA를 활용한 영속성 관리
- **사용자 인증/인가**: Spring Security를 이용한 안전한 로그인 및 권한 관리
- **RESTful API**: 프론트엔드 연동을 위한 규격화된 API 설계
- **클라우드 배포**: Docker 컨테이너를 이용한 AWS EC2 환경 배포 (Ch 07)

### 사용 기술 스택
- **Backend**: Java 17, Spring Boot 3.x
- **Database**: H2 (개발 환경), MySQL (운영 환경)
- **ORM**: Spring Data JPA
- **API/View**: Thymeleaf, RESTful API
- **Deployment**: AWS EC2, Docker

---

## 2. 📚 심층 학습 노트 인덱스 (Study Notes Index)

프로젝트를 진행하며 각 챕터에서 학습한 **스프링 부트 핵심 원리와 개념**을 정리했습니다. (클릭하여 Markdown 노트를 확인하세요.)

| Part | Chapter | 주요 학습 내용 | 정리 노트 |
| :--- | :--- | :--- | :--- |
| **Part 1. 기초** | Ch 01 | 스프링 부트 이해, Auto-Configuration 원리 분석 | [노트 보기](./study-notes/01_Part_스프링부트의기초/01_스프링부트_이해하기.md) |
| | Ch 02 | 환경 설정, 컨테이너와 DI 원리 | [노트 보기](./study-notes/01_Part_스프링부트의기초/02_스프링부트_시작하기.md) |
| **Part 2. 개발** | Ch 03 | 데이터베이스 연결 및 JPA 개념 심화 | [노트 보기](./study-notes/02_Part_스프링부트애플리케이션개발/03_데이터베이스_연동하기.md) |
| | Ch 04 | RESTful API 설계 및 DTO 활용 | [노트 보기](./study-notes/02_Part_스프링부트애플리케이션개발/04_RESTful_API_작성하기.md) |
| | Ch 05 | **Spring Security**를 활용한 인증/인가 구현 | [노트 보기](./study-notes/02_Part_스프링부트애플리케이션개발/05_스프링부트로_게시판_만들기.md) |
| **Part 3. 배포** | Ch 06 | Profile 설정 및 JAR 빌드 전략 | [노트 보기](./study-notes/03_Part_빌드및배포/06_프로파일및빌드.md) |
| | Ch 07 | **AWS와 Docker**를 활용한 클라우드 배포 | [노트 보기](./study-notes/03_Part_빌드및배포/07_애플리케이션_배포하기.md) |
| | Ch 08 | **ChatGPT API 연동** 과정 및 원리 | [노트 보기](./study-notes/03_Part_빌드및배포/08_스프링부트와_챗GPT_연동하기.md) |


---

## 3. Git 관리 및 실행 방법

### Git 커밋 컨벤션
프로젝트의 버전 관리에는 **Conventional Commits** 규칙을 적용하여 모든 변경 이력을 명확하게 기록했습니다.
- `feat`: 새로운 기능 추가 (주요 챕터 완료)
- `docs`: 학습 노트 문서 변경


<!--
git commit -m "feat: [Ch02] 스프링 컨테이너 설정 및 의존성 주입 구현"
git commit -m  "docs: [Ch01] 스프링 부트 Auto-Configuration 원리 정리 완료"
-->

### 로컬 환경에서 실행하는 방법
1. 프로젝트를 클론합니다: `git clone [당신의 레포지토리 URL]`
2. `spring-boot-project` 폴더로 이동하여 프로젝트를 IDE (IntelliJ 또는 Eclipse)로 엽니다.
3. [필요한 경우] `application.yml` 파일에서 DB 설정을 로컬 환경에 맞게 변경합니다.
4. 메인 애플리케이션 클래스를 실행합니다.

```markdown
