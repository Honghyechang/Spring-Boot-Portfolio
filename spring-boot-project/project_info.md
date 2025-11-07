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









