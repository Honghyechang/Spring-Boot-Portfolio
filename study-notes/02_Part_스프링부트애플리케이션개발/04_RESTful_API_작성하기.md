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
docs: [Ch04] 테스트 - 테스트 작성 완료
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

## 4.2.12 트랜잭션 처리

### 4.2.12.1 트랜잭션의 필요성

#### 문제 상황: 일괄 회원 등록 시 부분 성공 문제

실제 서비스에서는 여러 데이터를 **한 번의 요청으로 동시에 처리**해야 하는 경우가 많습니다. 예를 들어, 여러 회원을 한 번에 등록하는 기능을 구현한다고 가정해봅시다.

---

#### Member Entity 수정: 이메일 중복 방지

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
    
    // ✅ 이메일 유니크 제약조건 추가
    @Column(unique = true)
    private String email;
    
    private Integer age;

    private String password;
    private Boolean enabled;

    @OneToMany(mappedBy = "member", 
               cascade = CascadeType.ALL, 
               orphanRemoval = true)
    private List<Article> articles;
}
```

**@Column(unique = true)**:
- 데이터베이스 레벨에서 이메일 중복을 방지
- 같은 이메일로 두 번째 회원을 저장하려고 하면 예외 발생

---

#### Controller 수정: Batch 등록 엔드포인트 추가

```java
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ArticleService articleService;

    // ✅ 여러 회원을 한 번에 등록하는 엔드포인트
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<MemberResponse> postBatch(@RequestBody List<MemberRequest> memberRequests) {
        return memberService.createBatch(memberRequests);
    }

    // ... 나머지 메서드들 ...
}
```

**변경 사항**:
- 단일 회원 등록(`@RequestBody MemberRequest`)에서 **여러 회원 등록**(`@RequestBody List<MemberRequest>`)으로 변경
- 요청 본문으로 회원 리스트를 받아 처리

---

#### Service 초기 구현 (트랜잭션 미적용)

```java
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse mapToMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge())
                .build();
    }

    // ⚠️ 트랜잭션 없이 구현한 Batch 등록
    public List<MemberResponse> createBatch(List<MemberRequest> memberRequests) {
        List<MemberResponse> lists = memberRequests.stream()
                .map(i -> create(i))
                .collect(Collectors.toList());
        return lists;
    }

    public MemberResponse create(MemberRequest memberRequest) {
        Member member = Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .age(memberRequest.getAge())
                .build();
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    // ... 나머지 메서드들 ...
}
```

---

### 4.2.12.2 데이터 무결성 문제 발생

#### 테스트 시나리오 1: 정상적인 요청

**요청**:
```
POST http://localhost:8080/api/members
Content-Type: application/json

[
    {
        "name": "홍혜창",
        "email": "hyechang@spring.ac.kr",
        "age": 15
    },
    {
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 12
    },
    {
        "name": "홍길동",
        "email": "hong@spring.ac.kr",
        "age": 20
    }
]
```

**응답**:
```json
[
    {
        "id": 1,
        "name": "홍혜창",
        "email": "hyechang@spring.ac.kr",
        "age": 15
    },
    {
        "id": 2,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 12
    },
    {
        "id": 3,
        "name": "홍길동",
        "email": "hong@spring.ac.kr",
        "age": 20
    }
]
```

**상태 코드**: `201 Created` ✅

**결과**: 모든 회원이 정상적으로 등록됨

---

#### 테스트 시나리오 2: 중복 이메일 포함 (문제 발생)

**요청**:
```
POST http://localhost:8080/api/members
Content-Type: application/json

[
    {
        "name": "홍혜창",
        "email": "hong@spring.ac.kr",
        "age": 15
    },
    {
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 12
    },
    {
        "name": "홍길동",
        "email": "hong@spring.ac.kr",  // ⚠️ 첫 번째와 이메일 중복!
        "age": 20
    },
    {
        "name": "김구라",
        "email": "gugugu@spring.ac.kr",
        "age": 40
    }
]
```

**응답**:
```json
{
    "timestamp": "2025-10-24T04:01:58.229+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/members"
}
```

**상태 코드**: `500 Internal Server Error` ❌

---

#### 문제의 핵심: 부분 성공

**DB 상태 확인**:
```
GET http://localhost:8080/api/members
```

**응답**:
```json
[
    {
        "id": 1,
        "name": "홍혜창",
        "email": "hong@spring.ac.kr",
        "age": 15
    },
    {
        "id": 2,
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 12
    }
]
```

**문제 상황 분석**:

| 순서 | 회원 이름 | 이메일 | 결과 |
|-----|---------|--------|------|
| 1 | 홍혜창 | hong@spring.ac.kr | ✅ 성공 (DB에 저장됨) |
| 2 | 김우현 | woo@spring.ac.kr | ✅ 성공 (DB에 저장됨) |
| 3 | 홍길동 | hong@spring.ac.kr | ❌ 실패 (이메일 중복) |
| 4 | 김구라 | gugugu@spring.ac.kr | ❌ 처리되지 않음 |

**핵심 문제**:
- 하나의 요청(Batch)이 **부분적으로만 성공**
- 예외 발생 전까지의 데이터는 DB에 남아있음
- 예외 발생 후의 데이터는 처리되지 않음

---

### 4.2.12.3 ACID 원칙과 데이터 무결성

#### ACID 원칙이란?

**ACID**는 데이터베이스 트랜잭션이 안전하게 수행되도록 보장하는 4가지 속성입니다.

| 속성 | 영문 | 의미 | 설명 |
|-----|------|------|------|
| **원자성** | Atomicity | All or Nothing | 트랜잭션의 모든 작업이 완전히 수행되거나, 전혀 수행되지 않아야 함 |
| **일관성** | Consistency | 데이터 무결성 유지 | 트랜잭션 전후로 데이터베이스가 항상 일관된 상태를 유지해야 함 |
| **격리성** | Isolation | 동시 실행 보장 | 동시에 실행되는 트랜잭션들이 서로 영향을 주지 않아야 함 |
| **지속성** | Durability | 영구 저장 | 성공적으로 완료된 트랜잭션의 결과는 영구적으로 저장되어야 함 |

---

#### 현재 문제: 일관성(Consistency) 위반

**기대하는 동작 (ACID 준수)**:

```
1. 홍혜창 저장 시도 ✅
2. 김우현 저장 시도 ✅
3. 홍길동 저장 시도 ❌ (중복 이메일로 예외 발생)
4. 예외 감지 → 롤백(Rollback) 수행
5. 최종 결과: DB에 아무도 저장되지 않음
```

**실제 동작 (ACID 위반)**:

```
1. 홍혜창 저장 성공 → DB 커밋 ✅
2. 김우현 저장 성공 → DB 커밋 ✅
3. 홍길동 저장 실패 ❌
4. 예외 발생하여 메서드 종료
5. 최종 결과: 홍혜창, 김우현만 DB에 남음 (부분 성공)
```

**핵심**: 
- **원자성(Atomicity)** 위반: 일부만 성공하고 일부는 실패
- **일관성(Consistency)** 위반: 데이터베이스가 불완전한 상태

---

### 4.2.12.4 트랜잭션이란?

#### 트랜잭션의 정의

**트랜잭션(Transaction)**: 데이터베이스의 상태를 변경하는 **하나의 논리적인 작업 단위**

**특징**:
- 여러 개의 작업을 **하나의 묶음**으로 처리
- 모든 작업이 성공하거나, 모두 실패하여 **롤백(Rollback)** 되어야 함
- ACID 원칙을 보장

---

#### 트랜잭션 없이 작동하는 방식

```
createBatch() 메서드 실행
    ↓
1. create(홍혜창) 호출
    → memberRepository.save() 실행
    → DB 즉시 커밋 ✅
    
2. create(김우현) 호출
    → memberRepository.save() 실행
    → DB 즉시 커밋 ✅
    
3. create(홍길동) 호출
    → memberRepository.save() 실행
    → 이메일 중복으로 예외 발생 ❌
    → 메서드 종료
    
4. 최종 결과
    → 홍혜창, 김우현은 이미 커밋되어 DB에 남음
    → 홍길동, 김구라는 저장되지 않음
```

**핵심**: 각 `save()` 호출이 **독립적으로 커밋**되어 부분 성공 발생!

---

#### 트랜잭션을 적용한 방식

```
@Transactional이 붙은 createBatch() 메서드 실행
    ↓
트랜잭션 시작 (Begin Transaction)
    ↓
1. create(홍혜창) 호출
    → memberRepository.save() 실행
    → 영속성 컨텍스트에만 저장 (DB 커밋 보류)
    
2. create(김우현) 호출
    → memberRepository.save() 실행
    → 영속성 컨텍스트에만 저장 (DB 커밋 보류)
    
3. create(홍길동) 호출
    → memberRepository.save() 실행
    → 이메일 중복으로 예외 발생 ❌
    
4. 예외 감지 → 롤백(Rollback) 수행
    → 영속성 컨텍스트의 모든 변경사항 취소
    → DB에는 아무것도 반영되지 않음
    
5. 최종 결과
    → DB에 아무도 저장되지 않음 ✅
```

**핵심**: 메서드가 끝날 때까지 **커밋을 보류**하고, 예외 발생 시 **모든 작업을 롤백**!

---

### 4.2.12.5 @Transactional 어노테이션

#### Service에 트랜잭션 적용

```java
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse mapToMemberResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge())
                .build();
    }

    // ✅ 트랜잭션 적용
    @Transactional
    public List<MemberResponse> createBatch(List<MemberRequest> memberRequests) {
        List<MemberResponse> lists = memberRequests.stream()
                .map(i -> create(i))
                .collect(Collectors.toList());
        return lists;
    }

    public MemberResponse create(MemberRequest memberRequest) {
        Member member = Member.builder()
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .age(memberRequest.getAge())
                .build();
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    // ... 나머지 메서드들 ...
}
```

---

#### @Transactional의 동작 원리

**1. 메서드 실행 전**:

| 단계 | 내용 |
|-----|------|
| **트랜잭션 시작** | Spring이 자동으로 트랜잭션을 시작 (Begin Transaction) |
| **영속성 컨텍스트 생성** | JPA EntityManager가 생성되어 엔티티 관리 시작 |

---

**2. 메서드 실행 중**:

| 단계 | 내용 |
|-----|------|
| **변경사항 추적** | `save()` 호출 시 영속성 컨텍스트에 변경사항이 쌓임 |
| **DB 커밋 보류** | 실제 DB에는 아직 반영되지 않음 (Flush 대기) |

---

**3. 메서드 종료 시**:

**정상 종료 (예외 없음)**:

```
메서드 정상 종료
    ↓
영속성 컨텍스트의 변경사항을 DB에 플러시(Flush)
    ↓
트랜잭션 커밋(Commit)
    ↓
최종적으로 DB에 반영
```

**예외 발생 (RuntimeException, Error)**:

```
메서드 실행 중 예외 발생
    ↓
Spring이 예외를 감지
    ↓
트랜잭션 롤백(Rollback) 수행
    ↓
영속성 컨텍스트의 모든 변경사항 취소
    ↓
DB에는 아무것도 반영되지 않음
```

---

### 4.2.12.6 예외 종류와 롤백 규칙

#### Java 예외의 계층 구조

```
Throwable
    ├── Error (시스템 레벨 오류)
    │   └── OutOfMemoryError, StackOverflowError 등
    │
    └── Exception
        ├── RuntimeException (Unchecked Exception)
        │   ├── NullPointerException
        │   ├── IllegalArgumentException
        │   └── DataIntegrityViolationException (이메일 중복 등)
        │
        └── Checked Exception
            ├── IOException
            ├── SQLException
            └── 기타 명시적 예외 처리가 필요한 예외
```

---

#### @Transactional의 기본 롤백 규칙

| 예외 종류 | 컴파일 시점 처리 | 자동 롤백 여부 | 이유 |
|---------|---------------|-------------|------|
| **RuntimeException** | 명시적 처리 불필요 | ✅ 자동 롤백 | 예상하지 못한 오류이므로 데이터 무결성을 위해 롤백 |
| **Error** | 명시적 처리 불필요 | ✅ 자동 롤백 | 시스템 레벨 오류이므로 롤백 필요 |
| **Checked Exception** | try-catch 또는 throws 필수 | ❌ 롤백 안 함 | 개발자가 명시적으로 처리했으므로 의도가 있다고 판단 |

---

#### Checked Exception에서도 롤백하는 방법

**방법 1: try-catch에서 RuntimeException 던지기**

```java
@Transactional
public void someMethod() {
    try {
        // Checked Exception이 발생할 수 있는 코드
        riskyOperation();
    } catch (IOException e) {
        // Checked Exception을 RuntimeException으로 변환
        throw new RuntimeException("파일 처리 실패", e);
    }
}
```

---

**방법 2: rollbackFor 속성 사용**

```java
@Transactional(rollbackFor = Exception.class)
public void someMethod() throws IOException {
    // 모든 Exception (Checked 포함)에서 롤백
    riskyOperation();
}
```

| 속성 | 설명 | 예시 |
|-----|------|------|
| **rollbackFor** | 지정한 예외 발생 시 롤백 | `@Transactional(rollbackFor = {IOException.class, SQLException.class})` |
| **noRollbackFor** | 지정한 예외 발생 시 롤백하지 않음 | `@Transactional(noRollbackFor = IllegalArgumentException.class)` |

---

#### 이메일 중복 예외의 처리

**발생하는 예외**:
- `DataIntegrityViolationException` (Spring이 제공하는 예외)
- 이 예외는 `RuntimeException`을 상속받음

**결과**:
- `@Transactional`이 자동으로 감지하여 롤백 수행 ✅

```
1. 홍길동 저장 시도
    ↓
2. 이메일 중복으로 DataIntegrityViolationException 발생
    ↓
3. Spring이 예외를 감지 (RuntimeException 계열)
    ↓
4. 자동으로 트랜잭션 롤백
    ↓
5. 영속성 컨텍스트의 모든 변경사항 취소
    ↓
6. DB에 아무것도 반영되지 않음
```

---

### 4.2.12.7 트랜잭션 적용 후 테스트

#### 중복 이메일 요청 (트랜잭션 적용 후)

**요청**:
```
POST http://localhost:8080/api/members
Content-Type: application/json

[
    {
        "name": "홍혜창",
        "email": "hong@spring.ac.kr",
        "age": 15
    },
    {
        "name": "김우현",
        "email": "woo@spring.ac.kr",
        "age": 12
    },
    {
        "name": "홍길동",
        "email": "hong@spring.ac.kr",  // 중복!
        "age": 20
    },
    {
        "name": "김구라",
        "email": "gugugu@spring.ac.kr",
        "age": 40
    }
]
```

**응답**:
```json
{
    "timestamp": "2025-10-24T04:01:58.229+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/members"
}
```

**상태 코드**: `500 Internal Server Error`

---

**DB 상태 확인**:
```
GET http://localhost:8080/api/members
```

**응답**:
```json
[]
```

**결과**: DB에 **아무도 저장되지 않음** ✅

**트랜잭션 적용 전후 비교**:

| 구분 | 트랜잭션 적용 전 | 트랜잭션 적용 후 |
|-----|----------------|----------------|
| **홍혜창** | ✅ DB에 저장됨 | ❌ 롤백됨 |
| **김우현** | ✅ DB에 저장됨 | ❌ 롤백됨 |
| **홍길동** | ❌ 저장 안 됨 (예외) | ❌ 저장 안 됨 (예외) |
| **김구라** | ❌ 처리 안 됨 | ❌ 처리 안 됨 |
| **최종 결과** | ⚠️ 부분 성공 (2명 저장) | ✅ 완전 실패 (0명 저장) |
| **ACID 준수** | ❌ 원자성, 일관성 위반 | ✅ ACID 원칙 준수 |

---

## 4.3 테스트

### 4.3.1 테스트의 중요성

#### 테스트란?

**테스트(Testing)**: 작성한 코드가 의도한 대로 정확하게 동작하는지 검증하는 과정

**테스트의 목적**:

| 목적 | 설명 |
|-----|------|
| **품질 보장** | 소프트웨어가 요구사항을 충족하는지 확인 |
| **버그 예방** | 코드 변경 시 기존 기능이 깨지지 않는지 검증 (회귀 버그 방지) |
| **리팩토링 안정성** | 코드 개선 시 기능이 유지되는지 보장 |
| **문서화** | 테스트 코드 자체가 기능의 사용법을 보여주는 문서 역할 |

---

#### TDD (Test-Driven Development)

**TDD**: 테스트를 먼저 작성하고, 그 테스트를 통과하는 코드를 작성하는 개발 방법론

**TDD 개발 순서**:
```
1. 실패하는 테스트 작성 (Red)
    ↓
2. 테스트를 통과하는 최소한의 코드 작성 (Green)
    ↓
3. 코드 개선 (Refactor)
    ↓
반복...
```

**핵심**: 테스트를 적극적으로 활용하여 안정적인 소프트웨어 개발

---

### 4.3.2 Spring Boot 테스트 환경 구성

#### 필수 의존성 (build.gradle)

Spring Boot 프로젝트를 생성하면 테스트 관련 의존성이 자동으로 추가됩니다.

```gradle
dependencies {
    // Spring Boot 테스트 스타터 (JUnit 5, Mockito, AssertJ 등 포함)
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    
    // JUnit 플랫폼 런처 (테스트 실행 환경)
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    
    // 테스트 환경에서 Lombok 사용을 위한 의존성
    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'
}
```

**주요 라이브러리**:

| 라이브러리 | 역할 |
|----------|------|
| **JUnit 5** | Java 테스트 프레임워크 (테스트 실행 및 관리) |
| **Mockito** | Mock 객체 생성 라이브러리 (단위 테스트용) |
| **AssertJ** | 유창한(Fluent) 검증 라이브러리 |
| **Spring Test** | Spring 통합 테스트 지원 |

---

#### 테스트 코드 위치 및 관례

**테스트 코드의 위치**:
```
프로젝트 루트
├── src
│   ├── main
│   │   └── java
│   │       └── com.example.restfulapiSample
│   │           ├── controller
│   │           │   └── MemberController.java
│   │           ├── service
│   │           │   └── MemberService.java
│   │           └── repository
│   │               └── MemberRepository.java
│   │
│   └── test  ← 테스트 코드 위치
│       └── java
│           └── com.example.restfulapiSample  ← main과 동일한 패키지 구조
│               ├── controller
│               │   └── MemberControllerTests.java
│               ├── service
│               │   └── MemberServiceTests.java
│               └── repository
│                   └── MemberRepositoryTests.java
```

**테스트 코드 작성 관례**:

| 항목 | 규칙 | 예시 |
|-----|------|------|
| **위치** | `src/test/java` 폴더 | 실제 코드와 분리 |
| **패키지** | 테스트 대상과 동일한 패키지 구조 | `com.example.restfulapiSample.service` |
| **클래스명** | 테스트 대상 클래스명 + `Tests` | `MemberService` → `MemberServiceTests` |

**핵심**: 테스트 코드는 `test` 폴더에 `main`과 동일한 패키지 구조로 작성하며, 클래스명 뒤에 `Tests`를 붙이는 것이 관례!

---

### 4.3.3 기본 테스트 클래스 이해

#### RestfulapiSampleApplicationTests

프로젝트를 생성하면 기본적으로 제공되는 테스트 클래스입니다.

```java
package com.example.restfulapiSample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestfulapiSampleApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

---

#### @SpringBootTest 어노테이션

**@SpringBootTest**: Spring Boot 테스트에서 가장 핵심적인 어노테이션

**역할**:

| 순서 | 역할 | 설명 |
|-----|------|------|
| **1. 전체 애플리케이션 컨텍스트 로딩** | 실제 운영 환경 구성 | `@SpringBootApplication`이 붙은 메인 클래스를 찾아 전체 Spring 컨테이너를 구동하고, 실제 운영 환경처럼 모든 빈 객체들을 생성하고 의존성 주입(DI)을 완료 |
| **2. 준비된 빈 객체의 주입 허용** | 테스트에서 빈 사용 가능 | 컨텍스트 로딩이 완료된 후, 테스트 클래스 내에서 `@Autowired`를 통해 실제 빈 객체들을 주입받아 테스트에 사용 가능 |

**의미**:
```
@SpringBootTest가 붙은 테스트 클래스
= "실제 애플리케이션과 동일한 환경을 준비해라"
= "모든 빈을 생성하고 주입까지 완료해라"
```

**핵심**: `@SpringBootTest`는 실제 운영 환경과 최대한 유사한 통합 테스트 환경을 제공!

---

#### contextLoads() 메서드

```java
@Test
void contextLoads() {
}
```

**역할**: 스프링 컨테이너가 정상적으로 구동되는지 검증하는 가장 기본적인 테스트

**동작 원리**:

```
1. JUnit이 @SpringBootTest 클래스 발견
    ↓
2. Spring 컨테이너 구동 시작
    ↓
3. 모든 빈 객체 생성 및 의존성 주입 수행
    ↓
4. 컨텍스트 로딩 성공 시 contextLoads() 실행
    ↓
5. 메서드 내용이 비어있지만, 예외 없이 실행 완료
    ↓
6. 테스트 성공 ✅
```

**검증 내용**:
- 메서드가 비어있지만, 이 메서드가 예외 없이 실행되었다는 사실 자체가 **"Spring 컨테이너가 모든 빈을 정상적으로 생성하고 주입했다"**는 것을 증명
- 설정 오류, 순환 참조, 빈 생성 실패 등이 있으면 이 테스트에서 실패

**핵심**: `contextLoads()`는 가장 기본적인 **"환경 구성 검증 테스트"**!

---

### 4.3.4 테스트 실행 방법

#### 테스트 실행 방법 비교

| 구분 | 실행 단위 | 실행 방법 | 주요 용도 |
|-----|---------|----------|----------|
| **단일 클래스/메서드 테스트** | 특정 테스트 클래스 또는 메서드 | IDE (IntelliJ, Eclipse 등)에서 해당 테스트 파일 또는 메서드 우측 클릭 후 'Run' 선택 | 개발 중 특정 기능에 대한 빠른 검증 및 디버깅 |
| **전체 테스트 실행** | 프로젝트 내 모든 테스트 | Gradle 사용: `tasks` → `verification` → `test` 실행<br>또는 터미널에서 `./gradlew test` | 빌드 전 또는 배포 전 프로젝트의 전반적인 안정성 확인 |

---

#### 1. 단일 클래스 실행 (IDE 사용)

**IntelliJ에서 실행**:

```
1. 테스트 클래스 파일 열기
    예: RestfulapiSampleApplicationTests.java

2. 클래스명 또는 메서드명 우측 클릭
    ↓
3. "Run 'RestfulapiSampleApplicationTests'" 선택
    또는 메서드 단위: "Run 'contextLoads()'" 선택
    ↓
4. 테스트 실행 및 결과 확인
```

**장점**:
- ✅ 매우 빠름 (특정 테스트만 실행)
- ✅ 디버깅 모드로 쉽게 전환 가능
- ✅ 즉각적인 결과 확인

**단점**:
- ⚠️ 전체 프로젝트 상태를 대변하지 못함

---

#### 2. 전체 테스트 실행 (Gradle 사용)

**IntelliJ Gradle 탭에서 실행**:

```
1. IntelliJ 우측의 Gradle 탭 클릭
    ↓
2. 프로젝트명 → Tasks → verification → test 더블클릭
    ↓
3. 모든 테스트 실행 및 결과 확인
```

**터미널에서 실행**:

```bash
# 프로젝트 루트 디렉토리에서
./gradlew test
```

**장점**:
- ✅ 프로젝트의 모든 테스트를 한 번에 실행
- ✅ CI/CD 파이프라인에서 자동화 가능
- ✅ 전체 안정성 보장

**단점**:
- ⚠️ 시간이 오래 걸릴 수 있음 (테스트가 많을 경우)

---

#### 테스트 실행 전략

| 상황 | 권장 방법 |
|-----|---------|
| **개발 중 특정 기능 검증** | 단일 클래스/메서드 실행 (IDE) |
| **리팩토링 후 영향 범위 확인** | 관련 테스트 클래스 실행 (IDE) |
| **커밋 전 최종 확인** | 전체 테스트 실행 (Gradle) |
| **빌드 및 배포 전** | 전체 테스트 실행 (Gradle) |

---

### 4.3.5 Repository 테스트 작성

#### MemberRepository 인터페이스

테스트 대상이 되는 Repository입니다.

```java
package com.example.restfulapiSample.repository;

import com.example.restfulapiSample.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 함수명 쿼리 메서드
    List<Member> findByName(String name);
    List<Member> findByEmail(String email);
    List<Member> findByNameAndEmail(String name, String email);
    List<Member> findByNameOrEmail(String name, String email);
    List<Member> findByNameContaining(String name);
    List<Member> findByAgeGreaterThan(Integer age);
    List<Member> findByAgeLessThan(Integer age);
    List<Member> findByAgeBetween(Integer minAge, Integer maxAge);
    List<Member> findByAgeGreaterThanEqual(Integer age);
    List<Member> findByAgeLessThanEqual(Integer age);
    List<Member> findByNameLike(String name);
    List<Member> findByAge(Integer age);
    
    // 정렬
    List<Member> findAllByOrderByAgeDesc();
    List<Member> findAllByOrderByNameAsc();
    
    // @Query 어노테이션 사용
    @Query("select m from Member m where m.name = :name")
    List<Member> findMemberByName(@Param("name") String name);
    
    @Query("select m from Member m where m.name = :name and m.email = :email")
    List<Member> findMemberByNameEmail(@Param("name") String name, @Param("email") String email);
}
```

---

#### MemberRepositoryTests 작성

**테스트 환경에서 Lombok 사용을 위한 의존성 추가** (build.gradle):

```gradle
dependencies {
    // ... 기존 의존성 ...
    
    // 테스트 환경에서 Lombok 사용
    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'
}
```

**주의**: 이 의존성을 추가해야 테스트 코드에서 `@Slf4j`, `@Builder` 등 Lombok 어노테이션을 사용할 수 있습니다!

---

**MemberRepositoryTests.java**:

```java
package com.example.restfulapiSample.repository;

import com.example.restfulapiSample.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@DisplayName("MemberRepository 테스트")
public class MemberRepositoryTests {
    
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void doBeforeEach() {
        memberRepository.save(Member.builder()
                .name("홍혜창")
                .email("hyechang@spring.ac.kr")
                .age(30)
                .enabled(true)
                .build());
        memberRepository.save(Member.builder()
                .name("김우현")
                .email("woohyun@spring.ac.kr")
                .age(20)
                .enabled(true)
                .build());
        memberRepository.save(Member.builder()
                .name("김구라")
                .email("gugugu@spring.ac.kr")
                .age(20)
                .enabled(true)
                .build());
        memberRepository.save(Member.builder()
                .name("손흥민")
                .email("sonny@spring.ac.kr")
                .age(33)
                .enabled(true)
                .build());
    }

    @AfterEach
    public void doAfterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("첫번째 테스트")
    public void testUserCase1() {
        // count() 검증
        assertThat(memberRepository.count()).isEqualTo(4);
        
        // findByName() 검증
        assertThat(memberRepository.findByName("홍혜창").size()).isEqualTo(1);
    }

    @Test
    @DisplayName("두번째 테스트")
    @Disabled("잠시 테스트 중단")
    public void testUserCase2() {
        assertThat(memberRepository.findByNameLike("%현").size()).isEqualTo(1);
        assertThat(memberRepository.findByAge(20).size()).isEqualTo(2);
    }

    @RepeatedTest(value = 3, name = "테스트 종류: {displayName} 중 {currentRepetition}/{totalRepetitions}")
    @DisplayName("세번째 테스트")
    public void testUserCase3() {
        assertThat(memberRepository.findAllByOrderByAgeDesc().get(0).getName())
                .isEqualTo("손흥민");
    }
}
```

---

#### 테스트 환경에서의 의존성 주입

**@Autowired vs @RequiredArgsConstructor**:

| 구분 | @Autowired (필드 주입) | @RequiredArgsConstructor (생성자 주입) |
|-----|---------------------|--------------------------------|
| **작동 원리** | 필드에 직접 주입 | `final` 필드를 위한 생성자를 만들어 주입 |
| **테스트 환경 안정성** | ✅ 안정적 | ⚠️ JUnit 충돌 가능성 있음 |
| **권장 여부** | ✅ 테스트 코드에서 권장 | ⚠️ 테스트 코드에서는 비권장 |

**테스트 환경에서 @Autowired를 사용하는 이유**:

**문제 상황**:
```java
@SpringBootTest
@RequiredArgsConstructor  // ⚠️ 문제 발생 가능
public class MemberRepositoryTests {
    private final MemberRepository memberRepository;
    // ...
}
```

**발생하는 오류**:
- `ParameterResolutionException`: JUnit이 생성자 매개변수를 해결하지 못함
- JUnit이 Spring보다 먼저 생성자를 호출하려고 시도하여 충돌 발생

**해결 방법**:
```java
@SpringBootTest
public class MemberRepositoryTests {
    @Autowired  // ✅ 테스트 환경에서 안전
    private MemberRepository memberRepository;
    // ...
}
```

**핵심**: 
- **애플리케이션 코드**에서는 `@RequiredArgsConstructor` + 생성자 주입 권장 (불변성)
- **테스트 코드**에서는 `@Autowired` + 필드 주입 권장 (안정성)

---

#### @BeforeEach와 @AfterEach

**테스트 생명주기 관리 어노테이션**:

| 어노테이션 | 실행 시점 | 역할 |
|----------|---------|------|
| **@BeforeEach** | 각 `@Test` 메서드 실행 **직전** | 테스트 환경 초기화 (테스트 데이터 준비) |
| **@AfterEach** | 각 `@Test` 메서드 실행 **직후** | 테스트 환경 정리 (데이터 삭제, 자원 해제) |

---

**실행 순서 예시**:

```
MemberRepositoryTests 실행 시작
    ↓
doBeforeEach() 실행  ← 4명의 회원 저장
    ↓
testUserCase1() 실행  ← 첫 번째 테스트
    ↓
doAfterEach() 실행  ← 모든 회원 삭제
    ↓
doBeforeEach() 실행  ← 4명의 회원 저장 (다시)
    ↓
testUserCase2() 실행  ← 두 번째 테스트
    ↓
doAfterEach() 실행  ← 모든 회원 삭제
    ↓
doBeforeEach() 실행  ← 4명의 회원 저장 (다시)
    ↓
testUserCase3() 실행  ← 세 번째 테스트
    ↓
doAfterEach() 실행  ← 모든 회원 삭제
    ↓
테스트 종료
```

**핵심**: 각 테스트 메서드는 **완전히 독립적인 환경**에서 실행됨!

---

**테스트 데이터와 실제 DB**:

| 항목 | 설명 |
|-----|------|
| **실제 DB 저장 여부** | ❌ 저장되지 않음 |
| **트랜잭션 롤백** | `@SpringBootTest`는 기본적으로 `@Transactional`이 적용되어 각 테스트가 끝나면 자동 롤백 |
| **@AfterEach의 역할** | 트랜잭션 롤백과 별개로 **명시적으로 데이터를 정리**하여 테스트 간 간섭 방지 |

**핵심**: 
- 테스트 중 DB 변경사항은 **임시 트랜잭션 내에서만** 이루어짐
- 테스트 종료 시 자동으로 롤백되어 **실제 DB에는 영향 없음**

---

#### AssertJ와 assertThat

**AssertJ**: 유창한(Fluent) 검증 라이브러리

**assertThat의 의미**:
```java
assertThat(memberRepository.count()).isEqualTo(4);
```

해석: "나는 `memberRepository.count()`의 결과가 `4`와 같아야 한다고 주장한다"

---

**assertThat의 작동 원리**:

| 단계 | 코드 | 역할 |
|-----|------|------|
| **1. 검증 대상 전달** | `assertThat(actual)` | 실제 값(Actual Value)을 검증 체인의 시작점으로 전달 |
| **2. 검증 객체 반환** | `AbstractLongAssert<?>` | 해당 타입에 대한 검증 메서드들을 담은 객체 반환 |
| **3. 조건 검증** | `.isEqualTo(expected)` | 실제 값이 기대 값과 같은지 검증 |

---

**if 문과의 차이점**:

| 구분 | assertThat(...).isEqualTo(...) | if (... == ...) |
|-----|-------------------------------|----------------|
| **테스트 실패 처리** | ✅ 검증 실패 시 JUnit에게 실패를 알리고 즉시 중단 | ❌ 조건이 거짓이어도 테스트 계속 진행 (수동으로 예외를 던져야 함) |
| **에러 메시지** | ✅ 자동으로 상세한 메시지 생성 (`Expected 4, but was 5`) | ❌ 개발자가 직접 작성해야 함 |
| **가독성** | ✅ 메서드 체이닝으로 자연스러운 문장처럼 읽힘 | ❌ 조건문 형태로 의도 파악 어려움 |

---

**예시**:

```java
// ✅ AssertJ 사용 (권장)
assertThat(5).isEqualTo(4);
// 결과: Expected 4, but was 5 (매우 명확한 에러 메시지)

// ❌ if 문 사용 (비권장)
if (5 != 4) {
    // 개발자가 직접 예외를 던지지 않으면 테스트는 성공 처리될 수 있음
    throw new AssertionError("값이 다릅니다");
}
```

**핵심**: `assertThat`은 테스트의 의도를 명확히 표현하고, 실패 시 상세한 정보를 제공!

---

#### 주요 검증 메서드

**AssertJ의 주요 검증 메서드**:

| 메서드 | 의미 | 예시 |
|-------|------|------|
| **isEqualTo(expected)** | 같음 | `assertThat(count).isEqualTo(4)` |
| **isNotEqualTo(expected)** | 다름 | `assertThat(count).isNotEqualTo(0)` |
| **isNull()** | null임 | `assertThat(member).isNull()` |
| **isNotNull()** | null이 아님 | `assertThat(member).isNotNull()` |
| **isZero()** | 0임 | `assertThat(age).isZero()` |
| **isNotZero()** | 0이 아님 | `assertThat(id).isNotZero()` |
| **isGreaterThan(value)** | 초과 | `assertThat(age).isGreaterThan(18)` |
| **isLessThan(value)** | 미만 | `assertThat(age).isLessThan(65)` |
| **contains(element)** | 포함 | `assertThat(list).contains(member)` |
| **isEmpty()** | 비어있음 | `assertThat(list).isEmpty()` |

---

#### JUnit 5 테스트 어노테이션

**@DisplayName**: 테스트 이름을 사용자 친화적으로 표시

```java
@DisplayName("MemberRepository 테스트")
public class MemberRepositoryTests {
    
    @Test
    @DisplayName("첫번째 테스트")
    public void testUserCase1() {
        // ...
    }
}
```

**효과**:

| 기존 | @DisplayName 적용 후 |
|-----|-------------------|
| `MemberRepositoryTests` | `MemberRepository 테스트` |
| `testUserCase1()` | `첫번째 테스트` |

**장점**:
- ✅ 테스트 보고서 가독성 향상
- ✅ 테스트 의도를 명확히 표현
- ✅ 한글 등 다양한 언어 사용 가능

---

**@Disabled**: 테스트 비활성화

```java
@Test
@DisplayName("두번째 테스트")
@Disabled("잠시 테스트 중단")
public void testUserCase2() {
    // 이 테스트는 실행되지 않음
}
```

**사용 시나리오**:

| 상황 | 설명 |
|-----|------|
| **개발 중인 테스트** | 아직 완벽하게 구현되지 않아 빌드를 깨뜨리는 테스트를 잠시 제외 |
| **환경 의존적 테스트** | 특정 환경(DB, 네트워크 등)이 갖춰지지 않으면 실행할 수 없는 테스트를 임시로 건너뛰기 |
| **장기 실행 테스트** | 시간이 오래 걸리는 테스트를 평상시에는 제외하고 특정 시점에만 실행 |

**결과**: 테스트가 **생략(Skipped)** 되어 실행되지 않음

---

**@RepeatedTest**: 테스트 반복 실행

```java
@RepeatedTest(value = 3, name = "테스트 종류: {displayName} 중 {currentRepetition}/{totalRepetitions}")
@DisplayName("세번째 테스트")
public void testUserCase3() {
    // 이 테스트는 3번 반복 실행됨
}
```

**매개변수**:

| 속성 | 설명 | 예시 |
|-----|------|------|
| **value** | 반복 횟수 | `value = 3` → 3번 실행 |
| **name** | 각 반복의 표시 이름 | `{displayName}`, `{currentRepetition}`, `{totalRepetitions}` 사용 가능 |

**사용 시나리오**:

| 상황 | 설명 |
|-----|------|
| **안정성 검증** | 특정 테스트가 반복 실행될 때도 항상 동일한 결과(성공)를 내는지 확인 |
| **무작위성 검증** | 난수(Randomness)나 외부 요소에 의존하는 로직을 충분히 테스트 |
| **동시성 문제 탐지** | 반복 실행으로 타이밍 이슈나 경쟁 조건(Race Condition) 발견 |

**실행 결과 예시**:

```
[root]
  MemberRepository 테스트
    ✅ 첫번째 테스트
    ⊘ 두번째 테스트 (생략)
    ✅ 테스트 종류: 세번째 테스트 중 1/3
    ✅ 테스트 종류: 세번째 테스트 중 2/3
    ✅ 테스트 종류: 세번째 테스트 중 3/3
```

---

#### 테스트 실행 결과 분석

**초기 테스트 실패 예시**:

```java
@Test
@DisplayName("첫번째 테스트")
public void testUserCase1() {
    // ❌ 실패: @BeforeEach에서 4명을 저장했는데 0을 기대
    assertThat(memberRepository.count()).isEqualTo(0);
    
    assertThat(memberRepository.findByName("홍혜창").size()).isEqualTo(1);
}
```

**실패 원인**:
- `@BeforeEach`에서 4명의 회원을 저장
- 테스트는 `count()`가 0일 것을 기대
- 실제 값은 4 → **Assertion 실패**

**에러 메시지**:
```
Expected: 0L
but was: 4L
```

---

**수정된 테스트 (성공)**:

```java
@Test
@DisplayName("첫번째 테스트")
public void testUserCase1() {
    // ✅ 성공: 4명을 저장했으므로 4를 기대
    assertThat(memberRepository.count()).isEqualTo(4);
    
    // ✅ 성공: "홍혜창"이라는 이름을 가진 회원 1명 존재
    assertThat(memberRepository.findByName("홍혜창").size()).isEqualTo(1);
}
```

---

#### JUnit 테스트 실패 원칙

**Fail-Fast 원칙**: 하나의 테스트라도 실패하면 전체 테스트는 실패

**테스트 실패 전파**:

```
테스트 클래스 (MemberRepositoryTests)
├── testUserCase1() ❌ 실패
├── testUserCase2() ⊘ 생략
└── testUserCase3() ✅ 성공

최종 결과: ❌ FAILED
```

**원칙**:
- 테스트 클래스 내의 **단 하나의 @Test 메서드라도 실패**하면 전체 테스트는 실패
- Gradle 태스크(`:test`)는 `FAILED`를 반환하고 빌드가 실패

**메서드 내 Assertion 실패**:

```java
@Test
public void test() {
    assertThat(count).isEqualTo(0);  // ❌ 여기서 실패
    assertThat(name).isEqualTo("홍길동");  // 실행되지 않음
}
```

**핵심**: 
- 첫 번째 `assertThat`이 실패하는 순간 메서드 즉시 중단
- 이후의 검증은 실행되지 않음

---

### 4.3.6 Service 통합 테스트

#### MemberServiceTests 작성

**통합 테스트**: Service와 Repository가 함께 작동하는 것을 검증

```java
package com.example.restfulapiSample.service;

import com.example.restfulapiSample.dto.MemberRequest;
import com.example.restfulapiSample.dto.MemberResponse;
import com.example.restfulapiSample.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("MemberServiceTests 테스트")
public class MemberServiceTests {
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void afterEach() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 추가 및 조회")
    public void testUsers() {
        // 첫 번째 회원 생성
        MemberRequest memberRequest = MemberRequest.builder()
                .name("홍혜창")
                .email("hyechang@spring.ac.kr")
                .age(10)
                .build();
        MemberResponse memberResponse = memberService.create(memberRequest);
        
        // ID가 잘 생성되었는지 검증
        assertThat(memberResponse.getId()).isNotNull();

        // 두 번째 회원 생성
        memberRequest = MemberRequest.builder()
                .name("김우현")
                .email("woohyun@spring.ac.kr")
                .age(10)
                .build();
        memberResponse = memberService.create(memberRequest);
        
        // ID가 잘 생성되었는지 검증
        assertThat(memberResponse.getId()).isNotNull();

        // 전체 회원 조회
        List<MemberResponse> memberResponses = memberService.findAll();
        assertThat(memberResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("트랜잭션 커밋 테스트")
    public void testCommit() {
        List<MemberRequest> memberRequests = List.of(
                MemberRequest.builder().name("김우현").email("woohyun@spring.ac.kr").age(10).build(),
                MemberRequest.builder().name("홍혜창").email("hyechang@spring.ac.kr").age(10).build(),
                MemberRequest.builder().name("홍길동").email("hong@spring.ac.kr").age(10).build(),
                MemberRequest.builder().name("김구라").email("gugu@spring.ac.kr").age(10).build()
        );

        try {
            memberService.createBatch(memberRequests);
        } catch (Exception e) {
            // 예외 발생 시 무시
        }

        // 정상적으로 모두 커밋되었는지 검증
        assertThat(memberService.findAll().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("트랜잭션 롤백 테스트")
    public void testRollback() {
        List<MemberRequest> memberRequests = List.of(
                MemberRequest.builder().name("김우현").email("woohyun@spring.ac.kr").age(10).build(),
                MemberRequest.builder().name("홍혜창").email("hong@spring.ac.kr").age(10).build(),
                MemberRequest.builder().name("홍길동").email("hong@spring.ac.kr").age(10).build(),  // ⚠️ 중복 이메일!
                MemberRequest.builder().name("김구라").email("gugu@spring.ac.kr").age(10).build()
        );

        try {
            memberService.createBatch(memberRequests);
        } catch (Exception e) {
            // 예외 발생 시 무시
        }

        // 롤백되어 아무것도 저장되지 않았는지 검증
        assertThat(memberService.findAll().size()).isEqualTo(0);
    }
}
```

---

#### 통합 테스트 vs 단위 테스트

**통합 테스트 (Integration Test)**:

| 특징 | 설명 |
|-----|------|
| **정의** | 여러 컴포넌트가 함께 작동하는 것을 검증 |
| **범위** | Service + Repository + Database |
| **의존성** | 실제 빈 객체 사용 (`@Autowired`) |
| **DB 연결** | 실제 DB(H2 인메모리) 사용 |
| **속도** | 상대적으로 느림 |
| **검증 내용** | 전체 흐름이 정상적으로 작동하는지 |

**단위 테스트 (Unit Test)**:

| 특징 | 설명 |
|-----|------|
| **정의** | 각 컴포넌트를 독립적으로 검증 |
| **범위** | Service만 (Repository는 Mock) |
| **의존성** | 가짜 객체 사용 (`@MockitoBean`) |
| **DB 연결** | DB 사용하지 않음 (Mock 응답) |
| **속도** | 매우 빠름 |
| **검증 내용** | Service의 비즈니스 로직만 검증 |

---

**현재 코드의 특징**:

```java
@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private MemberRepository memberRepository;  // ← 실제 Repository 사용
}
```

**분석**:
- `MemberService`를 테스트하지만 내부에서 **실제 `MemberRepository`**를 사용
- Repository는 **실제 DB(H2 인메모리)**에 연결
- Service와 Repository가 **함께 작동하는 것**을 검증

**결론**: 이 테스트는 **통합 테스트**!

---

#### 테스트 환경에서 예외 처리

**트랜잭션 롤백 테스트의 문제**:

```java
@Test
public void testRollback() {
    // 중복 이메일 포함 → 예외 발생 예상
    memberService.createBatch(memberRequests);  // ❌ 예외 발생 시 테스트 중단!
    
    // 이 검증 코드에 도달하지 못함
    assertThat(memberService.findAll().size()).isEqualTo(0);
}
```

**문제점**:
- `createBatch()`에서 예외 발생 시 메서드가 즉시 종료
- 롤백 검증 코드(`assertThat...`)가 실행되지 않음
- JUnit이 테스트를 **"실행 중 오류(Error)"**로 처리

---

**해결 방법 1: try-catch 사용 (현재 코드)**

```java
@Test
public void testRollback() {
    try {
        memberService.createBatch(memberRequests);
    } catch (Exception e) {
        // 예외를 잡아서 테스트가 계속 진행되도록 함
    }
    
    // 롤백 검증 가능
    assertThat(memberService.findAll().size()).isEqualTo(0);
}
```

**장점**:
- ✅ Java 기본 문법만 사용
- ✅ 테스트가 중단되지 않고 계속 진행

**단점**:
- ⚠️ 예외 발생 여부를 명확히 검증하지 못함
- ⚠️ 만약 예외가 발생하지 않으면 테스트가 잘못된 이유로 성공할 수 있음

---

**해결 방법 2: assertThrows 사용 (권장)**

```java
@Test
public void testRollback() {
    // 예외 발생 자체를 검증
    assertThrows(DataIntegrityViolationException.class, () -> {
        memberService.createBatch(memberRequests);
    });
    
    // 롤백 검증
    assertThat(memberService.findAll().size()).isEqualTo(0);
}
```

**장점**:
- ✅ 예외 발생 여부를 명확히 검증
- ✅ 예상한 예외 타입이 아니면 테스트 실패
- ✅ 테스트의 의도가 명확

**단점**:
- JUnit API 필요

---

**비교 정리**:

| 방법 | 가독성 | 예외 검증 | 권장 여부 |
|-----|-------|---------|---------|
| **try-catch** | 보통 | ❌ 없음 | △ 동작은 하지만 비권장 |
| **assertThrows** | 높음 | ✅ 명확 | ✅ 권장 |

**핵심**: 
- `try-catch`는 기술적으로 가능하지만 테스트의 의도가 불명확
- `assertThrows`는 "이 코드는 예외를 발생시켜야 한다"는 검증을 명확히 표현

---

### 4.3.7 Controller 통합 테스트 (MockMvc)

#### MockMvc의 필요성

**기존 방식의 문제점**:

```
1. Spring Boot 애플리케이션 실행
    → WAS(Tomcat) 구동 (포트 8080 바인딩)
    ↓
2. Postman으로 HTTP 요청 전송
    → http://localhost:8080/api/members
    ↓
3. 응답 확인
```

**문제점**:

| 문제 | 설명 |
|-----|------|
| **시간 소모** | 서버를 구동하고 포트 바인딩을 기다리는 데 시간 소요 (초 단위) |
| **자원 소모** | WAS가 메모리(RAM)와 CPU 자원을 소모 |
| **반복 작업** | 코드 수정 → 서버 재시작 → Postman 요청 → 결과 확인 (매우 비효율적) |
| **자동화 불가** | 수동으로 Postman에서 요청을 보내야 하므로 CI/CD 파이프라인에 통합 어려움 |

---

**MockMvc의 해결책**:

**MockMvc**: 실제 서버를 구동하지 않고 **가상의 HTTP 요청을 생성**하여 Controller를 테스트하는 도구

**특징**:

| 항목 | 설명 |
|-----|------|
| **서버 구동** | ❌ WAS를 구동하지 않음 (포트 바인딩 없음) |
| **요청 생성** | ✅ 가상의 HTTP 요청 객체 생성 |
| **Controller 호출** | ✅ Spring MVC 내부 메커니즘을 통해 Controller에 직접 전달 |
| **응답 포착** | ✅ Controller의 응답을 가로채서 검증 |
| **속도** | ✅ 매우 빠름 (네트워크 부하 없음) |
| **자동화** | ✅ 코드로 작성되어 자동화 가능 |

---

**MockMvc의 역할 비유**:

```
실제 환경 (Postman):
클라이언트(Postman) → 네트워크 → WAS(Tomcat) → Controller

MockMvc 환경:
MockMvc(가상 클라이언트) → Spring MVC 내부 → Controller
```

**핵심**: 
- MockMvc는 **"서버 없이 Controller를 테스트"**할 수 있게 해줌
- 실제 네트워크를 거치지 않으므로 **매우 빠름**
- **통합 테스트**로 Controller → Service → Repository → DB 전체 흐름 검증 가능

---

#### 통합 테스트의 목적

**통합 테스트로 검증하는 범위**:

```
클라이언트 (HTTP 요청)
    ↓
Controller (요청 받기, 응답 반환)
    ↓
Service (비즈니스 로직)
    ↓
Repository (DB 접근)
    ↓
Database (H2)
```

**핵심**: 
- Controller 하나만 테스트하는 것이 아님
- **전체 애플리케이션 스택**이 정상적으로 작동하는지 검증
- RESTful API의 모든 엔드포인트가 제대로 동작하는지 확인

---

#### MemberControllerTests 작성

**환경 설정**:

```java
package com.example.restfulapiSample.controller;

import com.example.restfulapiSample.dto.MemberRequest;
import com.example.restfulapiSample.dto.MemberResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("회원 컨트롤러 테스트")
public class MemberControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("여러 명의 회원 생성 리스트")
    public void post() throws Exception {
        // 1. 생성 요청할 회원 정보를 리스트 형식으로 구성
        List<MemberRequest> memberRequests = List.of(
                MemberRequest.builder().name("김우현").email("woohyun@spring.ac.kr").age(10).build(),
                MemberRequest.builder().name("홍혜창").email("hyechang@spring.ac.kr").age(10).build(),
                MemberRequest.builder().name("홍길동").email("hong@spring.ac.kr").age(10).build(),
                MemberRequest.builder().name("김구라").email("gugu@spring.ac.kr").age(10).build()
        );

        // 2. 객체 → JSON으로 변환
        String requestBody = objectMapper.writeValueAsString(memberRequests);

        // 3. RequestBuilder 생성
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody);

        // 4. MockMvc를 이용하여 요청하고 결과 받기
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // 5. 응답 결과 검증 (JSON → 객체로 변환)
        List<MemberResponse> memberResponses = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<MemberResponse>>() {}
        );

        // 6. AssertJ를 사용한 검증
        assertThat(memberResponses.size()).isEqualTo(4);
        assertThat(memberResponses.get(0).getId()).isNotZero();
        assertThat(memberResponses.get(0).getName()).isEqualTo("김우현");
    }
}
```

---

#### 어노테이션 설명

**@AutoConfigureMockMvc**:

| 항목 | 설명 |
|-----|------|
| **역할** | Spring Context에 `MockMvc` 빈을 자동으로 설정 및 등록 |
| **효과** | `@Autowired`를 통해 `MockMvc` 객체를 주입받을 수 있음 |
| **필수 여부** | ✅ MockMvc를 사용하려면 반드시 필요 |

---

#### 주입받는 객체

**1. MockMvc**:

| 항목 | 설명 |
|-----|------|
| **역할** | 가짜 HTTP 클라이언트 |
| **기능** | - 가상의 HTTP 요청 생성<br>- Controller에 요청 전달<br>- 응답 포착 및 검증 |
| **핵심** | WAS 없이 웹 계층 테스트 가능 |

**2. ObjectMapper**:

| 항목 | 설명 |
|-----|------|
| **역할** | JSON 직렬화/역직렬화 담당 |
| **직렬화** | Java 객체 → JSON 문자열 (`writeValueAsString`) |
| **역직렬화** | JSON 문자열 → Java 객체 (`readValue`) |
| **Spring MVC 연결** | `@RequestBody`와 `@ResponseBody`가 내부적으로 사용하는 바로 그 객체 |

---

#### 테스트 단계별 분석

**1단계: 요청 데이터 준비 (Java 객체)**

```java
List<MemberRequest> memberRequests = List.of(
        MemberRequest.builder().name("김우현").email("woohyun@spring.ac.kr").age(10).build(),
        // ...
);
```

**설명**:
- 클라이언트가 서버로 보낼 데이터를 **Java 객체**로 준비
- 아직 HTTP 요청 형태가 아님

---

**2단계: 객체 → JSON 변환 (직렬화)**

```java
String requestBody = objectMapper.writeValueAsString(memberRequests);
```

**설명**:
- **직렬화(Serialization)**: Java 객체 → JSON 문자열
- HTTP 요청 본문(Body)은 텍스트(JSON) 형식이어야 하므로 변환 필요

**결과 예시**:
```json
[
    {"name":"김우현","email":"woohyun@spring.ac.kr","age":10},
    {"name":"홍혜창","email":"hyechang@spring.ac.kr","age":10},
    ...
]
```

**핵심**: 클라이언트가 서버로 데이터를 보낼 때는 JSON 문자열 형태!

---

**3단계: 가상 요청 생성 (RequestBuilder)**

```java
RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/members")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody);
```

**설명**:
- **RequestBuilder**: 가상의 HTTP 요청 객체 생성
- 이것이 바로 `HttpServletRequest`의 Mock 버전!

**각 메서드의 역할**:

| 메서드 | 역할 | HTTP 요청 요소 |
|-------|------|--------------|
| `.post("/api/members")` | POST 메서드로 `/api/members` 경로에 요청 | HTTP 메서드 + URL |
| `.contentType(MediaType.APPLICATION_JSON)` | 요청 본문의 타입이 JSON임을 명시 | `Content-Type` 헤더 |
| `.accept(MediaType.APPLICATION_JSON)` | 응답으로 받고 싶은 타입이 JSON임을 명시 | `Accept` 헤더 |
| `.content(requestBody)` | 요청 본문에 JSON 문자열을 담음 | HTTP Body |

**핵심**: 실제 HTTP 요청의 모든 정보를 가상 객체에 담음!

---

**4단계: 요청 실행 및 응답 검증**

```java
//MockMvc의 .andExpect() 체인에서 단 하나의 검증이라도 실패하면, 해당 @Test 메서드는 즉시 실패로 처리되고 중단됩니다.
MvcResult mvcResult = mockMvc.perform(requestBuilder)
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();
```

**동작 흐름**:

```
mockMvc.perform(requestBuilder)
    ↓
가상 요청을 Controller에 전달
    ↓
Controller → Service → Repository → DB
    ↓
Controller가 응답 생성
    ↓
MockMvc가 응답 포착
    ↓
검증 수행
```

**각 메서드의 역할**:

| 메서드 | 역할 | 검증 내용 |
|-------|------|----------|
| `.perform(requestBuilder)` | 가상 요청을 실행 | Controller 호출 |
| `.andExpect(status().is2xxSuccessful())` | HTTP 상태 코드 검증 | 200번대 성공 응답인지 확인 |
| `.andExpect(content().contentType(...))` | 응답 Content-Type 검증 | JSON 형식인지 확인 |
| `.andReturn()` | 최종 응답 결과 반환 | `MvcResult` 객체 반환 |

**MvcResult**: 
- HTTP 응답의 모든 정보를 담은 객체
- `HttpServletResponse`의 Mock 버전

---

**5단계: 응답 결과 분석 (JSON → 객체)**

```java
List<MemberResponse> memberResponses = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new TypeReference<List<MemberResponse>>() {}
);
```

**설명**:
- **역직렬화(Deserialization)**: JSON 문자열 → Java 객체
- `getContentAsString()`: HTTP 응답 본문을 JSON 문자열로 가져옴
- `readValue()`: JSON 문자열을 `List<MemberResponse>` 객체로 변환

**TypeReference의 역할**:
- Java의 제네릭 타입 정보(`List<MemberResponse>`)를 유지한 채 변환
- 단순히 `List.class`만 사용하면 제네릭 정보가 소실됨

---

**6단계: 최종 검증 (AssertJ)**

```java
assertThat(memberResponses.size()).isEqualTo(4);
assertThat(memberResponses.get(0).getId()).isNotZero();
assertThat(memberResponses.get(0).getName()).isEqualTo("김우현");
```

**검증 내용**:
- ✅ 4명의 회원이 모두 생성되었는지
- ✅ ID가 자동 생성되었는지 (0이 아님)
- ✅ 첫 번째 회원의 이름이 올바른지

---

#### MockMvc 테스트의 전체 흐름 요약

```
1. Java 객체 준비 (List<MemberRequest>)
    ↓
2. 객체 → JSON 변환 (ObjectMapper.writeValueAsString)
    ↓
3. 가상 HTTP 요청 생성 (RequestBuilder)
    ├─ POST /api/members
    ├─ Content-Type: application/json
    ├─ Accept: application/json
    └─ Body: JSON 문자열
    ↓
4. MockMvc로 요청 실행
    → Controller → Service → Repository → DB
    ↓
5. 응답 검증
    ├─ 상태 코드: 2xx
    └─ Content-Type: application/json
    ↓
6. 응답 본문 추출 (JSON 문자열)
    ↓
7. JSON → Java 객체 변환 (ObjectMapper.readValue)
    ↓
8. AssertJ로 최종 검증
```

**핵심**: 
- 실제 서버 없이 전체 통합 테스트 가능
- 매우 빠르고 자동화 가능
- CI/CD 파이프라인에 통합 용이

---

### 4.3.8 Service단위 테스트 (Mockito)

#### 단위 테스트의 개념

**단위 테스트 (Unit Test)**: 각 컴포넌트를 **독립적으로** 검증하는 테스트

**핵심 원칙**:
- 테스트 대상(SUT: System Under Test)만 실제 객체 사용
- 의존하는 다른 컴포넌트는 **Mock 객체**로 대체
- 외부 요인(DB, 네트워크 등)에 영향받지 않음

---

#### 통합 테스트 vs 단위 테스트 비교

**통합 테스트 (MemberServiceTests)**:

```java
@SpringBootTest
public class MemberServiceTests {
    @Autowired
    private MemberService memberService;  // 실제 객체
    
    @Autowired
    private MemberRepository memberRepository;  // 실제 객체 (DB 연결)
}
```

**동작**:
```
memberService.findById(1L) 호출
    ↓
실제 MemberRepository 사용
    ↓
실제 DB(H2)에 접근
    ↓
결과 반환
```

**특징**:
- ✅ 실제 환경과 유사
- ✅ 전체 흐름 검증 가능
- ⚠️ DB 상태에 의존
- ⚠️ 느림
- ⚠️ Service 로직만 검증하기 어려움

---

**단위 테스트 (MemberServiceUnitsTests)**:

```java
@SpringBootTest
public class MemberServiceUnitsTests {
    @Autowired
    private MemberService memberService;  // 실제 객체 (테스트 대상)
    
    @MockitoBean
    private MemberRepository memberRepository;  // Mock 객체 (가짜)
}
```

**동작**:
```
memberService.findById(1L) 호출
    ↓
Mock MemberRepository 사용
    ↓
DB에 접근하지 않음
    ↓
미리 정의된 가짜 응답 반환
```

**특징**:
- ✅ 매우 빠름 (DB 접근 없음)
- ✅ Service 로직만 순수하게 검증
- ✅ DB 상태에 독립적
- ⚠️ 실제 DB 동작은 검증 안 됨

---

#### Mock 객체란?

**Mock 객체**: 실제 객체를 흉내 내는 가짜 객체

**역할**:

| 항목 | 설명 |
|-----|------|
| **메서드 호출** | 실제 메서드를 호출하지 않음 |
| **응답 제공** | 개발자가 미리 정의한 가짜 응답 반환 |
| **의존성 격리** | 테스트 대상을 외부 의존성으로부터 격리 |
| **빠른 실행** | DB, 네트워크 등 외부 자원 사용하지 않음 |

**비유**:
```
실제 Repository = 진짜 은행 직원 (실제 계좌에서 돈을 꺼냄)
Mock Repository = 연습용 모형 (가짜 돈을 꺼내는 척함)
```

---

#### @MockitoBean 어노테이션

**@MockitoBean**: Spring Context에 Mock 객체를 등록하는 어노테이션

```java
@SpringBootTest
public class MemberServiceUnitsTests {
    @Autowired
    private MemberService memberService;
    
    @MockitoBean  // ← 핵심!
    private MemberRepository memberRepository;
}
```

**동작 원리**:

```
@SpringBootTest가 Spring Context 구동 시도
    ↓
MemberRepository 빈이 필요함
    ↓
@MockitoBean 발견
    ↓
실제 MemberRepository 대신 Mock 객체 생성
    ↓
Mock 객체를 Spring Context에 등록
    ↓
MemberService에 Mock 객체 주입
```

**핵심**: 
- Spring이 실제 Repository를 만들지 않음
- 대신 Mockito가 만든 가짜 객체를 주입
- MemberService는 진짜인지 가짜인지 모르고 사용

---

#### MemberServiceUnitsTests 작성

```java
package com.example.restfulapiSample.service;

import com.example.restfulapiSample.dto.MemberResponse;
import com.example.restfulapiSample.model.Member;
import com.example.restfulapiSample.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("단위 테스트")
public class MemberServiceUnitsTests {

    @Autowired
    MemberService memberService;

    @MockitoBean
    MemberRepository memberRepository;

    @Test
    public void test() {
        // Mock 객체의 동작 정의
        when(memberRepository.findById(1L))
                .thenReturn(
                        Optional.ofNullable(
                                Member.builder()
                                        .id(1L)
                                        .name("홍혜창")
                                        .email("hyechang@spring.ac.kr")
                                        .age(10)
                                        .build()
                        )
                );

        // 테스트 대상 메서드 호출
        MemberResponse memberResponse = memberService.findById(1L);

        // 검증
        assertThat(memberResponse.getId()).isEqualTo(1L);
        assertThat(memberResponse.getName()).isEqualTo("홍혜창");
    }
}
```

---

#### Mockito의 when...thenReturn 패턴

**기본 구조**:

```java
when(mockObject.method(arguments))
    .thenReturn(returnValue);
```

**의미**:
```
when: "만약 이 메서드가 호출된다면..."
thenReturn: "...이 값을 반환해라"
```

---

**코드 분석**:

```java
when(memberRepository.findById(1L))
    .thenReturn(
        Optional.ofNullable(
            Member.builder()
                .id(1L)
                .name("홍혜창")
                .email("hyechang@spring.ac.kr")
                .age(10)
                .build()
        )
    );
```

**단계별 해석**:

| 단계 | 코드 | 설명 |
|-----|------|------|
| **1. 메서드 호출 조건** | `when(memberRepository.findById(1L))` | "만약 `findById(1L)`이 호출된다면..." |
| **2. 반환 값 정의** | `.thenReturn(Optional.ofNullable(...))` | "...이 Optional 객체를 반환해라" |
| **3. Member 객체 생성** | `Member.builder()...` | 반환할 가짜 회원 데이터 |

---

**실제 동작 흐름**:

```
1. memberService.findById(1L) 호출
    ↓
2. Service 내부에서 memberRepository.findById(1L) 호출
    ↓
3. Mockito가 when...thenReturn 규칙 발견
    ↓
4. 실제 DB에 접근하지 않고 미리 정의된 Member 객체 반환
    ↓
5. Service가 반환받은 Member를 MemberResponse로 변환
    ↓
6. 테스트 코드가 MemberResponse 검증
```

**핵심**: 
- DB에 실제로 "홍혜창" 데이터가 없어도 됨
- Mockito가 가짜 데이터를 제공
- Service의 "데이터를 받아서 DTO로 변환하는 로직"만 순수하게 검증

---

#### Optional.ofNullable()을 사용하는 이유

**의문**:
```java
// 왜 Optional.ofNullable()로 감싸야 하나요?
.thenReturn(
    Optional.ofNullable(
        Member.builder()...
    )
);
```

**이유**: JpaRepository의 메서드 시그니처 때문

```java
public interface JpaRepository<T, ID> {
    Optional<T> findById(ID id);  // ← 반환 타입이 Optional!
}
```

**설명**:

| 항목 | 내용 |
|-----|------|
| **실제 메서드 시그니처** | `Optional<Member> findById(Long id)` |
| **반환 타입** | `Optional<Member>` (Member가 아님!) |
| **Mockito 요구사항** | 실제 메서드의 반환 타입과 정확히 일치해야 함 |
| **Optional의 역할** | "결과가 있을 수도 있고(값 존재), 없을 수도 있다(null)"는 것을 명확히 표현 |

---

**Optional 생성 방법**:

| 메서드 | 사용 시점 | 예시 |
|-------|---------|------|
| **Optional.of(value)** | 값이 절대 null이 아닐 때 | `Optional.of(member)` |
| **Optional.ofNullable(value)** | 값이 null일 수도 있을 때 | `Optional.ofNullable(member)` |
| **Optional.empty()** | 값이 없음을 표현할 때 | `Optional.empty()` |

**현재 코드**:
```java
Optional.ofNullable(Member.builder()...)
```

- Member 객체를 생성하고 있으므로 null이 아님
- 하지만 `Optional.ofNullable()`을 사용하는 것이 안전
- JPA 메서드의 반환 타입(`Optional<Member>`)과 정확히 일치

---

**회원을 찾지 못한 경우를 테스트하려면**:

```java
@Test
public void testMemberNotFound() {
    // 회원을 찾지 못한 경우
    when(memberRepository.findById(999L))
            .thenReturn(Optional.empty());  // ← 빈 Optional 반환
    
    // NotFoundException 발생 예상
    assertThrows(NotFoundException.class, () -> {
        memberService.findById(999L);
    });
}
```

---

#### 단위 테스트의 장점

| 장점 | 설명 |
|-----|------|
| **속도** | DB 접근 없이 메모리에서만 동작하여 매우 빠름 |
| **독립성** | 외부 의존성(DB, 네트워크)에 영향받지 않음 |
| **순수성** | 테스트 대상의 로직만 검증 가능 |
| **안정성** | DB 상태, 네트워크 상태에 관계없이 항상 동일한 결과 |
| **명확성** | 테스트 실패 시 원인이 명확 (Service 로직 자체의 문제) |

---

#### 통합 테스트 vs 단위 테스트 선택 가이드

**통합 테스트를 사용해야 할 때**:

| 상황 | 이유 |
|-----|------|
| **전체 흐름 검증** | Controller부터 DB까지 모든 계층이 정상 작동하는지 확인 |
| **실제 DB 동작 검증** | 쿼리 메서드, JPA 설정 등이 실제로 작동하는지 확인 |
| **통합 문제 발견** | 계층 간 연결, 트랜잭션, 데이터 변환 등의 문제 발견 |

---

**단위 테스트를 사용해야 할 때**:

| 상황 | 이유 |
|-----|------|
| **순수 로직 검증** | Service의 비즈니스 로직만 검증하고 싶을 때 |
| **빠른 피드백** | 수백 개의 테스트를 빠르게 실행하고 싶을 때 |
| **외부 의존성 제거** | DB가 없거나 네트워크가 불안정한 환경에서 테스트 |
| **복잡한 시나리오** | 특정 예외 상황이나 엣지 케이스를 쉽게 재현 |

---

**권장 전략**:

```
단위 테스트 (많이)
    ↓
각 계층을 독립적으로 빠르게 검증
    +
통합 테스트 (적절히)
    ↓
전체 흐름이 정상 작동하는지 확인
```

**비유**:
- **단위 테스트** = 자동차의 각 부품(엔진, 브레이크)을 따로따로 검사
- **통합 테스트** = 완성된 자동차를 실제로 운전해보는 것

---

### 4.3.9 핵심 정리

#### 테스트 환경 구성

| 항목 | 설명 |
|-----|------|
| **위치** | `src/test/java` |
| **패키지** | `main`과 동일한 구조 |
| **명명 규칙** | 클래스명 + `Tests` |
| **필수 의존성** | `spring-boot-starter-test` |

---

#### 주요 어노테이션

| 어노테이션 | 역할 | 사용 위치 |
|----------|------|----------|
| **@SpringBootTest** | 전체 Spring Context 로딩 | 클래스 |
| **@Autowired** | 의존성 주입 (테스트 환경 권장) | 필드 |
| **@MockitoBean** | Mock 객체 주입 | 필드 |
| **@AutoConfigureMockMvc** | MockMvc 자동 설정 | 클래스 |
| **@Test** | 테스트 메서드 지정 | 메서드 |
| **@DisplayName** | 테스트 이름 지정 | 클래스/메서드 |
| **@BeforeEach** | 각 테스트 전 실행 | 메서드 |
| **@AfterEach** | 각 테스트 후 실행 | 메서드 |
| **@Disabled** | 테스트 비활성화 | 메서드 |
| **@RepeatedTest** | 테스트 반복 실행 | 메서드 |

---

#### 테스트 유형 비교

| 구분 | 통합 테스트 | 단위 테스트 |
|-----|-----------|-----------|
| **대상** | Controller, Service, Repository | Service만 |
| **의존성** | 실제 객체 | Mock 객체 |
| **DB 연결** | ✅ 사용 | ❌ 사용 안 함 |
| **속도** | 느림 | 매우 빠름 |
| **검증 범위** | 전체 흐름 | 순수 로직 |
| **사용 시기** | 전체 통합 확인 | 개별 로직 검증 |

---

#### 주요 검증 도구

**AssertJ (assertThat)**:

```java
assertThat(actual).isEqualTo(expected);
assertThat(actual).isNotNull();
assertThat(list).hasSize(3);
```

**Mockito (when...thenReturn)**:

```java
when(mockRepository.findById(1L))
    .thenReturn(Optional.of(member));
```

**MockMvc (perform)**:

```java
mockMvc.perform(requestBuilder)
    .andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
```

---
## 4.4 웹 필터

### 4.4.1 필터(Filter)란?

#### 필터의 개념

**필터(Filter)**: 클라이언트의 HTTP 요청이 서블릿/컨트롤러에 도달하기 전이나 응답이 클라이언트에게 가기 전에 **공통적인 작업을 수행**할 수 있도록 요청과 응답을 **가로채는(Intercept)** 객체

**핵심**: 필터는 서블릿 컨테이너(톰캣)가 관리하는 웹 컴포넌트!

---

#### 서블릿 컨테이너의 역할 확장

**이전 이해**:
```
톰캣(서블릿 컨테이너) = 서블릿 객체의 생명주기 관리
```

**확장된 이해**:
```
톰캣(서블릿 컨테이너) = 서블릿 객체 + 필터 객체의 생명주기 관리
```

**서블릿 컨테이너가 관리하는 웹 컴포넌트**:

| 컴포넌트 | 역할 | 관리 내용 |
|---------|------|----------|
| **서블릿 객체** | 실제 비즈니스 로직 처리 (Controller) | 생성, 초기화, 서비스, 소멸 |
| **필터 객체** | 요청/응답 가로채기 및 전처리/후처리 | 생성, 소멸, 필터 체인 구성 및 관리 |

**핵심**: 톰캣은 서블릿뿐만 아니라 필터도 관리하며, 이들이 협력하여 요청을 처리할 수 있도록 환경을 제공!

---

#### 필터가 없을 때와 있을 때의 흐름 비교

**필터가 없을 때**:

```
클라이언트 (HTTP 요청)
    ↓
톰캣 (서블릿 컨테이너)
    ↓
서블릿/컨트롤러 (비즈니스 로직)
    ↓
톰캣
    ↓
클라이언트 (HTTP 응답)
```

---

**필터가 있을 때**:

```
클라이언트 (HTTP 요청)
    ↓
톰캣 (서블릿 컨테이너)
    ↓
필터 1 (전처리)
    ↓
필터 2 (전처리)
    ↓
서블릿/컨트롤러 (비즈니스 로직)
    ↓
필터 2 (후처리)
    ↓
필터 1 (후처리)
    ↓
톰캣
    ↓
클라이언트 (HTTP 응답)
```

**핵심**: 필터는 서블릿/컨트롤러의 앞과 뒤에서 공통 작업을 수행!

---

### 4.4.2 필터의 주요 사용 목적

필터는 웹 애플리케이션의 **공통 관심사(Cross-Cutting Concerns)** 를 서블릿/컨트롤러의 핵심 비즈니스 로직과 분리하여 관리할 때 사용됩니다.

#### 1. 보안 및 인증/인가 🛡️

| 목적 | 설명 | 예시 |
|-----|------|------|
| **로그인 확인** | 사용자가 로그인되었는지 체크 | 세션이나 토큰 검증 |
| **권한 확인** | 특정 URL에 접근할 권한이 있는지 체크 | 관리자 페이지 접근 제어 |
| **차단** | 인증/인가 실패 시 요청을 컨트롤러에 전달하지 않고 필터에서 차단 | 401 Unauthorized, 403 Forbidden 반환 |

---

#### 2. 로깅 및 감사 📜

| 목적 | 설명 |
|-----|------|
| **요청/응답 로깅** | 요청 시간, IP, URL, 응답 시간 등 모든 웹 요청 정보 자동 기록 |
| **성능 측정** | 요청 처리 소요 시간 측정으로 성능 병목 지점 파악 |

---

#### 3. 데이터 변환 및 조작 🛠️

| 목적 | 설명 |
|-----|------|
| **문자 인코딩 설정** | 한글 깨짐 방지를 위한 `UTF-8` 인코딩 설정 |
| **데이터 압축** | 응답 데이터를 GZIP으로 압축하여 전송 속도 향상 |
| **XSS 방어** | 요청 파라미터의 악성 스크립트 제거 또는 변환 |

---

#### 4. 기타 웹 환경 설정 ⚙️

| 목적 | 설명 |
|-----|------|
| **캐시 제어** | 응답 헤더에 캐시 관련 설정 추가 |
| **CORS 설정** | 다른 도메인에서의 요청 허용을 위한 헤더 추가 |

---

### 4.4.3 LogFilter 구현 (요청/응답 로깅)

#### LogFilter.java 작성

```java
package com.example.restfulapiSample.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@WebFilter(urlPatterns = "/api/*")  
@Slf4j
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, 
                        ServletResponse servletResponse, 
                        FilterChain filterChain) throws IOException, ServletException {
        
        // 1. 요청 전처리 (Inbound Request)
        long startTime = System.currentTimeMillis();
        log.info("time start - {}", ((HttpServletRequest)servletRequest).getRequestURI());

        // 2. 다음 필터 또는 서블릿으로 요청 전달
        filterChain.doFilter(servletRequest, servletResponse);

        // 3. 응답 후처리 (Outbound Response)
        long endTime = System.currentTimeMillis();
        log.info("time end - {}", ((HttpServletRequest)servletRequest).getRequestURI());
        log.info("total time : {} , status : {}", 
                (endTime - startTime), 
                ((HttpServletResponse)servletResponse).getStatus());
    }
}
```

---

#### Filter 인터페이스와 주요 메서드

**Filter 인터페이스**: 서블릿 표준 API가 제공하는 필터 인터페이스

```java
public interface Filter {
    void doFilter(ServletRequest request, 
                  ServletResponse response, 
                  FilterChain chain) 
            throws IOException, ServletException;
}
```

---

#### doFilter() 메서드의 매개변수

| 매개변수 | 타입 | 의미 | 실제 전달되는 객체 |
|---------|------|------|------------------|
| **servletRequest** | `ServletRequest` | 클라이언트의 HTTP 요청 정보 (헤더, 파라미터 등) | `HttpServletRequest` |
| **servletResponse** | `ServletResponse` | 서버가 클라이언트에게 보낼 응답 정보 | `HttpServletResponse` |
| **filterChain** | `FilterChain` | 현재 필터 다음으로 실행될 나머지 단계에 대한 정보 | 톰캣 내부 객체 |

---

#### FilterChain의 역할 (매우 중요!)

**FilterChain**: 해당 요청 URL에 적용되는 **모든 필터의 목록**과 **최종 목적지(컨트롤러)** 정보를 담고 있는 객체

**핵심 개념**:
```
filterChain.doFilter(request, response)
= "나의 전처리 작업은 끝났으니, 이제 다음 주자에게 요청을 넘겨주세요"
```

**FilterChain의 내부 구조**:

```
FilterChain {
    필터 목록: [LogFilter, AccessKeyFilter]
    최종 목적지: MemberController
    현재 위치: LogFilter
}
```

---

#### FilterChain의 작동 방식

**시나리오**: `/api/members` 요청이 들어왔을 때

```
1. 톰캣이 URL 분석
    ↓
2. 적용될 필터 확인: LogFilter, AccessKeyFilter
    ↓
3. FilterChain 객체 생성
    - 필터 목록: [LogFilter, AccessKeyFilter]
    - 최종 목적지: MemberController
    ↓
4. LogFilter.doFilter() 호출
    - 전처리: 시작 시간 기록
    - filterChain.doFilter() 호출 ← "다음으로 넘김"
    ↓
5. FilterChain이 다음 필터 확인
    ↓
6. AccessKeyFilter.doFilter() 호출
    - 전처리: 인증 확인
    - filterChain.doFilter() 호출 ← "다음으로 넘김"
    ↓
7. FilterChain이 더 이상 필터 없음 확인
    ↓
8. 최종 목적지 (MemberController) 호출
    - 비즈니스 로직 실행
    - 응답 생성
    ↓
9. 응답이 역순으로 복귀
    ↓
10. AccessKeyFilter의 filterChain.doFilter() 이후 코드 실행
    - 후처리: (있다면) 응답 데이터 변환
    ↓
11. LogFilter의 filterChain.doFilter() 이후 코드 실행
    - 후처리: 종료 시간 기록 및 로깅
    ↓
12. 톰캣을 거쳐 클라이언트에게 응답
```

**핵심**: 
- `filterChain.doFilter()` 호출 **전**: 요청 전처리
- `filterChain.doFilter()` 호출 **후**: 응답 후처리

---

#### 필터의 차단 기능

**필터의 강력한 기능**: `filterChain.doFilter()`를 호출하지 않으면 요청이 **차단**됨!

**통과 시키는 경우**:

```java
@Override
public void doFilter(...) {
    // 전처리
    log.info("요청 시작");
    
    // ✅ 다음 단계로 전달 (통과)
    filterChain.doFilter(request, response);
    
    // 후처리
    log.info("요청 종료");
}
```

---

**차단하는 경우**:

```java
@Override
public void doFilter(...) {
    // 전처리: 인증 확인
    if (!isAuthenticated(request)) {
        // ❌ filterChain.doFilter()를 호출하지 않음 (차단)
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return;  // 여기서 메서드 종료
    }
    
    // 인증 성공 시에만 다음 단계로 전달
    filterChain.doFilter(request, response);
}
```

**차단 흐름**:

```
클라이언트 요청
    ↓
톰캣
    ↓
필터 (인증 실패 감지)
    ↓
filterChain.doFilter() 호출 안 함 ❌
    ↓
401 Unauthorized 응답 반환
    ↓
컨트롤러에 도달하지 못함 (차단 성공)
```

**핵심**: 필터는 **방화벽**이나 **검문소** 역할을 수행!

---

#### LogFilter 코드 분석

**1단계: 요청 전처리**

```java
long startTime = System.currentTimeMillis();
log.info("time start - {}", ((HttpServletRequest)servletRequest).getRequestURI());
```

**동작**:
- 요청이 들어온 시작 시간 기록
- 요청 URI 로깅

**타입 캐스팅 이유**:
- `servletRequest`는 `ServletRequest` 타입 (일반적인 요청)
- `getRequestURI()` 메서드는 `HttpServletRequest`에만 존재
- HTTP 관련 메서드를 사용하려면 캐스팅 필요

---

**2단계: 다음 단계로 전달**

```java
filterChain.doFilter(servletRequest, servletResponse);
```

**동작**:
- FilterChain에게 "다음 필터 또는 컨트롤러를 실행해주세요" 요청
- 이 메서드가 반환될 때까지 대기 (블로킹)
- 다음 모든 처리가 완료된 후에야 이 줄 다음 코드 실행

---

**3단계: 응답 후처리**

```java
long endTime = System.currentTimeMillis();
log.info("time end - {}", ((HttpServletRequest)servletRequest).getRequestURI());
log.info("total time : {} , status : {}", 
        (endTime - startTime), 
        ((HttpServletResponse)servletResponse).getStatus());
```

**동작**:
- 요청 처리가 완료된 시간 기록
- 총 소요 시간 계산 (endTime - startTime)
- HTTP 응답 상태 코드 로깅

---

#### @WebFilter 어노테이션

**@WebFilter**: 서블릿 표준 API가 제공하는 필터 등록 어노테이션

```java
@WebFilter(urlPatterns = "/api/*")
public class LogFilter implements Filter {
    // ...
}
```

**속성**:

| 속성 | 설명 | 예시 |
|-----|------|------|
| **urlPatterns** | 필터를 적용할 URL 패턴 | `"/api/*"` - `/api/`로 시작하는 모든 요청 |
| **filterName** | 필터 이름 | `"logFilter"` |
| **servletNames** | 특정 서블릿에만 적용 | `{"memberServlet"}` |

---

**@ServletComponentScan 필요**:

`@WebFilter`를 사용하려면 메인 애플리케이션 클래스에 `@ServletComponentScan` 추가 필요!

```java
@SpringBootApplication
@EnableJpaAuditing
@ServletComponentScan  // ← @WebFilter 인식을 위해 필요
public class RestfulapiSampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestfulapiSampleApplication.class, args);
    }
}
```

**역할**: 서블릿 컨테이너(톰캣)가 `@WebFilter`, `@WebServlet`, `@WebListener` 어노테이션을 검색하고 등록할 수 있도록 스캔 활성화

---

#### LogFilter 실행 결과

**요청**:
```
GET http://localhost:8080/api/members
Authorization: Bearer hanbit-access-key
```

**로그 출력**:
```
INFO --- [nio-8080-exec-1] c.e.restfulapiSample.filter.LogFilter : time start - /api/members
(Hibernate SQL 실행...)
INFO --- [nio-8080-exec-1] c.e.restfulapiSample.filter.LogFilter : time end - /api/members
INFO --- [nio-8080-exec-1] c.e.restfulapiSample.filter.LogFilter : total time : 362 , status : 200
```

**분석**:
- 요청 시작과 종료 시간이 로깅됨
- 총 소요 시간: 362ms
- 응답 상태 코드: 200 OK

---

### 4.4.4 AccessKeyFilter 구현 (인증)

#### 인증 필터의 목적

**목표**: 허가받은 클라이언트만 API를 사용할 수 있도록 **인증(Authentication)** 수행

**동작 방식**:
- 클라이언트가 HTTP 헤더에 인증 키를 포함하여 요청
- 필터가 인증 키를 검증
- 유효한 키면 통과, 유효하지 않으면 차단

---

#### AccessKeyFilter.java 작성

```java
package com.example.restfulapiSample.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@WebFilter  
@Slf4j
public class AccessKeyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        log.info("인증 검사 시작");

        // 1. HTTP 헤더에서 인증 키 추출
        String accessKey = request.getHeader("Authorization");
        
        // 2. Bearer 토큰 형식 확인 및 키 추출
        if (accessKey != null && accessKey.startsWith("Bearer")) {
            accessKey = accessKey.replace("Bearer", "").trim();
            
            // 3. 인증 키 검증
            if (accessKey.equals("hanbit-access-key")) {
                // ✅ 인증 성공: 다음 단계로 전달
                filterChain.doFilter(request, response);
                log.info("인증 완료 및 정상 호출 후 종료");
                return;
            }
        }

        // ❌ 인증 실패: 401 Unauthorized 반환
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        log.info("인증 실패 후 종료");
    }
}
```

---

#### OncePerRequestFilter vs Filter

**Filter 인터페이스의 문제점**:

```java
public class MyFilter implements Filter {
    @Override
    public void doFilter(...) {
        // 인증 로직
        filterChain.doFilter(...);
    }
}
```

**문제 상황**:

```
1. /api/a 요청 도착
    ↓
2. MyFilter 실행 (1회) - 인증 완료
    ↓
3. 컨트롤러 내부에서 /api/b로 포워딩(Forwarding) 발생
    ↓
4. MyFilter 재실행 (2회) ⚠️ - 불필요한 중복 검증!
```

**포워딩(Forwarding)**:
- 서블릿 컨테이너 내부에서 현재 요청을 유지한 채 다른 서블릿/컨트롤러로 제어를 넘기는 방식
- 클라이언트는 이 과정을 알 수 없음
- 하지만 톰캣 입장에서는 새로운 요청처럼 처리되어 필터가 다시 실행됨

---

**OncePerRequestFilter의 해결책**:

```java
public class MyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(...) {
        // 인증 로직
        filterChain.doFilter(...);
    }
}
```

**동작 방식**:

```
1. /api/a 요청 도착
    ↓
2. OncePerRequestFilter 실행
    - 요청 객체에 "실행 완료 플래그" 표시
    - 인증 로직 수행 (doFilterInternal 실행)
    ↓
3. 컨트롤러 내부에서 /api/b로 포워딩 발생
    ↓
4. OncePerRequestFilter 재호출
    - "실행 완료 플래그" 확인
    - doFilterInternal 실행하지 않고 바로 filterChain.doFilter() 호출
    ↓
결과: 인증 로직은 1회만 실행됨 ✅
```

---

**비교 정리**:

| 구분 | implements Filter | extends OncePerRequestFilter |
|-----|------------------|----------------------------|
| **메서드** | `doFilter()` | `doFilterInternal()` |
| **중복 실행** | ⚠️ 포워딩 시 중복 실행 가능 | ✅ 요청당 1회만 실행 보장 |
| **인증/인가 사용** | ❌ 비권장 | ✅ 권장 (표준) |
| **포워딩 영향** | 받음 | 받지 않음 |

**핵심**: 
- **인증/인가 로직**은 한 번만 수행되어야 하므로 `OncePerRequestFilter` 사용
- "인증은 이미 완료했다!"는 것을 명확히 하여 불필요한 중복 실행 방지

---

#### AccessKeyFilter 코드 분석

**1단계: 인증 키 추출**

```java
String accessKey = request.getHeader("Authorization");
```

**동작**:
- HTTP 헤더에서 `Authorization` 헤더 값 추출
- 클라이언트는 다음 형식으로 헤더를 보냄:
  ```
  Authorization: Bearer hanbit-access-key
  ```

---

**2단계: Bearer 토큰 형식 확인**

```java
if (accessKey != null && accessKey.startsWith("Bearer")) {
    accessKey = accessKey.replace("Bearer", "").trim();
    // ...
}
```

**Bearer 토큰**:
- 토큰 기반 인증에서 가장 흔하게 사용되는 인증 스키마(Token Type)
- "이 토큰을 소유한 사람이 인증되었다"는 의미

**전처리**:
- `accessKey.startsWith("Bearer")`: `Bearer`로 시작하는지 확인
- `.replace("Bearer", "")`: `Bearer` 접두사 제거
- `.trim()`: 앞뒤 공백 제거
- 결과: `"hanbit-access-key"` (순수 키 값만 추출)

---

**3단계: 인증 키 검증**

```java
if (accessKey.equals("hanbit-access-key")) {
    filterChain.doFilter(request, response);
    log.info("인증 완료 및 정상 호출 후 종료");
    return;
}
```

**인증 성공 시**:
- ✅ `filterChain.doFilter()` 호출: 다음 필터 또는 컨트롤러로 요청 전달
- 로그 출력: "인증 완료 및 정상 호출 후 종료"
- `return`: 메서드 종료

---

**4단계: 인증 실패 처리**

```java
response.setStatus(HttpStatus.UNAUTHORIZED.value());
log.info("인증 실패 후 종료");
```

**인증 실패 시**:
- ❌ `filterChain.doFilter()` 호출하지 않음 (차단!)
- HTTP 상태 코드 설정: `401 Unauthorized`
- 로그 출력: "인증 실패 후 종료"
- 요청이 컨트롤러에 도달하지 못하고 필터에서 차단됨

---

#### 실제 테스트 결과

**테스트 1: 인증 없이 요청**

**요청**:
```
GET http://localhost:8080/api/members
(Authorization 헤더 없음)
```

**로그**:
```
INFO --- c.e.r.filter.AccessKeyFilter : 인증 검사 시작
INFO --- c.e.r.filter.AccessKeyFilter : 인증 실패 후 종료
```

**응답**:
```
401 Unauthorized
```

**결과**: 인증 실패로 차단됨 ✅

---

**테스트 2: 올바른 인증 키로 요청**

**요청**:
```
GET http://localhost:8080/api/members
Authorization: Bearer hanbit-access-key
```

**로그**:
```
INFO --- c.e.r.filter.AccessKeyFilter : 인증 검사 시작
INFO --- c.e.r.filter.AccessKeyFilter : 인증 완료 및 정상 호출 후 종료
(비즈니스 로직 실행...)
```

**응답**:
```
200 OK
[회원 목록 JSON]
```

**결과**: 인증 성공, 정상적으로 API 호출됨 ✅

---

**테스트 3: 잘못된 인증 키로 요청**

**요청**:
```
GET http://localhost:8080/api/members
Authorization: Bearer wrong-key
```

**로그**:
```
INFO --- c.e.r.filter.AccessKeyFilter : 인증 검사 시작
INFO --- c.e.r.filter.AccessKeyFilter : 인증 실패 후 종료
```

**응답**:
```
401 Unauthorized
```

**결과**: 인증 실패로 차단됨 ✅

---

### 4.4.5 필터 순서 제어 문제

#### 문제 상황: 필터 실행 순서가 예측 불가능

**@WebFilter 사용 시 문제**:

```java
@WebFilter(urlPatterns = "/api/*")
public class AccessKeyFilter extends OncePerRequestFilter {
    // 인증 필터
}

@WebFilter(urlPatterns = "/api/*")
public class LogFilter implements Filter {
    // 로깅 필터
}
```

**문제점**:
- 필터의 실행 순서가 **알파벳 순서**로 결정됨
- `AccessKeyFilter` → `LogFilter` 순으로 실행됨
- 개발자가 원하는 순서를 지정할 수 없음

---

**실제 실행 결과 (알파벳 순)**:

**로그**:
```
INFO --- c.e.r.filter.AccessKeyFilter : 인증 검사 시작
INFO --- c.e.restfulapiSample.filter.LogFilter : time start - /api/members
(비즈니스 로직 실행...)
INFO --- c.e.restfulapiSample.filter.LogFilter : time end - /api/members
INFO --- c.e.restfulapiSample.filter.LogFilter : total time : 326 , status : 200
INFO --- c.e.r.filter.AccessKeyFilter : 인증 완료 및 정상 호출 후 종료
```

**분석**:
- `AccessKeyFilter`가 먼저 실행됨 (알파벳 순)
- 하지만 로그 메시지 순서를 보면 혼란스러움
- "time start"가 "인증 검사 시작" 다음에 나옴

---

**원하는 순서**:

```
1. LogFilter (로깅 시작)
2. AccessKeyFilter (인증 검사)
3. 컨트롤러
4. AccessKeyFilter (인증 후처리)
5. LogFilter (로깅 종료, 총 소요 시간 측정)
```

**이유**: 
- LogFilter가 전체 요청의 시작과 끝을 감싸야 정확한 시간 측정 가능
- 인증은 로깅 다음에 수행되는 것이 논리적

---

### 4.4.6 FilterConfig를 통한 순서 제어

#### 해결 방법: FilterRegistrationBean사용

**@WebFilter의 한계**:

| 항목 | 설명 |
|-----|------|
| **등록 주체** | 서블릿 컨테이너(톰캣) |
| **관리 방식** | 서블릿 컴포넌트로 등록 (Spring Bean 아님) |
| **순서 제어** | ❌ 불가능 (알파벳 순으로 자동 결정) |
| **의존성 주입** | ❌ `@Autowired` 사용 불가 |

---

**FilterRegistrationBean의 장점**:

| 항목 | 설명 |
|-----|------|
| **등록 주체** | Spring 컨테이너 |
| **관리 방식** | Spring Bean으로 등록 |
| **순서 제어** | ✅ `setOrder()`로 명시적 순서 지정 가능 |
| **의존성 주입** | ✅ `@Autowired` 사용 가능 |

**핵심**: Spring이 필터를 빈으로 관리하면서 서블릿 컨테이너에 등록해주는 중개 역할!

---

#### FilterConfig.java 작성

**1단계: @WebFilter 어노테이션 제거**

```java
// @WebFilter(urlPatterns = "/api/*")  // ← 제거!
@Slf4j
public class LogFilter implements Filter {
    // ...
}

// @WebFilter  // ← 제거!
@Slf4j
public class AccessKeyFilter extends OncePerRequestFilter {
    // ...
}
```

**주의**: 
- `@WebFilter`를 제거하지 않으면 중복 등록됨
- `@ServletComponentScan`도 제거 (더 이상 필요 없음)

---

**2단계: FilterConfig 클래스 작성**

```java
package com.example.restfulapiSample.config;

import com.example.restfulapiSample.filter.AccessKeyFilter;
import com.example.restfulapiSample.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LogFilter());
        bean.addUrlPatterns("/api/*");
        bean.setOrder(1);  // ← 순서 지정: 1번 (가장 먼저)
        bean.setName("LogFilter");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<AccessKeyFilter> accessKeyFilter() {
        FilterRegistrationBean<AccessKeyFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new AccessKeyFilter());
        bean.addUrlPatterns("/api/*");
        bean.setOrder(2);  // ← 순서 지정: 2번 (LogFilter 다음)
        bean.setName("AccessKeyFilter");
        return bean;
    }
}
```

---

#### FilterRegistrationBean 상세 설명

**FilterRegistrationBean**: 필터를 Spring Bean으로 등록하고 서블릿 컨테이너에 등록을 중개하는 클래스

**주요 메서드**:

| 메서드 | 역할 | 예시 |
|-------|------|------|
| **setFilter()** | 등록할 필터 객체 지정 | `bean.setFilter(new LogFilter())` |
| **addUrlPatterns()** | 필터를 적용할 URL 패턴 지정 | `bean.addUrlPatterns("/api/*")` |
| **setOrder()** | 필터 실행 순서 지정 (숫자가 작을수록 먼저 실행) | `bean.setOrder(1)` |
| **setName()** | 필터 이름 지정 | `bean.setName("LogFilter")` |

---

#### 빈 등록 방식 비교

**방법 1: 어노테이션 기반 자동 등록**

```java
@Component  // 또는 @Service, @Repository, @Controller
public class MyService {
    // Spring이 자동으로 빈 생성 및 등록
}
```

**방법 2: Configuration 클래스 기반 수동 등록**

```java
@Configuration
public class AppConfig {
    @Bean
    public MyService myService() {
        return new MyService();  // 개발자가 직접 빈 생성 및 등록
    }
}
```

---

**필터의 경우**:

| 방식 | 어노테이션 | 순서 제어 | 권장 여부 |
|-----|----------|---------|---------|
| **@WebFilter** | 어노테이션 기반 | ❌ 불가능 | ❌ 비권장 |
| **FilterRegistrationBean** | Configuration 기반 | ✅ 가능 | ✅ 권장 |

**핵심**: 순서 제어가 필요한 필터는 `FilterConfig`를 통해 `@Bean`으로 등록!

---

#### 실행 순서 제어 결과

**설정된 순서**:

```
1. LogFilter (Order = 1)
2. AccessKeyFilter (Order = 2)
```

---

**실제 실행 로그**:

```
INFO --- c.e.restfulapiSample.filter.LogFilter : time start - /api/members
INFO --- c.e.r.filter.AccessKeyFilter : 인증 검사 시작
(비즈니스 로직 실행...)
INFO --- c.e.r.filter.AccessKeyFilter : 인증 완료 및 정상 호출 후 종료
INFO --- c.e.restfulapiSample.filter.LogFilter : time end - /api/members
INFO --- c.e.restfulapiSample.filter.LogFilter : total time : 299 , status : 200
```

**분석**:
- ✅ LogFilter가 먼저 실행됨 (time start)
- ✅ AccessKeyFilter가 그 다음 실행됨 (인증 검사 시작)
- ✅ 응답은 역순으로 복귀 (AccessKeyFilter → LogFilter)
- ✅ LogFilter가 전체 시간을 정확히 측정 (299ms)

---

**순서 제어 전후 비교**:

| 구분 | 순서 제어 전 (@WebFilter) | 순서 제어 후 (FilterConfig) |
|-----|-------------------------|--------------------------|
| **실행 순서** | 알파벳 순 (AccessKeyFilter → LogFilter) | 지정한 순서 (LogFilter → AccessKeyFilter) |
| **시간 측정** | ⚠️ 부정확 (인증 시간만 측정) | ✅ 정확 (전체 요청 시간 측정) |
| **가독성** | ❌ 로그 순서 혼란스러움 | ✅ 로그 순서 명확 |

---

### 4.4.7 필터 실행 흐름 전체 정리

#### 완전한 요청-응답 흐름

**요청**:
```
GET http://localhost:8080/api/members
Authorization: Bearer hanbit-access-key
```

---

**전체 흐름**:

```
1. 클라이언트 요청 (HTTP GET /api/members)
    ↓
2. 톰캣(서블릿 컨테이너) 요청 수신
    ↓
3. URL 분석: /api/members
    - LogFilter (Order=1) 적용 대상 확인
    - AccessKeyFilter (Order=2) 적용 대상 확인
    ↓
4. FilterChain 생성
    [LogFilter → AccessKeyFilter → MemberController]
    ↓
5. LogFilter.doFilter() 실행
    - 전처리: startTime 기록, 로그 출력
    - filterChain.doFilter() 호출
    ↓
6. AccessKeyFilter.doFilterInternal() 실행
    - 전처리: 인증 키 검증
    - 인증 성공 시 filterChain.doFilter() 호출
    ↓
7. MemberController.getAll() 실행
    - Service → Repository → DB
    - 회원 목록 조회
    - MemberResponse 리스트 반환
    ↓
8. 응답 역순 복귀
    ↓
9. AccessKeyFilter 후처리
    - 로그 출력: "인증 완료 및 정상 호출 후 종료"
    ↓
10. LogFilter 후처리
    - endTime 기록
    - 총 소요 시간 계산
    - 로그 출력: "time end", "total time"
    ↓
11. 톰캣을 거쳐 클라이언트에게 응답
    ↓
12. 클라이언트 수신 (200 OK, 회원 목록 JSON)
```
---

### 4.4.8 핵심 정리

#### 필터의 핵심 개념

| 항목 | 설명 |
|-----|------|
| **정의** | 요청/응답을 가로채서 전처리/후처리를 수행하는 객체 |
| **관리** | 서블릿 컨테이너(톰캣)가 관리 |
| **위치** | DispatcherServlet 앞 (가장 먼저 실행) |
| **역할** | 공통 관심사 처리 (인증, 로깅, 인코딩 등) |

---

#### 필터 구현 방법

| 방법 | 인터페이스/클래스 | 사용 시나리오 |
|-----|----------------|-------------|
| **일반 필터** | `implements Filter` | 단순 전처리/후처리 |
| **요청당 1회 필터** | `extends OncePerRequestFilter` | 인증/인가 (중복 실행 방지) |

---

#### 필터 등록 방법

| 방법 | 순서 제어 | 의존성 주입 | 권장 여부 |
|-----|---------|-----------|---------|
| **@WebFilter** | ❌ 불가능 | ❌ 제한적 | △ 단순 필터 |
| **FilterRegistrationBean** | ✅ 가능 | ✅ 가능 | ✅ 권장 |

---

#### FilterChain의 역할

```
filterChain.doFilter(request, response)
= "다음 필터 또는 컨트롤러로 요청을 전달해주세요"
```

**핵심**:
- 호출하면: 다음 단계로 **통과**
- 호출 안 하면: 요청 **차단**

---

#### 필터 실행 순서

```
Order 값이 작을수록 먼저 실행
Order 1 → Order 2 → Order 3 → Controller
```

**후처리는 역순**:
```
Controller → Order 3 → Order 2 → Order 1
```

---

#### 실무 활용 팁

**1. 필터 체인 순서 설계**:
```
1. 로깅 (전체 시간 측정)
2. 인코딩 설정
3. CORS 설정
4. 인증 (Authentication)
5. 인가 (Authorization)
→ Controller
```

---

**2. 인증 실패 시 빠른 차단**:
```java
if (!isAuthenticated) {
    response.setStatus(401);
    return;  // filterChain.doFilter() 호출 안 함
}
```

---

**3. OncePerRequestFilter 사용**:
- 인증/인가 필터는 항상 `OncePerRequestFilter` 상속
- 포워딩으로 인한 중복 실행 방지

---

**4. Configuration으로 관리**:
- `FilterConfig` 클래스에서 모든 필터 순서 관리
- 유지보수 용이, 순서 변경 간편








