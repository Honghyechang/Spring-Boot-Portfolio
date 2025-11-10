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
    private Long id;
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
        return member.map(this::mapToMemberDto);
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
    // 1. Optional<Member>를 가져옵니다.
    Optional<Member> member = memberRepository.findByEmail(email); 
    
    // 2. Optional의 map 메서드를 사용하여 조건부 변환을 수행합니다.
    return member.map(this::mapToMemberDto); 
    // 반환 타입은 Optional<MemberDto>입니다.
}
```

- 이메일로 회원을 조회합니다. (회원가입 시 중복 체크에 사용)
- `Optional<Member>`를 `Optional<MemberDto>`로 변환하는 스트림 기법:

Optional<Member> 객체가 가진 map() 메서드를 호출합니다. 이 메서드는 member 안에 Member 객체가 존재할 때만 변환 함수를 실행합니다.

이 패턴은 아래 if-else 문과 동일한 결과를 냅니다:
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

# 4. 게시글 조회, 입력, 수정, 삭제 구현

이 단계에서는 게시판의 핵심 기능인 게시글 CRUD(Create, Read, Update, Delete)를 구현합니다. 먼저 게시글 목록을 보여주는 기능부터 시작하여, 레이아웃 시스템과 페이징 처리를 구현합니다.

---

## 4.1 레이아웃 시스템 구축

실제 게시판 애플리케이션에서는 네비게이션바, 푸터 등 모든 페이지에서 공통으로 사용되는 UI 요소가 있습니다. 이를 매 페이지마다 복사-붙여넣기하는 것은 비효율적이며, 유지보수가 어렵습니다. Thymeleaf의 **프래그먼트(Fragment)** 기능을 사용하면 이 문제를 해결할 수 있습니다.

### 레이아웃 패턴의 핵심 아이디어

```
┌─────────────────────────────────────────┐
│ base-layout.html (레이아웃 틀)          │
│ ┌─────────────────────────────────────┐ │
│ │ <head> (공통 CSS, JS)               │ │
│ │ <nav> (네비게이션바)                │ │
│ │ ┌─────────────────────────────────┐ │ │
│ │ │ 여기에 각 페이지의 고유 내용 삽입│ │ │
│ │ │ (content 매개변수)              │ │ │
│ │ └─────────────────────────────────┘ │ │
│ │ <script> (공통 JavaScript)          │ │
│ └─────────────────────────────────────┘ │
└─────────────────────────────────────────┘
```

### base-layout.html - 공통 레이아웃 정의

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" 
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:fragment="layout(content)">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>스프링 부트 게시판</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand bg-dark" data-bs-theme="dark">
    <div class="container">
        <a class="navbar-brand" th:href="@{/article/list}">
            <img th:src="@{/images/spring.svg}" width="30" height="30" 
                 class="d-inline-block align-text-top">
            스프링 부트 게시판
        </a>

        <ul class="navbar-nav">
            <!-- 비로그인 사용자에게만 표시 -->
            <li sec:authorize="isAnonymous()" class="nav-item">
                <a th:href="@{/login}" class="nav-link">로그인</a>
            </li>
            <li sec:authorize="isAnonymous()" class="nav-item">
                <a th:href="@{/signup}" class="nav-link">회원가입</a>
            </li>

            <!-- 관리자에게만 표시 -->
            <li sec:authorize="hasAuthority('ROLE_ADMIN')" class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" role="button" 
                   data-bs-toggle="dropdown" aria-expanded="false">
                    관리
                </a>
                <ul class="dropdown-menu">
                    <li><a th:href="@{/member/list}">회원관리</a></li>
                </ul>
            </li>

            <!-- 로그인한 사용자에게만 표시 -->
            <li sec:authorize="isAuthenticated()" class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" role="button" 
                   data-bs-toggle="dropdown" aria-expanded="false" 
                   sec:authentication="principal.displayName">
                    이름
                </a>
                <ul class="dropdown-menu">
                    <li><a th:href="@{/password}">비밀번호 변경</a></li>
                    <li><a th:href="@{/logout}">로그아웃</a></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>

<!-- 여기에 각 페이지의 고유 내용이 삽입됩니다 -->
<div th:replace="${content}">내용 대체</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
        crossorigin="anonymous"></script>
</body>
</html>
```

#### 핵심 구성 요소 설명

**1. 프래그먼트 정의**

```html
th:fragment="layout(content)"
```

| 요소 | 설명 |
|------|------|
| `th:fragment="layout"` | 이 HTML을 `layout`이라는 이름의 **재사용 가능한 조각**으로 정의합니다. |
| `(content)` | 이 프래그먼트를 사용하는 쪽에서 전달받을 **매개변수**입니다. 각 페이지의 고유 내용이 이 매개변수로 전달됩니다. |

**2. 내용 삽입 영역**

```html
<div th:replace="${content}">내용 대체</div>
```

- `th:replace="${content}"`: 매개변수로 받은 `content`의 내용으로 이 `<div>` 태그 전체를 **교체(replace)**합니다.
- "내용 대체"라는 텍스트는 기본값으로, 실제 렌더링 시에는 전달된 내용으로 완전히 대체됩니다.

**3. Bootstrap Bundle JS 사용**

```html
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
        crossorigin="anonymous"></script>
```

- **중요**: `bootstrap.bundle.min.js`를 사용해야 드롭다운, 모달 등의 동적 기능이 작동합니다.
- `bootstrap.min.js`만 로드하면 Popper.js가 누락되어 드롭다운이 작동하지 않습니다.

**4. Spring Security 통합 태그**

| 태그 | 조건 | 표시 대상 |
|------|------|-----------|
| `sec:authorize="isAnonymous()"` | 비로그인 사용자 | 로그인, 회원가입 링크 |
| `sec:authorize="isAuthenticated()"` | 로그인한 사용자 | 사용자 정보, 비밀번호 변경, 로그아웃 |
| `sec:authorize="hasAuthority('ROLE_ADMIN')"` | 관리자 권한 보유 | 관리 메뉴 (회원관리) |
| `sec:authentication="principal.displayName"` | - | 현재 로그인한 사용자의 이름 표시 |

### 레이아웃 사용 예시 - article-list-test.html

레이아웃을 실제로 사용하는 방법을 테스트 페이지로 확인해봅시다.

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"   
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{/base-layout::layout(  ~{::section}  )}">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<section th:fragment="section">
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
</section>
</body>
</html>
```

#### 레이아웃 사용 방법 분석

**1. 레이아웃 호출 및 내용 전달**

```html
th:replace="~{/base-layout::layout(  ~{::section}  )}"
```

이 구문이 레이아웃 시스템의 핵심입니다. 각 부분을 분해해봅시다:

| 부분 | 의미 |
|------|------|
| `th:replace` | 현재 `<html>` 태그 전체를 다른 프래그먼트로 **교체**하라는 명령입니다. |
| `~{/base-layout::layout}` | `/base-layout.html` 파일의 `layout` 프래그먼트를 사용하겠다는 의미입니다. |
| `(~{::section})` | 현재 파일의 `section` 프래그먼트를 **매개변수로 전달**합니다. |

**2. 프래그먼트 문법 상세**

```
~{프래그먼트 선택자}
```

| 문법 | 의미 | 예시 |
|------|------|------|
| `~{/파일경로::프래그먼트명}` | 다른 파일의 프래그먼트를 참조 | `~{/base-layout::layout}` |
| `~{::프래그먼트명}` | **현재 파일**의 프래그먼트를 참조 | `~{::section}` |

**3. 동작 흐름**

```
1. 브라우저: /article/list-test 요청
   ↓
2. 컨트롤러: "article-list-test" 뷰 이름 반환
   ↓
3. Thymeleaf: article-list-test.html 파일 로드
   ↓
4. th:replace 구문 발견
   ↓
5. base-layout.html의 layout 프래그먼트 로드
   ↓
6. 현재 파일의 <section th:fragment="section"> 내용을 추출
   ↓
7. base-layout의 <div th:replace="${content}"> 위치에 section 내용 삽입
   ↓
8. 최종 HTML 생성: 네비게이션바 + 고유 내용 + JavaScript
   ↓
9. 브라우저로 응답
```

**4. 최종 렌더링 결과 구조**

```html
<!DOCTYPE html>
<html>
<head>
    <!-- base-layout의 공통 CSS -->
    <link href=".../bootstrap.min.css">
</head>
<body>
    <!-- base-layout의 네비게이션바 -->
    <nav class="navbar">...</nav>
    
    <!-- article-list-test의 section 내용이 여기에 삽입됨 -->
    <section>
        <a href="/signup">회원가입</a>
        <h2>로그인 성공 안녕하세요, 홍혜창님! ...</h2>
    </section>
    
    <!-- base-layout의 공통 JavaScript -->
    <script src=".../bootstrap.bundle.min.js"></script>
</body>
</html>
```

#### 레이아웃 패턴의 장점

| 장점 | 설명 |
|------|------|
| **재사용성** | 네비게이션바와 CSS/JS 링크를 한 번만 작성하면 모든 페이지에서 사용 가능합니다. |
| **유지보수성** | 네비게이션바를 수정하면 모든 페이지에 자동으로 반영됩니다. |
| **일관성** | 모든 페이지가 동일한 레이아웃 구조를 가지므로 UI 일관성이 유지됩니다. |
| **분리된 관심사** | 공통 레이아웃과 페이지 고유 내용을 명확하게 분리합니다. |

---

## 4.2 게시글 목록 화면 구현

게시글 목록을 보여주는 기능은 두 단계로 나누어 구현합니다:

1. **1단계**: 모든 게시글을 한 번에 조회하여 보여주기 (단순 List 사용)
2. **2단계**: 페이지네이션을 적용하여 일부 게시글만 조회하기 (Page 사용)

### 1단계: 전체 게시글 목록 조회

#### ArticleController - 기본 목록 조회

```java
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @RequestMapping("/list")
    public String getArticleList(Model model) {
        List<ArticleDto> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "article-list";
    }
}
```

**동작 흐름**

```
1. 사용자: GET /article/list 요청
   ↓
2. Controller: articleService.findAll() 호출
   ↓
3. Service: articleRepository.findAll() 호출
   ↓
4. Repository: SELECT * FROM article (전체 조회)
   ↓
5. Service: Article 엔티티를 ArticleDto로 변환
   ↓
6. Controller: Model에 articles 저장
   ↓
7. View: article-list.html 렌더링
```

#### ArticleService - 전체 조회 메서드

```java
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    // Entity → DTO 변환 메서드
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

    // 전체 게시글 조회
    public List<ArticleDto> findAll() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(i -> mapToArticleDto(i))
                .collect(Collectors.toList());
    }
}
```

**Stream을 이용한 변환**

```java
articles.stream()
    .map(i -> mapToArticleDto(i))
    .collect(Collectors.toList());
```

| 단계 | 메서드 | 역할 |
|------|--------|------|
| 1 | `stream()` | `List<Article>`을 스트림으로 변환합니다. |
| 2 | `map(i -> mapToArticleDto(i))` | 각 `Article` 객체를 `ArticleDto`로 변환합니다. |
| 3 | `collect(Collectors.toList())` | 변환된 요소들을 다시 `List<ArticleDto>`로 수집합니다. |

#### article-list.html (1단계 버전)

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"   
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{/base-layout::layout(~{::section})}">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<section th:fragment="section">
    <table class="table">
        <thead>
            <tr>
                <td>#</td>
                <td>제목</td>
                <td>작성자</td>
                <td>수정날짜</td>
            </tr>
        </thead>

        <tbody>
        <tr th:each="article : ${articles}">
            <td th:text="${article.id}"></td>
            <td>
                <a th:href="@{/article/content (id=${article.id})}" 
                   th:text="${article.title}"></a>
            </td>
            <td th:text="${article.name}"></td>
            <td th:text="${#temporals.format(article.updated,'yyyy-MM-dd HH:mm:ss')}"></td>
        </tr>
        </tbody>
    </table>
</section>
</body>
</html>
```

#### 핵심 Thymeleaf 문법

**1. 반복문 - th:each**

```html
<tr th:each="article : ${articles}">
```

- `${articles}`: Model에 저장된 `List<ArticleDto>`를 참조합니다.
- `article`: 현재 반복 중인 게시글 객체를 담는 변수명입니다.
- 각 반복마다 새로운 `<tr>` 태그가 생성됩니다.

**2. 링크에 파라미터 전달**

```html
<a th:href="@{/article/content (id=${article.id})}" 
   th:text="${article.title}"></a>
```

**URL 생성 원리**

```
th:href="@{/article/content (id=${article.id})}"
         └─────┬─────┘ └────────┬────────┘
          기본 경로      쿼리 파라미터
```

| 구성 요소 | 역할 | 결과 |
|-----------|------|------|
| `@{/article/content}` | 기본 경로 | `/article/content` |
| `(id=${article.id})` | 쿼리 파라미터 | `?id=5` |
| **최종 URL** | - | `/article/content?id=5` |

**파라미터 전달 방식**

Thymeleaf는 소괄호 `()` 안의 내용을 분석합니다:

1. 컨트롤러 매핑에 `{id}` 같은 **Path Variable**이 있는가?
   - 있다면: `/article/content/5` (경로에 포함)
   - 없다면: `/article/content?id=5` (쿼리 스트링으로)

현재 우리 컨트롤러는 `/article/content`로 매핑되어 있으므로, `id`는 쿼리 파라미터로 전달됩니다.

**3. 날짜 포맷팅 - #temporals**

```html
<td th:text="${#temporals.format(article.updated,'yyyy-MM-dd HH:mm:ss')}"></td>
```

**#calendars vs #temporals**

| 유틸리티 | 대상 Java 타입 | 사용 시기 |
|----------|---------------|----------|
| `#calendars` | `java.util.Date`, `java.util.Calendar` | Java 8 이전 레거시 타입 |
| `#temporals` | `java.time.LocalDateTime`, `java.time.ZonedDateTime` | Java 8 이후 모던 타입 |

**처음에 발생했던 오류**

```html
<!-- ❌ 오류 발생 -->
<td th:text="${#calendars.format(article.updated,'yyyy-MM-dd HH:mm:ss')}"></td>
```

**오류 원인**

```
LocalDateTime (Modern) → #calendars (Legacy) → 타입 불일치 오류!
```

- `Article` 엔티티의 `updated` 필드는 `LocalDateTime` 타입입니다.
- `#calendars`는 `Date`/`Calendar` 타입만 처리할 수 있습니다.
- 타입 불일치로 인해 예외가 발생합니다.

**해결 방법**

```html
<!-- ✅ 정상 동작 -->
<td th:text="${#temporals.format(article.updated,'yyyy-MM-dd HH:mm:ss')}"></td>
```

- `LocalDateTime` 타입은 반드시 `#temporals`를 사용해야 합니다.

#### 1단계 방식의 문제점

```java
public List<ArticleDto> findAll() {
    return articleRepository.findAll()  // SELECT * FROM article
            .stream()
            .map(i -> mapToArticleDto(i))
            .collect(Collectors.toList());
}
```

**성능 문제**

| 게시글 수 | 조회 시간 | 메모리 사용 | 네트워크 전송 |
|-----------|----------|-------------|--------------|
| 10개 | 빠름 | 적음 | 적음 |
| 100개 | 보통 | 보통 | 보통 |
| 1,000개 | 느림 | 많음 | 많음 |
| 10,000개 | **매우 느림** | **과도함** | **과도함** |

**문제점 요약**

1. **DB 부하**: 전체 게시글을 한 번에 조회하므로 DB에 부담을 줍니다.
2. **메모리 낭비**: 사용자는 일부만 볼 텐데 모든 데이터를 메모리에 올립니다.
3. **느린 응답**: 네트워크로 대량의 데이터를 전송하므로 응답이 느려집니다.
4. **나쁜 UX**: 사용자는 스크롤을 과도하게 해야 원하는 게시글을 찾을 수 있습니다.

**해결책**: **페이지네이션(Pagination)**을 적용하여 일부 게시글만 조회합니다.

---

### 2단계: 페이지네이션 적용

페이지네이션은 대량의 데이터를 작은 단위(페이지)로 나누어 보여주는 기법입니다.

#### Spring Data JPA의 Pageable

Spring Data JPA는 페이징 처리를 매우 간단하게 만들어주는 **Pageable** 인터페이스를 제공합니다.

**Pageable의 역할**

```
Pageable 객체는 "몇 페이지를, 몇 개씩, 어떤 순서로" 조회할지에 대한 정보를 담습니다.
```

| 속성 | 의미 | 예시 |
|------|------|------|
| `page` | 페이지 번호 (0부터 시작) | `page=2` (3번째 페이지) |
| `size` | 한 페이지당 데이터 개수 | `size=10` (10개씩) |
| `sort` | 정렬 기준 | `sort=id,desc` (ID 내림차순) |

#### ArticleController - 페이지네이션 버전

```java
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @RequestMapping("/list")
    public String getArticleList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) 
            Pageable pageable, 
            Model model) {
        
        Page<ArticleDto> page = articleService.findAll(pageable);
        model.addAttribute("page", page);
        return "article-list";
    }
}
```

**@PageableDefault 상세 분석**

```java
@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
Pageable pageable
```

| 속성 | 값 | 의미 |
|------|-----|------|
| `size` | `10` | 한 페이지당 10개의 게시글을 보여줍니다. |
| `sort` | `"id"` | `id` 필드를 기준으로 정렬합니다. |
| `direction` | `Sort.Direction.DESC` | 내림차순(최신순/큰 번호순)으로 정렬합니다. |

**Pageable 객체 생성 과정**

```
1. 클라이언트 요청: GET /article/list?page=2
   ↓
2. Spring MVC: URL 파라미터 확인
   - page=2 (명시적 요청)
   - size=? (없음 → 기본값 10 사용)
   - sort=? (없음 → 기본값 id,desc 사용)
   ↓
3. PageableHandlerMethodArgumentResolver 작동
   ↓
4. Pageable 객체 생성 및 주입
   - page: 2
   - size: 10
   - sort: id,DESC
   ↓
5. 컨트롤러 메서드 실행
```

**기본값 적용 규칙**

| 파라미터 | URL에 포함 | 사용되는 값 |
|----------|-----------|------------|
| `page` | ❌ | 자동으로 `0` (첫 페이지) |
| `page` | ✅ `?page=3` | 사용자가 요청한 `3` |
| `size` | ❌ | `@PageableDefault`의 `10` |
| `size` | ✅ `?size=20` | 사용자가 요청한 `20` |
| `sort` | ❌ | `@PageableDefault`의 `id,desc` |
| `sort` | ✅ `?sort=title,asc` | 사용자가 요청한 `title,asc` |

**클라이언트가 보내야 할 파라미터**

```html
<!-- 일반적인 페이지네이션 링크 -->
<a th:href="@{/article/list(page=0)}">1페이지</a>
<a th:href="@{/article/list(page=1)}">2페이지</a>
<a th:href="@{/article/list(page=2)}">3페이지</a>
```

- **page만 전달**: 나머지(`size`, `sort`)는 `@PageableDefault`가 처리합니다.
- **간결한 URL**: `/article/list?page=2`

#### ArticleService - 페이지네이션 메서드

```java
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

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

    public Page<ArticleDto> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable).map(i -> mapToArticleDto(i));
    }
}
```

**Page 객체의 구조**

```java
Page<ArticleDto> page = articleRepository.findAll(pageable).map(...);
```

`Page` 객체는 두 가지 정보를 담고 있습니다:

**1. 콘텐츠 정보 (현재 페이지 데이터)**

| 메서드 | 반환 타입 | 설명 |
|--------|----------|------|
| `getContent()` | `List<Article>` | 현재 페이지의 실제 데이터 목록입니다. |
| `getNumberOfElements()`
| `int` | 현재 페이지에 실제로 담긴 데이터 개수입니다. |

**2. 메타 정보 (페이지네이션 정보)**

| 메서드 | 반환 타입 | 설명 |
|--------|----------|------|
| `getTotalElements()` | `long` | 전체 게시글 수 (예: 95개) |
| `getTotalPages()` | `int` | 전체 페이지 수 (예: 10페이지) |
| `getNumber()` | `int` | 현재 페이지 번호 (0부터 시작) |
| `getSize()` | `int` | 한 페이지당 크기 (예: 10개) |
| `isFirst()` | `boolean` | 첫 페이지 여부 |
| `isLast()` | `boolean` | 마지막 페이지 여부 |
| `hasNext()` | `boolean` | 다음 페이지 존재 여부 |
| `hasPrevious()` | `boolean` | 이전 페이지 존재 여부 |
| `isEmpty()` | `boolean` | 데이터가 하나도 없는지 여부 |

**Page.map() 메서드의 동작**

```java
Page<Article> articlePage = articleRepository.findAll(pageable);
Page<ArticleDto> dtoPage = articlePage.map(i -> mapToArticleDto(i));
```

| 단계 | 동작 | 설명 |
|------|------|------|
| 1 | DB 조회 | `Page<Article>` 반환 (데이터 + 메타 정보) |
| 2 | `.map()` 실행 | 내부 콘텐츠만 변환 (메타 정보는 유지) |
| 3 | DTO 변환 | 각 `Article` → `ArticleDto` 변환 |
| 4 | 결과 반환 | `Page<ArticleDto>` (변환된 데이터 + 동일한 메타 정보) |

**예시: 전체 95개 게시글, 3페이지 요청 (페이지당 10개)**

```
Repository 반환:
Page<Article> {
  content: [Article 21, Article 22, ..., Article 30]  // 10개
  totalElements: 95
  totalPages: 10
  number: 2
  size: 10
}
         ↓ .map(i -> mapToArticleDto(i))
Service 반환:
Page<ArticleDto> {
  content: [ArticleDto 21, ArticleDto 22, ..., ArticleDto 30]  // 10개
  totalElements: 95  ← 메타 정보는 그대로
  totalPages: 10
  number: 2
  size: 10
}
```

**Page.map()의 특징**

- `Optional.map()`과 동일한 패턴입니다.
- **메타 정보는 변경하지 않고**, 내부 콘텐츠만 변환합니다.
- 페이징 정보를 유지하면서 Entity → DTO 변환이 가능합니다.

#### article-list.html (2단계 - 페이지네이션 버전)

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"   
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{/base-layout::layout(~{::section})}">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<section th:fragment="section">
    <table class="table">
        <thead>
            <tr>
                <td>#</td>
                <td>제목</td>
                <td>작성자</td>
                <td>수정날짜</td>
            </tr>
        </thead>

        <tbody>
        <tr th:each="article : ${page.content}">
            <td th:text="${article.id}"></td>
            <td>
                <a th:href="@{/article/content (id=${article.id})}" 
                   th:text="${article.title}"></a>
            </td>
            <td th:text="${article.name}"></td>
            <td th:text="${#temporals.format(article.updated,'yyyy-MM-dd HH:mm:ss')}"></td>
        </tr>
        </tbody>
    </table>

    <!-- 페이지네이션 바 -->
    <nav th:if="${!page.isEmpty()}">
        <ul th:with="start=${(page.number div page.size) * page.size},
                     last=${start + (page.size - 1) > (page.totalPages)-1 ? 
                            (page.totalPages)-1 : start + (page.size - 1)}" 
            class="pagination">
            
            <!-- 이전 페이지 버튼 -->
            <li class="page-item" th:classappend="${page.isFirst()} ? 'disabled'">
                <a class="page-link" th:href="@{/article/list(page=${(page.number)-1})}">&laquo;</a>
            </li>

            <!-- 페이지 번호 목록 -->
            <li th:each="pageNumber : ${#numbers.sequence(start, last)}" 
                class="page-item" 
                th:classappend="${page.number == pageNumber} ? 'active'">
                <a th:href="@{/article/list(page=${pageNumber})}" 
                   th:text="${pageNumber + 1}" 
                   class="page-link"></a>
            </li>

            <!-- 다음 페이지 버튼 -->
            <li class="page-item" th:classappend="${page.isLast()} ? 'disabled'">
                <a class="page-link" th:href="@{/article/list(page=${(page.number)+1})}">&raquo;</a>
            </li>
        </ul>
    </nav>

    <!-- 글쓰기 버튼 (로그인한 사용자만) -->
    <a th:href="@{/article/add}" sec:authorize="isAuthenticated()" class="btn btn-primary">
        글쓰기
    </a>
</section>
</body>
</html>
```

#### 페이지네이션 UI 구성 요소

**1. 데이터 테이블**

```html
<tr th:each="article : ${page.content}">
```

- `${page.content}`: `Page` 객체에서 현재 페이지의 데이터 목록을 가져옵니다.
- 이전 버전의 `${articles}` 대신 `${page.content}`를 사용합니다.

**2. 페이지네이션 바 표시 조건**

```html
<nav th:if="${!page.isEmpty()}">
```

- `page.isEmpty()`: 현재 페이지에 데이터가 하나도 없는지 확인합니다.
- `!page.isEmpty()`: 데이터가 하나라도 있을 때만 페이지네이션 바를 표시합니다.
- 게시글이 0개일 때 빈 페이지네이션 바가 표시되는 것을 방지합니다.

**3. th:with - 임시 변수 선언**

```html
<ul th:with="start=${(page.number div page.size) * page.size},
             last=${start + (page.size - 1) > (page.totalPages)-1 ? 
                    (page.totalPages)-1 : start + (page.size - 1)}" 
    class="pagination">
```

**th:with의 역할**

- **임시 지역 변수**를 선언합니다.
- `<ul>` 태그와 그 안의 모든 자식 태그에서 사용할 수 있습니다.
- 복잡한 계산식을 변수에 담아 코드를 간결하게 만듭니다.

**start 변수 계산**

```
start = (page.number div page.size) * page.size
```

`start`는 **현재 페이지 그룹의 첫 번째 페이지 인덱스**를 계산합니다.

| 단계 | 설명 | 예시 (현재 3페이지, 그룹 크기 5) |
|------|------|-----------------------------------|
| `page.number` | 현재 페이지 번호 | `2` (0부터 시작) |
| `div page.size` | 그룹 크기로 나눈 몫 | `2 div 5 = 0` |
| `* page.size` | 몫에 그룹 크기를 곱함 | `0 * 5 = 0` |
| **결과** | 그룹의 시작 인덱스 | `0` (0~4 그룹) |

**div 연산자**

```java
// Thymeleaf의 div는 정수 나눗셈 (몫만 반환)
2 div 5 = 0
7 div 5 = 1
12 div 5 = 2
```

**페이지 그룹 예시** (5개씩 묶을 때)

| 그룹 | 포함 페이지 | start 값 |
|------|------------|----------|
| 1그룹 | 0, 1, 2, 3, 4 | 0 |
| 2그룹 | 5, 6, 7, 8, 9 | 5 |
| 3그룹 | 10, 11, 12, 13, 14 | 10 |

**last 변수 계산**

```
last = start + (page.size - 1) > (page.totalPages)-1 ? 
       (page.totalPages)-1 : start + (page.size - 1)
```

`last`는 **현재 페이지 그룹의 마지막 페이지 인덱스**를 계산합니다.

| 부분 | 역할 |
|------|------|
| `start + (page.size - 1)` | 그룹의 예상 끝 인덱스 (예: 0 + 4 = 4) |
| `(page.totalPages) - 1` | 전체 페이지의 마지막 인덱스 (예: 17페이지 → 16) |
| `조건 ? 참 : 거짓` | 두 값 중 작은 값을 선택 |

**조건부 연산자 동작**

```
조건: start + 4 > totalPages - 1 ?
     (그룹 끝 예상이 전체 끝을 넘는가?)

참일 때: totalPages - 1 반환 (전체 페이지의 마지막)
거짓일 때: start + 4 반환 (그룹의 끝)
```

**예시 1: 전체 17페이지, 0그룹**

```
start = 0
그룹 끝 예상 = 0 + 4 = 4
전체 끝 = 17 - 1 = 16
4 > 16 ? → 거짓
last = 4
```

결과: 0~4 페이지 표시 (5개)

**예시 2: 전체 17페이지, 15번째 페이지 (3그룹)**

```
start = 15
그룹 끝 예상 = 15 + 4 = 19
전체 끝 = 17 - 1 = 16
19 > 16 ? → 참
last = 16
```

결과: 15~16 페이지만 표시 (2개)

**처음 시도했던 코드 (오류 발생)**

```html
<!-- ❌ 오류 코드 -->
<ul th:with="start=${T(java.lang.Math).floor((page.number/page.size))*page.size}, 
             last=${T(java.lang.Math).min((start+(page.size)-1), (page.totalPages)-1)}">
```

**발생했던 오류**

```
EL1031E: Problem locating method min(java.lang.Double, java.lang.Integer)
```

**오류 원인**

| 문제 | 설명 |
|------|------|
| 나눗셈 결과 타입 | `page.number / page.size`는 `Double`을 반환합니다. |
| `Math.min()` 호출 | `min(Double, Integer)`를 찾지 못합니다. |
| 타입 불일치 | SpEL이 적절한 메서드를 찾지 못해 오류가 발생합니다. |

**해결 방법**

```html
<!-- ✅ 해결된 코드 -->
<ul th:with="start=${(page.number div page.size) * page.size},
             last=${start + (page.size - 1) > (page.totalPages)-1 ? 
                    (page.totalPages)-1 : start + (page.size - 1)}">
```

**해결 포인트**

| 변경 사항 | 효과 |
|----------|------|
| `/` → `div` | 정수 나눗셈으로 타입 안전성 확보 |
| `Math.min()` → `? :` | Thymeleaf 내장 조건 연산자 사용 |
| `T(java.lang.Math)` 제거 | 외부 클래스 호출 없이 순수 Thymeleaf 로직으로 해결 |

**4. 이전 페이지 버튼**

```html
<li class="page-item" th:classappend="${page.isFirst()} ? 'disabled'">
    <a class="page-link" th:href="@{/article/list(page=${(page.number)-1})}">&laquo;</a>
</li>
```

**th:classappend 분석**

| 구성 요소 | 역할 |
|----------|------|
| `class="page-item"` | 기본 클래스 (항상 적용) |
| `th:classappend` | 조건에 따라 추가 클래스를 붙입니다 |
| `${page.isFirst()}` | 현재 페이지가 첫 페이지인지 확인 |
| `? 'disabled'` | 첫 페이지면 `disabled` 클래스 추가 |

**동작 결과**

```
첫 페이지(0)일 때:
<li class="page-item disabled">  ← 비활성화
    <a>...</a>
</li>

다른 페이지일 때:
<li class="page-item">  ← 활성화
    <a>...</a>
</li>
```

**링크 URL**

```html
th:href="@{/article/list(page=${(page.number)-1})}"
```

- 현재 페이지 번호에서 1을 뺀 페이지로 이동합니다.
- 예: 3페이지에서 클릭 → `/article/list?page=2`

**5. 페이지 번호 목록**

```html
<li th:each="pageNumber : ${#numbers.sequence(start, last)}" 
    class="page-item" 
    th:classappend="${page.number == pageNumber} ? 'active'">
    <a th:href="@{/article/list(page=${pageNumber})}" 
       th:text="${pageNumber + 1}" 
       class="page-link"></a>
</li>
```

**#numbers.sequence() 사용법**

```
#numbers.sequence(시작, 끝)
```

| 메서드 | 반환 타입 | 설명 |
|--------|----------|------|
| `#numbers.sequence(start, last)` | `List<Integer>` | `start`부터 `last`까지의 정수 목록을 생성합니다 (양 끝 포함) |

**예시**

```java
#numbers.sequence(0, 4)  → [0, 1, 2, 3, 4]
#numbers.sequence(5, 9)  → [5, 6, 7, 8, 9]
#numbers.sequence(15, 16) → [15, 16]
```

**반복문 동작**

```
start = 0, last = 4일 때:

pageNumber = 0 → <li>1</li>
pageNumber = 1 → <li>2</li>
pageNumber = 2 → <li>3</li>
pageNumber = 3 → <li>4</li>
pageNumber = 4 → <li>5</li>
```

**현재 페이지 강조**

```html
th:classappend="${page.number == pageNumber} ? 'active'"
```

- `page.number`: 현재 보고 있는 페이지 번호 (예: 2)
- `pageNumber`: 반복 중인 페이지 번호 (0, 1, 2, 3, 4)
- 일치하면 `active` 클래스를 추가하여 배경색 강조

**화면 표시 vs 서버 전달**

```html
th:text="${pageNumber + 1}"  ← 화면에는 1부터 표시
th:href="@{/article/list(page=${pageNumber})}"  ← 서버에는 0부터 전달
```

| 페이지 인덱스 | 화면 표시 | 서버 파라미터 |
|---------------|----------|--------------|
| 0 | `1` | `page=0` |
| 1 | `2` | `page=1` |
| 2 | `3` | `page=2` |

**6. 다음 페이지 버튼**

```html
<li class="page-item" th:classappend="${page.isLast()} ? 'disabled'">
    <a class="page-link" th:href="@{/article/list(page=${(page.number)+1})}">&raquo;</a>
</li>
```

- `page.isLast()`: 마지막 페이지 여부 확인
- 마지막 페이지면 `disabled` 클래스 추가
- 링크는 현재 페이지 + 1

#### 페이지네이션 전체 동작 흐름

```
1. 사용자: /article/list?page=2 요청
   ↓
2. Controller: Pageable 객체 생성 (page=2, size=10, sort=id,desc)
   ↓
3. Service: articleRepository.findAll(pageable)
   ↓
4. Repository: 
   SELECT * FROM article 
   ORDER BY id DESC 
   LIMIT 10 OFFSET 20
   ↓
5. Repository: Page<Article> 반환 (21~30번 게시글 + 메타 정보)
   ↓
6. Service: Page<ArticleDto>로 변환
   ↓
7. Controller: Model에 page 저장
   ↓
8. View: Thymeleaf 렌더링
   - th:with로 start=0, last=4 계산
   - 0~4 페이지 번호 버튼 생성
   - 현재 페이지(2)에 active 클래스
   ↓
9. 브라우저: 페이지네이션 UI 표시
```

#### 페이지네이션 결과 화면

**게시글 목록 테이블**

```
#  제목              작성자  수정날짜
21 게시글 21         홍혜창  2025-11-08 17:53:35
22 게시글 22         윤서준  2025-11-08 17:53:35
...
30 게시글 30         김우현  2025-11-08 17:53:35
```

**페이지네이션 바**

```
« [1] [2] [3] [4] [5] »
     ↑
   현재 페이지 (active)
```

- `«`: 이전 페이지 (첫 페이지면 비활성화)
- `[1]~[5]`: 페이지 번호 (현재 페이지 강조)
- `»`: 다음 페이지 (마지막 페이지면 비활성화)

---

## 4.3 페이지네이션 핵심 개념 정리

### Pageable 인터페이스

**생성 방법**

| 방법 | 코드 | 사용 시기 |
|------|------|----------|
| 어노테이션 | `@PageableDefault` | 컨트롤러에서 기본값 설정 |
| 수동 생성 | `PageRequest.of(page, size, sort)` | 프로그래밍 방식으로 생성 |
| URL 파라미터 | `?page=2&size=20&sort=title,asc` | 클라이언트가 직접 지정 |

**Pageable 처리 흐름**

```
URL 파라미터 ─┐
             ├─> PageableHandlerMethodArgumentResolver
@PageableDefault─┘         ↓
                    Pageable 객체 생성
                          ↓
                    Controller 메서드 주입
```

### Page 인터페이스

**Page vs List 비교**

| 항목 | List<T> | Page<T> |
|------|---------|---------|
| 데이터 | ✅ 포함 | ✅ 포함 (`getContent()`) |
| 전체 개수 | ❌ 없음 | ✅ `getTotalElements()` |
| 페이지 정보 | ❌ 없음 | ✅ `getTotalPages()`, `getNumber()` 등 |
| 다음/이전 여부 | ❌ 없음 | ✅ `hasNext()`, `hasPrevious()` |

**Page 객체 활용**

```java
Page<ArticleDto> page = ...;

// 데이터 접근
List<ArticleDto> articles = page.getContent();

// 페이지네이션 정보
int totalPages = page.getTotalPages();
long totalElements = page.getTotalElements();
boolean hasNext = page.hasNext();
```

### Thymeleaf 페이지네이션 패턴

**필수 구성 요소**

```html
<!-- 1. 데이터 표시 -->
<tr th:each="item : ${page.content}">
    ...
</tr>

<!-- 2. 페이지 그룹 계산 -->
<ul th:with="start=..., last=...">
    
    <!-- 3. 이전 버튼 -->
    <li th:classappend="${page.isFirst()} ? 'disabled'">
        <a th:href="@{/path(page=${page.number - 1})}">이전</a>
    </li>
    
    <!-- 4. 페이지 번호 -->
    <li th:each="pageNumber : ${#numbers.sequence(start, last)}"
        th:classappend="${page.number == pageNumber} ? 'active'">
        <a th:href="@{/path(page=${pageNumber})}" 
           th:text="${pageNumber + 1}">번호</a>
    </li>
    
    <!-- 5. 다음 버튼 -->
    <li th:classappend="${page.isLast()} ? 'disabled'">
        <a th:href="@{/path(page=${page.number + 1})}">다음</a>
    </li>
</ul>
```

### 성능 비교

**전체 조회 vs 페이지네이션**

| 지표 | 전체 조회 (10,000개) | 페이지네이션 (10개씩) |
|------|---------------------|---------------------|
| DB 조회 시간 | 500ms | 10ms |
| 메모리 사용 | 50MB | 500KB |
| 네트워크 전송 | 5MB | 50KB |
| 응답 속도 | 2초 | 0.1초 |
| UX | 긴 스크롤 필요 | 직관적인 탐색 |

---

## 4.4 학습 포인트 정리

### 새로 배운 개념

1. **Thymeleaf 프래그먼트 시스템**
   - `th:fragment`로 재사용 가능한 레이아웃 정의
   - `th:replace`로 레이아웃 적용 및 내용 전달
   - 매개변수를 통한 동적 콘텐츠 삽입

2. **Pageable과 Page**
   - Spring Data JPA의 페이징 처리 자동화
   - `@PageableDefault`로 기본값 설정
   - `Page.map()`을 통한 Entity → DTO 변환

3. **Thymeleaf 고급 기법**
   - `th:with`로 임시 변수 선언
   - `#numbers.sequence()`로 연속된 숫자 생성
   - `th:classappend`로 조건부 클래스 추가
   - `div` 연산자로 정수 나눗셈

4. **날짜/시간 처리**
   - `#temporals`로 `LocalDateTime` 포맷팅
   - 레거시(`#calendars`) vs 모던(`#temporals`) 구분

### 트러블슈팅 경험

| 문제 | 원인 | 해결 방법 |
|------|------|----------|
| 드롭다운 작동 안 함 | `bootstrap.min.js` 사용 | `bootstrap.bundle.min.js`로 변경 |
| 날짜 포맷 오류 | `#calendars` 사용 | `#temporals`로 변경 |
| `Math.min()` 오류 | 타입 불일치 | 조건부 연산자(`? :`)로 대체 |

### 전체 데이터 흐름

```
[Client] ── GET /article/list?page=2 ──> [Controller]
                                              ↓
                                         Pageable 생성
                                         (page=2, size=10)
                                              ↓
[Client] <── HTML (페이지네이션) ── [View] <── [Service]
                                              ↓
                                         Repository.findAll(pageable)
                                              ↓
                                         [Database]
                                         SELECT ... LIMIT 10 OFFSET 20
```
## 4.5 게시글 상세 조회 구현

게시글 목록에서 제목을 클릭하면 해당 게시글의 상세 내용을 볼 수 있는 기능을 구현합니다.

### 동작 흐름

```
1. 사용자: 게시글 목록에서 제목 클릭
   ↓
2. 브라우저: GET /article/content?id=4 요청
   ↓
3. Controller: id 파라미터 수신
   ↓
4. Service: articleRepository.findById(id) 호출
   ↓
5. Repository: DB에서 Article 엔티티 조회
   ↓
6. Service: Article → ArticleDto 변환
   ↓
7. Controller: Model에 article 저장
   ↓
8. View: article-content.html 렌더링
```

### article-list.html - 상세 페이지 링크

게시글 목록의 제목에 링크를 추가합니다.

```html
<tr th:each="article : ${page.content}">
    <td th:text="${article.id}"></td>
    <td>
        <a th:href="@{/article/content (id=${article.id})}" 
           th:text="${article.title}"></a>
    </td>
    <td th:text="${article.name}"></td>
    <td th:text="${#temporals.format(article.updated,'yyyy-MM-dd HH:mm:ss')}"></td>
</tr>
```

**링크 생성 방식**

```
th:href="@{/article/content (id=${article.id})}"
         └─────┬─────┘ └────────┬────────┘
          기본 경로      쿼리 파라미터
```

| 구성 요소 | 값 (예시) | 결과 URL |
|-----------|----------|----------|
| 기본 경로 | `/article/content` | `/article/content` |
| 쿼리 파라미터 | `id=4` | `?id=4` |
| **최종 URL** | - | `/article/content?id=4` |

> **💡 참고**: Thymeleaf는 소괄호 `()` 안의 파라미터를 쿼리 스트링으로 자동 변환합니다.

### ArticleController - 상세 조회 처리

```java
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @RequestMapping("/content")
    public String getContent(@RequestParam("id") Long id, Model model) {
        ArticleDto articleDto = articleService.findById(id);
        model.addAttribute("article", articleDto);
        return "article-content";
    }
}
```

**핵심 포인트**

| 요소 | 설명 |
|------|------|
| `@RequestParam("id")` | URL의 `?id=4` 쿼리 파라미터를 `Long id` 변수로 받습니다. |
| `articleService.findById(id)` | DB에서 해당 게시글을 조회하여 DTO로 변환합니다. |
| `model.addAttribute("article", articleDto)` | 뷰에서 사용할 데이터를 Model에 저장합니다. |

### ArticleService - findById 메서드

```java
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public ArticleDto findById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow();
        return mapToArticleDto(article);
    }
}
```

**orElseThrow()의 역할**

```java
articleRepository.findById(id).orElseThrow();
```

- `findById()`는 `Optional<Article>`을 반환합니다.
- `orElseThrow()`: 값이 있으면 `Article`을 반환하고, 없으면 예외를 던집니다.
- 존재하지 않는 게시글 ID로 접근 시 `NoSuchElementException`이 발생합니다.

### article-content.html - 상세 화면

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org" 
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{/base-layout::layout(~{::section})}">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<section th:fragment="section">
    <h1>게시판</h1>

    <table class="table">
        <tbody>
        <tr>
            <td style="width: 20%;">#</td>
            <td th:text="${article.id}"></td>
        </tr>

        <tr>
            <td>제목</td>
            <td th:text="${article.title}"></td>
        </tr>

        <tr>
            <td>내용</td>
            <td th:text="${article.description}"></td>
        </tr>

        <tr>
            <td>글쓴이</td>
            <td th:text="${article.name}"></td>
        </tr>

        <tr>
            <td>글쓴일</td>
            <td th:text="${#temporals.format(article.created,'yyyy-MM-dd HH:mm:ss')}"></td>
        </tr>

        <tr>
            <td>수정일</td>
            <td th:text="${#temporals.format(article.updated,'yyyy-MM-dd HH:mm:ss')}"></td>
        </tr>
        </tbody>
    </table>

    <!-- 목록 버튼 (모든 사용자) -->
    <a th:href="@{/article/list}" class="btn btn-info btn-sm">목록</a>

    <!-- 수정/삭제 버튼 (작성자만) -->
    <th:block sec:authorize="isAuthenticated()" 
              th:if="${#authentication.principal.memberId == article.memberId}">
        <a th:href="@{/article/edit(id=${article.id})}" 
           class="btn btn-warning btn-sm">수정</a>
        <a th:href="@{/article/delete(id=${article.id})}" 
           class="btn btn-danger btn-sm">삭제</a>
    </th:block>

</section>
</body>
</html>
```

#### 조건부 버튼 표시 로직

**2단계 검증 방식**

```html
<th:block sec:authorize="isAuthenticated()" 
          th:if="${#authentication.principal.memberId == article.memberId}">
```

| 순서 | 조건 | 검증 내용 |
|------|------|----------|
| **1단계** | `sec:authorize="isAuthenticated()"` | 사용자가 **로그인**했는지 확인합니다. 비로그인 사용자는 이 블록 전체가 렌더링되지 않습니다. |
| **2단계** | `th:if="${#authentication.principal.memberId == article.memberId}"` | 현재 로그인한 사용자의 ID와 게시글 작성자 ID를 비교합니다. 일치할 때만 수정/삭제 버튼을 표시합니다. |

**실행 우선순위**

```
sec:authorize (Spring Security)
      ↓ (통과하면)
th:if (Thymeleaf)
      ↓ (통과하면)
버튼 렌더링
```

- `sec:authorize`가 먼저 실행되어 인증 여부를 확인합니다.
- 인증된 경우에만 `th:if` 조건을 평가합니다.
- 두 조건을 모두 만족해야 수정/삭제 버튼이 표시됩니다.

**#authentication.principal 접근**

| 표현식 | 의미 |
|--------|------|
| `#authentication` | Spring Security의 `Authentication` 객체에 접근합니다. |
| `.principal` | 인증된 사용자 정보를 담은 `UserDetails` 객체입니다. (우리 프로젝트에서는 `MemberUserDetails`) |
| `.memberId` | `MemberUserDetails`의 `memberId` 필드 값입니다. |

#### 화면 표시 결과

**케이스 1: 비로그인 사용자**

```
[목록]
```

- 수정/삭제 버튼이 표시되지 않습니다.

**케이스 2: 다른 사용자가 로그인한 경우**

```
[목록]
```

- `sec:authorize`는 통과하지만, `th:if`에서 ID 불일치로 버튼이 표시되지 않습니다.

**케이스 3: 게시글 작성자가 로그인한 경우**

```
[목록] [수정] [삭제]
```

- 두 조건을 모두 만족하여 수정/삭제 버튼이 표시됩니다.

---

## 4.6 게시글 작성 기능 구현

로그인한 사용자만 새로운 게시글을 작성할 수 있는 기능을 구현합니다.

### 동작 흐름

```
1. 사용자: 게시글 목록에서 "글쓰기" 버튼 클릭
   ↓
2. 브라우저: GET /article/add 요청
   ↓
3. Controller: 빈 ArticleForm 객체 생성 및 Model에 저장
   ↓
4. View: article-add.html 렌더링 (빈 폼 표시)
   ↓
5. 사용자: 제목과 내용 입력 후 "저장" 버튼 클릭
   ↓
6. 브라우저: POST /article/add 요청 (폼 데이터 전송)
   ↓
7. Controller: @Valid로 자동 검증 + 수동 검증 (욕설 필터)
   ↓
8. 검증 실패 → 오류 메시지와 함께 폼 재표시
   검증 성공 → Service 계층 호출
   ↓
9. Service: ArticleForm + MemberUserDetails → Article 엔티티 생성
   ↓
10. Repository: DB에 새 게시글 저장
   ↓
11. Controller: 게시글 목록 페이지로 리다이렉트
```

### article-list.html - 글쓰기 버튼

게시글 목록 하단에 로그인한 사용자만 볼 수 있는 글쓰기 버튼을 추가합니다.

```html
<a th:href="@{/article/add}" 
   sec:authorize="isAuthenticated()" 
   class="btn btn-primary">글쓰기</a>
```

**sec:authorize="isAuthenticated()"**

- Spring Security의 조건부 렌더링 태그입니다.
- 로그인한 사용자에게만 이 링크가 표시됩니다.
- 비로그인 사용자가 HTML 소스를 봐도 이 태그 자체가 존재하지 않습니다.

### ArticleController - 게시글 작성 처리

```java
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    // GET 요청: 빈 폼 표시
    @GetMapping("/add")
    public String getAdd(@ModelAttribute("article") ArticleForm articleForm) {
        return "article-add";
    }

    // POST 요청: 폼 제출 및 저장
    @PostMapping("/add")
    public String add(
            @Valid @ModelAttribute("article") ArticleForm articleForm, 
            BindingResult bindingResult,
            @AuthenticationPrincipal MemberUserDetails memberUserDetails) {
        
        // 수동 검증 1: 제목 욕설 필터
        if (articleForm.getTitle().equals("T발")) {
            bindingResult.rejectValue("title", "SlangDetected", 
                    "욕설을 사용하지 마세요.");
        }

        // 수동 검증 2: 내용 욕설 필터
        if (articleForm.getDescription().equals("T발")) {
            bindingResult.rejectValue("description", "SlangDetected", 
                    "욕설을 사용하지 마세요.");
        }

        // 오류가 있으면 폼으로 다시 돌아가기
        if (bindingResult.hasErrors()) {
            return "article-add";
        }

        // 게시글 생성
        articleService.add(articleForm, memberUserDetails);
        return "redirect:/article/list";
    }
}
```

#### 핵심 포인트 분석

**1. GET 요청 처리**

```java
@GetMapping("/add")
public String getAdd(@ModelAttribute("article") ArticleForm articleForm) {
    return "article-add";
}
```

| 요소 | 역할 |
|------|------|
| `@ModelAttribute("article")` | 빈 `ArticleForm` 객체를 생성하여 `"article"` 이름으로 Model에 자동 저장합니다. |
| `return "article-add"` | `article-add.html` 템플릿을 렌더링합니다. |

- **회원가입 폼과 동일한 패턴**입니다.
- 뷰에서 `th:object="${article}"`로 이 객체를 참조할 수 있습니다.

**2. POST 요청 처리**

```java
@PostMapping("/add")
public String add(
        @Valid @ModelAttribute("article") ArticleForm articleForm, 
        BindingResult bindingResult,
        @AuthenticationPrincipal MemberUserDetails memberUserDetails)
```

**매개변수 상세 설명**

| 매개변수 | 역할 | 중요성 |
|----------|------|--------|
| `@Valid ArticleForm` | 폼 데이터를 받아 자동 검증(`@NotBlank`)을 수행합니다. | Bean Validation |
| `BindingResult` | 검증 오류를 수집하는 컨테이너입니다. | 오류 처리 |
| `@AuthenticationPrincipal MemberUserDetails` | **현재 로그인한 사용자의 정보**를 주입받습니다. | 작성자 식별 |

**@AuthenticationPrincipal의 중요성**

```java
@AuthenticationPrincipal MemberUserDetails memberUserDetails
```

- Spring Security가 `SecurityContext`에서 현재 인증된 사용자 정보를 가져옵니다.
- `memberUserDetails.getMemberId()`로 **작성자의 DB ID**를 안전하게 획득합니다.
- 세션 조작이나 hidden 필드 변조 공격을 방어할 수 있습니다.

**3. 2단계 검증 시스템**

```
1단계: @Valid (자동 검증)
   ↓
@NotBlank 어노테이션 검사
   ↓
2단계: 수동 검증
   ↓
비즈니스 규칙 검사 (욕설 필터)
   ↓
bindingResult.hasErrors() 확인
```

**수동 검증 예시**

```java
if (articleForm.getTitle().equals("T발")) {
    bindingResult.rejectValue("title", "SlangDetected", 
            "욕설을 사용하지 마세요.");
}
```

| 파라미터 | 값 | 의미 |
|----------|-----|------|
| **1번째** | `"title"` | 오류를 표시할 필드명 |
| **2번째** | `"SlangDetected"` | 오류 코드 (메시지 소스 키로 사용 가능) |
| **3번째** | `"욕설을 사용하지 마세요."` | 실제 표시될 오류 메시지 |

> **💡 실무 팁**: 실제 프로젝트에서는 정규표현식이나 외부 욕설 필터 라이브러리를 사용합니다.

### ArticleForm DTO

게시글 작성과 수정에 모두 사용되는 DTO입니다.

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

    private Long id;  // 작성 시: null, 수정 시: 게시글 ID

    @NotBlank(message = "게시글 제목을 입력하세요.")
    private String title;

    @NotBlank(message = "게시글 내용을 입력하세요")
    private String description;
}
```

#### id 필드의 이중 용도

| 사용 케이스 | id 값 | 역할 |
|------------|-------|------|
| **새 글 작성** | `null` | 사용되지 않음. DB가 자동으로 새 ID를 생성합니다. |
| **글 수정** | `숫자 (예: 5)` | 어떤 게시글을 수정할지 식별하는 키입니다. |

**설계 의도**

- 하나의 DTO를 작성(CREATE)과 수정(UPDATE)에 재사용하여 코드 중복을 줄입니다.
- `id`의 값 유무로 현재 작업이 '작성'인지 '수정'인지 판단합니다.

#### Bean Validation 어노테이션 정리

Spring Boot에서 가장 자주 사용되는 JSR-380 (Bean Validation) 어노테이션들입니다.

**1. 기본 제약 조건 (Null, Empty, Blank)**

| 어노테이션 | 허용하지 않는 값 | 적용 타입 | 특징 |
|-----------|----------------|----------|------|
| `@NotNull` | `null` | 모든 타입 | 빈 문자열(`""`)이나 공백(`" "`)은 허용합니다. |
| `@NotEmpty` | `null`, `""`, `size=0` | String, Collection, Array | 공백만 있는 문자열(`" "`)은 허용합니다. |
| `@NotBlank` | `null`, `""`, `"   "` | String 전용 | **가장 엄격한 검증**입니다. 실무에서 가장 많이 사용됩니다. |

**2. 문자열 및 형식 제약 조건**

| 어노테이션 | 속성 | 설명 | 주의사항 |
|-----------|------|------|----------|
| `@Email` | - | 유효한 이메일 형식인지 검증합니다. | `null`이나 빈 문자열은 통과시킵니다. `@NotBlank`와 함께 사용해야 합니다. |
| `@Size` | `min`, `max` | 문자열 길이나 컬렉션 크기를 검증합니다. | `@Size(min=8, max=20, message="8~20자로 입력하세요")` |
| `@Pattern` | `regexp` | 정규 표현식과 일치하는지 검증합니다. | 복잡한 형식 검사에 사용됩니다. |

**3. 숫자 및 값 범위 제약 조건**

| 어노테이션 | 속성 | 설명 |
|-----------|------|------|
| `@Min` | `value` | 지정된 최솟값 이상인지 검증합니다. |
| `@Max` | `value` | 지정된 최댓값 이하인지 검증합니다. |
| `@Positive` | - | 양수(0 초과)인지 검증합니다. |
| `@PositiveOrZero` | - | 양수이거나 0인지 검증합니다. |
| `@Negative` | - | 음수(0 미만)인지 검증합니다. |
| `@NegativeOrZero` | - | 음수이거나 0인지 검증합니다. |

**4. 날짜/시간 제약 조건**

| 어노테이션 | 설명 |
|-----------|------|
| `@Past` | 현재 시점보다 과거인지 검증합니다. |
| `@PastOrPresent` | 현재 시점이거나 과거인지 검증합니다. |
| `@Future` | 현재 시점보다 미래인지 검증합니다. |
| `@FutureOrPresent` | 현재 시점이거나 미래인지 검증합니다. |

**사용 예시**

```java
public class MemberForm {
    @NotBlank(message = "이름을 입력하세요.")
    @Size(min = 2, max = 20, message = "이름은 2~20자로 입력하세요.")
    private String name;

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @Min(value = 18, message = "18세 이상만 가입 가능합니다.")
    private Integer age;
}
```

### article-add.html - 작성 폼

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{/base-layout::layout(~{::section})}">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<section th:fragment="section">
    <h1>게시판</h1>
    
    <form th:object="${article}" th:action="@{/article/add}" method="post">
        <div class="mb-3">
            <label class="form-label">제목</label>
            <input type="text" th:field="*{title}" class="form-control">
            <p th:if="${#fields.hasErrors('title')}" 
               th:errors="*{title}" 
               class="text-danger"></p>
        </div>

        <div class="mb-3">
            <label class="form-label">내용</label>
            <textarea th:field="*{description}" class="form-control"></textarea>
            <p th:if="${#fields.hasErrors('description')}" 
               th:errors="*{description}" 
               class="text-danger"></p>
        </div>

        <button type="submit" class="btn btn-primary">저장</button>
    </form>
</section>
</body>
</html>
```

#### 폼 바인딩 패턴 복습

**1. th:object - 폼과 객체 연결**

```html
<form th:object="${article}" th:action="@{/article/add}" method="post">
```

- `th:object="${article}"`: Model의 `article` 객체를 폼과 연결합니다.
- 이후 `th:field`에서 `*{필드명}` 문법으로 해당 객체의 필드를 참조할 수 있습니다.

**2. th:field - 필드 바인딩**

```html
<input type="text" th:field="*{title}" class="form-control">
```

`th:field="*{title}"`는 다음 세 가지 HTML 속성을 자동으로 생성합니다:

| 생성되는 속성 | 값 | 역할 |
|--------------|-----|------|
| `id` | `title` | JavaScript나 CSS에서 요소 식별 |
| `name` | `title` | 폼 제출 시 서버로 전송되는 파라미터 이름 |
| `value` | `""` (또는 이전 입력값) | 오류 발생 시 이전 입력값 유지 |

**3. th:errors - 오류 메시지 표시**

```html
<p th:if="${#fields.hasErrors('title')}" 
   th:errors="*{title}" 
   class="text-danger"></p>
```

| 구성 요소 | 역할 |
|----------|------|
| `th:if="${#fields.hasErrors('title')}"` | `title` 필드에 오류가 있을 때만 `<p>` 태그를 렌더링합니다. |
| `th:errors="*{title}"` | `BindingResult`에서 `title` 필드의 오류 메시지를 추출하여 표시합니다. |
| `class="text-danger"` | Bootstrap의 빨간색 텍스트 스타일을 적용합니다. |

**오류 표시 예시**

```
제목
[입력 필드]
게시글 제목을 입력하세요.  ← 빨간색 텍스트

내용
[입력 필드]
욕설을 사용하지 마세요.  ← 빨간색 텍스트
```

### ArticleService - add 메서드

```java
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    public ArticleDto add(ArticleForm articleForm, MemberUserDetails memberUserDetails) {
        // 1. 작성자 정보 조회
        Member member = memberRepository.findById(memberUserDetails.getMemberId())
                .orElseThrow();

        // 2. ArticleForm → Article 엔티티 변환
        Article article = Article.builder()
                .title(articleForm.getTitle())
                .description(articleForm.getDescription())
                .member(member)  // 작성자 정보 연결
                .build();

        // 3. DB 저장
        articleRepository.save(article);

        // 4. Article → ArticleDto 변환 후 반환
        return mapToArticleDto(article);
    }
}
```

#### 핵심 처리 과정

**1. 작성자 정보 조회**

```java
Member member = memberRepository.findById(memberUserDetails.getMemberId())
        .orElseThrow();
```

| 단계 | 동작 | 설명 |
|------|------|------|
| 1 | `memberUserDetails.getMemberId()` | 현재 로그인한 사용자의 DB ID를 가져옵니다. |
| 2 | `memberRepository.findById(...)` | DB에서 해당 회원 엔티티를 조회합니다. |
| 3 | `.orElseThrow()` | 회원이 없으면 예외를 던집니다. (실제로는 로그인한 사용자이므로 항상 존재) |

**2. DTO → 엔티티 변환**

```java
Article article = Article.builder()
        .title(articleForm.getTitle())
        .description(articleForm.getDescription())
        .member(member)  // 외래키 관계 설정
        .build();
```

**Builder 패턴의 장점**

| 장점 | 설명 |
|------|------|
| **가독성** | 어떤 필드에 어떤 값이 설정되는지 명확합니다. |
| **불변성** | 모든 필드를 한 번에 설정하여 객체를 생성합니다. |
| **선택적 설정** | 필요한 필드만 설정할 수 있습니다. |

**외래키 관계 설정**

```java
.member(member)
```

- `Article` 엔티티의 `member` 필드는 `@ManyToOne`으로 `Member`와 연관관계를 맺고 있습니다.
- JPA가 이 객체 참조를 `article` 테이블의 `member_id` 외래키로 자동 변환합니다.

**3. DB 저장**

```java
articleRepository.save(article);
```

- JPA가 `INSERT`SQL을 자동으로 생성하여 실행합니다.
- `created`와 `updated` 필드는 `@CreatedDate`, `@LastModifiedDate` 어노테이션으로 자동 설정됩니다.

**실제 실행되는 SQL**

```sql
INSERT INTO article (title, description, member_id, created, updated)
VALUES ('제목', '내용', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
```

**4. 엔티티 → DTO 변환**

```java
return mapToArticleDto(article);
```

- 저장된 `Article` 엔티티를 `ArticleDto`로 변환하여 반환합니다.
- 컨트롤러는 이 DTO를 사용할 수도 있지만, 현재는 리다이렉트하므로 사용되지 않습니다.

### 전체 작성 프로세스 정리

```
┌─────────────────────────────────────────────────────────┐
│ 1. GET /article/add - 폼 초기화                          │
│    @ModelAttribute로 빈 ArticleForm 생성 → Model에 저장  │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 2. 사용자 입력                                           │
│    Thymeleaf (th:object, th:field)로 폼 데이터 입력      │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 3. POST /article/add - 폼 제출                           │
│    ├─ @Valid: @NotBlank 자동 검증                       │
│    └─ BindingResult: 검증 결과 수집                      │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 4. 수동 검증                                             │
│    rejectValue()로 비즈니스 로직 검증 (욕설 필터)         │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 5. 오류 처리                                             │
│    bindingResult.hasErrors() 확인                        │
│    ├─ 오류 있음: "article-add" 반환 (폼 재표시)          │
│    └─ 성공: articleService.add() 호출                    │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 6. Service 로직                                          │
│    ├─ 작성자 정보 조회 (Member)                          │
│    ├─ ArticleForm → Article 엔티티 변환                  │
│    └─ DB 저장 (INSERT)                                   │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ 7. 리다이렉트                                            │
│    "redirect:/article/list" - 게시글 목록으로 이동        │
└─────────────────────────────────────────────────────────┘
```

---

## 4.7 게시글 수정 기능 구현

기존 게시글의 내용을 수정하는 기능을 구현합니다. 작성 기능과 유사하지만, **기존 데이터를 폼에 미리 채워서 보여준다**는 점이 다릅니다.

### 동작 흐름

```
1. 사용자: 게시글 상세 페이지에서 "수정" 버튼 클릭
   ↓
2. 브라우저: GET /article/edit?id=5 요청
   ↓
3. Controller: id로 기존 게시글 조회
   ↓
4. Service: DB에서 Article 조회 → ArticleDto 변환
   ↓
5. Controller: ArticleForm에 기존 데이터 채우기
   ↓
6. View: article-edit.html 렌더링 (기존 내용이 채워진 폼)
   ↓
7. 사용자: 내용 수정 후 "저장" 버튼 클릭
   ↓
8. 브라우저: POST /article/edit 요청 (수정된 데이터 + id)
   ↓
9. Controller: 검증 수행
   ↓
10. Service: id로 기존 Article 조회 → 내용 수정 → 저장
   ↓
11. Controller: 게시글 목록으로 리다이렉트
```

### ArticleForm의 이중 활용

`ArticleForm` DTO는 **작성(CREATE)**과 **수정(UPDATE)** 두 가지 경우에 모두 사용됩니다.

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleForm {
    private Long id;  // ⭐ 핵심 필드
    
    @NotBlank(message = "게시글 제목을 입력하세요.")
    private String title;
    
    @NotBlank(message = "게시글 내용을 입력하세요")
    private String description;
}
```

#### id 필드의 역할 비교

| 작업 | id 값 | 초기화 방법 | 역할 |
|------|-------|------------|------|
| **작성 (CREATE)** | `null` | `@ModelAttribute`가 빈 객체 생성 | 사용되지 않음. DB가 자동으로 새 ID 생성 |
| **수정 (UPDATE)** | `5` (예시) | Controller에서 `articleForm.setId(5)` | **어떤 게시글을 수정할지 식별**하는 키 |

#### 주의사항

> **⚠️ 중요**: 수정 시에는 `ArticleForm`의 `id` 필드가 **반드시 채워져야** 합니다. 이 ID가 없으면 서버는 어떤 게시글을 수정해야 하는지 알 수 없습니다.

### ArticleController - 수정 처리

```java
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    // GET 요청: 기존 데이터를 채운 폼 표시
    @GetMapping("/edit")
    public String getEdit(
            @RequestParam("id") Long id,
            @ModelAttribute("article") ArticleForm articleForm) {
        
        // 1. DB에서 기존 게시글 조회
        ArticleDto articleDto = articleService.findById(id);
        
        // 2. ArticleForm에 기존 데이터 채우기
        articleForm.setTitle(articleDto.getTitle());
        articleForm.setDescription(articleDto.getDescription());
        articleForm.setId(articleDto.getId());  // ⭐ ID도 함께 설정
        
        return "article-edit";
    }

    // POST 요청: 수정된 데이터 저장
    @PostMapping("/edit")
    public String editArticle(
            @Valid @ModelAttribute("article") ArticleForm articleForm,
            BindingResult bindingResult) {
        
        // 수동 검증 1: 제목 욕설 필터
        if (articleForm.getTitle().equals("T발")) {
            bindingResult.rejectValue("title", "SlangDetected", 
                    "욕설을 사용하지 마세요.");
        }

        // 수동 검증 2: 내용 욕설 필터
        if (articleForm.getDescription().equals("T발")) {
            bindingResult.rejectValue("description", "SlangDetected", 
                    "욕설을 사용하지 마세요.");
        }

        // 오류가 있으면 수정 폼으로 다시 돌아가기
        if (bindingResult.hasErrors()) {
            return "article-edit";
        }

        // 게시글 수정
        articleService.update(articleForm);
        return "redirect:/article/list";
    }
}
```

#### 작성 vs 수정 비교

**GET 요청 처리 차이**

| 작업 | 작성 (getAdd) | 수정 (getEdit) |
|------|--------------|---------------|
| **파라미터** | `@ModelAttribute("article")` ArticleForm | `@RequestParam("id")` Long id<br>`@ModelAttribute("article")` ArticleForm |
| **초기화** | Spring이 자동으로 빈 객체 생성 | **개발자가 직접** 기존 데이터로 채움 |
| **id 필드** | `null` | 게시글 ID (예: `5`) |
| **목적** | 빈 폼 제공 | 기존 데이터가 채워진 폼 제공 |

**핵심 차이점**

```java
// 작성 (getAdd)
@GetMapping("/add")
public String getAdd(@ModelAttribute("article") ArticleForm articleForm) {
    // articleForm은 비어있음 (Spring이 자동 생성)
    return "article-add";
}

// 수정 (getEdit)
@GetMapping("/edit")
public String getEdit(
        @RequestParam("id") Long id,
        @ModelAttribute("article") ArticleForm articleForm) {
    
    ArticleDto articleDto = articleService.findById(id);
    
    // ⭐ 직접 데이터를 채워넣음
    articleForm.setTitle(articleDto.getTitle());
    articleForm.setDescription(articleDto.getDescription());
    articleForm.setId(articleDto.getId());  // ID 필수!
    
    return "article-edit";
}
```

**POST 요청 처리는 동일**

- 검증 로직이 완전히 같습니다.
- 차이점은 Service 계층에서 **INSERT vs UPDATE** 작업을 수행한다는 점뿐입니다.

### article-edit.html - 수정 폼

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{/base-layout::layout(~{::section})}">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<section th:fragment="section">
    <h1>게시글 수정</h1>
    
    <form th:object="${article}" th:action="@{/article/edit}" method="post">
        
        <!-- ⭐ 숨겨진 ID 필드 (매우 중요!) -->
        <input type="hidden" th:field="*{id}">

        <div class="mb-3">
            <label class="form-label">제목</label>
            <input type="text" th:field="*{title}" class="form-control">
            <p th:if="${#fields.hasErrors('title')}" 
               th:errors="*{title}" 
               class="text-danger"></p>
        </div>

        <div class="mb-3">
            <label class="form-label">내용</label>
            <textarea th:field="*{description}" class="form-control"></textarea>
            <p th:if="${#fields.hasErrors('description')}" 
               th:errors="*{description}" 
               class="text-danger"></p>
        </div>

        <button type="submit" class="btn btn-primary">저장</button>
    </form>
</section>
</body>
</html>
```

#### 숨겨진 ID 필드의 중요성

```html
<input type="hidden" th:field="*{id}">
```

**왜 필요한가?**

| 이유 | 설명 |
|------|------|
| **게시글 식별** | 서버가 어떤 게시글을 수정해야 하는지 알아야 합니다. |
| **보안** | URL 조작으로 다른 게시글을 수정하는 것을 방지합니다. |
| **데이터 무결성** | 수정 작업의 대상을 명확히 지정합니다. |

**동작 원리**

```
1. GET /article/edit?id=5
   ↓
2. Controller: articleForm.setId(5)
   ↓
3. Thymeleaf: <input type="hidden" name="id" value="5">
   ↓
4. 사용자가 폼 제출
   ↓
5. POST /article/edit
   - title: "수정된 제목"
   - description: "수정된 내용"
   - id: 5  ← 숨겨진 필드로 전송
   ↓
6. Service: articleRepository.findById(5) → 내용 수정
```

**보안 고려사항**

```html
<!-- ❌ 위험: URL 파라미터만 사용 -->
<!-- 사용자가 URL을 /article/edit?id=999로 변조 가능 -->

<!-- ✅ 안전: hidden 필드 + 권한 검증 -->
<input type="hidden" th:field="*{id}">
```

> **💡 실무 팁**: 실제 프로젝트에서는 서버에서 추가로 **수정 권한**을 검증해야 합니다. (현재 사용자가 작성자인지 확인)

**렌더링 결과**

```html
<form action="/article/edit" method="post">
    <input type="hidden" name="id" value="5">
    <input type="hidden" name="_csrf" value="랜덤토큰">
    
    <input type="text" name="title" value="기존 제목">
    <textarea name="description">기존 내용</textarea>
    
    <button type="submit">저장</button>
</form>
```

### ArticleService - update 메서드

```java
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleDto update(ArticleForm articleForm) {
        // 1. 기존 게시글 조회
        Article article = articleRepository.findById(articleForm.getId())
                .orElseThrow();
        
        // 2. 내용 수정 (Dirty Checking)
        article.setTitle(articleForm.getTitle());
        article.setDescription(articleForm.getDescription());
        
        // 3. 변경 사항 저장
        articleRepository.save(article);
        
        // 4. DTO 변환 후 반환
        return mapToArticleDto(article);
    }
}
```

#### JPA의 Dirty Checking

**동작 원리**

```java
Article article = articleRepository.findById(articleForm.getId()).orElseThrow();
// ↑ 영속성 컨텍스트에서 관리되는 엔티티

article.setTitle(articleForm.getTitle());
article.setDescription(articleForm.getDescription());
// ↑ 엔티티 내용 변경

articleRepository.save(article);
// ↑ JPA가 변경된 필드를 감지하여 UPDATE SQL 자동 생성
```

**실제 실행되는 SQL**

```sql
UPDATE article
SET title = '수정된 제목',
    description = '수정된 내용',
    updated = CURRENT_TIMESTAMP
WHERE id = 5;
```

**Dirty Checking의 장점**

| 장점 | 설명 |
|------|------|
| **자동화** | 개발자가 UPDATE SQL을 작성하지 않아도 됩니다. |
| **효율성** | 변경된 필드만 UPDATE 쿼리에 포함됩니다. |
| **일관성** | `@LastModifiedDate`가 자동으로 갱신됩니다. |

#### 수정 시 주의사항

**member 필드는 수정하지 않음**

```java
public ArticleDto update(ArticleForm articleForm) {
    Article article = articleRepository.findById(articleForm.getId()).orElseThrow();
    
    article.setTitle(articleForm.getTitle());
    article.setDescription(articleForm.getDescription());
    // article.setMember(...) ← 작성자는 변경하지 않음!
    
    articleRepository.save(article);
    return mapToArticleDto(article);
}
```

| 필드 | 수정 여부 | 이유 |
|------|----------|------|
| `title` | ✅ 수정 | 사용자가 제목을 변경할 수 있습니다. |
| `description` | ✅ 수정 | 사용자가 내용을 변경할 수 있습니다. |
| `member` | ❌ 유지 | 작성자는 변경할 수 없습니다. (데이터 무결성) |
| `created` | ❌ 유지 | 최초 작성 시간은 불변입니다. |
| `updated` | ✅ 자동 갱신 | `@LastModifiedDate`가 자동으로 현재 시간으로 설정됩니다. |

---

## 4.8 게시글 삭제 기능 구현

게시글을 삭제하는 기능을 구현합니다. 가장 간단한 기능이지만, 권한 검증이 중요합니다.

### 동작 흐름

```
1. 사용자: 게시글 상세 페이지에서 "삭제" 버튼 클릭
   ↓
2. 브라우저: GET /article/delete?id=5 요청
   ↓
3. Controller: id 파라미터 수신
   ↓
4. Service: articleRepository.deleteById(id) 호출
   ↓
5. Repository: DB에서 해당 게시글 삭제
   ↓
6. Controller: 게시글 목록으로 리다이렉트
```

### ArticleController - 삭제 처리

```java
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        articleService.delete(id);
        return "redirect:/article/list";
    }
}
```

**간단한 구조**

- 별도의 확인 페이지 없이 바로 삭제합니다.
- 실제 삭제 로직은 Service 계층에 위임합니다.
- 삭제 후 게시글 목록으로 리다이렉트합니다.

### ArticleService - delete 메서드

```java
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }
}
```

**Spring Data JPA의 deleteById()**

```java
articleRepository.deleteById(id);
```

- `JpaRepository`가 제공하는 기본 메서드입니다.
- 내부적으로 다음 두 단계를 수행합니다:
  1. `findById(id)` - 엔티티 조회
  2. `remove(entity)` - 엔티티 삭제

**실제 실행되는 SQL**

```sql
DELETE FROM article WHERE id = 5;
```

### 삭제 vs 회원 삭제의 차이

**게시글 삭제**

```java
public void delete(Long id) {
    articleRepository.deleteById(id);
}
```

- 단순히 게시글만 삭제합니다.
- 연관된 엔티티가 없으므로 바로 삭제 가능합니다.

**회원 삭제 (나중에 구현할 내용)**

```java
@Transactional
public void delete(Long id) {
    Member member = memberRepository.findById(id).orElseThrow();
    
    // ⭐ 회원이 작성한 게시글을 먼저 삭제
    articleRepository.deleteAllByMember(member);
    
    // 그 다음 회원 삭제
    memberRepository.deleteById(id);
}
```

| 작업 | 연관 데이터 처리 | 트랜잭션 필요 |
|------|----------------|--------------|
| **게시글 삭제** | 연관 데이터 없음 | 불필요 |
| **회원 삭제** | 회원이 작성한 게시글 먼저 삭제 | **필수** |

> **⚠️ 주의**: 외래키 제약조건 때문에 회원을 삭제하기 전에 해당 회원이 작성한 모든 게시글을 먼저 삭제해야 합니다.

### 실무 고려사항

**1. Soft Delete (논리적 삭제)**

실무에서는 데이터를 실제로 삭제하지 않고 **삭제 플래그**를 설정하는 경우가 많습니다.

```java
@Entity
public class Article {
    // ...
    
    private Boolean deleted = false;  // 삭제 여부
    private LocalDateTime deletedAt;  // 삭제 시간
}

// Service
public void delete(Long id) {
    Article article = articleRepository.findById(id).orElseThrow();
    article.setDeleted(true);
    article.setDeletedAt(LocalDateTime.now());
    articleRepository.save(article);
}
```

**2. 권한 검증**

현재 코드는 URL만 알면 누구나 삭제할 수 있습니다. 실무에서는 추가 검증이 필요합니다.

```java
@GetMapping("/delete")
public String delete(
        @RequestParam("id") Long id,
        @AuthenticationPrincipal MemberUserDetails userDetails) {
    
    // 작성자 확인
    ArticleDto article = articleService.findById(id);
    if (!article.getMemberId().equals(userDetails.getMemberId())) {
        throw new AccessDeniedException("삭제 권한이 없습니다.");
    }
    
    articleService.delete(id);
    return "redirect:/article/list";
}
```

**3. 확인 대화상자**

사용자 실수를 방지하기 위해 JavaScript 확인 대화상자를 추가할 수 있습니다.

```html
<a th:href="@{/article/delete(id=${article.id})}" 
   class="btn btn-danger btn-sm"
   onclick="return confirm('정말 삭제하시겠습니까?')">삭제</a>
```

---

## 4.9 게시글 기능 전체 흐름 정리

### CRUD 작업별 특징 비교

| 작업 | HTTP 메서드 | URL 패턴 | ArticleForm.id | 주요 특징 |
|------|------------|---------|---------------|----------|
| **Create** | GET/POST | `/article/add` | `null` | 빈 폼 제공, 새 레코드 삽입 |
| **Read** | GET | `/article/content?id=5` | - | 조회만 수행, DTO 반환 |
| **Update** | GET/POST | `/article/edit?id=5` | `5` | **기존 데이터로 폼 채움**, 레코드 수정 |
| **Delete** | GET | `/article/delete?id=5` | - | 레코드 삭제 후 리다이렉트 |

### 공통 패턴

**1. 폼 처리 패턴 (Create, Update)**

```
GET 요청
   ↓
ArticleForm 객체 준비 (빈 객체 OR 기존 데이터)
   ↓
View 렌더링 (article-add.html OR article-edit.html)
   ↓
POST 요청
   ↓
@Valid 자동 검증
   ↓
수동 검증 (rejectValue)
   ↓
bindingResult.hasErrors() 확인
   ├─ 오류: 폼 재표시
   └─ 성공: Service 호출 → DB 작업 → 리다이렉트
```

**2. 조회/삭제 패턴 (Read, Delete)**

```
GET 요청
   ↓
@RequestParam으로 id 수신
   ↓
Service 호출
   ├─ Read: ArticleDto 반환 → View에 전달
   └─ Delete: deleteById() → 리다이렉트
```

### 핵심 학습 포인트

**1. DTO의 이중 용도**

```java
public class ArticleForm {
    private Long id;  // 작성: null, 수정: 게시글 ID
    // ...
}
```

- 하나의 DTO를 CREATE와 UPDATE에 재사용하여 코드 중복을 줄입니다.
- `id` 필드의 값 유무로 작업 유형을 구분합니다.

**2. 검증 시스템**

```java
@Valid ArticleForm  // 1단계: Bean Validation
   ↓
@NotBlank 검사
   ↓
수동 검증  // 2단계: 비즈니스 규칙
   ↓
rejectValue("field", "code", "message")
   ↓
bindingResult.hasErrors()
```

**3. JPA의 자동화 기능**

| 기능 | 어노테이션 | 효과 |
|------|-----------|------|
| **자동 ID 생성** | `@GeneratedValue` | INSERT 시 DB가 자동으로 ID 할당 |
| **생성 시간** | `@CreatedDate` | 엔티티 생성 시 자동으로 현재 시간 설정 |
| **수정 시간** | `@LastModifiedDate` | 엔티티 수정 시 자동으로 현재 시간 갱신 |
| **Dirty Checking** | - | 변경된 필드만 UPDATE 쿼리에 포함 |

**4. Thymeleaf 폼 바인딩**

```html
<form th:object="${article}">
    <input th:field="*{title}">  <!-- id, name, value 자동 생성 -->
    <p th:errors="*{title}">     <!-- 오류 메시지 표시 -->
</form>
```

---

## 5. 회원 관리 화면 구현

이 장에서는 **관리자 전용 기능**인 회원 관리 화면을 구현합니다. 관리자는 모든 회원의 목록을 조회하고, 회원 정보를 수정하거나 삭제할 수 있습니다.

### 기능 개요

```
관리자 기능
├── 회원 목록 조회 (페이지네이션)
├── 회원 정보 수정 (이름만 수정 가능)
└── 회원 삭제 (작성한 게시글도 함께 삭제)
```

### 접근 권한

**base-layout.html의 권한 제어**

```html
<!-- 관리자에게만 표시 -->
<li sec:authorize="hasAuthority('ROLE_ADMIN')" class="nav-item dropdown">
    <a class="nav-link dropdown-toggle" role="button" 
       data-bs-toggle="dropdown" aria-expanded="false">
        관리
    </a>
    <ul class="dropdown-menu">
        <li>
            <a th:href="@{/member/list}">회원관리</a>
        </li>
    </ul>
</li>
```

**hasAuthority('ROLE_ADMIN')**

- Spring Security의 권한 검증 표현식입니다.
- `ROLE_ADMIN` 권한을 가진 사용자에게만 메뉴가 표시됩니다.
- 일반 사용자(`ROLE_USER`)는 이 메뉴를 볼 수 없습니다.

**권한 확인 방법**

```sql
-- data.sql에서 설정한 관리자 권한
INSERT INTO authority(authority, member_id) 
VALUES('ROLE_ADMIN', 2);  -- 윤서준에게 관리자 권한 부여
```

- `authority` 테이블에 `ROLE_ADMIN` 레코드가 있는 회원만 관리 메뉴에 접근할 수 있습니다.

---## 5.1 회원 목록 조회 구현

관리자가 모든 회원의 목록을 페이지네이션 방식으로 조회할 수 있는 기능을 구현합니다.

### 동작 흐름

```
1. 관리자: 네비게이션바에서 "회원관리" 클릭
   ↓
2. 브라우저: GET /member/list 요청
   ↓
3. Controller: Pageable 객체 생성 (page, size, sort)
   ↓
4. Service: memberRepository.findAll(pageable) 호출
   ↓
5. Repository: DB에서 페이지 단위로 회원 조회
   ↓
6. Service: Page<Member> → Page<MemberDto> 변환
   ↓
7. Controller: Model에 page 저장
   ↓
8. View: member-list.html 렌더링
```

### MemberController - 회원 목록 조회

```java
package com.example.Spring.Board.Project.controller;

import com.example.Spring.Board.Project.dto.MemberDto;
import com.example.Spring.Board.Project.dto.MemberForm;
import com.example.Spring.Board.Project.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
    public String memberList(
            Model model,
            @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) 
            Pageable pageable) {
        
        Page<MemberDto> page = memberService.findAll(pageable);
        model.addAttribute("page", page);
        return "member-list";
    }
}
```

#### 핵심 포인트

**@RequestMapping("/member")**

```java
@Controller
@RequestMapping("/member")
public class MemberController {
```

- 클래스 레벨에 `@RequestMapping`을 선언하면 모든 메서드의 경로 앞에 `/member`가 자동으로 붙습니다.
- `@GetMapping("/list")` → 실제 경로: `/member/list`
- 관리자 전용 기능을 하나의 컨트롤러로 묶어 관리합니다.

**페이지네이션 설정**

```java
@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC)
Pageable pageable
```

| 속성 | 값 | 의미 |
|------|-----|------|
| `size` | `2` | 한 페이지당 **2명**의 회원을 표시합니다. (테스트용 작은 값) |
| `sort` | `"id"` | 회원 ID를 기준으로 정렬합니다. |
| `direction` | `DESC` | 내림차순 (최근 가입한 회원이 먼저) |

> **💡 실무 팁**: `size=2`는 테스트용입니다. 실제 프로젝트에서는 `size=10` 또는 `size=20`을 많이 사용합니다.

### MemberService - 페이지네이션 조회

```java
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Page<MemberDto> findAll(Pageable pageable) {
        Page<Member> member = memberRepository.findAll(pageable);
        return member.map(i -> mapToMemberDto(i));
    }
}
```

**Page.map()의 재사용**

- `ArticleService`의 `findAll()`과 동일한 패턴입니다.
- `Page<Member>`를 `Page<MemberDto>`로 변환하되, 페이징 메타 정보는 유지합니다.

### member-list.html - 회원 목록 화면

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{/base-layout::layout(~{::section})}">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<section th:fragment="section">
    <h1>관리자 페이지</h1>
    
    <table class="table">
        <thead>
        <tr>
            <td>#</td>
            <td>이름</td>
            <td>이메일</td>
            <td></td>
        </tr>
        </thead>
        <tbody>
        <tr th:each="member : ${page.content}">
            <td th:text="${member.id}">#</td>
            <td th:text="${member.name}">이름</td>
            <td th:text="${member.email}">이메일</td>
            <td>
                <a th:href="@{/member/edit(id=${member.id})}" 
                   class="btn btn-warning btn-sm">수정</a>
                <a th:href="@{/member/delete(id=${member.id})}" 
                   class="btn btn-danger btn-sm">삭제</a>
            </td>
        </tr>
        </tbody>
    </table>

    <!-- 페이지네이션 바 -->
    <nav th:if="${!page.isEmpty()}">
        <ul class="pagination" 
            th:with="groupPage=2,
                     start=${(page.number div groupPage) * groupPage}, 
                     last=${start + groupPage - 1 > page.totalPages ? 
                            page.totalPages : start + groupPage - 1}">

            <!-- 이전 페이지 버튼 -->
            <li th:classappend="${page.first} ? 'disabled'" class="page-item">
                <a th:href="@{/member/list(page=${(page.number)-1})}" 
                   class="page-link">&laquo;</a>
            </li>

            <!-- 페이지 번호 목록 -->
            <li th:each="number : ${#numbers.sequence(start, last)}" 
                th:classappend="${page.number == number} ? 'active'" 
                class="page-item">
                <a th:href="@{/member/list(page=${number})}" 
                   th:text="${number + 1}" 
                   class="page-link"></a>
            </li>

            <!-- 다음 페이지 버튼 -->
            <li th:classappend="${page.last} ? 'disabled'" class="page-item">
                <a th:href="@{/member/list(page=${(page.number)+1})}" 
                   class="page-link">&raquo;</a>
            </li>
        </ul>
    </nav>
</section>
</body>
</html>
```

#### 페이지네이션 그룹 크기 변경

**groupPage=2 설정**

```html
th:with="groupPage=2,
         start=${(page.number div groupPage) * groupPage}, 
         last=${start + groupPage - 1 > page.totalPages ? 
                page.totalPages : start + groupPage - 1}"
```

| 설정 | 값 | 의미 |
|------|-----|------|
| `groupPage` | `2` | 페이지 번호를 **2개씩** 묶어서 표시합니다. |
| `start` 계산 | `(page.number div 2) * 2` | 현재 그룹의 시작 페이지 인덱스 |
| `last` 계산 | `start + 1` (또는 전체 마지막 페이지) | 현재 그룹의 끝 페이지 인덱스 |

**페이지 그룹 예시** (groupPage=2, size=2일 때)

| 그룹 | 포함 페이지 | 표시 번호 | start | last |
|------|------------|----------|-------|------|
| 1그룹 | 0, 1 | [1] [2] | 0 | 1 |
| 2그룹 | 2, 3 | [3] [4] | 2 | 3 |
| 3그룹 | 4, 5 | [5] [6] | 4 | 5 |

**article-list vs member-list 비교**

| 항목 | article-list | member-list |
|------|-------------|-------------|
| 페이지 크기 | `size=10` | `size=2` |
| 그룹 크기 | `page.size` (5개) | `groupPage=2` (2개) |
| 용도 | 게시글 목록 (데이터 많음) | 회원 목록 (테스트/관리) |

#### 수정/삭제 버튼

```html
<td>
    <a th:href="@{/member/edit(id=${member.id})}" 
       class="btn btn-warning btn-sm">수정</a>
    <a th:href="@{/member/delete(id=${member.id})}" 
       class="btn btn-danger btn-sm">삭제</a>
</td>
```

**게시글 vs 회원 관리 차이**

| 기능 | 게시글 | 회원 |
|------|--------|------|
| **수정/삭제 권한** | 작성자 본인만 | 관리자만 |
| **조건부 렌더링** | `sec:authorize + th:if` (작성자 확인) | 없음 (관리자 페이지 자체가 접근 제한) |
| **보안** | URL + SecurityContext | URL 경로 제한 (`/member/**`) |

---

## 5.2 회원 정보 수정 구현

관리자가 회원의 정보(이름)를 수정할 수 있는 기능을 구현합니다. **이메일은 로그인 아이디로 사용되므로 수정할 수 없습니다.**

### 동작 흐름

```
1. 관리자: 회원 목록에서 "수정" 버튼 클릭
   ↓
2. 브라우저: GET /member/edit?id=3 요청
   ↓
3. Controller: id로 회원 정보 조회
   ↓
4. Service: memberRepository.findById(id) → MemberDto
   ↓
5. Controller: MemberForm에 기존 데이터 채우기
   ↓
6. View: member-edit.html 렌더링 (기존 정보가 채워진 폼)
   ↓
7. 관리자: 이름 수정 후 "수정" 버튼 클릭
   ↓
8. 브라우저: POST /member/edit 요청
   ↓
9. Controller: @Valid 검증
   ↓
10. Service: id로 회원 조회 → 이름 수정 → 저장
   ↓
11. Controller: 회원 목록으로 리다이렉트
```

### MemberForm의 재사용

```java
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberForm {
    private Long id;  // ⭐ 회원 수정 시 사용
    
    @NotBlank(message="이름을 입력하세요.")
    private String name;
    
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    private String password;
    private String passwordConfirm;
}
```

#### MemberForm의 3가지 용도

| 사용 케이스 | id | password | 사용 필드 |
|------------|-----|----------|----------|
| **회원가입** | `null` | 필수 | name, email, password, passwordConfirm |
| **비밀번호 변경** | 로그인 사용자 | 필수 (PasswordForm 사용) | - |
| **관리자 수정** | 회원 ID | 불필요 | **id, name, email** |

### MemberController - 회원 수정 처리

```java
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    // GET 요청: 기존 회원 정보를 채운 폼 표시
    @GetMapping("/edit")
    public String getEdit(
            Model model,
            @ModelAttribute("member") MemberForm memberForm,
            @RequestParam("id") Long id) {
        
        // 1. DB에서 회원 정보 조회
        MemberDto memberDto = memberService.findById(id);
        
        // 2. MemberForm에 기존 데이터 채우기
        memberForm.setName(memberDto.getName());
        memberForm.setEmail(memberDto.getEmail());
        memberForm.setId(memberDto.getId());  // ⭐ ID 설정
        
        return "member-edit";
    }

    // POST 요청: 수정된 회원 정보 저장
    @PostMapping("/edit")
    public String memberEdit(
            @Valid @ModelAttribute("member") MemberForm memberForm,
            BindingResult bindingResult) {
        
        log.info("memberEdit post method called");

        // 검증 오류가 있으면 수정 폼으로 다시 돌아가기
        if (bindingResult.hasErrors()) {
            return "member-edit";
        }
        
        // 회원 정보 수정
        memberService.update(memberForm);
        return "redirect:/member/list";
    }
}
```

#### 핵심 포인트

**GET 요청 처리**

```java
@GetMapping("/edit")
public String getEdit(
        Model model,
        @ModelAttribute("member") MemberForm memberForm,
        @RequestParam("id") Long id)
```

| 매개변수 | 역할 |
|---------|------|
| `Model model` | 명시적으로 선언했지만 `@ModelAttribute`가 자동으로 Model에 추가하므로 실제로는 불필요합니다. |
| `@ModelAttribute("member")` | 빈 `MemberForm` 객체를 생성하여 Model에 저장합니다. |
| `@RequestParam("id")` | URL의 `?id=3` 파라미터를 받습니다. |

**POST 요청 처리**

```java
@PostMapping("/edit")
public String memberEdit(
        @Valid @ModelAttribute("member") MemberForm memberForm,
        BindingResult bindingResult)
```

- 게시글 수정과 동일한 패턴입니다.
- 추가 수동 검증이 없으므로 `@Valid`의 결과만 확인합니다.

### member-edit.html - 회원 수정 폼

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{/base-layout::layout(~{::section})}">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<section th:fragment="section">
    <h1>회원 정보 수정하기</h1>

    <form th:object="${member}" th:action="@{/member/edit}" method="post">
        <!-- 숨겨진 ID 필드 -->
        <input type="hidden" th:field="*{id}">
        
        <!-- 이름 (수정 가능) -->
        <div class="mb-3">
            <label class="form-label">이름</label>
            <input class="form-control" type="text" th:field="*{name}">
            <p th:if="${#fields.hasErrors('name')}" 
               th:errors="*{name}" 
               class="text-danger"></p>
        </div>

        <!-- 이메일 (읽기 전용) -->
        <div class="mb-3">
            <label class="form-label">이메일</label>
            <input type="text" 
                   th:field="*{email}" 
                   class="form-control-plaintext" 
                   readonly>
            <p th:if="${#fields.hasErrors('email')}" 
               th:errors="*{email}" 
               class="text-danger"></p>
        </div>

        <button type="submit" class="btn btn-primary">수정</button>
    </form>
</section>
</body>
</html>
```

#### 읽기 전용 필드 처리

**class="form-control-plaintext" + readonly**

```html
<input type="text" 
       th:field="*{email}" 
       class="form-control-plaintext" 
       readonly>
```

| 속성/클래스 | 역할 | 효과 |
|-----------|------|------|
| `readonly` | HTML 속성 | 사용자가 값을 수정할 수 없습니다. |
| `form-control-plaintext` | Bootstrap 클래스 | 일반 텍스트처럼 보이도록 스타일링합니다. (테두리 없음) |

**readonly vs disabled 비교**

| 속성 | 폼 제출 시 | 사용 시기 |
|------|-----------|----------|
| `readonly` | ✅ 값이 전송됨 | 표시는 하되 수정만 막고 싶을 때 |
| `disabled` | ❌ 값이 전송되지 않음 | 아예 사용하지 않는 필드일 때 |

**이메일 읽기 전용의 이유**

| 이유 | 설명 |
|------|------|
| **로그인 아이디** | 이메일이 변경되면 사용자가 로그인할 수 없습니다. |
| **데이터 무결성** | 이메일은 회원 식별자로 사용되므로 변경하면 안 됩니다. |
| **보안** | 이메일 변경은 별도의 인증 절차가 필요합니다. |

> **💡 실무 팁**: 이메일 변경 기능을 제공하려면 본인 인증(SMS, 이메일 인증 등)을 추가로 구현해야 합니다.

### MemberService - update 메서드

```java
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberDto update(MemberForm memberForm) {
        // 1. 기존 회원 조회
        Member member = memberRepository.findById(memberForm.getId())
                .orElseThrow();
        
        // 2. 정보 수정 (이름과 이메일만)
        member.setEmail(memberForm.getEmail());  // 실제로는 변경되지 않음 (readonly)
        member.setName(memberForm.getName());
        
        // 3. 변경 사항 저장
        memberRepository.save(member);
        
        // 4. DTO 변환 후 반환
        return mapToMemberDto(member);
    }
}
```

#### 주의사항

**비밀번호는 수정하지 않음**

```java
public MemberDto update(MemberForm memberForm) {
    Member member = memberRepository.findById(memberForm.getId()).orElseThrow();
    
    member.setEmail(memberForm.getEmail());
    member.setName(memberForm.getName());
    // member.setPassword(...) ← 비밀번호는 변경하지 않음!
    
    memberRepository.save(member);
    return mapToMemberDto(member);
}
```

| 필드 | 수정 여부 | 이유 |
|------|----------|------|
| `name` | ✅ 수정 | 관리자가 회원 이름을 변경할 수 있습니다. |
| `email` | ✅ 전송되지만 실제 변경 없음 | `readonly`로 수정 불가, 로그인 아이디 유지 |
| `password` | ❌ 유지 | 비밀번호 변경은 별도 기능으로 처리 (본인만 가능) |

---

## 5.3 회원 삭제 구현

관리자가 회원을 삭제하는 기능을 구현합니다. **회원 삭제 시 해당 회원이 작성한 모든 게시글도 함께 삭제해야 합니다.**

### 동작 흐름

```
1. 관리자: 회원 목록에서 "삭제" 버튼 클릭
   ↓
2. 브라우저: GET /member/delete?id=3 요청
   ↓
3. Controller: id 파라미터 수신
   ↓
4. Service: 
   a) 회원 조회
   b) 해당 회원이 작성한 게시글 전체 삭제
   c) 회원 삭제
   ↓
5. Repository: 
   - articleRepository.deleteAllByMember(member)
   - memberRepository.deleteById(id)
   ↓
6. Controller: 회원 목록으로 리다이렉트
```

### MemberController - 회원 삭제 처리

```java
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/delete")
    public String memberDelete(@RequestParam("id") Long id) {
        memberService.delete(id);
        return "redirect:/member/list";
    }
}
```

- 게시글 삭제와 동일하게 간단한 구조입니다.
- 복잡한 로직은 Service 계층에서 처리합니다.

### MemberService - delete 메서드

```java
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public void delete(Long id) {
        // 1. 회원 조회
        Member member = memberRepository.findById(id).orElseThrow();
        
        // 2. 회원이 작성한 게시글 전체 삭제
        articleRepository.deleteAllByMember(member);
        
        // 3. 회원 삭제
        memberRepository.deleteById(id);
    }
}
```

#### 핵심 포인트

**1. 삭제 순서의 중요성**

```java
// ✅ 올바른 순서
articleRepository.deleteAllByMember(member);  // 1. 게시글 먼저 삭제
memberRepository.deleteById(id);              // 2. 회원 삭제

// ❌ 잘못된 순서
memberRepository.deleteById(id);              // 회원 삭제 시도
articleRepository.deleteAllByMember(member);  // 외래키 제약 조건 위반!
```

**외래키 제약 조건**

```sql
CREATE TABLE article(
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    -- ...
    member_id INTEGER,
    FOREIGN KEY (member_id) REFERENCES member(id)
);
```

| 시나리오 | 결과 |
|---------|------|
| 회원을 먼저 삭제 | ❌ `article` 테이블의 `member_id`가 참조하는 레코드가 없어져 **외래키 제약 조건 위반** 오류 발생 |
| 게시글을 먼저 삭제 | ✅ 참조하는 레코드가 없으므로 회원 삭제 가능 |

**2. @Transactional의 필수성**

```java
@Transactional
public void delete(Long id) {
    Member member = memberRepository.findById(id).orElseThrow();
    articleRepository.deleteAllByMember(member);  // ← 1단계
    memberRepository.deleteById(id);              // ← 2단계
}
```

**트랜잭션의 역할**

| 상황 | @Transactional 없음 | @Transactional 있음 |
|------|-------------------|-------------------|
| **1단계 성공, 2단계 실패** | 게시글만 삭제되고 회원은 남음 (데이터 불일치) | 전체 롤백 → 아무 것도 삭제되지 않음 |
| **1단계 실패** | 즉시 오류 발생 | 트랜잭션 롤백 |

**트랜잭션 동작 예시**

```
시작: @Transactional
   ↓
1. articleRepository.deleteAllByMember(member)
   - DELETE FROM article WHERE member_id = 3 (성공)
   ↓
2. memberRepository.deleteById(id)
   - DELETE FROM member WHERE id = 3 (성공)
   ↓
커밋: 모든 변경사항을 DB에 영구 반영

---

만약 2단계에서 오류 발생:
   ↓
롤백: 1단계의 게시글 삭제도 취소
   ↓
DB 상태는 메서드 호출 전과 동일
```

> **💡 실무 원칙**: 여러 DB 작업을 하나의 논리적 단위로 묶을 때는 반드시 `@Transactional`을 사용해야 합니다.

**3. deleteAllByMember 커스텀 메서드**

```java
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Transactional
    void deleteAllByMember(Member member);
}
```

**Spring Data JPA 메서드 이름 규칙**

| 메서드명 | 생성되는 쿼리 |
|---------|-------------|
| `deleteAllByMember` | `DELETE FROM article WHERE member_id = ?` |
| `findByMember` | `SELECT * FROM article WHERE member_id = ?` |
| `countByMember` | `SELECT COUNT(*) FROM article WHERE member_id = ?` |

**메서드 이름 분해**

```
deleteAllByMember
   ↓       ↓
delete  Member
모두 삭제  조건: member 필드
```

**실제 실행되는 SQL**

```sql
-- 1. 게시글 삭제
DELETE FROM article WHERE member_id = 3;

-- 2. 회원 삭제
DELETE FROM member WHERE id = 3;
```

### 게시글 삭제 vs 회원 삭제 비교

| 항목 | 게시글 삭제 | 회원 삭제 |
|------|----------|----------|
| **연관 데이터** | 없음 | 작성한 게시글 |
| **삭제 순서** | 단일 테이블 | 1) 게시글 삭제 → 2) 회원 삭제 |
| **트랜잭션** | 불필요 (단일 작업) | **필수** (2단계 작업) |
| **외래키 고려** | 없음 | 외래키 제약 조건 고려 필수 |

---

## 5.4 관리자 기능 전체 흐름 정리

### 권한 기반 접근 제어

**네비게이션바 (base-layout.html)**

```html
<li sec:authorize="hasAuthority('ROLE_ADMIN')" class="nav-item dropdown">
    <a class="nav-link dropdown-toggle">관리</a>
    <ul class="dropdown-menu">
        <li><a th:href="@{/member/list}">회원관리</a></li>
    </ul>
</li>
```

**Security 설정 (SecurityConfiguration.java)**

```java
http
    .authorizeHttpRequests((auth) -> {
        auth.requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")
            // ...
    });
```

| 계층 | 접근 제어 방법 | 효과 |
|------|---------------|------|
| **UI 계층** | `sec:authorize="hasAuthority('ROLE_ADMIN')"` | 관리자에게만 메뉴 표시 |
| **서버 계층** | `.requestMatchers("/member/**").hasAuthority("ROLE_ADMIN")` | 관리자가 아니면 403 Forbidden 오류 |

### 회원 관리 CRUD 정리

| 작업 | URL | 트랜잭션 | 특이사항 |

| 작업 | URL | 트랜잭션 | 특이사항 |
|------|-----|---------|----------|
| **Read (목록)** | GET `/member/list` | 불필요 | 페이지네이션 (size=2, groupPage=2) |
| **Update** | GET/POST `/member/edit?id=3` | 불필요 | 이메일은 읽기 전용, 이름만 수정 |
| **Delete** | GET `/member/delete?id=3` | **필수** | 게시글 먼저 삭제 → 회원 삭제 |

### 핵심 학습 포인트

**1. 외래키 제약 조건과 삭제 순서**

```java
@Transactional
public void delete(Long id) {
    Member member = memberRepository.findById(id).orElseThrow();
    
    // ⭐ 순서가 매우 중요!
    articleRepository.deleteAllByMember(member);  // 1. 자식 먼저
    memberRepository.deleteById(id);              // 2. 부모 나중
}
```

**데이터베이스 관계**

```
member (부모)
   ↑
   │ FOREIGN KEY
   │
article (자식)
```

- 부모 테이블을 삭제하기 전에 자식 테이블의 참조를 먼저 제거해야 합니다.
- 이 순서를 지키지 않으면 `Constraint Violation` 오류가 발생합니다.

**2. @Transactional의 ACID 보장**

```java
@Transactional
public void delete(Long id) {
    // A: Atomicity (원자성)
    // - 모든 작업이 성공하거나, 모두 실패합니다.
    
    articleRepository.deleteAllByMember(member);  // 작업 1
    memberRepository.deleteById(id);              // 작업 2
    
    // C: Consistency (일관성)
    // - 외래키 제약 조건 등 DB 규칙을 항상 만족합니다.
    
    // I: Isolation (격리성)
    // - 다른 트랜잭션의 영향을 받지 않습니다.
    
    // D: Durability (지속성)
    // - 커밋된 데이터는 영구적으로 저장됩니다.
}
```

**트랜잭션 실패 시나리오**

| 시점 | @Transactional 없음 | @Transactional 있음 |
|------|-------------------|-------------------|
| 게시글 10개 삭제 성공 | ✅ DB에 즉시 반영 | ⏳ 메모리에만 저장 |
| 11번째 게시글 삭제 실패 | ❌ 10개는 삭제됨, 회원은 남음 | ✅ 전체 롤백, 아무 것도 삭제 안 됨 |

**3. Spring Data JPA 메서드 이름 규칙**

```java
void deleteAllByMember(Member member);
```

**규칙 분해**

| 부분 | 의미 | 설명 |
|------|------|------|
| `deleteAll` | DELETE 작업 | 조건에 맞는 모든 레코드를 삭제합니다. |
| `By` | WHERE 절 시작 | 뒤에 나오는 조건으로 필터링합니다. |
| `Member` | 필드명 | `Article` 엔티티의 `member` 필드를 의미합니다. |
| `(Member member)` | 매개변수 | WHERE 조건에 사용할 값입니다. |

**자주 사용되는 패턴**

| 메서드 패턴 | 생성되는 쿼리 | 예시 |
|-----------|-------------|------|
| `findBy필드` | SELECT ... WHERE 필드 = ? | `findByEmail(String email)` |
| `deleteBy필드` | DELETE ... WHERE 필드 = ? | `deleteByMember(Member member)` |
| `countBy필드` | SELECT COUNT(*) WHERE 필드 = ? | `countByMember(Member member)` |
| `existsBy필드` | SELECT EXISTS(SELECT 1 WHERE 필드 = ?) | `existsByEmail(String email)` |

**4. 읽기 전용 필드의 구현**

```html
<input type="text" 
       th:field="*{email}" 
       class="form-control-plaintext" 
       readonly>
```

| 속성/클래스 | 브라우저 동작 | 폼 제출 | 스타일 |
|-----------|-------------|--------|--------|
| `readonly` | 수정 불가 | ✅ 값 전송됨 | 기본 입력 필드 |
| `form-control-plaintext` | - | - | 일반 텍스트처럼 표시 |

**실무에서의 활용**

```html
<!-- 1. 주요 식별자 (이메일, 아이디 등) -->
<input type="text" th:field="*{email}" readonly>

<!-- 2. 계산된 값 (총합, 평균 등) -->
<input type="text" th:value="${totalAmount}" readonly>

<!-- 3. 시스템 생성 값 (생성일, 수정일 등) -->
<input type="text" 
       th:value="${#temporals.format(member.created, 'yyyy-MM-dd')}" 
       readonly>
```

---

## 5.5 프로젝트 최종 구조

### 디렉토리 구조

```
src/main/java/com/example/Spring/Board/Project/
├── config/
│   └── SecurityConfiguration.java         (Spring Security 설정)
├── controller/
│   ├── ArticleController.java             (게시글 관련 요청 처리)
│   ├── HomeController.java                (로그인, 회원가입, 비밀번호 변경)
│   └── MemberController.java              (관리자용 회원 관리)
├── dto/
│   ├── ArticleDto.java                    (게시글 응답 DTO)
│   ├── ArticleForm.java                   (게시글 요청 DTO)
│   ├── MemberDto.java                     (회원 응답 DTO)
│   ├── MemberForm.java                    (회원 요청 DTO)
│   └── PasswordForm.java                  (비밀번호 변경 DTO)
├── model/
│   ├── Article.java                       (게시글 엔티티)
│   ├── Authority.java                     (권한 엔티티)
│   ├── Member.java                        (회원 엔티티)
│   └── MemberUserDetails.java             (Spring Security UserDetails 구현)
├── repository/
│   ├── ArticleRepository.java             (게시글 데이터 접근)
│   ├── AuthorityRepository.java           (권한 데이터 접근)
│   └── MemberRepository.java              (회원 데이터 접근)
├── service/
│   ├── ArticleService.java                (게시글 비즈니스 로직)
│   └── MemberService.java                 (회원 비즈니스 로직)
└── SpringBoardProjectApplication.java     (메인 클래스)

src/main/resources/
├── templates/
│   ├── article-add.html                   (게시글 작성 폼)
│   ├── article-content.html               (게시글 상세)
│   ├── article-edit.html                  (게시글 수정 폼)
│   ├── article-list.html                  (게시글 목록)
│   ├── article-list-test.html             (테스트 페이지)
│   ├── base-layout.html                   (공통 레이아웃)
│   ├── login.html                         (로그인 폼)
│   ├── logout.html                        (로그아웃 확인)
│   ├── member-edit.html                   (회원 수정 폼)
│   ├── member-list.html                   (회원 목록)
│   ├── password.html                      (비밀번호 변경 폼)
│   └── signup.html                        (회원가입 폼)
├── static/
│   └── images/
│       └── spring.svg                     (로고 이미지)
├── application.properties                 (애플리케이션 설정)
├── data.sql                               (초기 데이터)
└── schema.sql                             (테이블 스키마)
```

### 계층별 역할 정리

**1. Controller 계층**

| 클래스 | 경로 | 역할 |
|--------|------|------|
| `HomeController` | `/`, `/login`, `/signup`, `/password`, `/logout` | 메인 페이지, 인증 관련 처리 |
| `ArticleController` | `/article/**` | 게시글 CRUD 처리 |
| `MemberController` | `/member/**` | 관리자 전용 회원 관리 |

**2. Service 계층**

| 클래스 | 주요 메서드 | 책임 |
|--------|-----------|------|
| `MemberService` | `create()`, `update()`, `delete()`, `checkPassword()` | 회원 관련 비즈니스 로직, Entity ↔ DTO 변환 |
| `ArticleService` | `add()`, `update()`, `delete()`, `findAll(Pageable)` | 게시글 관련 비즈니스 로직, Entity ↔ DTO 변환 |

**3. Repository 계층**

| 인터페이스 | 커스텀 메서드 | 역할 |
|-----------|-------------|------|
| `MemberRepository` | `findByEmail()` | 회원 데이터 접근 |
| `ArticleRepository` | `deleteAllByMember()` | 게시글 데이터 접근 |
| `AuthorityRepository` | `findByMember()` | 권한 데이터 접근 |

**4. Model 계층**

| 클래스 | 관계 | 설명 |
|--------|------|------|
| `Member` | 1:N → `Authority`, 1:N → `Article` | 회원 정보 |
| `Authority` | N:1 → `Member` | 회원의 권한 (ROLE_USER, ROLE_ADMIN) |
| `Article` | N:1 → `Member` | 게시글 (작성자 연결) |
| `MemberUserDetails` | - | Spring Security 통합용 |

### 주요 URL 매핑 정리

**인증 및 회원 관리**

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/` | 메인 페이지 (→ `/article/list`) | 모두 |
| GET | `/login` | 로그인 폼 | 비로그인 |
| POST | `/login` | 로그인 처리 (Spring Security) | 비로그인 |
| GET | `/signup` | 회원가입 폼 | 비로그인 |
| POST | `/signup` | 회원가입 처리 | 비로그인 |
| GET | `/password` | 비밀번호 변경 폼 | 로그인 |
| POST | `/password` | 비밀번호 변경 처리 | 로그인 |
| POST | `/logout` | 로그아웃 처리 (Spring Security) | 로그인 |

**게시글 관리**

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/article/list` | 게시글 목록 (페이징) | 모두 |
| GET | `/article/content?id=5` | 게시글 상세 | 모두 |
| GET | `/article/add` | 게시글 작성 폼 | 로그인 |
| POST | `/article/add` | 게시글 작성 처리 | 로그인 |
| GET | `/article/edit?id=5` | 게시글 수정 폼 | 작성자 |
| POST | `/article/edit` | 게시글 수정 처리 | 작성자 |
| GET | `/article/delete?id=5` | 게시글 삭제 | 작성자 |

**관리자 기능**

| 메서드 | URL | 설명 | 권한 |
|--------|-----|------|------|
| GET | `/member/list` | 회원 목록 (페이징) | 관리자 |
| GET | `/member/edit?id=3` | 회원 수정 폼 | 관리자 |
| POST | `/member/edit` | 회원 수정 처리 | 관리자 |
| GET | `/member/delete?id=3` | 회원 삭제 | 관리자 |

### 데이터베이스 스키마 요약

```
member (회원)
  ├── id (PK)
  ├── name
  ├── email (로그인 아이디)
  └── password (BCrypt 암호화)

authority (권한)
  ├── id (PK)
  ├── authority (ROLE_ADMIN, ROLE_USER)
  └── member_id (FK → member.id)

article (게시글)
  ├── id (PK)
  ├── title
  ├── description
  ├── created (생성 시간)
  ├── updated (수정 시간)
  └── member_id (FK → member.id)
```

**관계 구조**

```
member (1) ──────< (N) authority
   │
   │
   └──────< (N) article

---

## 6. 최종 애플리케이션 결과

### 구현된 기능 요약

**1. 회원 관리**

```
✅ 회원가입 (이메일 중복 체크, 비밀번호 일치 검증)
✅ 로그인 (Spring Security 기반 인증)
✅ 로그아웃 (세션 무효화)
✅ 비밀번호 변경 (기존 비밀번호 확인)
✅ 회원 목록 조회 (관리자 전용, 페이징)
✅ 회원 정보 수정 (관리자 전용)
✅ 회원 삭제 (관리자 전용, 게시글 연계 삭제)
```

**2. 게시글 관리**

```
✅ 게시글 목록 조회 (페이지네이션, 최신순)
✅ 게시글 상세 조회
✅ 게시글 작성 (로그인 필수, 작성자 자동 연결)
✅ 게시글 수정 (작성자 본인만)
✅ 게시글 삭제 (작성자 본인만)
✅ 욕설 필터링 (제목, 내용)
```

**3. 보안 및 권한**

```
✅ BCrypt 비밀번호 암호화
✅ CSRF 토큰 보호
✅ 세션 기반 인증
✅ 역할 기반 접근 제어 (ROLE_USER, ROLE_ADMIN)
✅ 작성자 권한 확인 (수정/삭제 버튼 조건부 표시)
```

### 프로젝트 실행 및 테스트

**1. 애플리케이션 실행**

```bash
./gradlew bootRun
```

접속 주소: `http://localhost:8080`

**2. 테스트 계정**

| 이름 | 이메일 | 비밀번호 | 권한 |
|------|--------|---------|------|
| 홍혜창 | HyechangHong@spring.ac.kr | password | - |
| 윤서준 | SeojunYoon@spring.ac.kr | password | ROLE_ADMIN |
| 김우현 | WoohyunKim@spring.ac.kr | password | - |
| 손흥민 | Sonny@spring.ac.kr | password | - |

**3. 기능 테스트 시나리오**

```
시나리오 1: 일반 사용자
1. 홍혜창 계정으로 로그인
2. 게시글 목록 확인
3. 새 게시글 작성
4. 자신의 게시글 수정 및 삭제
5. 다른 사용자 게시글은 수정/삭제 버튼 미표시 확인
6. 비밀번호 변경

시나리오 2: 관리자
1. 윤서준 계정으로 로그인
2. 네비게이션바에 "관리" 메뉴 표시 확인
3. 회원 목록 조회
4. 회원 정보 수정 (이름 변경)
5. 회원 삭제 (해당 회원의 게시글도 함께 삭제 확인)
```

### 학습 성과

**1. Spring Boot 핵심 기술 체득**

| 기술 | 학습 내용 |
|------|----------|
| **Spring Data JPA** | 엔티티 설계, Repository 메서드, 페이지네이션, JPQL |
| **Spring MVC** | Controller-Service-Repository 패턴, RESTful API, 예외 처리 |
| **Spring Security** | 인증/인가, 권한 제어, BCrypt 암호화, UserDetailsService |
| **Thymeleaf** | 템플릿 엔진, 프래그먼트, 조건부 렌더링, 폼 바인딩 |
| **Bean Validation** | `@Valid`, `@NotBlank`, `BindingResult`, 커스텀 검증 |

**2. 실무 패턴 및 원칙**

```
✅ 계층 분리 (Controller → Service → Repository)
✅ DTO 패턴 (Entity 직접 노출 방지)
✅ 트랜잭션 관리 (@Transactional)
✅ 외래키 제약 조건 고려
✅ 권한 기반 접근 제어 (RBAC)
✅ 입력값 검증 (클라이언트 + 서버)
✅ 비밀번호 암호화 (평문 저장 금지)
```

**3. 문제 해결 경험**

| 문제 | 해결 방법 | 학습 포인트 |
|------|----------|------------|
| 드롭다운 메뉴 작동 안 함 | `bootstrap.bundle.min.js` 사용 | JavaScript 라이브러리 의존성 |
| 날짜 포맷 오류 | `#calendars` → `#temporals` | Java 8 Time API |
| `Math.min()` 타입 오류 | 조건부 연산자 `? :` 사용 | SpEL 타입 안전성 |
| 회원 삭제 시 외래키 오류 | 게시글 먼저 삭제 | 외래키 제약 조건 순서 |

---

## 7. 마무리

### 프로젝트 회고

이 프로젝트를 통해 다음과 같은 실무 역량을 체득했습니다:

**1. 풀스택 웹 애플리케이션 개발**
- 데이터베이스 설계부터 프론트엔드 화면까지 전 과정 경험
- MVC 패턴 기반의 체계적인 코드 구조화

**2. Spring 생태계 통합 활용**
- Spring Boot, Spring Data JPA, Spring Security의 유기적 결합
- 각 기술의 강점을 살린 효율적인 개발

**3. 실무 중심 설계 원칙**
- Entity와 DTO의 분리를 통한 계층 간 독립성 확보
- 트랜잭션과 외래키를 고려한 안전한 데이터 처리
- 권한 기반 접근 제어를 통한 보안 강화

---


