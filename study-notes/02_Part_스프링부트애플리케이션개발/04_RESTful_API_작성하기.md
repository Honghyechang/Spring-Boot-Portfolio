# 04_RESTful_API_작성하기

<!--
# 첫 작성 (4.1)
docs: [Ch04] RESTful API 작성하기 - RESTful API 개념 추가

# 다음 업데이트 (4.2 추가)
docs: [Ch04] RESTful API 작성하기 - JPA 기반 API 서버 구현 추가

# 다음 업데이트 (4.3 추가)
docs: [Ch04] RESTful API 작성하기 - 테스트 방법 추가

# 마지막 완료 (4.4 추가)
docs: [Ch04] RESTful API 작성하기 - 웹 필터 추가 및 완료
-->

## 📌 학습 목표
RESTful API의 개념을 이해하고, Spring Boot와 JPA를 사용하여 실제 API 서버를 구축하는 방법을 익힌다.

---

## 목차

- [4.1 RESTful API란 무엇인가?](#41-restful-api란-무엇인가)
- [4.2 JPA로 RESTful API 서버 만들기](#42-jpa로-restful-api-서버-만들기)
- [4.3 테스트](#43-테스트)
- [4.4 웹 필터](#44-웹-필터)

---

## 4.1 RESTful API란 무엇인가?

### 4.1.1 웹 애플리케이션의 진화

#### 정적 웹페이지 vs 동적 웹페이지

웹 애플리케이션은 크게 두 가지로 나눌 수 있습니다:

| 구분 | 설명 | 담당 서버 |
|-----|------|----------|
| **정적 웹페이지** | 모든 사용자에게 같은 화면을 보여줌 | 웹 서버 (Apache, Nginx) |
| **동적 웹페이지** | 사용자마다, 시간마다 다른 화면을 보여줌 | **WAS (웹 애플리케이션 서버)** |

---

#### 응답 방식의 변화

| 구분 | 전통적인 방식 | RESTful API 방식 |
|-----|-------------|-----------------|
| **서버의 역할** | **화면(HTML)** 까지 완성하여 응답 | **데이터(JSON)** 만 응답 |
| **클라이언트의 역할** | 완성된 HTML을 단순히 표시 | 데이터를 받아서 화면을 동적으로 생성 |
| **주요 응답 내용** | HTML 문서 전체 (텍스트, 이미지, 레이아웃 모두 포함) | JSON 또는 XML 형태의 데이터 |
| **장점** | 초기 로딩 속도 빠름, SEO 유리 | 서버-클라이언트 역할 분리, 다양한 클라이언트 지원 (Web, App, IoT) |
| **기술 예시** | JSP, PHP, ASP | **React, Flutter, Vue.js 등** |

**핵심**: 과거에는 서버가 **완성된 페이지(HTML)** 를 보냈다면, 현재는 **순수한 데이터**만 보내고 클라이언트가 화면을 구성합니다. 이것이 **RESTful API**의 핵심입니다!

---

### 4.1.2 RESTful API를 위한 백엔드 기술

#### 스프링 기술이란?

RESTful API를 구현하기 위한 대표적인 백엔드 기술이 **스프링(Spring)** 입니다.

**"스프링 기술"의 의미**:
- **스프링 프레임워크(Spring Framework)**: 엔터프라이즈급 애플리케이션 개발을 위한 핵심 프레임워크
- **스프링 부트(Spring Boot)**: 스프링을 더 쉽고 빠르게 사용할 수 있도록 설정을 자동화한 도구

**핵심**: "스프링 기술"은 스프링 프레임워크와 스프링 부트 등을 통틀어 말하는 것입니다.

---

### 4.1.3 WAS, 서블릿 컨테이너, 톰캣의 관계

#### 핵심 개념 정리

```
WAS (웹 애플리케이션 서버)
= 웹 서버 기능 + 서블릿 컨테이너 기능

톰캣(Tomcat) = 서블릿 컨테이너 = WAS의 대표적 예시
```

---

#### WAS (Web Application Server)

**정의**: 동적 웹페이지를 응답해주는 서버

**구성**:
- 웹 서버 기능: 정적 콘텐츠(HTML, CSS, JS) 제공
- **서블릿 컨테이너 기능**: 동적 콘텐츠 처리 ⭐ (핵심!)

**WAS만으로도 가능하지만, 실제로는 웹 서버와 분리하는 이유**:

| 구성 | 역할 | 이유 |
|-----|------|------|
| **웹 서버** (Apache, Nginx) | 정적 콘텐츠 처리 | 전문 웹 서버가 더 빠르고 효율적 |
| **WAS** (Tomcat) | 동적 콘텐츠 처리 | 비즈니스 로직 처리에 집중 |

**핵심**: WAS의 핵심은 **서블릿 컨테이너** 기능입니다!

---

#### 서블릿 컨테이너

**정의**: 서블릿의 **생성, 실행, 소멸(생명주기)** 을 책임지는 관리 시스템

**역할**:
- 서블릿 객체의 생명주기 관리
- 웹 서버와의 통신 지원
- 멀티스레드로 동시 요청 처리

**핵심**: 톰캣은 서블릿 컨테이너이며, 서블릿을 관리합니다!

---

#### 톰캣(Tomcat)

**정의**: 서블릿 컨테이너 기능을 수행하는 대표적인 WAS 소프트웨어

```
톰캣 = 서블릿 컨테이너 = WAS
```

---

#### 역할 분담 요약

| 서버 | 역할 | 비고 |
|-----|------|------|
| **웹 서버** | 정적 콘텐츠 처리 (HTML, CSS, JS) | Apache, Nginx |
| **WAS (Tomcat)** | 동적 콘텐츠 처리 | 서블릿 컨테이너 포함 |
| **서블릿 컨테이너** | 서블릿 생명주기 관리 | Tomcat의 핵심 기능 |

---

### 4.1.4 서블릿(Servlet)이란?

#### 서블릿의 정의

**서블릿(Servlet)**: 클라이언트의 요청을 처리하고 응답을 생성하기 위해 작성된 **동적 로직을 수행하는 Java 클래스**

**핵심**: 서블릿은 **동적 웹 서비스의 핵심 구성 요소**이자 Java 웹 프로그래밍의 표준 규약입니다.

---

#### 서블릿 객체와 디스패처 서블릿

**서블릿 클래스의 객체 = 서블릿 객체**

**디스패처 서블릿(DispatcherServlet)**:
- 스프링 프레임워크가 제공하는 **가장 핵심적인 서블릿 객체**
- 서블릿 인터페이스를 구현한 Java 서블릿 클래스

---

#### 디스패처 서블릿의 역할

**1. 프론트 컨트롤러(Front Controller)**:
- 클라이언트의 모든 요청을 가장 먼저 받아 처리하는 **단일 진입점**

**2. 요청 분석 및 위임**:
- 요청을 분석하여 적절한 **컨트롤러(Controller)** 를 찾아 요청을 위임

**3. 응답 취합**:
- 컨트롤러의 처리 결과를 전달받아 클라이언트에게 최종 응답

---

#### 큰 틀에서의 작동 방식

```
클라이언트 요청 (HTTP)
    ↓
웹 서버 (정적 콘텐츠 처리)
    ↓
WAS (Tomcat) - 서블릿 컨테이너
    ↓
디스패처 서블릿 (요청 받음)
    ↓
컨트롤러 (비즈니스 로직 처리)
    ↓
디스패처 서블릿 (응답 취합)
    ↓
WAS (Tomcat)
    ↓
웹 서버
    ↓
클라이언트 응답 (JSON 데이터)
```

---

#### 개발자의 역할과 서블릿의 관계

| 개념 | 개발자의 역할 | 설명 |
|-----|-------------|------|
| **디스패처 서블릿** | 알아야 함 (이해) | 스프링 MVC의 최전방 컨트롤러로서, 모든 HTTP 요청을 받아 컨트롤러로 전달하는 중앙 통제소 역할. 스프링 부트를 사용하면 설정 없이 자동 생성됨. |
| **일반 서블릿 객체** | 만들지 않음 (몰라도 됨) | 과거에는 요청마다 서블릿을 직접 만들고 `web.xml`에 등록했지만, 스프링이 등장하면서 디스패처 서블릿 하나로 모든 요청 처리 가능. |
| **서블릿 컨테이너** | 직접 관리 안 함 (이해) | 서블릿 컨테이너(Tomcat)가 서블릿 객체의 생성, 소멸 등 복잡한 생명주기를 알아서 관리. 개발자는 작동 원리만 이해하면 됨. |

---

#### 핵심 정리

**개발자가 알아야 할 것**:
- ✅ 서블릿 컨테이너가 무엇인지
- ✅ 서블릿이 무엇인지
- ✅ 디스패처 서블릿이 있다는 것

**스프링 부트 환경에서**:
- `DispatcherServlet`이 서블릿 컨테이너에 의해 **자동 생성**
- 개발자는 HTTP 요청을 `@Controller`의 메서드와 매핑하고 **비즈니스 로직 구현에만 집중**
- 실제 Java Servlet 코드를 작성할 필요 없음!

---

### 4.1.5 RESTful API 설계 원칙

RESTful API를 설계할 때는 다음 원칙들을 따라야 효율적이고 일관성 있는 API를 만들 수 있습니다.

---

#### 1. 자원 중심의 설계 (Resource-Oriented) ⭐

**원칙**: 서버가 다루는 **자원(Resource)** 에 집중하여 설계

**핵심**: URL은 **명사(자원)** 로 표현하고, **동사(행동)** 는 포함하지 않음!

**올바른 예시** ✅:
```
GET /members          # 회원 목록 조회
GET /members/1        # ID가 1인 회원 조회
POST /members         # 새 회원 생성
PUT /members/1        # ID가 1인 회원 수정
DELETE /members/1     # ID가 1인 회원 삭제
```

**잘못된 예시** ❌:
```
GET /getMembers       # 동사 포함 (X)
GET /insertMember     # 동사 포함 (X)
GET /updateMember/1   # 동사 포함 (X)
GET /deleteMember/1   # 동사 포함 (X)
```

**핵심**: 자원(member)에만 집중! 동작은 HTTP 메서드로 표현!

---

#### 2. HTTP 메서드를 사용한 의도 표현 ⭐

**원칙**: 자원에 대한 **행동(동사)** 은 HTTP 메서드로 표현

| HTTP 메서드 | 역할 | 예시 |
|-----------|------|------|
| **GET** | 조회 (Read) | `GET /members/1` - ID가 1인 회원 조회 |
| **POST** | 생성 (Create) | `POST /members` - 새 회원 생성 |
| **PUT** | 전체 수정 (Update) | `PUT /members/1` - ID가 1인 회원 전체 수정 |
| **PATCH** | 부분 수정 (Partial Update) | `PATCH /members/1` - ID가 1인 회원 일부 수정 |
| **DELETE** | 삭제 (Delete) | `DELETE /members/1` - ID가 1인 회원 삭제 |

**올바른 예시** ✅:
```
GET /members/1        # 조회
POST /members         # 생성
PUT /members/1        # 수정
DELETE /members/1     # 삭제
```

**잘못된 예시** ❌:
```
GET /members/1/delete      # DELETE 메서드 사용해야 함
POST /members/1/update     # PUT/PATCH 메서드 사용해야 함
```

**핵심**: URL은 자원만, 행동은 HTTP 메서드로!

---

#### 3. 정확하고 일관된 URL 설계

**규칙**:
- ✅ **명사 사용** (동사 X)
- ✅ **소문자 사용**
- ✅ **복수형 사용** (일반적)
- ✅ **하이픈(`-`)** 사용 (단어 구분 시)
- ❌ 언더바(`_`) 사용 금지
- ❌ 카멜 케이스(camelCase) 사용 금지

**올바른 예시** ✅:
```
GET /members
GET /members/1
GET /blog-posts             # 하이픈으로 단어 구분
GET /user-profiles/1
```

**잘못된 예시** ❌:
```
GET /addMember              # 동사 포함 (X)
GET /Members                # 대문자 사용 (X)
GET /add_member             # 언더바 사용 (X)
GET /blogPosts              # 카멜 케이스 (X)
GET /delete/member/32       # 동사 포함 (X)
```

**핵심**: 소문자 + 복수형 + 하이픈 조합!

---

#### 4. HTTP 상태 코드로 응답 ⭐

**원칙**: 요청 처리 결과를 **HTTP 상태 코드**로 명확하게 전달

**주요 상태 코드**:

| 코드 | 범위 | 의미 | 예시 |
|-----|------|------|------|
| **200 OK** | 2xx | 요청 성공 | 조회/수정/삭제 성공 |
| **201 Created** | 2xx | 생성 성공 | POST 요청으로 자원 생성 성공 |
| **204 No Content** | 2xx | 성공했지만 응답 본문 없음 | DELETE 성공 |
| **304 Not Modified** | 3xx | 자원 변경 없음 (캐시 사용) | 캐시된 자원 사용 |
| **400 Bad Request** | 4xx | 잘못된 요청 | 유효성 검증 실패 |
| **401 Unauthorized** | 4xx | 인증 필요 | 로그인 필요 |
| **403 Forbidden** | 4xx | 권한 없음 | 접근 권한 없음 |
| **404 Not Found** | 4xx | 자원 없음 | 존재하지 않는 ID 조회 |
| **500 Internal Server Error** | 5xx | 서버 오류 | 서버 내부 오류 |

**상태 코드 범위**:
- **2xx**: 성공
- **3xx**: 리다이렉션 (자원 위치 변경 등)
- **4xx**: 클라이언트 오류
- **5xx**: 서버 오류

**핵심**: 응답 본문뿐만 아니라 **상태 코드로도 명확하게 결과 전달**!

---

#### 5. Stateless (무상태성)

**원칙**: 서버는 클라이언트의 **이전 요청을 저장하지 않음**

**의미**:
- 같은 클라이언트의 요청이어도 **매번 독립적인 새로운 요청**으로 처리
- 서버는 클라이언트의 상태를 기억하지 않음

**장점**:
- ✅ 특정 서버에 대한 의존성 감소
- ✅ 서버 확장성 향상 (로드 밸런싱 용이)
- ✅ 서버 장애 시 다른 서버로 요청 전환 가능

**문제**: 매번 인증 등 복잡한 과정을 거쳐야 하는가?

**해결**:
- **쿠키(Cookie)**: 클라이언트 측에 상태 정보 저장
- **세션(Session)**: 서버 측에 상태 정보 저장
- **JWT(JSON Web Token)**: 토큰 기반 인증 (무상태성 유지)

**핵심**: 서버는 상태를 저장하지 않지만, 쿠키/세션/토큰으로 해결 가능!

---

#### 6. 계층화 구조 (Layered System)

**원칙**: 클라이언트와 서버 사이에 **여러 중간 계층**이 존재할 수 있음

**계층 예시**:
```
클라이언트
    ↓
게이트웨이 (로드 밸런서)
    ↓
인증 서버 (토큰 검증)
    ↓
API 서버 (비즈니스 로직)
    ↓
데이터베이스
```

**특징**:
- 각 계층은 **자신의 역할만** 수행
- 클라이언트는 중간 계층의 존재를 **알 필요 없음** (투명성)
- 보안, 로드 밸런싱, 캐싱 등을 계층별로 처리

**핵심**: 클라이언트는 최종 서버만 보이지만, 실제로는 여러 계층을 거침!

---

#### 7. 클라이언트-서버 구조 (Client-Server Architecture)

**원칙**: 클라이언트와 서버의 **역할을 명확히 분리**

| 역할 | 담당 | 책임 |
|-----|------|------|
| **클라이언트** | UI/UX | 사용자 인터페이스, 화면 표시 |
| **서버** | 데이터 + 비즈니스 로직 | 데이터 처리, 비즈니스 규칙 적용 |

**RESTful API의 동작**:
```
1. 서버: 데이터 처리 후 JSON 형태로 응답
2. 클라이언트: JSON 데이터를 받아 화면 구성
   - 웹 브라우저 (React, Vue.js)
   - 모바일 앱 (Flutter, React Native)
   - 데스크톱 앱
```

**장점**:
- ✅ 서버와 클라이언트 독립적으로 개발 가능
- ✅ 하나의 API로 여러 플랫폼 지원 (Web, App, IoT)
- ✅ 유지보수 용이

**핵심**: 서버는 데이터만, 클라이언트는 화면만!

---

#### 8. 캐시 가능 (Cacheable)

**원칙**: 응답 데이터를 **캐시**하여 성능 향상

**문제**: 같은 클라이언트가 같은 요청을 반복하면 서버가 매번 처리해야 하는가?

**해결**: 캐시 기능 활용

**캐시 방법**:

**1. ETag (Entity Tag) 방식**:
```
1. 서버: 응답 시 데이터의 버전(해시값)을 ETag로 전송
   Response: ETag: "abc123"

2. 클라이언트: 재요청 시 ETag를 함께 전송
   Request: If-None-Match: "abc123"

3. 서버: 데이터가 변경되지 않았다면
   Response: 304 Not Modified (데이터 전송 X)
   → 클라이언트는 캐시된 데이터 사용
```

**2. 시간 기반 캐시**:
```
1. 서버: 응답 시 유효 시간 지정
   Response: Cache-Control: max-age=3600 (1시간 유효)

2. 클라이언트: 유효 시간 내에는 캐시된 데이터 사용
   → 서버 요청 없이 즉시 응답
```

**장점**:
- ✅ 서버 부하 감소
- ✅ 네트워크 트래픽 감소
- ✅ 응답 속도 향상

**핵심**: 반복 요청 시 캐시를 활용하여 성능 향상!

---

### 4.1.6 RESTful API 설계 원칙 요약

| 원칙 | 핵심 내용 |
|-----|----------|
| **1. 자원 중심** | URL은 명사로, 동사는 HTTP 메서드로 |
| **2. HTTP 메서드** | GET(조회), POST(생성), PUT(수정), DELETE(삭제) |
| **3. 일관된 URL** | 소문자 + 복수형 + 하이픈 |
| **4. 상태 코드** | 2xx(성공), 4xx(클라이언트 오류), 5xx(서버 오류) |
| **5. 무상태성** | 서버는 상태 저장 X (쿠키/세션/토큰으로 해결) |
| **6. 계층화** | 중간 계층 존재 (게이트웨이, 인증 서버 등) |
| **7. 클라이언트-서버** | 역할 분리 (UI vs 데이터/로직) |
| **8. 캐시** | ETag, 시간 기반 캐시로 성능 향상 |

---



## 4.2 JPA로 RESTful API 서버 만들기

이번 장에서는 **게시판 회원 관리 시스템**을 구현하기 위한 RESTful API 서버를 작성합니다. API를 통해 회원 정보의 **CRUD(생성, 조회, 수정, 삭제)** 작업을 수행할 수 있습니다.

---

### 4.2.1 Controller와 Repository의 역할

RESTful API 서버를 구현하기 위해서는 **Controller**와 **Repository** 두 가지 핵심 계층이 필요합니다.

| 구성 요소 | 역할 | 계층 | 위치 |
|---------|------|------|------|
| **Controller** | 클라이언트의 요청을 받아 처리하고 결과를 반환 | **표현 계층(Presentation Layer)** | 최전방 (클라이언트와 가장 가까움) |
| **Repository** | 데이터베이스에 접근하여 정보를 저장/관리 | **영속성 계층(Persistence Layer)** | 내부 (데이터베이스와 연결) |

---

#### 요청 처리 흐름

```
클라이언트 (HTTP 요청)
    ↓
WAS (Tomcat)
    ↓
디스패처 서블릿 (DispatcherServlet)
    ↓
컨트롤러 (Controller) - 요청 처리 로직
    ↓
리포지토리 (Repository) - DB 접근
    ↓
데이터베이스 (H2)
    ↓
리포지토리 → 컨트롤러 → 디스패처 서블릿 → 클라이언트 (HTTP 응답)
```

**핵심**: 클라이언트가 요청하면 **WAS(Tomcat)** 이 디스패처 서블릿에게 전달하고, 디스패처 서블릿이 대응되는 **Controller**를 찾아 요청을 위임한 후 그 결과를 반환합니다.

---

### 4.2.2 프로젝트 환경 설정

#### 의존성 추가 (build.gradle)

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    compileOnly 'org.projectlombok:lombok'
    runtimeOnly 'com.h2database:h2'
    annotationProcessor 'org.projectlombok:lombok'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

**주요 의존성 설명**:

| 의존성 | 역할 |
|-------|------|
| **spring-boot-starter-web** | 내장 서버(Tomcat)를 이용하여 HTTP 프로토콜로 컨트롤러에 접근 가능. HTTP 요청을 받아 Tomcat 서버에서 처리하고, 컨트롤러와 매칭시켜 결과를 반환하는 작업을 수행. |
| **spring-boot-starter-data-jpa** | Spring Data JPA를 사용하여 데이터베이스 작업을 간편하게 처리. |
| **h2** | 인메모리 데이터베이스. 개발/테스트 환경에서 간편하게 사용 가능. |

---

#### H2 데이터베이스 콘솔 활성화

**application.properties 설정**:

```properties
spring.application.name=restfulapiSample

# H2 콘솔 활성화
spring.h2.console.enabled=true
```

---

#### H2 콘솔 사용 방법

**1. 애플리케이션 실행**:
- IntelliJ의 Gradle 탭에서: `Tasks` → `application` → `bootRun` 실행
- 또는 터미널에서: `./gradlew bootRun`

**2. H2 콘솔 접속**:
- 브라우저에서 `http://localhost:8080/h2-console` 접속

**3. 연결 정보 입력**:

| 항목 | 값 | 설명 |
|-----|-----|------|
| **JDBC URL** | `jdbc:h2:mem:testdb` | 인메모리 모드로 실행. 콘솔 로그에 표시된 URL을 사용할 수도 있음 (예: `jdbc:h2:mem:8c984628-...`) |
| **User Name** | `sa` | 기본 사용자 이름 |
| **Password** | (빈칸) | 기본 패스워드 없음 |

**4. 실시간 데이터 확인**:
- `bootRun`을 실행한 상태에서 H2 콘솔을 통해 실시간으로 데이터베이스 상태를 확인할 수 있습니다.
- SQL 쿼리를 직접 실행하여 테이블 생성, 데이터 조회 등이 가능합니다.

---

#### bootRun 실행 상태와 역할

| 항목 | 상태 및 역할 |
|-----|-------------|
| **bootRun** | 실행 중 유지. 종료하지 않고 계속 실행 상태를 유지하면서 개발 진행. |
| **스프링 부트 애플리케이션** | 실행 중. 작성한 모든 컨트롤러, 서비스, 비즈니스 로직이 메모리에 로드되어 요청 대기. |
| **내장 Tomcat (WAS)** | 실행 중. 8080 포트에서 클라이언트의 HTTP 요청을 수신 대기. |

**핵심**: `bootRun`을 끄지 않고 웹 브라우저나 API 테스트 도구(Postman, Thunder Client 등)를 통해 `localhost:8080/특정경로`로 요청을 보내면서 실시간으로 결과를 확인합니다.

---

#### H2 인메모리 데이터베이스 특징

| 모드 | JDBC URL | 데이터 유지 여부 | 특징 |
|-----|----------|----------------|------|
| **인메모리** | `jdbc:h2:mem:testdb` | ❌ (재시작 시 초기화) | 서버를 껐다 켤 때마다 항상 깨끗한 상태로 시작. 테스트에 용이. |
| **파일 기반** | `jdbc:h2:file:./data/testdb` | ✅ (재시작 후에도 유지) | 데이터를 파일로 저장하여 영구 보관 가능. |

**주의**: H2를 **인메모리 모드**로 사용하면 애플리케이션을 재시작할 때마다 데이터가 초기화됩니다. 데이터를 유지하려면 파일 기반 모드로 변경해야 합니다.

---

### 4.2.3 Entity와 Repository 작성

#### Entity 클래스 작성 (Member.java)

**Entity**는 데이터베이스 테이블과 매핑되는 Java 클래스입니다.

```java
package com.example.restfulapiSample.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private Integer age;
}
```

**어노테이션 설명**:

| 어노테이션 | 역할 |
|----------|------|
| `@Entity` | JPA가 관리하는 엔티티 클래스로 지정. 데이터베이스 테이블과 매핑됨. |
| `@Id` | 기본 키(Primary Key) 필드 지정. |
| `@GeneratedValue` | 기본 키 값을 자동 생성. `IDENTITY` 전략은 DB가 자동으로 값을 증가시킴. |
| `@Builder` | 빌더 패턴으로 객체 생성 가능 (예: `Member.builder().name("홍길동").build()`). |
| `@AllArgsConstructor` | 모든 필드를 포함하는 생성자 자동 생성. |
| `@NoArgsConstructor` | 기본 생성자 자동 생성 (JPA 필수). |
| `@Data` | Getter, Setter, toString, equals, hashCode 자동 생성. |

---

#### Repository 인터페이스 작성 (MemberRepository.java)

**Repository**는 데이터베이스 접근을 위한 인터페이스입니다.

```java
package com.example.restfulapiSample.repository;

import com.example.restfulapiSample.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

**설명**:
- `JpaRepository<Member, Long>`: Member 엔티티를 다루며, 기본 키 타입은 Long
- 기본 CRUD 메서드 자동 제공:
  - `save()`: 저장/수정
  - `findAll()`: 전체 조회
  - `findById()`: ID로 조회
  - `deleteById()`: ID로 삭제

**핵심**: `JpaRepository`를 상속받으면 기본적인 CRUD 메서드를 구현 없이 사용할 수 있습니다!

---

### 4.2.4 Controller 작성

#### MemberController.java 전체 코드

```java
package com.example.restfulapiSample.controller;

import com.example.restfulapiSample.model.Member;
import com.example.restfulapiSample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")  // 기본 경로
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @PostMapping()
    public Member post(@RequestBody Member member) {
        return memberRepository.save(member);
    }

    @GetMapping
    public List<Member> getAll() {
        return memberRepository.findAll();
    }

    @GetMapping("/{id}")
    public Member get(@PathVariable("id") Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Member put(@PathVariable("id") Long id, @RequestBody Member member) {
        member.setId(id);
        return memberRepository.save(member);
    }

    @PatchMapping("/{id}")
    public Member patch(@PathVariable("id") Long id, @RequestBody Member patch) {
        Member member = memberRepository.findById(id).orElse(null);
        if (member != null) {
            if (patch.getAge() != null) {
                member.setAge(patch.getAge());
            }
            if (patch.getName() != null) {
                member.setName(patch.getName());
            }
            if (patch.getEmail() != null) {
                member.setEmail(patch.getEmail());
            }
            memberRepository.save(member);
        }
        return member;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        memberRepository.deleteById(id);
    }
}
```

---

#### 4.2.4.1 Controller 핵심 어노테이션

**클래스 레벨 어노테이션**:

| 어노테이션 | 역할 |
|----------|------|
| `@RestController` | `@Controller` + `@ResponseBody`의 결합. 반환 값을 자동으로 JSON으로 변환하여 HTTP 응답 본문에 전송. |
| `@RequestMapping("/api/members")` | 이 컨트롤러의 **기본 경로(Base URI)** 설정. 모든 메서드는 `/api/members`로 시작하는 경로를 처리. |
| `@RequiredArgsConstructor` | `final` 필드에 대한 생성자를 자동 생성 (의존성 주입). |

---

#### @Controller vs @RestController

| 특징 | @Controller | @RestController |
|-----|------------|----------------|
| **목적** | 전통적인 웹 페이지(화면) 반환 | RESTful API (데이터) 반환 |
| **포함된 기능** | @Controller | @Controller + @ResponseBody |
| **기본 응답** | 뷰(View) 이름 | 데이터(JSON, XML) |
| **사용 시점** | 서버 측에서 HTML을 렌더링할 때 (JSP, Thymeleaf) | 데이터만 주고받는 API를 만들 때 |

**핵심**: `@RestController`를 사용하면 메서드의 반환 값이 자동으로 JSON으로 변환되어 클라이언트에게 전달됩니다!

---

#### 4.2.4.2 HTTP 메서드별 기능 구현

**메서드 레벨 어노테이션**:

| RESTful 작업 | HTTP 메서드 | 스프링 어노테이션 | 대체 표현 |
|------------|-----------|----------------|----------|
| **조회 (Read)** | GET | `@GetMapping` | `@RequestMapping(method = RequestMethod.GET)` |
| **생성 (Create)** | POST | `@PostMapping` | `@RequestMapping(method = RequestMethod.POST)` |
| **전체 수정 (Update)** | PUT | `@PutMapping` | `@RequestMapping(method = RequestMethod.PUT)` |
| **부분 수정 (Partial Update)** | PATCH | `@PatchMapping` | `@RequestMapping(method = RequestMethod.PATCH)` |
| **삭제 (Delete)** | DELETE | `@DeleteMapping` | `@RequestMapping(method = RequestMethod.DELETE)` |

---

#### 4.2.4.3 회원 생성 (POST)

```java
@PostMapping()
public Member post(@RequestBody Member member) {
    return memberRepository.save(member);
}
```

**동작 방식**:

| 단계 | 내용 |
|-----|------|
| **1. 요청 수신** | 클라이언트가 `POST /api/members`로 JSON 데이터 전송 |
| **2. JSON → 객체 변환** | `@RequestBody`와 Jackson이 JSON을 Member 객체로 자동 변환 |
| **3. DB 저장** | `memberRepository.save(member)`로 데이터베이스에 저장 |
| **4. 응답 생성** | 저장된 Member 객체를 JSON으로 변환하여 반환 |

---

**실제 테스트 예시**:

**요청**:
```
POST http://localhost:8080/api/members
Content-Type: application/json

{
    "name": "홍혜창",
    "email": "hyechang@spring.ac.kr",
    "age": 10
}
```

**응답**:
```json
{
    "id": 1,
    "name": "홍혜창",
    "email": "hyechang@spring.ac.kr",
    "age": 10
}
```

**상태 코드**: `200 OK`

---

**헤더 설정의 의미**:

| Key (헤더 이름) | Value (설정 값) | 의미 |
|---------------|---------------|------|
| **Content-Type** | `application/json` | 클라이언트가 서버로 보내는 요청 본문의 데이터 형식은 JSON |
| **Accept** | `application/json` | 클라이언트가 서버로부터 응답받기를 원하는 데이터 형식은 JSON |

**핵심**: "나는 JSON 형식으로 보낼 테니, 응답도 JSON 형식으로 줘!"라는 명시적인 통신 규약을 서버에게 전달.

---

#### 4.2.4.4 전체 회원 조회 (GET)

```java
@GetMapping
public List<Member> getAll() {
    return memberRepository.findAll();
}
```

**동작 방식**:

| 단계 | 내용 |
|-----|------|
| **1. 요청 수신** | 클라이언트가 `GET /api/members`로 요청 |
| **2. DB 조회** | `memberRepository.findAll()`로 모든 회원 조회 |
| **3. 응답 생성** | List<Member>를 JSON 배열로 변환하여 반환 |

---

**실제 테스트 예시**:

**요청**:
```
GET http://localhost:8080/api/members
```

**응답**:
```json
[
    {
        "id": 1,
        "name": "홍혜창",
        "email": "hyechang@spring.ac.kr",
        "age": 10
    },
    {
        "id": 2,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 30
    },
    {
        "id": 3,
        "name": "홍길동",
        "email": "hong@spring.ac.kr",
        "age": 20
    }
]
```

---

#### 4.2.4.5 특정 회원 조회 (GET with Path Variable)

```java
@GetMapping("/{id}")
public Member get(@PathVariable("id") Long id) {
    return memberRepository.findById(id).orElse(null);
}
```

**동작 방식**:

| 요소 | 코드 | 설명 |
|-----|------|------|
| **HTTP 메서드** | `@GetMapping` | GET 요청을 받음 (데이터 조회) |
| **경로 지정** | `("/{id}")` | URL 경로의 마지막 세그먼트가 변수(id)임을 나타냄 |
| **메서드 정의** | `public Member get(...)` | 반환 타입이 Member이므로 JSON으로 자동 변환되어 응답 |
| **경로 변수 매핑** | `@PathVariable("id") Long id` | URL 경로의 `{id}` 값을 추출하여 메서드 인자 `id`에 바인딩 |
| **데이터 조회** | `memberRepository.findById(id)` | JPA 레포지토리를 사용하여 해당 ID의 회원 조회 |
| **NULL 처리** | `.orElse(null)` | 조회 결과가 없을 경우 `null` 반환 |

---

**@PathVariable의 역할**:

`@PathVariable`은 URL 경로 자체에 포함된 데이터를 메서드 인자로 가져오는 역할입니다.

| 요청 URL 예시 | @PathVariable 작동 |
|-------------|-------------------|
| `/api/members/123` | 경로의 `123`을 추출하여 `Long id` 변수에 **123** 할당 |
| `/api/members/1` | 경로의 `1`을 추출하여 `Long id` 변수에 **1** 할당 |

---

**실제 테스트 예시**:

**요청**:
```
GET http://localhost:8080/api/members/1
```

**응답** (회원이 존재할 때):
```json
{
    "id": 1,
    "name": "홍혜창",
    "email": "hyechang@spring.ac.kr",
    "age": 10
}
```

**상태 코드**: `200 OK`

---

**경로 변수 이름과 메서드 인자 이름**:

코드에서 `get(@PathVariable("id") Long id)`와 같이 어노테이션 안에 **"id"**를 명시한 것은, URL 경로 변수 이름(`{id}`)과 Java 메서드 인자 이름(`id`)을 명시적으로 매핑한 것입니다.

만약 Java 인자 이름을 `memberId`로 변경하고 싶다면:

```java
// URL의 {id} 경로 변수 값을 Java의 memberId 변수에 할당
@GetMapping("/{id}")
public Member get(@PathVariable("id") Long memberId) {
    return memberRepository.findById(memberId).orElse(null);
}
```

---

**현재 문제점** ⚠️:

존재하지 않는 ID를 조회하면:

**요청**:
```
GET http://localhost:8080/api/members/100
```

**응답**:
```
null
```

**상태 코드**: `200 OK`

**문제**: RESTful API 설계 원칙에 따르면 자원이 없을 때는 **404 Not Found** 오류를 반환해야 하는데, 현재는 `200 OK`와 함께 `null`을 반환하고 있습니다.

**해결**: 다음 장에서 예외 처리를 통해 이 문제를 해결합니다!

---

#### 4.2.4.6 회원 수정 (PUT vs PATCH)

**PUT과 PATCH의 차이점**:

| 구분 | PUT | PATCH |
|-----|-----|-------|
| **의미** | 리소스 **전체**를 완전히 교체 | 리소스의 **일부**만 수정 |
| **미입력 필드** | `null`로 덮어씀 | 기존 값 유지 |
| **예시** | 회원 정보의 모든 필드를 새로운 내용으로 덮어쓰기 | 회원 정보 중 이름만 변경하기 |

---

**PUT - 전체 수정**:

```java
@PutMapping("/{id}")
public Member put(@PathVariable("id") Long id, @RequestBody Member member) {
    member.setId(id);
    return memberRepository.save(member);
}
```

---

**PUT 동작 예시**:

**이전 상태** (DB):
```json
{
    "id": 1,
    "name": "홍길동",
    "email": "hong@spring.ac.kr",
    "age": 20
}
```

**요청 1** - 모든 필드 포함:
```
PUT http://localhost:8080/api/members/1
Content-Type: application/json

{
    "name": "고길동",
    "email": "hong@spring.ac.kr",
    "age": 25
}
```

**이후 상태** (DB):
```json
{
    "id": 1,
    "name": "고길동",
    "email": "hong@spring.ac.kr",
    "age": 25
}
```

✅ **결과**: 모든 필드가 요청한 값으로 수정됨.

---

**요청 2** - 일부 필드만 포함:
```
PUT http://localhost:8080/api/members/1
Content-Type: application/json

{
    "name": "고길동",
    "age": 25
}
```

**이후 상태** (DB):
```json
{
    "id": 1,
    "name": "고길동",
    "email": null,
    "age": 25
}
```

⚠️ **주의**: `email` 필드를 보내지 않았더니 `null`로 덮어써짐!

**핵심**: PUT은 **"보내준 내용으로 전체를 바꿔버려!"**

---

**PATCH - 부분 수정**:

```java
@PatchMapping("/{id}")
public Member patch(@PathVariable("id") Long id, @RequestBody Member patch) {
    Member member = memberRepository.findById(id).orElse(null);
    if (member != null) {
        if (patch.getAge() != null) {
            member.setAge(patch.getAge());
        }
        if (patch.getName() != null) {
            member.setName(patch.getName());
        }
        if (patch.getEmail() != null) {
            member.setEmail(patch.getEmail());
        }
        memberRepository.save(member);
    }
    return member;
}
```

**동작 방식**:
1. 기존 회원 정보를 DB에서 조회
2. 요청으로 들어온 필드만 확인하여 `null`이 아닌 값만 수정
3. 수정된 회원 정보를 DB에 저장

---

**PATCH 동작 예시**:

**이전 상태** (DB):
```json
{
    "id": 1,
    "name": "홍길동",
    "email": "hong@spring.ac.kr",
    "age": 20
}
```

**요청** - 이름만 변경:
```
PATCH http://localhost:8080/api/members/1
Content-Type: application/json

{
    "name": "고길동"
}
```

**이후 상태** (DB):
```json
{
    "id": 1,
    "name": "고길동",
    "email": "hong@spring.ac.kr",
    "age": 20
}
```

✅ **결과**: `name`만 수정되고, `email`과 `age`는 기존 값 유지!

**핵심**: PATCH는 **"보낸 내용만 부분적으로 수정해줘!"**

---

**PUT vs PATCH 비교 표**:

| 시나리오 | PUT 결과 | PATCH 결과 |
|---------|---------|-----------|
| name만 전송 | name: 새값<br>email: **null**<br>age: **null** | name: 새값<br>email: 기존값<br>age: 기존값 |
| 모든 필드 전송 | 모두 새값으로 교체 | 모두 새값으로 교체 |

---

#### 4.2.4.7 회원 삭제 (DELETE)

```java
@DeleteMapping("/{id}")
public void delete(@PathVariable("id") Long id) {
    memberRepository.deleteById(id);
}
```

**동작 방식**:

| 단계 | 내용 |
|-----|------|
| **1. 요청 수신** | 클라이언트가 `DELETE /api/members/{id}`로 요청 |
| **2. DB 삭제** | `memberRepository.deleteById(id)`로 해당 ID의 회원 삭제 |
| **3. 응답 생성** | 반환 타입이 `void`이므로 응답 본문 없이 상태 코드만 반환 |

---

**실제 테스트 예시**:

**이전 상태** (DB):
```json
[
    {
        "id": 1,
        "name": "고길동",
        "email": "hong@spring.ac.kr",
        "age": 20
    },
    {
        "id": 2,
        "name": "홍혜창",
        "email": "honghyechang@spring.ac.kr",
        "age": 25
    },
    {
        "id": 3,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 44
    }
]
```

**요청**:
```
DELETE http://localhost:8080/api/members/2
```

**응답**:
- 응답 본문: (없음)
- 상태 코드: `200 OK`

**이후 상태** (DB):
```json
[
    {
        "id": 1,
        "name": "고길동",
        "email": "hong@spring.ac.kr",
        "age": 20
    },
    {
        "id": 3,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 44
    }
]
```

✅ **결과**: ID가 2인회원이 삭제됨!

---

### 4.2.5 @RestController의 데이터 변환 메커니즘

#### 요청부터 응답까지의 전체 흐름

`@RestController`는 RESTful API 서버를 구축할 때, **데이터(JSON)** 를 받고 다시 **데이터(JSON)** 로 응답하는 과정을 극도로 단순화시켜주는 스프링의 핵심 어노테이션입니다.

---

#### 1. 요청 처리: JSON → 객체 변환

| 구성 요소 | 역할 |
|---------|------|
| **@RequestBody** | 클라이언트의 HTTP 요청 본문(JSON 문자열)을 읽어 들임 |
| **메시지 컨버터 (Jackson)** | JSON 문자열을 개발자가 지정한 Java 객체(Member 등)로 자동 변환 |
| **결과** | 개발자는 메서드 파라미터에서 바로 사용 가능한 Java 객체를 받음 |

**예시**:
```java
@PostMapping()
public Member post(@RequestBody Member member) {
    // member 객체는 이미 JSON에서 변환된 상태
    return memberRepository.save(member);
}
```

---

#### 2. 응답 처리: 객체 → JSON 변환 (핵심)

이것이 `@RestController`의 가장 큰 특징입니다!

| 구성 요소 | 역할 |
|---------|------|
| **@RestController** | `@Controller` + `@ResponseBody`의 기능을 모두 포함 |
| **@ResponseBody (자동 적용)** | 메서드에서 반환하는 Java 객체(`return member;`)를 HTTP 응답 본문으로 직접 전송하도록 지시 |
| **메시지 컨버터 (Jackson)** | 반환된 Java 객체를 읽어 들여 JSON 문자열로 자동 변환 |
| **결과** | 클라이언트는 JSON 형식의 순수한 데이터를 응답으로 받음 |

---

#### 핵심 요약표

| 항목 | 기능 |
|-----|------|
| **@RestController** | `@Controller` + `@ResponseBody`의 결합으로, 객체 ↔ JSON 변환이 자동으로 일어나는 API 전용 컨트롤러 |
| **@RequestBody** | 요청 시 JSON을 Java 객체로 자동 변환 |
| **@ResponseBody** | 응답 시 Java 객체를 JSON으로 자동 변환 (`@RestController` 사용 시 생략 가능) |
| **메시지 컨버터** | 이 자동 변환(객체 ↔ JSON) 작업을 수행하는 도구가 필요하며, 스프링 부트에서 Jackson 라이브러리가 그 역할을 수행 |
| **메서드 명** | 기능 구현에는 영향을 주지 않지만, **목적(getAllMembers, createMember 등)** 을 반영하여 작성하는 것이 협업과 유지보수를 위해 필수적 |

**핵심**: 이 구조 덕분에 개발자는 HTTP 통신이나 JSON 변환과 같은 복잡한 기술적인 부분 대신, **비즈니스 로직을 담은 Java 객체를 다루는 데만 집중**할 수 있습니다!

---

### 4.2.6 JSON과 Entity 속성 불일치 처리 (DTO 패턴)

실제 개발에서는 클라이언트가 보내는 JSON 필드 이름과 데이터베이스에 매핑되는 `@Entity`의 필드 이름이 다를 수 있습니다. 예를 들어, JSON은 `user_name`, Entity는 `username`인 경우입니다.

이럴 때 `@Entity`를 오염시키지 않고 처리하는 방법이 **DTO 패턴**입니다.

---

#### DTO 패턴의 핵심

| 구분 | @Entity | DTO (Data Transfer Object) |
|-----|---------|---------------------------|
| **역할** | 데이터베이스의 순수한 모델 역할 (DB 스펙 유지) | JSON 통신 스펙을 반영하는 중간 매개체 (어댑터 역할) |
| **불일치 처리** | 건드리지 않음 (DB 무결성 유지) | JSON 스펙을 반영하여 변환을 전담 |

---

#### DTO를 사용한 데이터 흐름 및 변환

| 단계 | 입력/출력 객체 | 사용 기술 | 불일치 해결책 |
|-----|--------------|----------|-------------|
| **요청 (JSON → Java)** | MemberRequest (DTO) | `@RequestBody`와 Jackson | DTO 필드에 `@JsonProperty("JSON_필드명")`을 사용하여 JSON의 이름과 DTO 필드를 매핑 |
| **중간 변환** | DTO → Entity | 개발자가 직접 작성한 `toEntity()` 메서드 | DTO의 데이터를 꺼내 `@Entity Member` 객체를 생성하거나 업데이트 |
| **응답 (Java → JSON)** | MemberResponse (DTO) | `@RestController` (= `@ResponseBody` 포함)와 Jackson | Entity에서 필요한 데이터만 꺼내 **MemberResponse**를 만들고, 필드 이름이 다를 경우 여기에 `@JsonProperty`를 사용하여 JSON 이름으로 출력되게 함 |

---

#### DTO 사용 시의 개발 편의성

**Jackson 작동**: JSON을 DTO로 변환하기 위해, DTO 클래스에는 다음과 같은 Lombok 어노테이션을 붙여 Jackson의 자동 매핑을 돕습니다:
- `@NoArgsConstructor`: 기본 생성자 (Jackson 필수)
- `@Getter`, `@Setter` (혹은 `@Data`): Getter/Setter 제공

**최종 결론**: `@RestController`의 자동 JSON 변환 기능을 활용하되, **DTO를 사용하여 JSON과 Entity 스펙의 충돌을 분리**하고, `@JsonProperty`는 **Entity가 아닌 DTO에 적용**함으로써 안정적인 API를 구축할 수 있습니다.

---

#### DTO 패턴 구현 예제

**1. 데이터베이스 모델 (Entity)**

데이터베이스의 규칙을 따르는 핵심 모델입니다. `@JsonProperty` 같은 통신 관련 어노테이션은 사용하지 않습니다.

```java
// Member.java (@Entity)
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor // Lombok: 기본 생성자 (JPA 필수)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DB와 Java 코드의 표준을 따르는 필드명 (username)
    private String username;

    public Member(String username) {
        this.username = username;
    }
}
```

---

**2. 요청 DTO (Request DTO)**

클라이언트가 보내는 JSON 스펙(예: `user_name`)을 받아 Entity로 변환하는 역할을 합니다.

```java
// MemberRequest.java (요청 DTO)
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter // Lombok: Jackson이 JSON 값을 DTO에 주입하기 위해 필요
@NoArgsConstructor // Lombok: Jackson이 DTO 객체를 생성하기 위해 필요
public class MemberRequest {

    // 💡 해결책: JSON의 'user_name' 필드를 DTO의 'username' 필드에 매핑
    @JsonProperty("user_name")
    private String username;

    // DTO를 Entity 객체로 변환하는 메서드
    public Member toEntity() {
        // DTO의 데이터를 Entity 생성자에 전달
        return new Member(this.username);
    }
}
```

---

**3. 응답 DTO (Response DTO)**

서버가 클라이언트에게 JSON으로 응답할 때, Entity에서 필요한 데이터만 추출하고 JSON 스펙을 맞춰줍니다.

```java
// MemberResponse.java (응답 DTO)
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MemberResponse {

    // 💡 해결책: Entity의 'id'를 JSON 응답의 'user_id'로 변환
    @JsonProperty("user_id")
    private final Long id;

    private final String username;

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
    }

    // Entity를 Response DTO로 변환하는 정적 팩토리 메서드
    public static MemberResponse from(Member member) {
        return new MemberResponse(member);
    }
}
```

---

**4. 컨트롤러 (Controller) 로직**

컨트롤러는 오직 DTO만 사용하며, Entity와 DTO 간의 변환을 담당합니다.

```java
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @PostMapping // 새로운 회원 생성 (Create)
    public MemberResponse createMember(@RequestBody MemberRequest requestDto) {

        // 1. DTO를 Entity로 변환 (toEntity() 사용)
        Member member = requestDto.toEntity();

        // 2. Entity를 DB에 저장
        Member savedMember = memberRepository.save(member);

        // 3. 저장된 Entity를 Response DTO로 변환하여 JSON으로 응답
        return MemberResponse.from(savedMember);
    }
}
```

---

**5. 결과 (JSON 통신)**

| 구분 | 전송 데이터 (클라이언트) | 수신 데이터 (클라이언트) |
|-----|----------------------|----------------------|
| **요청 (POST)** | `{ "user_name": "새로운 사용자" }` | `{ "user_id": 101, "username": "새로운 사용자" }` |

**핵심**: DTO가 `@JsonProperty`를 통해 불일치 필드를 매핑하고, Entity를 순수하게 유지하며 통신을 완료합니다.

---

### 4.2.7 포트 설정 변경

스프링 부트 프로젝트는 내장된 **톰캣(WAS)** 을 기본적으로 8080 포트에서 실행합니다. 이 설정을 변경하려면 프로젝트 내의 설정 파일인 **application.properties**에 한 줄을 추가하면 됩니다.

---

#### 포트 설정 파일 위치

포트 설정을 변경하려면 프로젝트의 리소스 폴더에 있는 `src/main/resources/application.properties` 파일을 수정해야 합니다.

---

#### 포트 변경 방법 (application.properties)

사용하려는 포트 번호를 지정하여 다음 설정을 추가하거나 수정합니다. 예를 들어, 포트를 **8090**으로 변경하고 싶다면:

```properties
# server.port 설정이 스프링 부트 내장 WAS(Tomcat)의 포트를 지정합니다.
server.port=8090
```

---

#### 적용 및 확인

1. `application.properties` 파일을 저장합니다.
2. 실행 중이던 스프링 부트 애플리케이션(`bootRun`)을 종료했다가 다시 실행합니다.
3. 애플리케이션이 다시 시작되면, 콘솔 로그에서 톰캣 서버가 새로운 포트(예: 8090)로 초기화되었는지 확인합니다.

```
Tomcat initialized with port 8090 (http)
```

4. 이제부터는 `http://localhost:8090/`과 같이 새로운 포트를 사용하여 WAS에 요청을 보내야 합니다.

---

### 4.2.8 회원 관리 API 전체 동작 흐름 정리

지금까지 작성한 회원 관리 RESTful API의 전체 동작을 정리하면 다음과 같습니다:

---

#### API 엔드포인트 요약

| HTTP 메서드 | 엔드포인트 | 기능 | 요청 본문 | 응답 |
|-----------|----------|------|---------|------|
| **POST** | `/api/members` | 새 회원 생성 | JSON (name, email, age) | 생성된 회원 정보 (JSON) |
| **GET** | `/api/members` | 전체 회원 조회 | 없음 | 전체 회원 목록 (JSON 배열) |
| **GET** | `/api/members/{id}` | 특정 회원 조회 | 없음 | 해당 회원 정보 (JSON) |
| **PUT** | `/api/members/{id}` | 회원 전체 수정 | JSON (name, email, age) | 수정된 회원 정보 (JSON) |
| **PATCH** | `/api/members/{id}` | 회원 부분 수정 | JSON (수정할 필드만) | 수정된 회원 정보 (JSON) |
| **DELETE** | `/api/members/{id}` | 회원 삭제 | 없음 | 응답 본문 없음 |

---

#### 데이터 흐름도

```
[클라이언트] 
    ↓ HTTP 요청 (JSON)
[Tomcat WAS] 
    ↓
[DispatcherServlet] 
    ↓
[MemberController] 
    ↓ @RequestBody (JSON → Member 객체)
[비즈니스 로직 처리]
    ↓
[MemberRepository] 
    ↓
[H2 Database]
    ↓
[MemberRepository] 
    ↓
[MemberController] 
    ↓ @ResponseBody (Member 객체 → JSON)
[DispatcherServlet] 
    ↓
[Tomcat WAS] 
    ↓ HTTP 응답 (JSON)
[클라이언트]
```

---

### 4.2.9 현재까지의 성과와 다음 과제

#### 완성된 기능 ✅

- ✅ H2 인메모리 데이터베이스 연동
- ✅ JPA Entity 및 Repository 구성
- ✅ RESTful API Controller 작성
- ✅ CRUD 전체 기능 구현 (생성, 조회, 수정, 삭제)
- ✅ PUT과 PATCH의 차이 구현
- ✅ JSON ↔ 객체 자동 변환

---

#### 현재 남아있는 문제점 ⚠️

**1. 예외 처리 부재**:
- 존재하지 않는 회원을 조회하면 `null`을 반환하고 `200 OK`를 응답
- RESTful 원칙에 따르면 `404 Not Found`를 반환해야 함

**2. 비즈니스 로직과 Controller의 혼재**:
- Controller에 데이터 처리 로직이 직접 포함되어 있음
- Service 계층 분리 필요

**3. 유효성 검증 부재**:
- 잘못된 데이터(예: 음수 나이, 빈 이메일)가 그대로 저장됨
- Validation 추가 필요

---



