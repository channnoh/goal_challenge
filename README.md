


# Goal Challenge Project(목표 챌린지 서비스)

<br/>

## 📌프로젝트 소개
<br/>
- 원하는 챌린지를 신청하고, 같은 챌린지를 진행하는 유저들과 동기부여를 얻으며, 함께 챌린지를 진행하는 
 서비스를 구축하기 위한 프로젝트입니다.
<br/><br/>
- 회원가입, 로그인, 챌린지 건의 / 등록 / 조회 / 신청 / 시작 / 조회 / 진행 / 종료 API를 제공합니다.  
<br/><br/>


<hr><br>


## ⏱개발 기간
<br>

- **2024.03.23 ~ ?**

<br>


<hr><br>

## 👨‍💻Tech Stack
<br/>


<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/redis-%23DD0031.svg?&style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white"> 
<br><br>


<hr><br>

## ⭐주요 기능
<br>

### [ 회원가입 ]
- [x] 유저, 관리자로 회원 가입 할 수 있다.
- [x] 유저는 회원 탈퇴 할 수 있다.

### [ 로그인 ]
- [x] 로그인 인증은 JWT를 사용한다.

### [ 챌린지 건의 ]
- [X] 유저는 원하는 챌린지를 건의 할 수 있다. (챌린지 명, 챌린지 목적, 챌린지 기간(ex.10일, 100일)을 작성하여 건의한다.)

### [ 챌린지 등록 ]
- [X] 관리자는 유저가 건의한 챌린지를 조회할 수 있다. (등록순)
- [X] 관리자는 유저가 건의한 챌린지를 등록할 수 있다.
- [X] 챌린지 시작일은 관리자가 챌린지를 등록하는 시점에서 일주일 후로 하며, 종료일은 시작일에서 유저가 건의한 기간을 더한 날짜로 한다.
- [X] 챌린지의 신청제한 인원은 항상 10명이라고 가정한다.
- [X] 챌린지를 등록할 때 챌린지의 상태는 모집중으로 한다.


### [ 챌린지 조회 ]
- [ ] 유저는 챌린지 목록을 조회할 수 있다.
  (최신순, 마감임박순, 챌린지 상태순(진행중, 모집중, 실패, 완료))

### [ 챌린지 신청 ]
- [ ] 모든 챌린지는 10명의 인원 제한이 있다. (동시성 이슈 발생 가능성 존재) - Redis Lock 적용
- [ ] 유저는 챌린지의 신청 인원이 차지 않았고, 챌린지 상태가 모집중인 챌린지를 신청할 수 있다.
- [ ] 유저는 최대 5개의 챌린지를 신청할 수 있다.
- [ ] 유저는 챌린지 신청기간이 끝나기 전에 신청한 챌린지를 취소할 수 있다.

### [ 챌린지 시작 ]
- [ ] 신청 기간이 종료되는 시점(챌린지 시작일 하루 전날 자정까지)에 5명 이상 신청했으면 챌린지가 시작된다.
- [ ] 챌린지가 시작될 때 챌린지의 상태는 진행중으로 바뀐다.
- [ ] 챌린지가 시작되면 챌린지를 신청한 유저에게 push알림이 간다. (FCM 이용)

### [ 챌린지 조회 ]
- [ ] 유저는 본인의 챌린지 기록을 날짜순으로 조회할 수 있다.
- [ ] 같은 챌린지를 진행 중인 다른 유저의 챌린지 기록을 조회할 수 있다.(챌린지 기록은 공개된 경우에만 조회가능하다.)
- [ ] 유저는 본인이 참여한 챌린지 목록을 조회할 수 있다.

### [ 챌린지 진행 ]
- [ ] 유저는 매일 챌린지 기록을 등록, 수정, 삭제 할 수 있다.
- [ ] 유저는 챌린지 기록을 공개/비공개 설정할 수 있다.
- [ ] 챌린지 기록에는 이미지 첨부 가능하다. (Multipart, S3 스토리지 서비스 이용)
- [ ] 각각 1회, 2회 챌린지 불참시 알림이 간다.
- [ ] 3회 이상 챌린지를 불참 하면 해당 챌린지에 실패하여 유저의 챌린지 참여 상태가 실패가 된다.

### [ 챌린지 종료 ]
- [ ] 챌린지 신청기간이 끝나는 시점에 신청인원이 5명 미만이면 챌린지상태는 실패가 되며 종료된다.
- [ ] 챌린지에 참여하고 있는 인원이 모두 실패 했을 경우 챌린지의 상태는 실패 상태가 되며 챌린지가 종료된다.
- [ ] 챌린지 종료일자 자정에 챌린지의 상태는 챌린지 완료 상태가 된다.

<br>
<hr><br>

## 📜ERD
<br>

![ERD](/doc/ERD/ERD.png)


<br>
<hr><br>

## 🔥Trouble Shooting
<br>

### [Go to the trouble shooting section](doc/TROUBLE_SHOOTING.md)

<br>
<hr>
