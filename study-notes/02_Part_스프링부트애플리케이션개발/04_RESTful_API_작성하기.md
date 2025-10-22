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


## 4.2.10 Service 계층 분리와 DTO 패턴 적용

### 4.2.10.1 기존 구조의 문제점

#### 이전 코드의 구조

**Controller → Repository 직접 접근**:

```
[Controller (표현 계층)]
    ↓ 직접 접근
[Repository (영속성 계층)]
    ↓
[Database]
```

**문제점**:
- Controller에서 `@Entity` 객체를 `@RequestBody`로 직접 받아 사용
- 도메인 모델(`@Entity`)이 외부에 그대로 노출됨
- 비즈니스 로직이 Controller에 포함됨

---

#### Entity 직접 노출의 위험성

**예시 - Member Entity에 민감한 정보가 포함된 경우**:

```java
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private Integer age;
    
    // ⚠️ 민감한 정보
    private String password;        // 내부 관리 목적의 패스워드
    private Boolean enabled;        // 계정 활성화 여부
}
```

**위험 상황**:

| 시나리오 | 문제점 |
|---------|--------|
| **외부에서 직접 접근 가능** | 클라이언트가 `password` 필드를 포함한 JSON을 보낼 경우 의도치 않게 비밀번호가 변경될 수 있음 |
| **내부 정보 노출** | `enabled` 같은 내부 관리 필드가 외부로 노출되어 보안 문제 발생 |
| **예기치 못한 수정** | 클라이언트가 수정하면 안 되는 필드를 수정할 수 있음 |

**핵심 문제**: 레포지토리에서 관리하는 도메인 모델 객체(`@Entity`)를 그대로 Controller에 노출하는 것은 권장되지 않습니다!

---

### 4.2.10.2 해결 방법: Service 계층 + DTO 패턴

#### DTO (Data Transfer Object)란?

**정의**: 계층 간 데이터를 전달하기 위한 단순한 객체

**역할**:
- `@Entity`를 직접 다루지 않고, 데이터를 전달하는 용도로만 사용
- 필요한 필드만 선택적으로 포함
- 외부에 노출되어도 안전한 데이터만 담음

---

#### 개선된 3계층 구조

```
[Controller (표현 계층)]
    ↓ DTO (MemberRequest, MemberResponse) 사용
[Service (서비스 계층)]  ← 비즈니스 로직 처리
    ↓ Entity (Member) 사용
[Repository (영속성 계층)]
    ↓
[Database]
```

**각 계층의 역할**:

| 계층 | 역할 | 사용 객체 |
|-----|------|---------|
| **Controller** | HTTP 요청/응답 처리, Service 호출 | DTO (MemberRequest, MemberResponse) |
| **Service** | 비즈니스 로직 처리, DTO ↔ Entity 변환 | DTO, Entity |
| **Repository** | 데이터베이스 접근 (CRUD) | Entity |

**핵심**: Controller는 Service에게 요청하고, Service는 Repository에 접근하며, 가운데에서 비즈니스 로직을 처리합니다!

---

### 4.2.10.3 Entity 수정

기존 `Member` 엔티티에 내부 관리 목적의 필드를 추가합니다.

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

    // 추가: 내부 관리 목적의 필드
    private String password;    // 패스워드 (외부 노출 금지)
    private Boolean enabled;    // 계정 활성화 여부 (외부 노출 금지)
}
```

**주의**: 이 필드들은 외부(클라이언트)에 절대 노출되어서는 안 됩니다!

---

### 4.2.10.4 DTO 클래스 작성

#### MemberRequest (요청 DTO)

**역할**: 클라이언트로부터 받는 JSON 데이터를 담는 그릇

```java
package com.example.restfulapiSample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequest {

    private String name;
    private String email;
    private Integer age;
    
    // ✅ password, enabled 필드는 포함하지 않음!
}
```

**핵심**: 클라이언트가 보내야 할 필드만 포함하여, 민감한 정보는 외부에서 접근할 수 없도록 차단!

---

#### MemberResponse (응답 DTO)

**역할**: 클라이언트에게 반환할 응답 데이터를 담는 객체

```java
package com.example.restfulapiSample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponse {
    
    private Long id;
    private String name;
    private String email;
    private Integer age;
    
    // ✅ password, enabled 필드는 포함하지 않음!
}
```

**핵심**: 클라이언트에게 보여줄 필드만 선택적으로 포함하여, 내부 정보는 노출하지 않음!

---

#### Request vs Response DTO 비교

| 구분 | MemberRequest | MemberResponse |
|-----|--------------|---------------|
| **방향** | 클라이언트 → 서버 | 서버 → 클라이언트 |
| **역할** | 요청 데이터 수신 | 응답 데이터 전송 |
| **포함 필드** | name, email, age | id, name, email, age |
| **특징** | `id` 없음 (생성 시 자동 부여) | `id` 포함 (조회 결과) |

---

### 4.2.10.5 Service 계층 작성

#### MemberService.java

```java
package com.example.restfulapiSample.service;

import com.example.restfulapiSample.dto.MemberRequest;
import com.example.restfulapiSample.dto.MemberResponse;
import com.example.restfulapiSample.exception.NotFoundException;
import com.example.restfulapiSample.model.Member;
import com.example.restfulapiSample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // Member → MemberResponse 변환 메서드
    public MemberResponse mapToMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge())
                .build();
    }

    // 회원 생성
    public MemberResponse create(MemberRequest memberRequest) {
        Member member = Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .age(memberRequest.getAge())
                .build();
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    // 전체 회원 조회
    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponse> memberResponses = members.stream()
                .map(m -> mapToMemberResponse(m))
                .collect(Collectors.toList());
        return memberResponses;
    }

    // 특정 회원 조회
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        return mapToMemberResponse(member);
    }

    // 회원 전체 수정 (PUT)
    public MemberResponse update(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        
        // 애초에 없다면 null로 초기화될 것 (전체 교체)
        member.setName(memberRequest.getName());
        member.setEmail(memberRequest.getEmail());
        member.setAge(memberRequest.getAge());
        
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    // 회원 부분 수정 (PATCH)
    public MemberResponse patch(Long id, MemberRequest memberRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        
        // null이 아닌 필드만 수정
        if (memberRequest.getAge() != null) {
            member.setAge(memberRequest.getAge());
        }
        if (memberRequest.getEmail() != null) {
            member.setEmail(memberRequest.getEmail());
        }
        if (memberRequest.getName() != null) {
            member.setName(memberRequest.getName());
        }

        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    // 회원 삭제
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        memberRepository.delete(member);
    }
}
```

---

#### Service 계층의 핵심 역할

| 메서드 | 역할 | 설명 |
|-------|------|------|
| **mapToMemberResponse()** | Entity → Response DTO 변환 | Member 객체를 MemberResponse로 변환 (외부 노출 안전) |
| **create()** | 회원 생성 | Request DTO → Entity 변환 → DB 저장 → Response DTO 반환 |
| **findAll()** | 전체 조회 | Entity 리스트를 Response DTO 리스트로 변환 |
| **findById()** | 특정 회원 조회 | 존재하지 않으면 예외 발생 (NotFoundException) |
| **update()** | 전체 수정 (PUT) | 요청받은 모든 필드로 교체 (null 포함) |
| **patch()** | 부분 수정 (PATCH) | null이 아닌 필드만 선택적으로 수정 |
| **delete()** | 회원 삭제 | 존재하지 않으면 예외 발생 |

**핵심**: Service 계층이 DTO를 받아서 비즈니스 로직을 처리하고, Entity를 다루며, 다시 DTO로 변환하여 반환합니다!

---

#### Service vs Repository 메서드 명명 규칙

**Service 계층의 메서드명**: 비즈니스 로직의 목적을 명확히 표현

**Repository 계층의 메서드명**: 데이터 접근 작업 자체를 표현

| 작업 | Service 메서드명 (권장) | Repository 메서드명 |
|-----|----------------------|-------------------|
| **회원 생성** | `create()`, `register()`, `signUp()` | `save()` |
| **전체 조회** | `findAll()`, `getMembers()`, `getAllMembers()` | `findAll()` |
| **특정 조회** | `findById()`, `getMemberDetail()` | `findById()` |
| **수정** | `update()`, `modifyMember()` | `save()` |
| **삭제** | `delete()`, `removeMember()` | `delete()`, `deleteById()` |

**핵심**: 
- Service는 **비즈니스 행위**를 담은 이름 사용 (예: `placeOrder`, `cancelOrder`, `signUp`)
- Repository는 **데이터 작업**을 담은 이름 사용 (예: `save`, `findById`, `delete`)

**주의**: 단순한 CRUD만 수행하는 경우에는 이름이 유사해도 무방하지만, 복잡한 비즈니스 로직이 포함될수록 명확히 구분하는 것이 좋습니다!

---

### 4.2.10.6 예외 처리 구현

#### NotFoundException 작성

존재하지 않는 회원을 조회할 때 발생시킬 예외 클래스입니다.

```java
package com.example.restfulapiSample.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Not Found!!")
@NoArgsConstructor
public class NotFoundException extends RuntimeException {
}
```

---

#### @ResponseStatus 어노테이션

| 속성 | 값 | 의미 |
|-----|-----|------|
| **code** | `HttpStatus.NOT_FOUND` | HTTP 상태 코드를 404로 설정 |
| **reason** | `"Not Found!!"` | 에러 메시지 (클라이언트에게 전달) |

**작동 방식**:
1. Service에서 `throw new NotFoundException()` 실행
2. Spring MVC가 예외를 감지하고 `@ResponseStatus` 확인
3. HTTP 상태 코드를 **404 Not Found**로 설정하여 응답

---

#### RuntimeException vs Checked Exception

**NotFoundException이 RuntimeException을 상속받는 이유**:

| 구분 | RuntimeException (Unchecked) | Exception (Checked) |
|-----|----------------------------|-------------------|
| **컴파일 시점** | try-catch 강제 안 함 | try-catch 또는 throws 필수 |
| **발생 원인** | 논리적 오류, 복구 어려운 상황 | 외부 요인, 복구 가능한 상황 |
| **처리 방식** | 최상위 계층에서 일괄 처리 | 메서드마다 명시적 처리 |
| **예시** | `NullPointerException`, `NotFoundException` | `IOException`, `SQLException` |

**핵심**: `RuntimeException`을 상속받으면 Service에서 `throw` 할 때 try-catch를 사용할 필요가 없습니다!

```java
// ✅ RuntimeException: try-catch 불필요
public MemberResponse findById(Long id) {
    Member member = memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());  // 바로 throw 가능
    return mapToMemberResponse(member);
}
```

---

### 4.2.10.7 Controller 수정

이제 Controller는 Repository를 직접 주입받지 않고, **Service를 주입**받아 사용합니다.

```java
package com.example.restfulapiSample.controller;

import com.example.restfulapiSample.dto.MemberRequest;
import com.example.restfulapiSample.dto.MemberResponse;
import com.example.restfulapiSample.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;  // ✅ Service 주입

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)  // 201 Created 반환
    public MemberResponse post(@RequestBody MemberRequest memberRequest) {
        return memberService.create(memberRequest);
    }

    @GetMapping
    public List<MemberResponse> getAll() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    public MemberResponse get(@PathVariable("id") Long id) {
        return memberService.findById(id);
    }

    @PutMapping("/{id}")
    public MemberResponse put(@PathVariable("id") Long id, 
                              @RequestBody MemberRequest memberRequest) {
        return memberService.update(id, memberRequest);
    }

    @PatchMapping("/{id}")
    public MemberResponse patch(@PathVariable("id") Long id, 
                                @RequestBody MemberRequest memberRequest) {
        return memberService.patch(id, memberRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        memberService.delete(id);
    }
}
```

---

#### Controller 변경 사항 비교

| 구분 | 이전 (문제 있음) | 현재 (개선됨) |
|-----|----------------|-------------|
| **의존성 주입** | `MemberRepository` 직접 주입 | `MemberService` 주입 |
| **사용 객체** | `Member` (Entity) | `MemberRequest`, `MemberResponse` (DTO) |
| **비즈니스 로직** | Controller에 직접 구현 | Service에 위임 |
| **역할** | 요청 처리 + 비즈니스 로직 혼재 | 요청 처리만 담당 |

**핵심**: Controller는 이제 단순히 HTTP 요청을 받아 Service에 위임하고, 그 결과를 응답하는 역할만 수행합니다!

---

#### @ResponseStatus(HttpStatus.CREATED) 사용

**목적**: POST 요청이 성공적으로 새로운 리소스를 생성했을 때 **201 Created**를 반환

| 설정 | 상태 코드 | 의미 |
|-----|----------|------|
| **없음** (기본) | 200 OK | 요청 성공 |
| **@ResponseStatus(HttpStatus.CREATED)** | 201 Created | 리소스 생성 성공 (RESTful 원칙 준수) |

---

### 4.2.10.8 Repository (변경 없음)

Repository는 이전과 동일하게 사용됩니다.

```java
package com.example.restfulapiSample.repository;

import com.example.restfulapiSample.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
```

**핵심**: Repository는 순수하게 데이터 접근만 담당하며, 비즈니스 로직은 Service에서 처리합니다!

---

### 4.2.10.9 실제 동작 테스트

#### 1. 회원 생성 (POST)

**요청**:
```
POST http://localhost:8080/api/members
Content-Type: application/json

{
    "name": "김우현",
    "email": "woo@spring.ac.kr",
    "age": 44
}
```

**응답**:
```json
{
    "id": 1,
    "name": "김우현",
    "email": "woo@spring.ac.kr",
    "age": 44
}
```

**상태 코드**: `201 Created` ✅

---

#### 2. 전체 회원 조회 (GET)

**요청**:
```
GET http://localhost:8080/api/members
```

**응답**:
```json
[
    {
        "id": 1,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 44
    },
    {
        "id": 2,
        "name": "홍혜창",
        "email": "hong@spring.ac.kr",
        "age": 22
    }
]
```

**상태 코드**: `200 OK`

---

#### 3. 특정 회원 조회 - 성공 (GET)

**현재 상태** (DB):
```json
[
    {
        "id": 1,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 44
    },
    {
        "id": 2,
        "name": "홍혜창",
        "email": "hong@spring.ac.kr",
        "age": 22
    }
]
```

**요청**:
```
GET http://localhost:8080/api/members/1
```

**응답**:
```json
{
    "id": 1,
    "name": "김우현",
    "email": "woo@spring.ac.kr",
    "age": 44
}
```

**상태 코드**: `200 OK`

---

#### 4. 특정 회원 조회 - 실패 (GET)

**요청**:
```
GET http://localhost:8080/api/members/100
```

**응답**:
```json
{
    "timestamp": "2025-10-22T01:43:21.196+00:00",
    "status": 404,
    "error": "Not Found",
    "path": "/api/members/100"
}
```

**상태 코드**: `404 Not Found` ✅

**핵심**: 이전에는 `200 OK`와 함께 `null`을 반환했지만, 이제는 **404 Not Found** 에러를 제대로 처리합니다!

---

#### 5. 회원 전체 수정 (PUT) - 실패

**요청**:
```
PUT http://localhost:8080/api/members/100
Content-Type: application/json

{
    "name": "강우현"
}
```

**응답**:
```json
{
    "timestamp": "2025-10-22T02:08:43.189+00:00",
    "status": 404,
    "error": "Not Found",
    "path": "/api/members/100"
}
```

**상태 코드**: `404 Not Found` ✅

---

#### 6. 회원 전체 수정 (PUT) - 성공

**이전 상태** (DB):
```json
{
    "id": 2,
    "name": "김우현",
    "email": "woo@spring.ac.kr",
    "age": 50
}
```

**요청**:
```
PUT http://localhost:8080/api/members/2
Content-Type: application/json

{
    "name": "강우현"
}
```

**이후 상태** (DB):
```json
{
    "id": 2,
    "name": "강우현",
    "email": null,
    "age": null
}
```

**상태 코드**: `200 OK`

**핵심**: PUT은 전체 교체이므로 보내지 않은 필드(`email`, `age`)는 `null`로 설정됩니다!

---

#### 7. 회원 부분 수정 (PATCH)

**이전 상태** (DB):
```json
{
    "id": 1,
    "name": "홍혜창",
    "email": "hong@spring.ac.kr",
    "age": 22
}
```

**요청**:
```
PATCH http://localhost:8080/api/members/1
Content-Type: application/json

{
    "name": "김혜창"
}
```

**이후 상태** (DB):
```json
{
    "id": 1,
    "name": "김혜창",
    "email": "hong@spring.ac.kr",
    "age": 22
}
```

**상태 코드**: `200 OK`

**핵심**: PATCH는 부분 수정이므로 보내지 않은 필드(`email`, `age`)는 기존 값을 유지합니다!

---

#### 8. 회원 삭제 (DELETE) - 실패

**요청**:
```
DELETE http://localhost:8080/api/members/100
```

**응답**:
```json
{
    "timestamp": "2025-10-22T02:10:47.222+00:00",
    "status": 404,
    "error": "Not Found",
    "path": "/api/members/100"
}
```

**상태 코드**: `404 Not Found` ✅

---

#### 9. 회원 삭제 (DELETE) - 성공

**이전 상태** (DB):
```json
[
    {
        "id": 1,
        "name": "김혜창",
        "email": "hong@spring.ac.kr",
        "age": 22
    },
    {
        "id": 2,
        "name": "강우현",
        "email": null,
        "age": null
    }
]
```

**요청**:
```
DELETE http://localhost:8080/api/members/1
```

**이후 상태** (DB):
```json
[
    {
        "id": 2,
        "name": "강우현",
        "email": null,
        "age": null
    }
]
```

**상태 코드**: `200 OK`

**핵심**: ID가 1인 회원이 정상적으로 삭제되었습니다!

---

### 4.2.10.10 예외 발생 시 응답 처리 흐름

#### 정상적인 경우

```
memberService.findById(id) 호출
    ↓
Member 객체 성공적으로 조회
    ↓
mapToMemberResponse(member) 실행
    ↓
MemberResponse 객체 반환
    ↓
Controller에서 HTTP 200 OK와 함께 응답 본문(JSON)으로 전송
```

---

#### 예외가 발생하는 경우

```
memberService.findById(id) 호출
    ↓
memberRepository.findById(id) 결과가 empty
    ↓
.orElseThrow(() -> new NotFoundException()) 실행
    ↓
NotFoundException 예외 발생
    ↓
메서드 실행 즉시 중단, Controller로 예외 전파
    ↓
Spring MVC가 예외 감지
    ↓
@ResponseStatus 어노테이션 확인
    ↓
HTTP 404 Not Found 상태 코드 설정
    ↓
에러 정보를 담은 응답 본문(JSON) 반환
```

**핵심**: 
- 성공하면 `return` 값이 응답
- 실패하면 `throw` 된 예외가 응답으로 변환

---

### 4.2.10.11 개선된 구조의 장점

#### 계층별 책임 분리

| 계층 | 이전 (문제) | 현재 (개선) |
|-----|-----------|-----------|
| **Controller** | 비즈니스 로직 포함, Entity 직접 사용 | HTTP 처리만, DTO 사용 |
| **Service** | 없음 | 비즈니스 로직 담당, DTO ↔ Entity 변환 |
| **Repository** | Controller에서 직접 호출 | Service에서만 호출 |

---

#### 주요 개선 사항

| 항목 | 개선 내용 | 효과 |
|-----|----------|------|
| **보안 강화** | Entity의 민감한 필드(password, enabled)가 외부에 노출되지 않음 | ✅ 외부에서 의도치 않은 수정 불가 |
| **예외 처리** | 존재하지 않는 리소스 조회 시 404 Not Found 반환 | ✅ RESTful 원칙 준수 |
| **계층 분리** | Controller, Service, Repository 역할 명확 | ✅ 유지보수 용이, 테스트 편리 |
| **재사용성** | 비즈니스 로직이 Service에 모여 있어 다른 Controller에서도 사용 가능 | ✅ 코드 중복 방지 |
| **상태 코드** | POST 성공 시 201 Created 반환 | ✅ RESTful API 설계 원칙 준수 |

---

### 4.2.10.12 DTO 패턴 적용 전후 비교

#### 데이터 흐름 비교

**이전 구조 (문제)**:
```
[클라이언트]
    ↓ JSON (모든 필드 포함 가능)
[Controller]
    ↓ @RequestBody Member (Entity 직접 사용)
[Repository]
    ↓ Entity
[Database]
    ↓
[Repository]
    ↓ Entity (password, enabled 포함)
[Controller]
    ↓ @ResponseBody Member (민감한 정보 노출 위험)
[클라이언트]
```

**문제점**:
- ⚠️ 클라이언트가 `password`, `enabled` 필드를 포함한 JSON 전송 가능
- ⚠️ 응답에 민감한 정보가 포함될 수 있음
- ⚠️ 비즈니스 로직이 Controller에 혼재

---

**현재 구조 (개선)**:
```
[클라이언트]
    ↓ JSON (name, email, age만)
[Controller]
    ↓ @RequestBody MemberRequest (필요한 필드만)
[Service]
    ↓ MemberRequest → Member 변환
[Repository]
    ↓ Entity
[Database]
    ↓
[Repository]
    ↓ Entity
[Service]
    ↓ Member → MemberResponse 변환 (필요한 필드만)
[Controller]
    ↓ @ResponseBody MemberResponse (안전)
[클라이언트]
```

**장점**:
- ✅ 클라이언트는 `password`, `enabled` 필드에 접근 불가
- ✅ 응답에 필요한 정보만 선택적으로 포함
- ✅ 비즈니스 로직이 Service에 집중

---

### 4.2.10.13 Service 계층의 데이터 변환 과정

#### 요청 처리 흐름 (POST 예시)

```
1. 클라이언트 요청
   POST /api/members
   { "name": "홍길동", "email": "hong@spring.ac.kr", "age": 25 }

2. Controller (표현 계층)
   @RequestBody MemberRequest memberRequest
   → Jackson이 JSON을 MemberRequest 객체로 변환

3. Service 호출
   memberService.create(memberRequest)

4. Service (서비스 계층) - DTO → Entity 변환
   Member member = Member.builder()
       .name(memberRequest.getName())
       .email(memberRequest.getEmail())
       .age(memberRequest.getAge())
       .build();
   → MemberRequest의 데이터를 꺼내 Member Entity 생성

5. Repository 호출 (영속성 계층)
   memberRepository.save(member)
   → Member Entity를 DB에 저장
   → DB가 id를 자동 생성 (예: id = 1)

6. Service - Entity → Response DTO 변환
   return MemberResponse.builder()
       .id(member.getId())           // 1
       .name(member.getName())       // "홍길동"
       .email(member.getEmail())     // "hong@spring.ac.kr"
       .age(member.getAge())         // 25
       .build();
   → password, enabled는 포함하지 않음!

7. Controller - 응답 반환
   return memberService.create(memberRequest)
   → Jackson이 MemberResponse를 JSON으로 변환

8. 클라이언트 응답
   201 Created
   {
       "id": 1,
       "name": "홍길동",
       "email": "hong@spring.ac.kr",
       "age": 25
   }
```

**핵심**: Service가 DTO와 Entity 사이의 변환을 전담하여, Controller는 변환 로직을 알 필요가 없습니다!

---

### 4.2.10.14 @ResponseStatus reason 속성과 응답 본문

#### reason 속성이 보이지 않는 이유

`@ResponseStatus`의 `reason` 속성에 설정한 값이 Postman 등의 클라이언트에서 직접 보이지 않는 이유:

**설명**:
대부분의 최신 HTTP 클라이언트(Postman, 브라우저 개발자 도구 등)는 기본적으로:
- HTTP 상태 코드(예: 404)
- 표준 이유 구절(예: Not Found)

만 크게 표시하고, `reason`에 명시된 사용자 정의 문자열을 응답 본문(Body)에 명시적으로 포함시키지 않습니다.

**실제 응답 구조**:
```json
{
    "timestamp": "2025-10-22T01:43:21.196+00:00",
    "status": 404,
    "error": "Not Found",  // ← 표준 에러 메시지
    "path": "/api/members/100"
}
```

**reason 값 확인 방법**:
- 서버 로그 확인
- 또는 커스텀 예외 핸들러(`@ExceptionHandler`)를 사용하여 응답 본문에 명시적으로 포함

---

### 4.2.10.15 패키지 역할할


**각 패키지의 역할**:

| 패키지 | 역할 | 주요 클래스 |
|-------|------|-----------|
| **controller** | HTTP 요청/응답 처리 | MemberController |
| **dto** | 계층 간 데이터 전송 객체 | MemberRequest, MemberResponse |
| **exception** | 사용자 정의 예외 | NotFoundException |
| **model** | 데이터베이스 엔티티 | Member |
| **repository** | 데이터 접근 | MemberRepository |
| **service** | 비즈니스 로직 | MemberService |

---

### 4.2.10.16 핵심 정리

#### Service 계층 도입의 핵심 가치

| 항목 | 설명 |
|-----|------|
| **1. 보안** | Entity의 민감한 필드를 외부로부터 보호 |
| **2. 책임 분리** | Controller는 HTTP 처리, Service는 비즈니스 로직, Repository는 데이터 접근 |
| **3. 재사용성** | 비즈니스 로직을 여러 Controller에서 재사용 가능 |
| **4. 테스트 용이성** | 각 계층을 독립적으로 테스트 가능 |
| **5. 유지보수성** | 코드 수정 시 영향 범위가 명확 |

---

#### DTO 패턴의 핵심 가치

| 항목 | 설명 |
|-----|------|
| **1. 데이터 캡슐화** | 필요한 데이터만 선택적으로 전달 |
| **2. 보안 강화** | 민감한 정보 노출 방지 |
| **3. 유연성** | 요청과 응답에 필요한 필드를 독립적으로 관리 |
| **4. 스키마 독립성** | Entity 구조 변경이 API 스펙에 영향을 주지 않음 |

---

#### RESTful API 원칙 준수

| 원칙 | 적용 방법 | 구현 |
|-----|----------|------|
| **자원 중심 설계** | URL에 명사 사용 | `/api/members` |
| **HTTP 메서드** | GET, POST, PUT, PATCH, DELETE 구분 | 각 CRUD 작업에 맞는 메서드 사용 |
| **적절한 상태 코드** | 201 Created, 404 Not Found 등 | `@ResponseStatus`, `NotFoundException` |
| **무상태성** | 각 요청은 독립적 | 세션 사용 없이 처리 |

---

### 4.2.10.17 Service 계층 도입으로 해결된 문제

#### 이전 문제점 ⚠️ → 해결 방법 ✅

| 이전 문제점 | 해결 방법 | 효과 |
|-----------|---------|------|
| **예외 처리 부재**<br>존재하지 않는 회원 조회 시 `null` 반환 및 `200 OK` | `NotFoundException` 정의<br>`.orElseThrow()` 사용 | ✅ 404 Not Found 응답으로 RESTful 원칙 준수 |
| **비즈니스 로직과 Controller 혼재**<br>PATCH 로직이 Controller에 직접 구현 | Service 계층 분리<br>`patch()` 메서드로 로직 이동 | ✅ 책임 분리, 재사용 가능 |
| **Entity 직접 노출**<br>`password`, `enabled` 등 민감 정보 노출 위험 | DTO 패턴 적용<br>MemberRequest, MemberResponse 사용 | ✅ 필요한 필드만 선택적으로 전달 |
| **유효성 검증 부재**<br>잘못된 데이터 저장 가능 | (다음 장에서 해결 예정)<br>Bean Validation 사용 | 🔄 학습 예정 |

---


## 4.2.11 게시글 RESTful API 구현

### 4.2.11.1 게시글 API URL 설계

#### 계층적 URL vs 단순 URL

회원(Member)이 게시글(Article)을 작성할 때, API URL을 어떻게 설계하는 것이 좋을까요?

**계층적 URL 방식**:
```
POST /api/members/{회원아이디}/articles
GET /api/members/{회원아이디}/articles/{게시글아이디}
```

**장점**:
- ✅ 리소스 간의 계층 관계를 명확히 표현
- ✅ 특정 회원의 게시글임을 URL에서 직관적으로 파악 가능

**단점**:
- ⚠️ URL이 너무 길어짐
- ⚠️ 복잡한 계층 구조에서는 관리가 어려워짐

---

**단순 URL 방식 (권장)**:
```
POST /api/articles?memberId={회원아이디}
GET /api/articles/{게시글아이디}
GET /api/articles?memberId={회원아이디}  // 특정 회원의 게시글 조회
```

**장점**:
- ✅ URL이 간결함
- ✅ 리소스 중심 설계 (Article이 독립적인 리소스)
- ✅ 필요시 쿼리 파라미터로 필터링 가능

**핵심**: 상위 계층 없이 바로 리소스에 접근하는 것이 바람직합니다!

---

### 4.2.11.2 게시글 Entity 및 Repository 작성

#### Article Entity

```java
package com.example.restfulapiSample.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EntityListeners(AuditingEntityListener.class)  // JPA Auditing 리스너 등록
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    // JPA Auditing - 자동 시간 관리
    @CreatedDate
    private Date created;      // 생성 시간 자동 기록
    
    @LastModifiedDate
    private Date updated;      // 수정 시간 자동 기록

    // 다대일 관계 - 여러 게시글이 하나의 회원에 속함
    @ManyToOne
    Member member;
}
```

---

#### JPA Auditing (감사 기능)

**정의**: 데이터의 생성 및 수정을 자동으로 감시하고 추적하는 기능

**주요 어노테이션**:

| 어노테이션 | 역할 | 설명 |
|----------|------|------|
| **@CreatedDate** | 생성 시간 자동 기록 | 엔티티가 처음 저장될 때 현재 시간을 자동으로 설정 |
| **@LastModifiedDate** | 수정 시간 자동 기록 | 엔티티가 수정될 때마다 현재 시간으로 자동 갱신 |
| **@CreatedBy** | 생성자 자동 기록 | 누가 최초로 생성했는지 기록 (별도 설정 필요) |
| **@LastModifiedBy** | 수정자 자동 기록 | 누가 가장 최근에 수정했는지 기록 (별도 설정 필요) |

**필수 설정**:

1. **Entity에 리스너 등록**:
```java
@EntityListeners(AuditingEntityListener.class)
```
- `AuditingEntityListener` 클래스가 `@CreatedDate`, `@LastModifiedDate`가 붙은 필드를 자동으로 관리

2. **Application 클래스에서 JPA Auditing 활성화**:

```java
package com.example.restfulapiSample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // ✅ JPA Auditing 활성화
public class RestfulapiSampleApplication {

    public static void main(String[] args) {
       SpringApplication.run(RestfulapiSampleApplication.class, args);
    }
}
```

**핵심**: `@EnableJpaAuditing`을 추가해야 리스너 기능이 활성화됩니다!

---

#### Article과 Member의 관계

```java
@ManyToOne
Member member;
```

**관계 설명**:
- **다대일(Many-to-One)**: 여러 게시글(Article)이 하나의 회원(Member)에 속함
- 데이터베이스에는 `article` 테이블에 `member_id` 외래 키 컬럼이 생성됨

---

#### ArticleRepository

```java
package com.example.restfulapiSample.repository;

import com.example.restfulapiSample.model.Article;
import com.example.restfulapiSample.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 특정 회원의 모든 게시글 조회
    public List<Article> findByMember(Member member);
}
```

**커스텀 메서드**:
- `findByMember(Member member)`: Spring Data JPA의 **쿼리 메서드 자동 생성** 기능
- Member 객체를 받아 해당 회원이 작성한 모든 게시글을 조회

**생성되는 SQL**:
```sql
SELECT * FROM article WHERE member_id = ?
```

---

### 4.2.11.3 게시글 DTO 작성

#### ArticleRequest (요청 DTO)

```java
package com.example.restfulapiSample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleRequest {

    String title;
    String description;
    
    // ✅ memberId는 URL 파라미터나 경로 변수로 받으므로 DTO에 포함하지 않음
}
```

**특징**:
- 클라이언트가 보낼 필드만 포함 (제목, 내용)
- `memberId`는 URL에서 받으므로 DTO에 포함하지 않음

---

#### ArticleResponse (응답 DTO)

```java
package com.example.restfulapiSample.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponse {

    private Long id;           // 게시글 ID
    private Long memberId;     // 작성자 ID
    
    private String name;       // 작성자 이름
    private String email;      // 작성자 이메일
    
    private String title;      // 게시글 제목
    private String description;// 게시글 내용
    
    private Date created;      // 생성 시간
    private Date updated;      // 수정 시간
}
```

**특징**:
- 게시글 정보 + 작성자 정보를 함께 포함
- 클라이언트가 별도로 회원 정보를 조회할 필요 없이 한 번에 제공

---

### 4.2.11.4 게시글 Service 작성

#### ArticleService

```java
package com.example.restfulapiSample.service;

import com.example.restfulapiSample.dto.ArticleRequest;
import com.example.restfulapiSample.dto.ArticleResponse;
import com.example.restfulapiSample.exception.NotFoundException;
import com.example.restfulapiSample.model.Article;
import com.example.restfulapiSample.model.Member;
import com.example.restfulapiSample.repository.ArticleRepository;
import com.example.restfulapiSample.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;  // ✅ 여러 Repository 사용

    // Article → ArticleResponse 변환
    public ArticleResponse mapToArticleResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .memberId(article.getMember().getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .build();
    }

    // 게시글 생성
    public ArticleResponse create(Long memberId, ArticleRequest articleRequest) {
        // 1. 회원 존재 여부 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);
        
        // 2. 게시글 Entity 생성
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .description(articleRequest.getDescription())
                .member(member)
                .build();
        
        // 3. 저장
        articleRepository.save(article);
        
        // 4. Response DTO 변환 및 반환
        return mapToArticleResponse(article);
    }

    // 특정 회원의 게시글 조회
    public List<ArticleResponse> findByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);
        
        List<Article> articles = articleRepository.findByMember(member);
        
        return articles.stream()
                .map(i -> mapToArticleResponse(i))
                .collect(Collectors.toList());
    }

    // 모든 게시글 조회
    public List<ArticleResponse> findAll() {
        List<Article> articles = articleRepository.findAll();
        
        return articles.stream()
                .map(i -> mapToArticleResponse(i))
                .collect(Collectors.toList());
    }

    // 게시글 삭제
    public void delete(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        articleRepository.delete(article);
    }

    // 게시글 전체 수정 (PUT)
    public ArticleResponse update(Long id, ArticleRequest articleRequest) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        
        article.setTitle(articleRequest.getTitle());
        article.setDescription(articleRequest.getDescription());
        
        articleRepository.save(article);
        return mapToArticleResponse(article);
    }

    // 게시글 부분 수정 (PATCH)
    public ArticleResponse patch(Long id, ArticleRequest articleRequest) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        
        if (articleRequest.getDescription() != null) {
            article.setDescription(articleRequest.getDescription());
        }
        
        if (articleRequest.getTitle() != null) {
            article.setTitle(articleRequest.getTitle());
        }
        
        articleRepository.save(article);
        return mapToArticleResponse(article);
    }
}
```

---

#### Service에서 여러 Repository 사용

**핵심 원칙**: 하나의 Service는 **하나의 비즈니스 목표를 달성**하기 위해 **여러 Repository를 사용**할 수 있습니다.

| 구성 요소 | 역할 | 비유 |
|---------|------|------|
| **Repository** | 단순 작업자 | 하나의 데이터 조각(엔티티)을 DB에 저장하거나 조회하는 단순 작업만 수행 |
| **Service** | 총괄 책임자 (지휘관) | 여러 단순 작업을 조합하여 하나의 비즈니스 기능을 완성 |

---

**예시: 게시글 작성 비즈니스 로직**

```
1. MemberRepository → 작성자 정보 조회
2. ArticleRepository → 게시글 저장
3. PointRepository → 작성자에게 포인트 지급 (예시)
```

**Service의 역할**:
- 이 3개의 Repository를 순차적으로 호출하여 **"게시글 작성"**이라는 하나의 비즈니스 기능을 완성
- 트랜잭션을 통해 **데이터 일관성 보장** (다음 장에서 학습)

---

**트랜잭션의 필요성** (미리보기):

**문제 상황**:
```
1. 게시글 저장 성공 ✅
2. 포인트 지급 실패 ❌ (시스템 오류)
→ 결과: 게시글만 있고 포인트는 없는 데이터 불일치 발생
```

**해결: @Transactional**:
```java
@Transactional
public ArticleResponse create(Long memberId, ArticleRequest articleRequest) {
    // 모든 작업이 성공하거나, 하나라도 실패하면 전체 롤백
}
```

| 구분 | Repository의 @Transactional | Service의 @Transactional |
|-----|---------------------------|-------------------------|
| **목적** | 단일 쿼리의 실행 보장 | 다수의 DB 작업 묶음 보장 (비즈니스 전체의 원자성) |
| **범위** | 해당 Repository 메서드 하나 | Service 메서드 전체 |

**핵심**: 
- Service의 함수는 **논리적인 단위**로 이루어져야 함
- 트랜잭션이 필요함 (다음 시간에 자세히 학습)
- 하나의 Service에서 여러 Repository를 사용하는 것이 일반적!

---

### 4.2.11.5 Controller 설계와 단일 책임 원칙

#### 문제 상황: 단일 책임 원칙 위반

**잘못된 설계 - MemberController에 게시글 로직 포함**:

```java
@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    private final ArticleService articleService;  // ⚠️ 문제!

    // ... 회원 관련 메서드들 ...

    // ⚠️ 게시글 생성 - 단일 책임 원칙 위반!
    @PostMapping("/{id}/articles")
    public ArticleResponse postArticle(@PathVariable("id") Long id, 
                                      @RequestBody ArticleRequest articleRequest) {
        return articleService.create(id, articleRequest);
    }
}
```

**문제점**:

| 항목 | 설명 |
|-----|------|
| **단일 책임 원칙 위반** | MemberController가 회원(Member) 리소스뿐만 아니라 게시글(Article) 리소스까지 담당 |
| **책임 모호화** | Controller의 역할이 불명확해짐 |
| **유지보수 어려움** | 게시글 관련 로직이 여러 Controller에 분산될 수 있음 |

---

#### 올바른 설계: Controller 분리

**권장 구조**:

| Controller | 담당 리소스 | 책임 |
|-----------|----------|------|
| **MemberController** | Member (회원) | 회원 관련 CRUD만 처리 |
| **ArticleController** | Article (게시글) | 게시글 관련 CRUD만 처리 |

**핵심**: 게시글과 관련된 CRUD 책임은 **ArticleController**가 가지는 것이 바람직!

---

### 4.2.11.6 레거시 코드 처리: 리다이렉션 vs 포워드

실제 개발 환경에서는 이미 구현된 잘못된 설계를 발견하거나, 외부에서 사용 중인 레거시 URL을 변경할 수 없는 상황이 발생합니다.

이때 **이전 코드를 바꾸지 않고** 새로운 올바른 구조로 연결하는 두 가지 방법이 있습니다:

1. **리다이렉션(Redirection)**
2. **포워드(Forward)**

---

#### HttpServletRequest와 HttpServletResponse 이해

**매우 중요!** 리다이렉션과 포워드를 이해하기 전에 이 두 객체를 반드시 이해해야 합니다!

**객체 생성 시점**:
클라이언트가 요청을 보내면 **서블릿 컨테이너(Tomcat)** 이 자동으로 2가지 객체를 생성합니다:

| 객체 | 역할 |
|-----|------|
| **HttpServletRequest** | 요청에 대한 정보를 담음 (헤더, URL, 파라미터, 본문 등) |
| **HttpServletResponse** | 응답 결과에 대한 정보를 담음 (상태 코드, 헤더, 본문 등) |

---

**Controller 메서드에서 명시적 주입 가능**:

```java
@PostMapping()
@ResponseStatus(HttpStatus.CREATED)
public MemberResponse post(
    @RequestBody MemberRequest memberRequest,
    HttpServletRequest request,   // ✅ 요청 정보 객체 주입
    HttpServletResponse response  // ✅ 응답 객체 주입
) {
    // 필요시 request.getHeader("...")나 response.addHeader("...") 등 직접 사용 가능
    return memberService.create(memberRequest);
}
```

**핵심**: 
- 평소에는 Spring이 자동으로 처리하여 개발자가 직접 다룰 필요가 없음
- 필요할 때는 매개변수로 주입받아 직접 사용 가능!

---

#### HTTP 요청/응답 과정에서의 Request와 Response 작동 원리

**4단계 순환 과정**:

**1. 요청 객체 생성 및 정보 주입 (입력)**:

| 객체 | 역할 | 설명 |
|-----|------|------|
| **HttpServletRequest** | 요청 정보의 컨테이너 | 클라이언트의 요청 헤더, URL, 파라미터, 본문 등 모든 정보를 담음 |
| **HttpServletResponse** | 응답 정보의 빈 도화지 | 클라이언트에게 돌려줄 빈 응답 그릇 생성 (이 시점에는 비어있음) |

**2. 컨트롤러 호출 및 데이터 주입 (처리)**:
- Spring MVC가 `HttpServletRequest`의 정보를 분석하여 컨트롤러 메서드 호출
- `@RequestBody MemberRequest memberRequest`: Spring이 `HttpServletRequest`의 요청 본문 스트림을 읽어서 Java 객체로 변환(역직렬화)하여 주입

**3. 응답 객체 작성 (출력 준비)**:
- 컨트롤러가 `return MemberResponse` 실행
- Spring이 이 객체를 JSON으로 변환(직렬화)
- JSON 데이터와 HTTP 상태 코드를 `HttpServletResponse`에 채워 넣음

**4. 클라이언트에게 응답 및 객체 소멸 (최종)**:
- `HttpServletResponse`를 네트워크를 통해 클라이언트에게 전송
- 응답 완료 후 두 객체는 메모리에서 소멸

**핵심**: 
- `HttpServletRequest`는 클라이언트의 **입력**을 담당하는 통로
- `HttpServletResponse`는 서버의 **출력**을 담아 전달하는 통로

---

#### 리다이렉션(Redirection)

**정의**: 클라이언트에게 새 URL을 알려주고 **다시 요청하게 만드는** 방식

**특징**:

| 항목 | 내용 |
|-----|------|
| **요청 주체** | 클라이언트 (새 요청 발생) |
| **HTTP 요청 횟수** | 2회 (원래 요청 + 클라이언트의 재요청) |
| **URL 변경** | 변경됨 (클라이언트 브라우저 주소창이 바뀜) |
| **데이터 유지** | ❌ 유지되지 않음 (새로운 HttpServletRequest 생성) |
| **HTTP 상태 코드** | 302 Found 또는 301 Moved Permanently |

---

**논리적 흐름 (데이터 소멸의 이유)**:

```
1. 클라이언트 요청 (1차 요청)
   → 서버가 HttpServletRequest(요청-1)와 HttpServletResponse(응답-1) 생성

2. 서버 응답 (Redirect 명령)
   → 서버가 "302 Found" 상태 코드와 새 경로를 응답-1에 담아 전송
   → 요청-1 객체는 역할이 끝나 소멸 준비

3. 클라이언트 재요청 (2차 요청)
   → 클라이언트가 브라우저에서 자동으로 새 경로로 두 번째 HTTP 요청 전송

4. 새 Request 생성
   → 서버가 완전히 새로운 HttpServletRequest(요청-2) 객체 생성
   
5. 결과
   → 요청-1에 담겨있던 @RequestBody 데이터, setAttribute 데이터 등은
      모두 요청-1 객체와 함께 소멸
   → 요청-2는 완전히 새로운 요청이므로 이전 정보를 알 수 없음
```

**핵심**: **새로운 요청**이므로 이전 요청의 데이터는 유지될 수 없음!

---

**사용 방법 (@RestController)**:

```java
@PostMapping("/{id}/articles")
public void postArticle(@PathVariable("id") Long id,
                        @RequestBody ArticleRequest articleRequest,
                        HttpServletResponse response) throws IOException {
    
    // 리다이렉션 수행
    response.sendRedirect("/api/articles?memberId=" + id);
}
```

**주의**: 
- 리다이렉션은 새로운 요청이므로 `@RequestBody`로 받은 데이터는 전달되지 않음!
- 데이터가 필요하면 URL 파라미터로 전달하거나 세션 사용

---

#### 포워드(Forward)

**정의**: 서버 내부에서 요청을 다른 곳으로 **이어서 처리하게 만드는** 방식

**특징**:

| 항목 | 내용 |
|-----|------|
| **요청 주체** | 서버 (내부 이동) |
| **HTTP 요청 횟수** | 1회 (서버 내부 처리) |
| **URL 변경** | ❌ 변경되지 않음 (클라이언트는 모름) |
| **데이터 유지** | ✅ 유지됨 (기존 HttpServletRequest 객체 사용) |
| **클라이언트 인지** | 클라이언트는 내부 처리 과정을 알 수 없음 |

---

**논리적 흐름 (데이터 유지의 이유)**:

```
1. 클라이언트 요청 (1회)
   → 서버가 HttpServletRequest(요청-1)와 HttpServletResponse(응답-1) 생성

2. 서버 내부 위임
   → 첫 번째 컨트롤러가 forward 실행
   → 클라이언트에게 응답하지 않고 서버 내부에서 제어권을 두 번째 컨트롤러로 전달

3. 객체 재사용
   → 기존의 HttpServletRequest(요청-1)와 HttpServletResponse(응답-1) 객체를
      두 번째 컨트롤러에 그대로 전달

4. 데이터 유지
   → 요청-1 객체에 담겨있던 모든 데이터(URL, 파라미터, setAttribute 정보 등)가
      그대로 유지됨

5. 결과
   → 클라이언트는 한 번의 요청에 대한 응답만 받음
   → 동일한 HttpServletRequest 객체를 사용하므로 데이터가 안전하게 전달됨
```

**핵심**: **동일한 HttpServletRequest 객체**를 사용하므로 데이터가 유지됨!

---

**RequestBody 데이터 전달 방법**:

**중요**: `@RequestBody`로 읽은 데이터는 스트림이 **일회성**이므로 다시 읽을 수 없습니다!

**문제 상황**:
```
1. 첫 번째 컨트롤러에서 @RequestBody ArticleRequest 읽음
   → HTTP 요청 본문 스트림 소비(consume)됨

2. 두 번째 컨트롤러에서 다시 @RequestBody로 읽으려고 시도
   → 스트림이 이미 비어있어 데이터를 얻을 수 없음 ❌
```

**해결: setAttribute 사용**:

```java
// 1단계: 스트림을 읽어 만든 객체를 HttpServletRequest에 저장
request.setAttribute("articleRequest", articleRequest);

// 2단계: 두 번째 컨트롤러에서 getAttribute로 꺼내 사용
ArticleRequest articleRequest = (ArticleRequest) request.getAttribute("articleRequest");
```

**핵심**: 
- RequestBody를 한 번 읽으면 HttpServletRequest의 스트림에서 데이터가 소멸
- 읽은 데이터를 `setAttribute`로 저장한 후 `getAttribute`로 꺼내 사용!

---

**사용 방법 (@RestController)**:

**첫 번째 컨트롤러 (MemberController)**:

```java
@PostMapping("/{id}/articles")
public void postArticle(@PathVariable("id") Long id,
                        @RequestBody ArticleRequest articleRequest,
                        HttpServletRequest request,
                        HttpServletResponse response) 
        throws ServletException, IOException {
    
    // 1. RequestBody 데이터를 속성에 저장
    request.setAttribute("articleRequest", articleRequest);
    
    // 2. 포워드 수행 (PostMapping이므로 POST 요청으로 전달됨)
    request.getSession()
           .getServletContext()
           .getRequestDispatcher("/api/articles?memberId=" + id)
           .forward(request, response);
}
```

**두 번째 컨트롤러 (ArticleController)**:

```java
@PostMapping  // ✅ 포워드된 POST 요청을 받음public ArticleResponse postArticle(@RequestParam("memberId") Long memberId,
                                      HttpServletRequest request) {
    
    // 3. 저장된 데이터를 꺼내 사용
    ArticleRequest articleRequest = (ArticleRequest) request.getAttribute("articleRequest");
    
    return articleService.create(memberId, articleRequest);
}
```

---

**@RestController에서 포워드 사용 시 주의사항**:

**문제**: `@RestController`는 반환 값을 응답 본문 데이터로 간주하므로, 일반 `@Controller`처럼 `return "forward:/경로"` 방식이 작동하지 않음

**해결**: 서블릿 API를 직접 사용하여 포워딩 강제

```java
// ❌ @RestController에서 작동하지 않음
return "forward:/api/articles?memberId=" + id;

// ✅ 서블릿 API로 직접 포워딩 (반환 타입 void)
request.getSession()
       .getServletContext()
       .getRequestDispatcher("/api/articles?memberId=" + id)
       .forward(request, response);
```

**핵심**: `@RestController`에서는 `void` 반환 타입과 서블릿 API를 직접 사용해야 포워딩 가능!

---

#### 리다이렉션 vs 포워드 비교 정리

| 구분 | 리다이렉션 (Redirect) | 포워드 (Forward) |
|-----|---------------------|-----------------|
| **요청 주체** | 클라이언트 | 서버 |
| **HTTP 요청 횟수** | 2회 | 1회 |
| **URL 변경** | ✅ 변경됨 | ❌ 변경 안 됨 |
| **데이터 유지** | ❌ 유지 안 됨 | ✅ 유지됨 |
| **HttpServletRequest** | 새로 생성 (요청-2) | 재사용 (요청-1) |
| **사용 시나리오** | 데이터가 필요 없을 때<br>URL 변경이 필요할 때<br>POST 후 새로고침 방지 | 데이터를 전달해야 할 때<br>서버 내부 로직 분리<br>클라이언트가 알 필요 없는 처리 |
| **장점** | URL이 바뀌어 북마크 가능<br>새로고침 시 중복 요청 방지 | 데이터 유지<br>빠름 (1회 요청)<br>내부 처리 숨김 |
| **단점** | 데이터 유지 안 됨<br>느림 (2회 요청) | URL 변경 안 됨<br>새로고침 시 동일 요청 재전송 |

---

**언제 무엇을 사용할까?**

| 상황 | 권장 방법 | 이유 |
|-----|---------|------|
| **RequestBody 데이터가 필요한 경우** | ✅ 포워드 | 데이터를 setAttribute로 전달 가능 |
| **단순 URL 변경만 필요한 경우** | ✅ 리다이렉션 | 데이터 필요 없고 URL만 바꾸면 됨 |
| **POST 후 결과 페이지로 이동** | ✅ 리다이렉션 | 새로고침 시 중복 제출 방지 (PRG 패턴) |
| **서버 내부 로직 분리** | ✅ 포워드 | 클라이언트는 내부 처리를 알 필요 없음 |

---

### 4.2.11.7 포워드를 사용한 실제 구현

#### MemberController (레거시 URL 유지)

```java
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ArticleService articleService;  // ⚠️ 단일 책임 원칙 위반

    // ... 회원 관련 메서드들 ...

    // 레거시 URL 유지 - 포워드로 ArticleController에 위임
    @PostMapping("/{id}/articles")
    public void postArticle(@PathVariable("id") Long id,
                            @RequestBody ArticleRequest articleRequest,
                            HttpServletRequest request,
                            HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. RequestBody 데이터 저장
        request.setAttribute("articleRequest", articleRequest);
        
        // 2. PostMapping이므로 POST 요청으로 포워드됨
        request.getSession()
               .getServletContext()
               .getRequestDispatcher("/api/articles?memberId=" + id)
               .forward(request, response);
    }
}
```

---

#### ArticleController (올바른 구조)

```java
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // 게시글 생성 (포워드 요청 처리)
    @PostMapping
    public ArticleResponse postArticle(@RequestParam("memberId") Long memberId,
                                      HttpServletRequest request) {
        // 포워드된 데이터 꺼내기
        ArticleRequest articleRequest = (ArticleRequest) request.getAttribute("articleRequest");
        return articleService.create(memberId, articleRequest);
    }

    // 게시글 조회 (memberId 선택적)
    @GetMapping
    public List<ArticleResponse> getByMemberId(
            @RequestParam(value = "memberId", required = false) Long memberId) {
        
        if (memberId == null) {
            // 전체 게시글 조회
            return articleService.findAll();
        } else {
            // 특정 회원의 게시글 조회
            return articleService.findByMemberId(memberId);
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        articleService.delete(id);
    }

    // 게시글 전체 수정 (PUT)
    @PutMapping("/{id}")
    public ArticleResponse update(@PathVariable("id") Long id,
                                  @RequestBody ArticleRequest articleRequest) {
        return articleService.update(id, articleRequest);
    }

    // 게시글 부분 수정 (PATCH)
    @PatchMapping("/{id}")
    public ArticleResponse patch(@PathVariable("id") Long id,
                                 @RequestBody ArticleRequest articleRequest) {
        return articleService.patch(id, articleRequest);
    }
}
```

---

#### @RequestParam과 @PathVariable 비교

| 특징 | @RequestParam | @PathVariable |
|-----|--------------|--------------|
| **데이터 위치** | URL의 쿼리 파라미터 (`?` 뒤) | URL의 경로 변수 (`/` 사이) |
| **사용 목적** | 필터링, 정렬, 페이징 등 부가적인 데이터나 선택적 옵션 | 리소스를 식별하는 고유 값 (ID 등) |
| **URL 예시** | `/articles?memberId=100` | `/articles/100` |
| **필수 여부** | `required` 속성으로 제어 가능 | 기본적으로 필수 |
| **포워드 시 사용** | 포워딩 경로에 붙은 데이터 (`forward:경로?key=value`)를 받음 | 포워딩 시 경로 자체에 데이터를 포함하여 받음 |

---

**@RequestParam의 required 속성**:

| 속성 값 | 의미 | 동작 |
|--------|------|------|
| **true** (기본값) | 필수 | 클라이언트가 파라미터를 누락하면 400 Bad Request 발생 |
| **false** | 선택 | 클라이언트가 파라미터를 누락해도 정상 처리 (null 바인딩) |

**예시**:
```java
// memberId가 선택적 파라미터
@GetMapping("/articles")
public List<ArticleResponse> getArticles(
    @RequestParam(value = "memberId", required = false) Long id) {
    
    if (id != null) {
        // memberId가 있을 때만 처리
    }
}
```

---

### 4.2.11.8 포워딩된 컨트롤러의 매핑 규칙

**중요**: 포워드된 요청의 HTTP 메서드는 **원래 요청의 메서드를 유지**합니다!

```
MemberController에서 @PostMapping으로 요청 받음
    ↓ forward 수행
ArticleController의 @PostMapping 메서드가 처리
```

**핵심**: 
- `@PostMapping`으로 포워드하면 `@PostMapping`으로 받아야 함
- HTTP 메서드가 변경되지 않음!

---

### 4.2.11.9 실제 동작 테스트

#### 게시글 생성 (POST - 포워드 사용)

**현재 회원 상태**:
```json
[
    {
        "id": 1,
        "name": "홍혜창",
        "email": "hong@spring.ac.kr",
        "age": 22
    },
    {
        "id": 2,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 33
    }
]
```

**요청** (레거시 URL 사용):
```
POST http://localhost:8080/api/members/1/articles
Content-Type: application/json

{
    "title": "포워드는 재밌습니다",
    "description": "포워드 공부"
}
```

**응답**:
```json
{
    "id": 2,
    "memberId": 1,
    "name": "김우현",
    "email": "woo@spring.ac.kr",
    "title": "포워드는 재밌습니다",
    "description": "포워드 공부",
    "created": "2025-10-22T05:10:58.272+00:00",
    "updated": "2025-10-22T05:10:58.272+00:00"
}
```

**상태 코드**: `200 OK`

**처리 흐름**:
```
1. 클라이언트 → MemberController (POST /api/members/1/articles)
2. MemberController → ArticleRequest를 setAttribute로 저장
3. MemberController → ArticleController로 포워드
4. ArticleController → getAttribute로 데이터 꺼내기
5. ArticleService → 게시글 생성
6. ArticleController → 클라이언트에게 응답
```

**핵심**: 클라이언트는 한 번의 요청만 보냈지만, 서버 내부에서 두 Controller를 거침!

---

#### 특정 회원의 게시글 조회 (GET)

**현재 게시글 상태**:
```json
[
    {
        "id": 1,
        "memberId": 1,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "title": "스프링 부트",
        "description": "스프링 부트 공부",
        "created": "2025-10-22T05:39:47.316+00:00",
        "updated": "2025-10-22T05:39:47.316+00:00"
    },
    {
        "id": 2,
        "memberId": 1,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "title": "데이터베이스",
        "description": "데이터베이스 공부",
        "created": "2025-10-22T05:39:59.063+00:00",
        "updated": "2025-10-22T05:39:59.063+00:00"
    }
]
```

**요청**:
```
GET http://localhost:8080/api/articles?memberId=1
```

**응답**: 위의 게시글 목록

**상태 코드**: `200 OK`

---

### 4.2.11.10 연관 관계 삭제 문제와 해결

#### 문제 상황: 회원 삭제 시 500 에러

**현재 상황**:
- 회원 ID 1이 작성한 게시글 2개 존재
- 회원 ID 1을 삭제하려고 시도

**요청**:
```
DELETE http://localhost:8080/api/members/1
```

**응답**:
```json
{
    "timestamp": "2025-10-22T05:41:42.457+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/members/1"
}
```

**상태 코드**: `500 Internal Server Error` ❌

---

**원인 분석**:

**JPA 환경의 기본 동작 (RESTRICT/NO ACTION)**:
1. JPA가 `memberRepository.delete(member)` 실행
2. 데이터베이스에 `DELETE FROM member WHERE id = 1` 쿼리 전송
3. 데이터베이스가 외래 키 제약 조건 확인
4. `article` 테이블에 `member_id = 1`인 데이터가 존재함을 발견
5. **외래 키 제약 조건 위반(SQL Integrity Constraint Violation)** 오류 반환
6. 애플리케이션으로 오류 전파 → 500 에러 발생

**핵심**: JPA 기본 설정은 **RESTRICT**와 유사하며, 연관된 자식 데이터가 있으면 부모 삭제를 막음!

---

#### 해결 방법

**방법 1: Service에서 명시적으로 처리**

```java
public void deleteMember(Long id) {
    Member member = memberRepository.findById(id)
            .orElseThrow(NotFoundException::new);
    
    // 1. 해당 회원의 모든 게시글 조회
    List<Article> articles = articleRepository.findByMember(member);
    
    // 2. 모든 게시글 삭제
    articleRepository.deleteAll(articles);
    
    // 3. 회원 삭제
    memberRepository.delete(member);
}
```

**장점**: 명시적이고 제어 가능
**단점**: 코드가 복잡해짐

---

**방법 2: CASCADE와 orphanRemoval 사용 (권장)**

**Member Entity 수정**:

```java
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

    private String password;
    private Boolean enabled;

    // ✅ CASCADE와 orphanRemoval 설정
    @OneToMany(mappedBy = "member", 
               cascade = CascadeType.ALL, 
               orphanRemoval = true)
    private List<Article> articles;
}
```

---

**@OneToMany 관계 설정 설명**:

| 항목 | 설명 |
|-----|------|
| **mappedBy** | `Article` 엔티티의 `member` 필드가 관계의 주인임을 명시 (양방향 관계) |
| **cascade** | 부모 엔티티의 생명주기 변화를 자식 엔티티에 전파 |
| **orphanRemoval** | 컬렉션에서 제거된 엔티티를 고아로 간주하고 DB에서 삭제 |

**주의**: `@OneToMany`는 DB의 외래 키 구조를 Java 컬렉션(`List<Article>`) 형태로 표현하기 위한 메타데이터이며, DB에 별도 컬럼으로 저장되지 않음!

---

#### CascadeType.ALL의 의미

**CascadeType.ALL**: 부모 엔티티의 모든 생명주기 변화를 자식에게 전파

| 포함된 타입 | 의미 |
|----------|------|
| **PERSIST** | 부모 저장 시 자식도 함께 저장 |
| **MERGE** | 부모 병합 시 자식도 함께 병합 |
| **REMOVE** | 부모 삭제 시 자식도 함께 삭제 ⭐ (핵심!) |
| **REFRESH** | 부모 새로고침 시 자식도 함께 새로고침 |
| **DETACH** | 부모 분리 시 자식도 함께 분리 |

---

**CascadeType.REMOVE의 동작**:

**논리**: "내가(Member) 죽으면, 나에게 종속된 모든 것(Article)도 함께 죽어라."

**작동 방식**:
```
1. 개발자: memberRepository.delete(member) 호출

2. JPA 자동 처리:
   a. member의 articles 컬렉션에 있는 모든 Article 찾기
   b. 각 Article에 대해 DELETE 쿼리 실행
   c. 마지막으로 Member에 대해 DELETE 쿼리 실행

3. 결과: 회원과 관련된 모든 게시글이 자동 삭제됨
```

**핵심**: 부모 엔티티가 **명시적으로 삭제될 때** 자식도 함께 삭제!

---

#### orphanRemoval = true의 의미

**orphanRemoval**: 컬렉션에서 제거된 엔티티를 고아 객체로 간주하고 DB에서 삭제

**논리**: "부모(Member)는 살아있지만, 자식(Article)이 더 이상 부모의 리스트에 속하지 않는다면, 이 자식은 존재할 이유가 없으므로 삭제한다."

**작동 방식**:
```
1. 개발자: member.getArticles().remove(articleA) 실행
   (Java 리스트에서 articleA 객체 제거)

2. 트랜잭션 커밋 시 JPA 감지:
   a. articleA가 메모리에는 있지만 member의 컬렉션에는 없음
   b. articleA를 고아 객체로 판단

3. DB 반영: articleA에 대한 DELETE 쿼리 자동 전송

4. 결과: 리스트에서 제거 = DB에서도 삭제
```

**핵심**: 부모는 살리고 **컬렉션에서 자식만 분리**할 때 자식 삭제!

---

#### CascadeType.REMOVE vs orphanRemoval

| 구분 | CascadeType.REMOVE | orphanRemoval = true |
|-----|-------------------|---------------------|
| **트리거** | 부모 엔티티 삭제 | 컬렉션에서 자식 제거 |
| **부모 상태** | 부모도 삭제됨 | 부모는 살아있음 |
| **동작** | 부모 삭제 시 자식 삭제 | 리스트에서 제거 시 자식 삭제 |
| **예시** | `memberRepository.delete(member)` | `member.getArticles().remove(article)` |

---

**orphanRemoval 동작 비교**:

| 설정 | 동작 | 객체와 DB 동기화 |
|-----|------|----------------|
| **orphanRemoval = true** | `member.getArticles().remove(articleA)` 실행 시 DB에서도 articleA 삭제 | ✅ 동기화 O |
| **orphanRemoval = false** (기본값) | `member.getArticles().remove(articleA)` 실행해도 DB에는 articleA 계속 존재 | ❌ 동기화 X (고아 데이터 발생) |

---

#### 해결 후 테스트

**요청**:
```
DELETE http://localhost:8080/api/members/1
```

**응답**:
- 응답 본문: (없음)
- 상태 코드: `200 OK` ✅

**결과 확인**:
```
GET http://localhost:8080/api/members
```

```json
[
    {
        "id": 2,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 33
    }
]
```

**게시글 확인**:
```
GET http://localhost:8080/api/articles
```

```json
[]
```

**핵심**: 회원 삭제 시 관련된 모든 게시글도 자동으로 삭제됨! ✅

---




