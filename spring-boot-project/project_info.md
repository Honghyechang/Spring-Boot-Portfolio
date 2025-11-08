# Spring Board Project

<!--
prject_info.md 

초기 2개 목차 완료
docs: [Board] 스키마 구축 및 데이터 모델 구현 학습 정리

로그인 화면 구현 완료	
docs: [Board] 로그인/회원가입 화면 구현 내용 추가

게시글 CRUD 내용 추가	
docs: [Board] 게시글 조회/입력/수정/삭제 기능 정리

여러 목차를 한 번에 완료
docs: [Board] 5, 6장 회원 관리 및 최종 결과 학습 정리 완료


code

새로운 기능 구현	
feat	feat: 게시글 등록 API 및 뷰 구현
feat: Spring Security 기반 로그인 필터 설정 추가

기존 코드 버그 수정	
fix	fix: 비밀번호 암호화 시 salt 값 오적용 버그 수정
fix: 검색어 누락 시 500 에러 발생하는 문제 해결

기능 변경 없는 코드 구조 개선
refactor	refactor: Repository 메서드를 JpaRepository로 통합
refactor: MemberForm 객체에 @Getter, @Setter 적용

의존성, 설정 파일 변경
chore	chore: build.gradle에 Spring Security 의존성 추가
-->


## 📌 프로젝트 개요

### 프로젝트 소개

**Spring Board Project**는 지금까지 학습한 스프링 부트 핵심 기술들을 통합하여 구현하는 실전 게시판 애플리케이션입니다. 

이 프로젝트는 단순한 CRUD 기능을 넘어서, 실무에서 요구되는 **회원 관리**, **인증/인가**, **권한 기반 접근 제어**, **게시글 관리** 등의 복합적인 기능을 모두 아우르는 종합 애플리케이션입니다.

### 학습 목표

이 프로젝트를 통해 다음의 실무 역량을 체득합니다:

| 영역 | 학습 내용 |
|-----|----------|
| **데이터베이스 설계** | 관계형 데이터베이스 스키마 설계 및 JPA 엔티티 매핑 |
| **백엔드 로직** | Spring Data JPA를 활용한 비즈니스 로직 구현 |
| **화면 구성** | 타임리프 템플릿 엔진을 통한 동적 웹 페이지 생성 |
| **보안** | Spring Security 기반 인증/인가 시스템 구축 |
| **권한 관리** | 역할 기반 접근 제어 (RBAC) 구현 |
| **통합 개발** | MVC 패턴 기반 풀스택 웹 애플리케이션 개발 |

### 프로젝트 주요 기능

**핵심 기능 목록**:

```
📋 게시판 시스템
   ├── 게시글 목록 조회 (페이징)
   ├── 게시글 상세 조회
   ├── 게시글 작성 (로그인 필수)
   ├── 게시글 수정 (본인만)
   └── 게시글 삭제 (본인 또는 관리자)

👥 회원 관리
   ├── 회원가입
   ├── 로그인/로그아웃
   ├── 회원 정보 수정
   └── 회원 목록 조회 (관리자만)

🔐 보안 및 권한
   ├── 세션 기반 인증
   ├── CSRF 보호
   ├── 역할 기반 접근 제어 (USER, ADMIN)
   └── Remember-Me (자동 로그인)
```

### 기술 스택

**사용 기술**:

| 계층 | 기술 |
|-----|------|
| **프레임워크** | Spring Boot 3.5.7 |
| **ORM** | Spring Data JPA |
| **템플릿 엔진** | Thymeleaf |
| **보안** | Spring Security |
| **데이터베이스** | H2 Database (개발), MySQL (운영) |
| **빌드 도구** | Gradle |
| **언어** | Java 21 |

### 프로젝트 구조

```
spring-board-project/
├── src/main/java/
│   └── com.example.board/
│       ├── controller/     (웹 요청 처리)
│       ├── service/        (비즈니스 로직)
│       ├── repository/     (데이터 접근)
│       ├── model/          (엔티티 및 DTO)
│       ├── config/         (설정 클래스)
│       └── BoardApplication.java
├── src/main/resources/
│   ├── templates/          (타임리프 뷰)
│   ├── static/             (CSS, JS, 이미지)
│   ├── schema.sql          (테이블 생성)
│   ├── data.sql            (초기 데이터)
│   └── application.properties
└── build.gradle
```

---

## 목차

- [1. 스키마 구축](#1-스키마-구축)
- [2. 데이터 모델과 비즈니스 로직 구현](#2-데이터-모델과-비즈니스-로직-구현)
- [3. 로그인 및 회원가입 화면 구현](#3-로그인-및-회원가입-화면-구현)
- [4. 게시글 조회, 입력, 수정, 삭제 구현](#4-게시글-조회-입력-수정-삭제-구현)
- [5. 회원 관리 화면 구현](#5-회원-관리-화면-구현)
- [6. 최종 애플리케이션 결과](#6-최종-애플리케이션-결과)


---

## 🚀 시작하기

### 사전 요구사항

| 항목 | 버전 |
|-----|------|
| **JDK** | 21 이상 |
| **Gradle** | 8.x 이상 |
| **IDE** | IntelliJ IDEA 권장 |

### 프로젝트 생성

**Spring Initializr 설정**:

```
Project: Gradle - Groovy
Language: Java
Spring Boot: 3.5.7
Packaging: JAR
Java: 21

Dependencies:
- Spring Web
- Spring Data JPA
- Spring Security
- Thymeleaf
- H2 Database
- Lombok
- Validation
```

### 실행 방법

```bash

#프로젝트 복제 (Clone)
git clone https://github.com/Honghyechang/Spring-Boot-Portfolio.git

#실제 실행할 프로젝트 폴더로 이동
cd Spring-Boot-Portfolio/spring-boot-project/Spring-Board-Project

#애플리케이션 실행 (Run)
./gradlew bootRun
```
접속주소 : http://localhost:8080

---

# Spring Board Project 학습 문서

## 목차
- [1. 스키마 구축](#1-스키마-구축)
- [2. 데이터 모델과 비즈니스 로직 구현](#2-데이터-모델과-비즈니스-로직-구현)
- [3. 로그인 및 회원가입 화면 구현](#3-로그인-및-회원가입-화면-구현)

---

## 1. 스키마 구축

### 1.1 프로젝트 의존성 설정

Spring Boot 게시판 프로젝트를 시작하기 위해 필요한 의존성들을 `build.gradle`에 추가합니다.

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
    
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
    
    runtimeOnly 'com.h2database:h2'
    
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}
```

#### 의존성 설명

| 의존성 | 역할 |
|--------|------|
| `spring-boot-starter-data-jpa` | JPA를 통한 데이터베이스 접근 및 ORM 기능 제공 |
| `spring-boot-starter-thymeleaf` | 서버 사이드 템플릿 엔진으로 동적 HTML 생성 |
| `spring-boot-starter-validation` | Bean Validation을 통한 입력값 검증 (`@NotBlank`, `@Email` 등) |
| `spring-boot-starter-web` | Spring MVC 기반 웹 애플리케이션 개발 |
| `spring-boot-starter-security` | 인증/인가 및 보안 기능 제공 |
| `thymeleaf-extras-springsecurity6` | Thymeleaf에서 Spring Security 기능 사용 (`sec:authorize` 등) |
| `lombok` | 반복적인 코드(Getter/Setter, 생성자 등) 자동 생성 |
| `h2` | 개발 환경용 인메모리 데이터베이스 |

---

### 1.2 데이터베이스 설정 (application.properties)

```properties
spring.application.name=Spring-Board-Project

# H2 데이터베이스 설정
spring.datasource.url=jdbc:h2:mem:demo
spring.h2.console.enabled=true

# SQL 초기화 설정
spring.sql.init.mode=always
spring.sql.init.encoding=utf-8

# JPA 설정
spring.jpa.hibernate.ddl-auto=none
```

#### 설정 속성 상세 설명

| 속성 | 설정값 | 의미 및 중요성 |
|------|--------|----------------|
| `spring.datasource.url` | `jdbc:h2:mem:demo` | H2 인메모리 데이터베이스를 `demo`라는 고정된 이름으로 생성합니다. 이를 통해 H2 Console에서 쉽게 접근할 수 있습니다. |
| `spring.h2.console.enabled` | `true` | 개발 중 `http://localhost:8080/h2-console`로 접속하여 실시간으로 테이블 구조와 데이터를 확인할 수 있습니다. (DBeaver와 유사한 웹 기반 DB 브라우저) |
| `spring.sql.init.mode` | `always` | 애플리케이션 시작 시 `schema.sql`과 `data.sql` 파일을 **항상 실행**하도록 보장합니다. |
| `spring.sql.init.encoding` | `utf-8` | SQL 스크립트 파일을 UTF-8 인코딩으로 읽어 한글 주석이나 데이터가 깨지지 않도록 합니다. |
| `spring.jpa.hibernate.ddl-auto` | `none` | 🔥 **가장 중요한 설정**입니다. Hibernate가 `@Entity` 클래스를 기반으로 테이블을 자동 생성/수정/삭제하는 DDL 작업을 **완전히 비활성화**합니다. |

#### `ddl-auto=none`의 의미와 책임 분리

이 설정의 핵심은 **DDL(테이블 구조 정의)과 DML(데이터 조작)의 책임을 분리**하는 것입니다.

| 작업 유형 | 처리 주체 | 설명 |
|-----------|-----------|------|
| **DDL** (테이블 생성) | 개발자 (SQL 파일) | 테이블 구조를 만드는 책임을 Hibernate에서 **`schema.sql`** 파일로 넘깁니다. 개발자가 직접 SQL로 테이블을 정의합니다. |
| **DML** (데이터 CRUD) | JPA / Hibernate | 테이블이 이미 존재한다는 전제 하에, `@Entity` 객체를 이용한 데이터 조회, 삽입, 수정 등의 작업은 정상적으로 수행됩니다. |

> **결론**: `@Entity`는 여전히 DB 테이블의 **레코드(행)**를 표현하지만, **테이블 자체를 생성하는 작업(DDL)**은 Hibernate에게 위임하지 않고 수동 SQL 스크립트로 관리하겠다는 의미입니다. 이는 실무에서 테이블 구조를 명확하게 관리하고 버전 관리하기 위한 권장 방식입니다.

---

### 1.3 데이터베이스 스키마 정의 (schema.sql)

`src/main/resources/schema.sql` 파일에 테이블 구조를 정의합니다.

```sql
CREATE TABLE member(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL,
    password VARCHAR(256)
);

CREATE TABLE authority(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(256),
    member_id INTEGER,
    FOREIGN KEY(member_id) REFERENCES member(id)
);

CREATE TABLE article(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(256),
    description VARCHAR(4096),
    created DATETIME,
    updated DATETIME,
    member_id INTEGER,
    FOREIGN KEY (member_id) REFERENCES member(id)
);
```

#### 테이블 설명

**1. member 테이블** - 회원 정보 저장

| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| `id` | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 회원 고유 번호 |
| `name` | VARCHAR(128) | NOT NULL | 회원 이름 |
| `email` | VARCHAR(128) | NOT NULL | 이메일 (로그인 아이디로 사용) |
| `password` | VARCHAR(256) | - | BCrypt로 암호화된 비밀번호 |

**2. authority 테이블** - 회원 권한 정보 저장

| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| `id` | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 권한 고유 번호 |
| `authority` | VARCHAR(256) | - | 권한명 (예: `ROLE_ADMIN`, `ROLE_USER`) |
| `member_id` | INTEGER | FOREIGN KEY → member(id) | 권한을 가진 회원의 ID |

**3. article 테이블** - 게시글 정보 저장

| 컬럼 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| `id` | INTEGER | PRIMARY KEY, AUTO_INCREMENT | 게시글 고유 번호 |
| `title` | VARCHAR(256) | - | 게시글 제목 |
| `description` | VARCHAR(4096) | - | 게시글 본문 |
| `created` | DATETIME | - | 작성 시간 |
| `updated` | DATETIME | - | 수정 시간 |
| `member_id` | INTEGER | FOREIGN KEY → member(id) | 게시글 작성자 ID |

#### 테이블 관계 구조

```
member (1) ──────< (N) authority
   │
   │
   └──────< (N) article
```

- 한 명의 회원(`member`)은 여러 개의 권한(`authority`)을 가질 수 있습니다.
- 한 명의 회원(`member`)은 여러 개의 게시글(`article`)을 작성할 수 있습니다.

---

### 1.4 초기 데이터 삽입 (data.sql)

`src/main/resources/data.sql` 파일에 개발 편의를 위한 초기 데이터를 정의합니다.

```sql
-- 회원 데이터 삽입 (비밀번호는 BCrypt로 암호화된 'password')
INSERT INTO member(name, email, password) 
VALUES ('홍혜창','HyechangHong@spring.ac.kr','$2a$12$QWIo01qrkw4CuQdj/xZ.meJpuBB02UOxCplTXTdUc424f/aBbPU32');

INSERT INTO member(name, email, password) 
VALUES ('윤서준','SeojunYoon@spring.ac.kr','$2a$12$QWIo01qrkw4CuQdj/xZ.meJpuBB02UOxCplTXTdUc424f/aBbPU32');

INSERT INTO member(name, email, password) 
VALUES ('김우현','WoohyunKim@spring.ac.kr','$2a$12$QWIo01qrkw4CuQdj/xZ.meJpuBB02UOxCplTXTdUc424f/aBbPU32');

INSERT INTO member(name, email, password) 
VALUES ('손흥민','Sonny@spring.ac.kr','$2a$12$QWIo01qrkw4CuQdj/xZ.meJpuBB02UOxCplTXTdUc424f/aBbPU32');

-- 권한 데이터 삽입 (윤서준에게 관리자 권한 부여)
INSERT INTO authority(authority, member_id) 
VALUES('ROLE_ADMIN', 2);

-- 게시글 데이터 삽입
INSERT INTO article(title, description, created, updated, member_id) 
VALUES ('첫 번째 게시글 제목','첫 번째 게시글 본문', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

INSERT INTO article(title, description, created, updated, member_id) 
VALUES ('두 번째 게시글 제목','두 번째 게시글 본문', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);

INSERT INTO article(title, description, created, updated, member_id) 
VALUES ('세 번째 게시글 제목','세 번째 게시글 본문', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);

INSERT INTO article(title, description, created, updated, member_id) 
VALUES ('네 번째 게시글 제목','네 번째 게시글 본문', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 4);
```

#### 초기 데이터 설명

**삽입된 회원 정보**

| ID | 이름 | 이메일 | 비밀번호 | 권한 |
|----|------|--------|----------|------|
| 1 | 홍혜창 | HyechangHong@spring.ac.kr | password | - |
| 2 | 윤서준 | SeojunYoon@spring.ac.kr | password | ROLE_ADMIN |
| 3 | 김우현 | WoohyunKim@spring.ac.kr | password | - |
| 4 | 손흥민 | Sonny@spring.ac.kr | password | - |

> **💡 비밀번호 정보**: 모든 회원의 비밀번호는 `password`이며, BCrypt 알고리즘으로 암호화되어 저장되었습니다.

**초기 데이터 준비의 목적**

실제 게시판 애플리케이션에서는 회원가입과 게시글 작성을 직접 수행할 수 있지만, **개발 편의성과 테스트 용이성**을 위해 미리 몇 개의 회원과 게시글을 초기화합니다. 이를 통해:

- 로그인 기능을 즉시 테스트할 수 있습니다.
- 게시글 목록 조회 화면을 바로 확인할 수 있습니다.
- 권한에 따른 접근 제어를 테스트할 수 있습니다.

---

### 1.5 스키마 구축 동작 흐름

애플리케이션 시작 시 다음 순서로 데이터베이스가 초기화됩니다:

```
1. Spring Boot 애플리케이션 시작
   ↓
2. application.properties 설정 로드
   ↓
3. H2 인메모리 데이터베이스 생성 (jdbc:h2:mem:demo)
   ↓
4. spring.sql.init.mode=always 설정 확인
   ↓
5. schema.sql 실행 → 테이블 생성
   ↓
6. data.sql 실행 → 초기 데이터 삽입
   ↓
7. JPA 엔티티 클래스 로드 (ddl-auto=none이므로 테이블 생성은 하지 않음)
   ↓
8. 애플리케이션 준비 완료
```

#### 중요한 설정 포인트

1. **`spring.sql.init.mode=always`**: SQL 스크립트 파일을 항상 실행합니다.
2. **`spring.jpa.hibernate.ddl-auto=none`**: Hibernate의 자동 DDL 생성을 비활성화합니다.
3. **`spring.sql.init.encoding=utf-8`**: SQL 파일의 한글이 깨지지 않도록 인코딩을 지정합니다.

이 설정들의 조합으로 **개발자가 직접 작성한 SQL 스크립트로만 테이블을 관리**하는 명확한 구조를 만들 수 있습니다.

---

## 2. 데이터 모델과 비즈니스 로직 구현

이 단계에서는 데이터베이스 테이블과 매핑되는 엔티티 클래스, 데이터 전송 객체(DTO), 그리고 비즈니스 로직을 처리하는 서비스 계층을 구현합니다.

> **⚠️ 주의사항**: 현재 작성하는 코드는 프로젝트의 **초기 틀(골격)**입니다. 이후 기능 추가에 따라 서비스와 레포지토리에 메서드가 계속 추가될 것입니다.

---

### 2.1 엔티티 클래스 (Model) 구현

엔티티 클래스는 데이터베이스 테이블과 1:1로 매핑되는 Java 객체입니다. `@Entity` 어노테이션을 통해 JPA가 관리하는 영속성 객체가 됩니다.

#### Member 엔티티

```java
package com.example.Spring.Board.Project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
}
```

**어노테이션 설명**

| 어노테이션 | 역할 |
|-----------|------|
| `@Entity` | 이 클래스가 JPA 엔티티임을 선언하며, `member` 테이블과 매핑됩니다. |
| `@Data` | Lombok: Getter, Setter, toString, equals, hashCode를 자동 생성합니다. |
| `@AllArgsConstructor` | Lombok: 모든 필드를 매개변수로 받는 생성자를 생성합니다. |
| `@NoArgsConstructor` | Lombok: 기본 생성자를 생성합니다. (JPA 스펙 요구사항) |
| `@Builder` | Lombok: 빌더 패턴을 사용한 객체 생성을 지원합니다. |
| `@Id` | 이 필드가 기본키(Primary Key)임을 나타냅니다. |
| `@GeneratedValue` | 기본키 생성 전략을 지정합니다. `IDENTITY`는 DB의 AUTO_INCREMENT를 사용합니다. |

#### Authority 엔티티

```java
package com.example.Spring.Board.Project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String authority;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
```

**연관관계 설명**

| 어노테이션 | 설명 |
|-----------|------|
| `@ManyToOne` | 여러 개의 `Authority`가 하나의 `Member`와 연관됩니다. (N:1 관계) |
| `@JoinColumn(name = "member_id")` | 외래키 컬럼명을 `member_id`로 지정합니다. 이 컬럼이 `member` 테이블의 `id`를 참조합니다. |

#### Article 엔티티

```java
package com.example.Spring.Board.Project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    @CreatedDate
    private LocalDateTime created;

    @LastModifiedDate
    private LocalDateTime updated;
}
```

**Auditing 기능 설명**

| 어노테이션 | 역할 |
|-----------|------|
| `@EntityListeners(AuditingEntityListener.class)` | JPA Auditing 기능을 활성화하여 생성/수정 시간을 자동으로 관리합니다. |
| `@CreatedDate` | 엔티티가 생성될 때 현재 시간이 자동으로 설정됩니다. |
| `@LastModifiedDate` | 엔티티가 수정될 때마다 현재 시간이 자동으로 갱신됩니다. |

> **💡 Auditing 활성화**: 메인 클래스에 `@EnableJpaAuditing` 어노테이션을 추가해야 Auditing 기능이 작동합니다.

---

### 2.2 UserDetails 구현 클래스

Spring Security에서 인증된 사용자 정보를 담는 `UserDetails` 인터페이스를 구현합니다.

#### MemberUserDetails 클래스

```java
package com.example.Spring.Board.Project.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberUserDetails implements UserDetails {

    // Spring Security 필수 필드
    private Collection<SimpleGrantedAuthority> authorities;
    private String password;
    private String username; // 이메일을 아이디로 사용

    // 애플리케이션 추가 필드
    private String displayName;
    private Long memberId;

    public MemberUserDetails(Member member, List<Authority> authorities) {
        // 필수 설정
        this.password = member.getPassword();
        this.username = member.getEmail();
        this.authorities = authorities.stream()
                .map(i -> new SimpleGrantedAuthority(i.getAuthority()))
                .collect(Collectors.toList());
        
        // 추가 필드 설정
        this.displayName = member.getName();
        this.memberId = member.getId();
    }
}
```

**UserDetails 구현의 핵심**

1. **필수 필드**: Spring Security가 인증/인가 처리에 사용하는 필드들
   - `username`: 로그인 아이디 (여기서는 이메일)
   - `password`: 암호화된 비밀번호
   - `authorities`: 권한 목록 (ROLE_USER, ROLE_ADMIN 등)

2. **추가 필드**: 애플리케이션에서 사용할 추가 정보
   - `displayName`: 화면에 표시할 사용자 이름
   - `memberId`: 사용자의 DB 고유 ID

3. **권한 변환 로직**: `Authority` 엔티티 리스트를 Spring Security의 `SimpleGrantedAuthority` 컬렉션으로 변환합니다.

---

### 2.3 DTO (Data Transfer Object) 구현

DTO는 계층 간 데이터 전송을 위한 객체입니다. 엔티티를 직접 노출하지 않고 필요한 데이터만 전달하여 **보안성과 유연성**을 높입니다.

#### DTO 패턴의 핵심 개념

```
[Client] ─── RequestDTO ───> [Controller] ───> [Service] ───> [Repository]
                                                     │              │
                                                     │         [Entity]
                                                     │              │
[Client] <─── ResponseDTO ─── [Controller] <──── [Service] <─── [Repository]
```

**Request DTO**: 클라이언트가 서버로 데이터를 전송할 때 사용  
**Response DTO**: 서버가 클라이언트로 데이터를 응답할 때 사용

#### MemberForm (Request DTO)

```java
package com.example.Spring.Board.Project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm {
    @NotBlank(message="이름을 입력하세요.")
    private String name;
    
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    private String password;
    private String passwordConfirm;
}
```

**Bean Validation 어노테이션**

| 어노테이션 | 검증 내용 |
|-----------|----------|
| `@NotBlank` | null, 빈 문자열(""), 공백만 있는 문자열("   ")을 모두 거부합니다. |
| `@Email` | 이메일 형식(@가 포함되고 도메인이 있는 형식)인지 검증합니다. |

#### MemberDto (Response DTO)

```java
package com.example.Spring.Board.Project.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDto {
    private Long id;
    private String name;
    private String email;
    // password는 포함하지 않음 (보안)
}
```

> **💡 보안 원칙**: Response DTO에는 민감한 정보(비밀번호)를 포함하지 않습니다.

#### ArticleForm (Request DTO)

```java
package com.example.Spring.Board.Project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleForm {
    private Long id; // 수정 시에만 사용
    
    @NotBlank(message = "게시글 제목을 입력하세요.")
    private String title;
    
    @NotBlank(message = "게시글 내용을 입력하세요")
    private String description;
}
```

**ArticleForm의 2가지 사용 케이스**

1. **새 글 작성**: `id`는 null 상태로 전달됩니다.
2. **글 수정**: 기존 게시글의 `id`를 함께 전달하여 어떤 게시글을 수정할지 식별합니다.

#### ArticleDto (Response DTO)

```java
package com.example.Spring.Board.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime created;
    private LocalDateTime updated;
    
    // 작성자 정보 포함
    private Long memberId;
    private String name;
    private String email;
}
```

**Response DTO의 특징**: 게시글 정보뿐만 아니라 작성자의 정보(`name`, `email`)도 함께 포함하여 화면에서 바로 사용할 수 있도록 합니다.

#### PasswordForm (Request DTO)

```java
package com.example.Spring.Board.Project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordForm {
    @NotBlank(message = "기존의 패스워드를 입력해주세요.")
    private String old;
    
    @NotBlank(message="새로운 패스워드를 입력해주세요.")
    @Size(min = 8, message = "8글자 이상 입력해주세요.")
    private String password;
    
    @NotBlank(message = "새로운 패스워드를 확인해주세요")
    private String passwordConfirm;
}
```

**비밀번호 변경 프로세스**

1. `old`: 현재 비밀번호 확인 (DB의 암호화된 비밀번호와 비교)
2. `password`: 새로운 비밀번호 (8자 이상 검증)
3. `passwordConfirm`: 새 비밀번호 재확인 (일치 여부 검증)

---

### 2.4 Repository 계층 구현

Spring Data JPA를 사용하여 데이터베이스 접근 계층을 구현합니다. `JpaRepository`를 상속받으면 기본 CRUD 메서드가 자동으로 제공됩니다.

#### MemberRepository

```java
package com.example.Spring.Board.Project.repository;

import com.example.Spring.Board.Project.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
```

**JpaRepository 상속의 이점**

| 자동 제공 메서드 | 기능 |
|-----------------|------|
| `save(entity)` | 엔티티 저장 또는 수정 |
| `findById(id)` | ID로 엔티티 조회 |
| `findAll()` | 모든 엔티티 조회 |
| `deleteById(id)` | ID로 엔티티 삭제 |
| `count()` | 전체 엔티티 개수 조회 |

**커스텀 메서드**

```java
Optional<Member> findByEmail(String email);
```

- Spring Data JPA의 **메서드 이름 규칙**을 따라 자동으로 쿼리가 생성됩니다.
- `findBy` + `필드명`: 해당 필드로 조회하는 쿼리를 자동 생성
- 생성되는 SQL: `SELECT * FROM member WHERE email = ?`
- `Optional` 반환: 결과가 없을 수 있음을 명시적으로 표현

#### AuthorityRepository

```java
package com.example.Spring.Board.Project.repository;

import com.example.Spring.Board.Project.model.Authority;
import com.example.Spring.Board.Project.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findByMember(Member member);
}
```

**커스텀 메서드 설명**

```java
List<Authority> findByMember(Member member);
```

- 특정 회원의 모든 권한을 조회합니다.
- 생성되는 SQL: `SELECT * FROM authority WHERE member_id = ?`
- 한 명의 회원이 여러 권한을 가질 수 있으므로 `List`로 반환합니다.

#### ArticleRepository

```java
package com.example.Spring.Board.Project.repository;

import com.example.Spring.Board.Project.model.Article;
import com.example.Spring.Board.Project.model.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Transactional
    void deleteAllByMember(Member member);
}
```

**@Transactional의 역할**

```java
@Transactional
void deleteAllByMember(Member member);
```

- 특정 회원이 작성한 모든 게시글을 삭제합니다.
- `@Transactional`: 이 메서드가 **하나의 트랜잭션**으로 처리됨을 보장합니다.
  - 모든 삭제가 성공하거나, 하나라도 실패하면 전체 롤백됩니다.
- 생성되는 SQL: `DELETE FROM article WHERE member_id = ?`

---

### 2.5 Service 계층 구현

Service 계층은 비즈니스 로직을 처리하고 **Entity ↔ DTO 변환**을 담당합니다.

#### 서비스 계층의 핵심 역할

```
[Controller]
     ↓ RequestDTO (MemberForm)
[Service]
     ↓ Entity로 변환 (Member)
[Repository] → DB 저장
     ↓ Entity 반환 (Member)
[Service]
     ↓ ResponseDTO로 변환 (MemberDto)
[Controller] → 화면 전달
```

#### MemberService (초기 버전)

```java
package com.example.Spring.Board.Project.service;

import com.example.Spring.Board.Project.dto.MemberDto;
import com.example.Spring.Board.Project.dto.MemberForm;
import com.example.Spring.Board.Project.model.Member;
import com.example.Spring.Board.Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // Entity → DTO 변환
    public MemberDto mapToMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    // ID로 회원 조회
    public MemberDto findById(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        return mapToMemberDto(member);
    }

    // 회원 생성
    public MemberDto create(MemberForm memberForm) {
        Member member = Member.builder()
                .name(memberForm.getName())
                .email(memberForm.getEmail())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .build();
        
        memberRepository.save(member);
        return mapToMemberDto(member);
    }
}
```

**주요 메서드 설명**

1. **mapToMemberDto(Member member)**
   - Entity를 DTO로 변환하는 매퍼 메서드
   - 비밀번호는 제외하고 필요한 정보만 DTO에 담습니다.

2. **create(MemberForm memberForm)**
   - RequestDTO를 받아 Entity를 생성합니다.
   - `passwordEncoder.encode()`: 평문 비밀번호를 BCrypt로 암호화합니다.
   - 암호화된 비밀번호로 DB에 저장합니다.

> **💡 초기 틀**: 현재는 기본적인 CRUD 메서드만 구현했습니다. 이후 비밀번호 변경, 이메일 중복 확인 등의 메서드가 추가될 것입니다.

#### MemberService (최종 버전)

```java
package com.example.Spring.Board.Project.service;

import com.example.Spring.Board.Project.dto.MemberDto;
import com.example.Spring.Board.Project.dto.MemberForm;
import com.example.Spring.Board.Project.model.Member;
import com.example.Spring.Board.Project.repository.AuthorityRepository;
import com.example.Spring.Board.Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    public MemberDto mapToMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }

    public MemberDto findById(Long id) {
        Member member = memberRepository.findById(id).orElse(null);
        return mapToMemberDto(member);
    }

    public MemberDto create(MemberForm memberForm) {
        Member member = Member.builder()
                .name(memberForm.getName())
                .email(memberForm.getEmail())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .build();
        
        memberRepository.save(member);
        return mapToMemberDto(member);
    }

    // 이메일로 회원 조회 (중복 체크용)
    public Optional<MemberDto> findByEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.stream()
                .map(this::mapToMemberDto)
                .findFirst();
    }

    // 비밀번호 확인
    public boolean checkPassword(Long id, String password) {
        return passwordEncoder.matches(
                password,
                memberRepository.findById(id).orElseThrow().getPassword()
        );
    }

    // 비밀번호 변경
    public void updatePassword(Long id, String password) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }
}
```

**추가된 메서드 설명**

**1. findByEmail(String email)**

```java
public Optional<MemberDto> findByEmail(String email) {
    Optional<Member> member = memberRepository.findByEmail(email);
    return member.stream()
            .map(this::mapToMemberDto)
            .findFirst();
}
```

- 이메일로 회원을 조회합니다. (회원가입 시 중복 체크에 사용)
- `Optional<Member>`를 `Optional<MemberDto>`로 변환하는 스트림 기법:

| 상태 | 처리 과정 | 결과 |
|------|----------|------|
| 값이 존재 | `stream()` → `map(변환)` → `findFirst()` | `Optional.of(MemberDto)` |
| 값이 없음 | `stream()` → `map(실행 안 됨)` → `findFirst()` | `Optional.empty()` |

- 이 패턴은 아래 if-else 문과 동일한 결과를 냅니다:

```java
if (member.isPresent()) {
    MemberDto dto = mapToMemberDto(member.get());
    return Optional.of(dto);
} else {
    return Optional.empty();
}
```

**2. checkPassword(Long id, String password)**

```java
public boolean checkPassword(Long id, String password) {
    return passwordEncoder.matches(
            password,
            memberRepository.findById(id).orElseThrow().getPassword()
    );
}
```

- 사용자가 입력한 평문 비밀번호와 DB에 저장된 암호화된 비밀번호를 비교합니다.
- `passwordEncoder.matches()`: BCrypt 알고리즘으로 비밀번호를 안전하게 비교합니다.
- 비밀번호 변경 전 본인 확인에 사용됩니다.

**3. updatePassword(Long id, String password)**

```java
public void updatePassword(Long id, String password) {
    Member member = memberRepository.findById(id).orElseThrow();
    member.setPassword(passwordEncoder.encode(password));
    memberRepository.save(member);
}
```

- 새로운 비밀번호를 암호화하여 DB에 저장합니다.
- **중요**: 평문 비밀번호를 그대로 저장하지 않고 반드시 `encode()`로 암호화합니다.

#### ArticleService

```java
package com.example.Spring.Board.Project.service;

import com.example.Spring.Board.Project.dto.ArticleDto;
import com.example.Spring.Board.Project.model.Article;
import com.example.Spring.Board.Project.repository.ArticleRepository;
import com.example.Spring.Board.Project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    // Entity → DTO 변환
    public ArticleDto mapToArticleDto(Article article) {
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .memberId(article.getMember().getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail())
                .build();
    }
}
```

**Entity → DTO 변환의 특징**

- `Article` 엔티티는 `Member` 엔티티와 연관관계를 맺고 있습니다.
- `ArticleDto`에는 게시글 정보뿐만 아니라 작성자 정보(`name`, `email`)도 함께 담습니다.
- 이를 통해 화면에서 별도의 조회 없이 작성자 정보를 바로 표시할 수 있습니다.

---

### 2.6 Entity ↔ DTO 변환 패턴 정리

#### 왜 DTO를 사용하는가?

| 구분 | Entity 직접 사용 | DTO 사용 |
|------|-----------------|----------|
| **보안** | 비밀번호 등 민감 정보가 노출될 위험 | 필요한 정보만 선택적으로 전달 |
| **유연성** | DB 구조 변경 시 모든 계층에 영향 | DB 변경이 있어도 DTO만 수정 |
| **성능** | 불필요한 연관 엔티티까지 로드 가능 | 필요한 데이터만 조회하여 전달 |
| **순환 참조** | 양방향 연관관계 시 JSON 직렬화 오류 | 단방향 데이터 구조로 안전 |

#### 변환 흐름 예시

**회원 생성 프로세스**

```
1. 클라이언트: MemberForm (name, email, password, passwordConfirm)
   ↓
2. Controller: @Valid 검증 통과
   ↓
3. Service: Member Entity 생성
   - password → passwordEncoder.encode(password)
   ↓
4. Repository: DB 저장 (INSERT)
   ↓
5. Service: Member → MemberDto 변환 (password 제외)
   ↓
6. Controller: MemberDto 반환 또는 View에 전달
```

**게시글 조회 프로세스**

```
1. Repository: Article Entity 조회 (Member와 함께)
   ↓
2. Service: ArticleDto 변환
   - Article의 title, description, created, updated
   - Member의 name, email 추가
   ↓
3. Controller: ArticleDto를 View에 전달
   ↓
4. View: 게시글 정보와 작성자 정보 표시
```

---

## 3. 로그인 및 회원가입 화면 구현

이 단계에서는 Spring Security를 설정하고, 실제 로그인/회원가입/비밀번호 변경 기능을 구현합니다.

### 3.1 Spring Security 설정

#### SecurityConfiguration 클래스

```java
package com.example.Spring.Board.Project.config;

import com.example.Spring.Board.Project.model.Authority;
import com.example.Spring.Board.Project.model.Member;
import com.example.Spring.Board.Project.model.MemberUserDetails;
import com.example.Spring.Board.Project.repository.AuthorityRepository;
import com.example.Spring.Board.Project.repository.MemberRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((auth) -> {
                auth.requestMatchers("/", "/signup", "/article/list", "/article/content").permitAll()
                    .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated();
            })
            .formLogin((login) -> {
                login.loginPage("/login")
                     .defaultSuccessUrl("/")
                     .permitAll();
            })
            .logout((logout) -> {
                logout.logoutUrl("/logout")
                      .logoutSuccessUrl("/")
                      .permitAll();
            });
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            MemberRepository memberRepository,
            AuthorityRepository authorityRepository) {
        
        return username -> {
            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            List<Authority> authorities = authorityRepository.findByMember(member);
            
            return new MemberUserDetails(member, authorities);
        };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
```

#### 설정 1: SecurityFilterChain - 접근 권한 설정

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((auth) -> {
            auth.requestMatchers("/", "/signup", "/article/list", "/article/content").permitAll()
                .requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();
        })
        .formLogin((login) -> {
            login.loginPage("/login")
                 .defaultSuccessUrl("/")
                 .permitAll();
        })
        .logout((logout) -> {
            logout.logoutUrl("/logout")
                  .logoutSuccessUrl("/")
                  .permitAll();
        });
    
    return http.build();
}
```

**접근 권한 규칙**

| URL 패턴 | 권한 요구사항 | 설명 |
|----------|--------------|------|
| `/`, `/signup`, `/article/list`, `/article/content` | `permitAll()` | 모든 사용자(비로그인 포함) 접근 가능 |
| `/member/**` | `hasAuthority("ROLE_ADMIN")` | `ROLE_ADMIN` 권한을 가진 관리자만 접근 가능 |
| `anyRequest()` | `authenticated()` | 위에 명시되지 않은 모든 경로는 로그인 필수 |

**로그인 설정**

```java
.formLogin((login) -> {
    login.loginPage("/login")           // 커스텀 로그인 페이지 경로
         .defaultSuccessUrl("/")        // 로그인 성공 시 이동할 경로
         .permitAll();                  // 로그인 페이지 자체는 누구나 접근 가능
})
```

- `loginPage("/login")`: Spring Security의 기본 로그인 페이지 대신 우리가 만든 페이지를 사용합니다.
- `permitAll()`: `/login` GET 요청과 POST 요청 모두 비로그인 상태에서 접근 가능해야 합니다.

**로그아웃 설정**

```java
.logout((logout) -> {
    logout.logoutUrl("/logout")         // 로그아웃 처리 경로
          .logoutSuccessUrl("/")        // 로그아웃 성공 시 이동할 경로
          .permitAll();                 // 로그아웃 기능도 누구나 접근 가능
})
```

- `logoutUrl("/logout")`: POST 요청으로 이 경로에 접근하면 로그아웃이 처리됩니다.
- `permitAll()`: 로그아웃 처리 자체는 권한 검사 없이 진행되어야 합니다.

#### 설정 2: PasswordEncoder - 비밀번호 암호화

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

**BCrypt의 특징**

- **단방향 해시**: 암호화는 가능하지만 복호화는 불가능합니다.
- **Salt 자동 생성**: 같은 비밀번호여도 매번 다른 해시값이 생성됩니다.
- **느린 처리 속도**: 무차별 대입 공격(Brute Force)을 방어하기 위해 의도적으로 느리게 설계되었습니다.

**사용 예시**

```java
// 회원가입 시
String rawPassword = "password123";
String encodedPassword = passwordEncoder.encode(rawPassword);
// 결과: $2a$10$abcd1234...  (매번 다른 값)

// 로그인 시
boolean matches = passwordEncoder.matches("password123", encodedPassword);
// 결과: true (비밀번호 일치)
```

#### 설정 3: UserDetailsService - 사용자 정보 로드

```java
@Bean
public UserDetailsService userDetailsService(
        MemberRepository memberRepository,
        AuthorityRepository authorityRepository) {
    
    return username -> {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        List<Authority> authorities = authorityRepository.findByMember(member);
        
        return new MemberUserDetails(member, authorities);
    };
}
```

**UserDetailsService의 역할**

Spring Security가 로그인 처리 시 다음 순서로 동작합니다:

```
1. 사용자가 /login에 POST 요청 (username, password)
   ↓
2. Spring Security가 UserDetailsService.loadUserByUsername(username) 호출
   ↓
3. DB에서 username(이메일)로 Member 조회
   ↓
4. 해당 Member의 Authority(권한) 목록 조회
   ↓
5. MemberUserDetails 객체 생성 및 반환
   ↓
6. Spring Security가 비밀번호 검증 (PasswordEncoder 사용)
   ↓
7. 인증 성공 시 SecurityContext에 저장
```

**핵심 포인트**

- `username` 파라미터: 로그인 폼의 `name="username"` 입력값이 전달됩니다.
- 이 프로젝트에서는 **이메일을 아이디로 사용**하므로 `findByEmail()`로 조회합니다.
- 권한 정보도 함께 조회하여 `MemberUserDetails`에 담아 반환합니다.

#### 설정 4: WebSecurityCustomizer - 특정 경로 제외

```java
@Bean
public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
            .requestMatchers(PathRequest.toH2Console())
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
}
```

**보안 필터 제외 대상**

| 경로 | 이유 |
|------|------|
| H2 Console (`/h2-console/**`) | 개발 환경에서 DB 조회를 위한 콘솔 화면 |
| 정적 리소스 (`/css/**`, `/js/**`, `/images/**`) | CSS, JavaScript, 이미지 파일은 인증 불필요 |

- 이 경로들은 Spring Security 필터 체인을 완전히 우회합니다.
- 성능 최적화: 정적 파일에 대해 매번 세션 검사, CSRF 검사를 하지 않습니다.

---

### 3.2 로그인 화면 구현

#### HomeController - 로그인/로그아웃 경로 설정

```java
package com.example.Spring.Board.Project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    @RequestMapping("/")
    public String getHome() {
        return "forward:/article/list";
    }

    @RequestMapping("/login")
    public String getLogin() {
        return "login";
    }

    @RequestMapping("/logout")
    public String getLogout() {
        return "logout";
    }
}
```

**경로 설명**

| 경로 | HTTP 메서드 | 처리 내용 |
|------|-------------|----------|
| `/` | GET | 게시글 목록 페이지로 포워딩합니다. (`forward:/article/list`) |
| `/login` | GET | 로그인 화면(`login.html`)을 보여줍니다. |
| `/logout` | GET | 로그아웃 확인 화면(`logout.html`)을 보여줍니다. (선택사항) |

> **💡 참고**: `/login` GET은 필수이지만, `/logout` GET은 선택사항입니다. 로그아웃 확인 페이지 없이 바로 POST 요청으로 로그아웃을 처리할 수도 있습니다.

#### login.html - 로그인 화면

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>스프링 부트 게시판</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1>로그인</h1>
    
    <!-- 로그인 실패 시 오류 메시지 표시 -->
    <div class="mb-3 text-danger" th:if="${param.error}">
        아이디와 패스워드가 잘못 되었습니다.
    </div>
    
    <form th:action="@{/login}" method="post">
        <div class="col-3 mb-3">
            <input type="text" id="username" name="username" 
                   placeholder="이메일 아이디를 입력해주세요.">
        </div>
        <div class="col-3 mb-3">
            <input type="password" id="password" name="password" 
                   placeholder="비밀번호를 입력해주세요.">
        </div>
        <div class="col-3 mb-3">
            <button type="submit" class="btn btn-primary">로그인</button>
        </div>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" 
        crossorigin="anonymous"></script>
</body>
</html>
```

**핵심 포인트**

**1. param.error를 통한 오류 메시지 표시**

```html
<div class="mb-3 text-danger" th:if="${param.error}">
    아이디와 패스워드가 잘못 되었습니다.
</div>
```

- `param`: Thymeleaf의 내장 객체로, 현재 요청의 **쿼리 파라미터**에 접근합니다.
- `${param.error}`: URL에 `?error`가 포함되어 있는지 확인합니다.

**로그인 실패 시 동작 흐름**

```
1. 사용자: /login (POST) - 잘못된 아이디/비밀번호 제출
   ↓
2. Spring Security: 인증 실패 감지
   ↓
3. 리다이렉트: HTTP 302 → /login?error
   ↓
4. 브라우저: /login?error (GET) 새로운 요청
   ↓
5. Controller: login.html 반환
   ↓
6. Thymeleaf: ${param.error}가 존재하므로 오류 메시지 표시
```

- `param`은 `HttpServletRequest`의 쿼리 파라미터를 읽어옵니다.
- 컨트롤러에서 `Model`에 명시적으로 추가하지 않아도 접근 가능합니다.

**2. 폼 필드의 name 속성**

```html
<input type="text" name="username" />
<input type="password" name="password" />
```

- Spring Security는 **기본적으로** `username`과 `password`라는 이름의 파라미터를 기대합니다.
- 이 이름들이 `UserDetailsService`의 `loadUserByUsername(username)` 메서드로 전달됩니다.

| 필드 | name 속성 | 역할 |
|------|-----------|------|
| 아이디 | `username` | `UserDetailsService.loadUserByUsername()`에 전달됩니다. |
| 비밀번호 | `password` | Spring Security가 `PasswordEncoder`로 검증합니다. |

> **⚠️ 중요**: `id` 속성은 선택사항이지만, `name` 속성은 **반드시 username과 password**여야 합니다. (커스텀 설정으로 변경 가능하지만 기본값 사용 권장)

**3. CSRF 토큰 자동 삽입**

```html
<form th:action="@{/login}" method="post">
```

- Thymeleaf의 `th:action`을 사용하면 Spring Security의 **CSRF 토큰**이 자동으로 hidden 필드로 삽입됩니다.
- 실제 렌더링된 HTML:

```html
<form action="/login" method="post">
    <input type="hidden" name="_csrf" value="랜덤한 토큰 값"/>
    <!-- 나머지 폼 필드 -->
</form>
```

- CSRF 토큰은 **Cross-Site Request Forgery** 공격을 방어합니다.

**4. Bootstrap 사용**

```html
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
      rel="stylesheet" crossorigin="anonymous">
```

- 프로젝트에서는 Bootstrap CDN을 사용하여 빠르게 UI를 구성합니다.
- CSS(디자인)와 JavaScript(동적 기능)를 각각 `<head>`와 `<body>` 끝에 삽입합니다.

#### logout.html - 로그아웃 확인 화면

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>스프링 부트 게시판</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <h1>로그아웃 하시겠습니까?</h1>
    <form th:action="@{/logout}" method="post">
        <button type="submit" class="btn btn-primary">로그아웃</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" 
        crossorigin="anonymous"></script>
</body>
</html>
```

**로그아웃 처리**

```html
<form th:action="@{/logout}" method="post">
```

- Spring Security는 보안상의 이유로 **POST 요청으로만 로그아웃**을 처리합니다.
- GET 요청으로 로그아웃을 허용하면 CSRF 공격에 취약해집니다.
- `th:action`을 사용하면 CSRF 토큰이 자동으로 포함됩니다.

**로그아웃 동작 흐름**

```
1. 사용자: 로그아웃 버튼 클릭
   ↓
2. 브라우저: /logout (POST) 요청 (CSRF 토큰 포함)
   ↓
3. Spring Security: 세션 무효화 및 SecurityContext 초기화
   ↓
4. 리다이렉트: HTTP 302 → / (logoutSuccessUrl 설정)
   ↓
5. 사용자: 비로그인 상태로 메인 페이지 표시
```

#### article-list-test.html - 테스트용 메인 화면

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"   
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <a th:href="@{/signup}">회원가입</a>
    
    <!-- 로그인한 사용자에게만 표시 -->
    <h2 sec:authorize="isAuthenticated()">
        로그인 성공
        안녕하세요, <span sec:authentication="principal.displayName">이름</span>님! 반갑습니다.

        <p>로그인 아이디 (이메일): 
           <strong><span sec:authentication="principal.username">아이디</span></strong>
        </p>

        <p>권한: <span sec:authentication="principal.authorities">권한 없음</span></p>

        <a th:href="@{/password}">비밀번호 변경 페이지 이동</a>
        <a th:href="@{/logout}">로그아웃 페이지 이동</a>
    </h2>
    
    <!-- 비로그인 사용자에게만 표시 -->
    <h2 sec:authorize="isAnonymous()">비회원 고객님 안녕하세요.</h2>
</body>
</html>
```

**Spring Security 통합 태그**

| 태그 | 설명 |
|------|------|
| `sec:authorize="isAuthenticated()"` | 로그인한 사용자에게만 해당 영역을 표시합니다. |
| `sec:authorize="isAnonymous()"` | 비로그인 사용자에게만 해당 영역을 표시합니다. |
| `sec:authentication="principal.displayName"` | 현재 로그인한 사용자의 `displayName` 필드 값을 표시합니다. |
| `sec:authentication="principal.username"` | 현재 로그인한 사용자의 `username`(이메일) 값을 표시합니다. |
| `sec:authentication="principal.authorities"` | 현재 로그인한 사용자의 권한 목록을 표시합니다. |

**principal 객체**

- `principal`은 `SecurityContext`에 저장된 인증 정보(`Authentication` 객체)의 핵심 정보입니다.
- 우리 프로젝트에서는 `MemberUserDetails` 객체가 `principal`입니다.
- 따라서 `MemberUserDetails`의 모든 필드에 접근 가능합니다:
  - `principal.displayName` → `MemberUserDetails.getDisplayName()`
  - `principal.username` → `MemberUserDetails.getUsername()`
  - `principal.memberId` → `MemberUserDetails.getMemberId()`

**로그인/로그아웃 테스트 결과**

**1. 비로그인 상태**

```
비회원 고객님 안녕하세요.
[회원가입]
```

**2. 로그인 성공 (홍혜창 계정)**

```
회원가입

로그인 성공
안녕하세요, 홍혜창님! 반갑습니다.
로그인 아이디 (이메일): HyechangHong@spring.ac.kr
권한: []

비밀번호 변경 페이지 이동
로그아웃 페이지 이동
```

**3. 로그인 실패**

URL: `http://localhost:8080/login?error`

```
로그인
아이디와 패스워드가 잘못 되었습니다.

[이메일 아이디를 입력해주세요.]
[비밀번호를 입력해주세요.]
[로그인]
```

---

### 3.3 회원가입 화면 구현

회원가입은 다음 3단계로 구성됩니다:

```
1. GET /signup → 빈 폼 표시
2. POST /signup → 입력값 검증
3. 성공 → /login으로 리다이렉트 / 실패 → 오류와 함께 폼 재표시
```

#### HomeController - 회원가입 처리

```java
@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    
    private final MemberService memberService;

    // 회원가입 폼 표시
    @GetMapping("/signup")
    public String getSignup(@ModelAttribute("member") MemberForm memberForm) {
        return "signup";
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String postMemberAdd(
            @Valid @ModelAttribute("member") MemberForm memberForm,
            BindingResult bindingResult) {
        
        // 수동 검증 1: 비밀번호 길이 체크
        if (memberForm.getPassword() == null || 
            memberForm.getPassword().length() < 8) {
            bindingResult.rejectValue("password", "MissMatch", 
                    "패스워드를 8글자 이상 입력하세요.");
        }
        
        // 수동 검증 2: 비밀번호 일치 여부
        if (!memberForm.getPassword().equals(memberForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "MissMatch", 
                    "입력하신 패스워드가 다릅니다.");
        }
        
        // 수동 검증 3: 이메일 중복 체크
        if (memberService.findByEmail(memberForm.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "AlreadyExist", 
                    "사용중인 이메일입니다.");
        }

        log.info("검사 완료");

        // 오류가 있으면 폼으로 다시 돌아가기
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // 회원 생성
        memberService.create(memberForm);
        return "redirect:/login";
    }
}
```

**@ModelAttribute의 역할**

**GET 요청 시**

```java
@GetMapping("/signup")
public String getSignup(@ModelAttribute("member") MemberForm memberForm) {
    return "signup";
}
```

1. Spring이 빈 `MemberForm` 객체를 생성합니다.
2. 이 객체를 `"member"`라는 이름으로 `Model`에 자동 저장합니다.
3. `signup.html`에서 `th:object="${member}"`로 이 객체를 참조할 수 있습니다.

**POST 요청 시**

```java
@PostMapping("/signup")
public String postMemberAdd(
        @Valid @ModelAttribute("member") MemberForm memberForm,
        BindingResult bindingResult) {
    // ...
}
```

1. Spring이 폼 데이터를 받아 새로운 `MemberForm` 객체를 생성합니다.
2. 폼의 `name` 속성과 `MemberForm`의 필드명을 매칭하여 값을 채웁니다.
3. `@Valid`가 붙어있으므로 Bean Validation을 실행합니다.
4. 검증 결과는 `BindingResult`에 자동으로 저장됩니다.

**@Valid와 BindingResult의 협업**

| 순서 | 동작 | 담당 |
|------|------|------|
| 1 | 폼 데이터를 `MemberForm` 객체로 바인딩 | Spring MVC |
| 2 | `@NotBlank`, `@Email` 등 자동 검증 실행 | `@Valid` |
| 3 | 검증 오류를 `FieldError` 객체로 수집 | `BindingResult` |
| 4 | 추가 비즈니스 로직 검증 | `rejectValue()` |
| 5 | 오류 여부 확인 | `bindingResult.hasErrors()` |

**rejectValue()를 통한 수동 검증**

```java
bindingResult.rejectValue("필드명", "에러코드", "에러메시지");
```

- **첫 번째 인수**: 오류를 표시할 **필드명** (MemberForm의 필드와 일치해야 함)
- **두 번째 인수**: 에러 코드 (메시지 소스에서 사용, 여기서는 단순 식별용)
- **세 번째 인수**: 실제 표시될 오류 메시지

**수동 검증이 필요한 이유**

| 검증 유형 | 자동 검증 (@Valid) | 수동 검증 (rejectValue) |
|----------|-------------------|----------------------|
| 필드 자체의 규칙 | ✅ `@NotBlank`, `@Size`, `@Email` | ❌ |
| 두 필드 간 비교 | ❌ | ✅ 비밀번호 일치 확인 |
| DB 접근 필요 | ❌ | ✅ 이메일 중복 체크 |
| 복잡한 비즈니스 로직 | ❌ | ✅ 서비스 계층 호출 |

**회원가입 처리 흐름**

```
1. 사용자: /signup (POST) - 폼 데이터 제출
   ↓
2. Spring: MemberForm 객체 생성 및 데이터 바인딩
   ↓
3. @Valid: 자동 검증 실행 (@NotBlank, @Email 등)
   ↓
4. Controller: 수동 검증 실행
   - 비밀번호 길이 체크
   - 비밀번호 일치 여부
   - 이메일 중복 체크
   ↓
5. bindingResult.hasErrors() 확인
   ├─ 오류 있음 → "signup" 반환 (폼 재표시)
   └─ 오류 없음 → memberService.create() 호출
                 → "redirect:/login" 반환
```

#### signup.html - 회원가입 폼

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet" crossorigin="anonymous">
</head>
<body>
<section>
    <h1>회원가입</h1>
    <form th:object="${member}" th:action="@{/signup}" method="post">
        <div class="mb-3">
            <label class="form-label">이름</label>
            <input type="text" th:field="*{name}" class="form-control">
            <p th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></p>
        </div>

        <div class="mb-3">
            <label class="form-label">이메일(아이디)</label>
            <input type="text" th:field="*{email}" class="form-control">
            <p th:if="${#fields.hasErrors('email')}" th:errors="*{email}"></p>
        </div>

        <div class="mb-3">
            <label class="form-label">비밀번호</label>
            <input type="password" th:field="*{password}" class="form-control">
            <p th:if="${#fields.hasErrors('password')}" th:errors="*{password}"></p>
        </div>

        <div class="mb-3">
            <label class="form-label">비밀번호 확인</label>
            <input type="password" th:field="*{passwordConfirm}" class="form-control">
            <p th:if="${#fields.hasErrors('passwordConfirm')}" 
               th:errors="*{passwordConfirm}"></p>
        </div>

        <button type="submit" class="btn btn-primary">생성</button>
    </form>
</section>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" 
        crossorigin="anonymous"></script>
</body>
</html>
```

**핵심 Thymeleaf 문법**

**1. th:object - 폼과 객체 연결**

```html
<form th:object="${member}" th:action="@{/signup}" method="post">
```

- `th:object="${member}"`: 폼 전체를 `Model`의 `member` 객체와 연결합니다.
- 이후 `th:field`에서 `*{필드명}` 문법으로 해당 객체의 필드를 참조할 수 있습니다.

**2. th:field - 필드 바인딩**

```html
<input type="text" th:field="*{name}" class="form-control">
```

`th:field="*{name}"`는 다음 세 가지 HTML 속성을 **자동으로 생성**합니다:

| 생성되는 속성 | 값 | 역할 |
|--------------|-----|------|
| `id` | `name` | JavaScript나 CSS에서 요소 식별 |
| `name` | `name` | 폼 제출 시 서버로 전송되는 파라미터 이름 |
| `value` | `${member.name}` | 오류 발생 시 이전 입력값을 유지 |

실제 렌더링된 HTML:

```html
<input type="text" id="name" name="name" value="" class="form-control">
```

- GET 요청(첫 화면): `value=""`
- POST 실패 후 재표시: `value="홍길동"` (사용자가 입력했던 값)

**3. #fields - 오류 확인 및 표시**

```html
<p th:if="${#fields.hasErrors('name')}" th:errors="*{name}"></p>
```

- `#fields`: Thymeleaf의 유틸리티 객체로, `BindingResult`에 접근합니다.
- `#fields.hasErrors('name')`: `name` 필드에 오류가 있는지 확인 (true/false)
- `th:errors="*{name}"`: `name` 필드의 오류 메시지를 출력합니다.

**오류 메시지 출력 과정**

```
1. th:if="${#fields.hasErrors('name')}"
   → #fields가 Model의 BindingResult를 찾음
   → name 필드에 오류가 있는지 확인
   ↓
2. 오류가 있다면 <p> 태그 렌더링
   ↓
3. th:errors="*{name}"
   → *{name}이 th:object="${member}"의 name 필드를 가리킴
   → BindingResult에서 name 필드의 오류 메시지 추출
   → <p> 태그 안에 메시지 출력
```

**회원가입 실행 결과**

**케이스 1: 모든 필드 비어있음**

```
회원가입

이름
[입력 필드]
이름을 입력하세요.

이메일(아이디)
[입력 필드]
이메일을 입력하세요.

비밀번호
[입력 필드]
패스워드를 8글자 이상 입력하세요.

비밀번호 확인
[입력 필드]
패스워드를 8글자 이상 입력하세요.
```

**케이스 2: 이메일 형식 오류**

```
이메일(아이디)
abcdefg
이메일 형식이 맞지 않습니다.
```

**케이스 3: 비밀번호 길이 부족**

```
비밀번호
1234
패스워드를 8글자 이상 입력하세요.
```

**케이스 4: 비밀번호 불일치**

```
비밀번호
password123

비밀번호 확인
password456
입력하신 패스워드가 다릅니다.
```

**케이스 5: 이메일 중복**

```
이메일(아이디)
HyechangHong@spring.ac.kr
사용중인 이메일입니다.
```

**케이스 6: 회원가입 성공**

입력값:
- 이름: 김구라
- 이메일: gura@spring.ac.kr
- 비밀번호: password
- 비밀번호 확인: password

→ `/login` 페이지로 리다이렉트

**오류 누적의 원리**

하나의 필드에 여러 오류가 동시에 발생하면 모두 `BindingResult`에 저장됩니다:

```java
// 자동 검증 오류
@Email(message = "이메일 형식이 맞지 않습니다.")
private String email;

// 수동 검증 오류
if (memberService.findByEmail(memberForm.getEmail()).isPresent()) {
    bindingResult.rejectValue("email", "AlreadyExist", "사용중인 이메일입니다.");
}
```

결과:

```
이메일(아이디)
abcdefg
이메일 형식이 맞지 않습니다., 사용중인 이메일입니다.
```

> **💡 참고**: `th:errors`는 기본적으로 모든 오류 메시지를 쉼표로 구분하여 출력합니다.

**비밀번호 필드의 특별한 동작**

```html
<input type="password" th:field="*{password}" class="form-control">
```

- `type="password"` 필드는 보안상의 이유로 **브라우저가 value 속성을 무시**합니다.
- 회원가입 실패 후 재표시되더라도 비밀번호 필드는 **항상 빈칸**으로 나타납니다.
- 사용자는 비밀번호를 다시 입력해야 합니다.

---

### 3.4 비밀번호 변경 기능 구현

비밀번호 변경은 **로그인한 사용자만** 접근할 수 있으며, 다음을 검증합니다:

1. 기존 비밀번호가 올바른지 확인
2. 새 비밀번호가 8자 이상인지 확인
3. 새 비밀번호와 확인 비밀번호가 일치하는지 확인

#### HomeController - 비밀번호 변경 처리

```java
@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {
    
    private final MemberService memberService;

    // 비밀번호 변경 폼 표시
    @GetMapping("/password")
    public String getPassword(@ModelAttribute("password") PasswordForm passwordForm) {
        return "password";
    }

    // 비밀번호 변경 처리
    @PostMapping("/password")
    public String postPassword(
            @Valid @ModelAttribute("password") PasswordForm passwordForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal MemberUserDetails memberUserDetails) {
        
        // 수동 검증 1: 새 비밀번호 일치 여부
        if (!passwordForm.getPassword().equals(passwordForm.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "MissMatch", 
                    "비밀번호가 같지 않습니다.");
        }
        
        // 수동 검증 2: 기존 비밀번호 확인
        if (!memberService.checkPassword(
                memberUserDetails.getMemberId(), 
                passwordForm.getOld())) {
            bindingResult.rejectValue("old", "MissMatch", 
                    "기존의 비밀번호가 옳지 않습니다.");
        }

        // 오류가 있으면 폼으로 다시 돌아가기
        if (bindingResult.hasErrors()) {
            return "password";
        }

        // 비밀번호 변경
        memberService.updatePassword(
                memberUserDetails.getMemberId(), 
                passwordForm.getPassword());
        
        return "redirect:/";
    }
}
```

**@AuthenticationPrincipal의 역할**

```java
@AuthenticationPrincipal MemberUserDetails memberUserDetails
```

- Spring Security가 현재 로그인한 사용자의 정보를 주입해줍니다.
- `SecurityContext`에 저장된 `Authentication` 객체의 `principal`을 가져옵니다.
- 우리 프로젝트에서는 `MemberUserDetails` 객체가 주입됩니다.

**@AuthenticationPrincipal의 중요성**

```java
memberUserDetails.getMemberId()
```

- 현재 로그인한 사용자의 DB ID를 안전하게 획득합니다.
- 세션 조작이나 hidden 필드 변조 공격을 방어할 수 있습니다.
- 인증된 사용자의 정보만 신뢰할 수 있습니다.

**비밀번호 변경 흐름**

```
1. 사용자: /password (POST) - 폼 데이터 제출
   ↓
2. Spring: PasswordForm 객체 생성 및 데이터 바인딩
   ↓
3. @Valid: 자동 검증 (@NotBlank, @Size 등)
   ↓
4. @AuthenticationPrincipal: 로그인한 사용자 정보 주입
   ↓
5. Controller: 수동 검증
   - 새 비밀번호 일치 여부
   - 기존 비밀번호 확인 (DB와 비교)
   ↓
6. bindingResult.hasErrors() 확인
   ├─ 오류 있음 → "password" 반환
   └─ 오류 없음 → memberService.updatePassword() 호출
                 → "redirect:/" 반환 (로그인 상태 유지)
```

#### password.html - 비밀번호 변경 폼

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet" crossorigin="anonymous">
</head>
<body>
<section>
    <h1>비밀번호 변경</h1>
    <form th:object="${password}" th:action="@{/password}" method="post">
        <div class="col-3 mb-3">
            <label class="form-label">기존 패스워드</label>
            <input type="password" th:field="*{old}" class="form-control">
            <p th:if="${#fields.hasErrors('old')}" 
               th:errors="*{old}" class="text-danger"></p>
        </div>

        <div class="col-3 mb-3">
            <label class="form-label">새로운 패스워드</label>
            <input type="password" th:field="*{password}" class="form-control">
            <p th:if="${#fields.hasErrors('password')}" 
               th:errors="*{password}" class="text-danger"></p>
        </div>

        <div class="col-3 mb-3">
            <label class="form-label">패스워드 확인</label>
            <input type="password" th:field="*{passwordConfirm}" class="form-control">
            <p th:if="${#fields.hasErrors('passwordConfirm')}" 
               th:errors="*{passwordConfirm}" class="text-danger"></p>
        </div>

        <button type="submit" class="btn btn-primary">변경</button>
    </form>
</section>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js" 
        crossorigin="anonymous"></script>
</body>
</html>
```

**비밀번호 변경 실행 결과**

**케이스 1: 모든 필드 비어있음**

```
비밀번호 변경

기존 패스워드
[입력 필드]
기존의 패스워드를 입력해주세요.
기존의 비밀번호가 옳지 않습니다.

새로운 패스워드
[입력 필드]
8글자 이상 입력해주세요.
새로운 패스워드를 입력해주세요.

패스워드 확인
[입력 필드]


새로운 패스워드를 확인해주세요
```

**케이스 2: 기존 비밀번호 틀림**

```
기존 패스워드
wrongpassword
기존의 비밀번호가 옳지 않습니다.
```

**케이스 3: 새 비밀번호 8자 미만**

```
새로운 패스워드
1234567
8글자 이상 입력해주세요.
```

**케이스 4: 새 비밀번호 불일치**

```
새로운 패스워드
password123

패스워드 확인
password456
비밀번호가 같지 않습니다.
```

**케이스 5: 비밀번호 변경 성공**

입력값:
- 기존 패스워드: password
- 새로운 패스워드: password1234
- 패스워드 확인: password1234

→ `/` 페이지로 리다이렉트 (로그인 상태 유지)

#### MemberService - 비밀번호 관련 메서드

```java
// 비밀번호 확인
public boolean checkPassword(Long id, String password) {
    return passwordEncoder.matches(
            password,
            memberRepository.findById(id).orElseThrow().getPassword()
    );
}

// 비밀번호 변경
public void updatePassword(Long id, String password) {
    Member member = memberRepository.findById(id).orElseThrow();
    member.setPassword(passwordEncoder.encode(password));
    memberRepository.save(member);
}
```

**checkPassword() 메서드**

```java
public boolean checkPassword(Long id, String password) {
    return passwordEncoder.matches(
            password,
            memberRepository.findById(id).orElseThrow().getPassword()
    );
}
```

- `passwordEncoder.matches(평문, 암호화된값)`: BCrypt로 비밀번호를 안전하게 비교합니다.
- DB에서 회원 정보를 조회하여 저장된 암호화된 비밀번호를 가져옵니다.
- 평문 비밀번호를 같은 방식으로 해싱한 후 비교합니다.

**BCrypt 비교 원리**

```
1. DB 저장값: $2a$10$abc123...(salt + hash)
   ↓
2. 사용자 입력: "password"
   ↓
3. BCrypt: 저장값에서 salt 추출
   ↓
4. BCrypt: 입력값 + salt로 해시 계산
   ↓
5. 계산된 해시와 저장된 해시 비교
   ↓
6. 일치 여부 반환 (true/false)
```

**updatePassword() 메서드**

```java
public void updatePassword(Long id, String password) {
    Member member = memberRepository.findById(id).orElseThrow();
    member.setPassword(passwordEncoder.encode(password));
    memberRepository.save(member);
}
```

- 새로운 비밀번호를 **반드시 암호화**하여 저장합니다.
- `passwordEncoder.encode()`: 평문을 BCrypt로 암호화합니다.
- 같은 비밀번호여도 매번 다른 해시값이 생성됩니다. (Salt 때문)

**비밀번호 암호화 예시**

```java
String rawPassword = "password1234";
String encoded1 = passwordEncoder.encode(rawPassword);
String encoded2 = passwordEncoder.encode(rawPassword);

System.out.println(encoded1);
// $2a$10$N1Q2Z3M4a5b6c7d8e9f0g.hash1

System.out.println(encoded2);
// $2a$10$R5S6T7U8v9w0x1y2z3a4b.hash2

// 다른 해시값이지만 둘 다 같은 평문을 나타냅니다.
```

---

### 3.5 폼 처리 패턴 정리

회원가입과 비밀번호 변경은 동일한 패턴을 따릅니다:

#### 공통 처리 흐름

```
┌─────────────────────────────────────────────────────────┐
│ 1. GET 요청 - 폼 표시                                    │
│    @GetMapping                                           │
│    └─ @ModelAttribute로 빈 객체 생성 → Model에 자동 저장 │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 2. 사용자 입력                                           │
│    Thymeleaf (th:object, th:field)                       │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 3. POST 요청 - 폼 제출                                   │
│    @PostMapping                                          │
│    ├─ @Valid: 자동 검증 (@NotBlank, @Email, @Size)      │
│    └─ BindingResult: 검증 결과 수집                      │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 4. 수동 검증                                             │
│    rejectValue()로 비즈니스 로직 검증                     │
│    ├─ 두 필드 비교 (비밀번호 일치)                        │
│    ├─ DB 조회 (이메일 중복, 기존 비밀번호 확인)            │
│    └─ 복잡한 규칙 검증                                    │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 5. 결과 처리                                             │
│    bindingResult.hasErrors() 확인                        │
│    ├─ 오류 있음: 폼 재표시 (이전 입력값 + 오류 메시지)     │
│    └─ 성공: 서비스 호출 → 리다이렉트                       │
└─────────────────────────────────────────────────────────┘
```

#### Thymeleaf 폼 바인딩 패턴

```html
<!-- 1. 폼과 객체 연결 -->
<form th:object="${objectName}" th:action="@{/path}" method="post">
    
    <!-- 2. 각 필드 바인딩 -->
    <input type="text" th:field="*{fieldName}">
    
    <!-- 3. 오류 메시지 표시 -->
    <p th:if="${#fields.hasErrors('fieldName')}" 
       th:errors="*{fieldName}">
    </p>
    
    <button type="submit">제출</button>
</form>
```

**핵심 요소**

| 요소 | 역할 |
|------|------|
| `th:object="${objectName}"` | 폼 전체를 Model의 객체와 연결 |
| `th:field="*{fieldName}"` | 필드 바인딩 (id, name, value 자동 생성) |
| `#fields.hasErrors('fieldName')` | 특정 필드에 오류가 있는지 확인 |
| `th:errors="*{fieldName}"` | 오류 메시지 출력 |

#### 컨트롤러 패턴

```java
@Controller
public class SomeController {
    
    // GET: 폼 표시
    @GetMapping("/path")
    public String getForm(@ModelAttribute("objectName") FormDto formDto) {
        return "form-template";
    }
    
    // POST: 폼 처리
    @PostMapping("/path")
    public String postForm(
            @Valid @ModelAttribute("objectName") FormDto formDto,
            BindingResult bindingResult) {
        
        // 수동 검증
        if (/* 검증 조건 */) {
            bindingResult.rejectValue("fieldName", "errorCode", "message");
        }
        
        // 오류 확인
        if (bindingResult.hasErrors()) {
            return "form-template"; // 폼 재표시
        }
        
        // 비즈니스 로직 처리
        service.process(formDto);
        
        return "redirect:/success-path";
    }
}
```

---

### 3.6 주요 개념 정리

#### 1. @ModelAttribute의 의미

**GET 요청 시**
- 빈 객체를 생성하여 Model에 저장
- 폼의 "청사진" 역할

**POST 요청 시**
- 폼 데이터를 객체로 변환 (데이터 바인딩)
- 검증 후에도 Model에 자동 저장 (오류 발생 시 재표시용)

#### 2. @Valid와 BindingResult

**@Valid**
- Bean Validation 어노테이션 기반 자동 검증
- `@NotBlank`, `@Email`, `@Size` 등

**BindingResult**
- 검증 오류를 수집하는 컨테이너
- `rejectValue()`로 수동 오류 추가 가능
- `hasErrors()`로 오류 여부 확인

**위치 규칙**: BindingResult는 @Valid 대상 바로 다음에 위치해야 합니다.

```java
public String method(
    @Valid @ModelAttribute FormDto dto,  // @Valid 대상
    BindingResult result) {               // 바로 다음
    // ...
}
```

#### 3. th:field의 3가지 역할

```html
<input type="text" th:field="*{name}">
```

생성되는 HTML 속성:

```html
<input type="text" id="name" name="name" value="이전값">
```

| 속성 | 역할 |
|------|------|
| `id="name"` | CSS/JavaScript에서 요소 식별 |
| `name="name"` | 폼 제출 시 서버로 전송되는 키 (데이터 바인딩에 사용) |
| `value="이전값"` | 오류 발생 시 이전 입력값 유지 (UX 개선) |

#### 4. #fields의 역할

```html
<p th:if="${#fields.hasErrors('fieldName')}" 
   th:errors="*{fieldName}"></p>
```

- `#fields`: Thymeleaf 유틸리티 객체
- Model의 `BindingResult`에 접근
- 특정 필드의 오류 여부 확인 및 메시지 출력

#### 5. 오류 메시지 표시 과정

```
1. Controller에서 bindingResult.rejectValue("email", "error", "메시지")
   ↓
2. BindingResult에 FieldError 객체 저장
   ↓
3. Model에 자동 저장 (objectNameBindingResult)
   ↓
4. View 렌더링 시 #fields가 BindingResult 참조
   ↓
5. th:errors로 오류 메시지 출력
```

#### 6. HTTP 무상태와 객체 생명주기

**각 요청마다 새로운 객체 생성**

```
GET /signup  → MemberForm 객체 (ID 1) 생성 → 응답 후 소멸
POST /signup → MemberForm 객체 (ID 2) 생성 → 응답 후 소멸
POST /signup → MemberForm 객체 (ID 3) 생성 → 응답 후 소멸
```

- HTTP는 무상태(Stateless)이므로 서버는 이전 요청을 기억하지 않습니다.
- 매 요청마다 독립적인 객체를 생성하여 처리합니다.
- 오류 발생 시 Model에 담아 View로 전달하여 이전 입력값을 보여줍니다.

#### 7. PasswordEncoder의 중요성

**회원가입/비밀번호 변경 시**

```java
String encoded = passwordEncoder.encode(rawPassword);
member.setPassword(encoded); // 암호화된 값 저장
```

**로그인/비밀번호 확인 시**

```java
boolean matches = passwordEncoder.matches(inputPassword, storedPassword);
```

- 평문 비밀번호를 절대 저장하지 않습니다.
- BCrypt는 같은 평문이어도 매번 다른 해시를 생성합니다. (Salt)
- 복호화는 불가능하며, 비교만 가능합니다.

#### 8. Spring Security 통합

**SecurityContext 활용**

```java
@AuthenticationPrincipal MemberUserDetails userDetails
```

- 현재 로그인한 사용자 정보를 안전하게 주입
- 세션 조작 공격 방어
- 인증된 사용자의 ID를 신뢰할 수 있음

**접근 제어**

```java
.authorizeHttpRequests((auth) -> {
    auth.requestMatchers("/password").authenticated(); // 로그인 필수
})
```

- 비밀번호 변경은 로그인한 사용자만 접근 가능
- Spring Security가 자동으로 로그인 페이지로 리다이렉트

---






