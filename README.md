# StarCoffee

**스타벅스 App** 을 클론한 프로젝트 입니다.

MSA 아키텍처 컨셉을 차용하여 하나의 애플리케이션에 각각의 Service 는 독립적으로 구현되어 있으며 비즈니스 로직은 WebClient 를 통해서 소통하고 있습니다.


### 기술스택
- Java11
- Spring boot 2.7 (Gradle)
- MyBatis
- MySQL
- Redis
- Kafka


### 프로젝트 구조
- 준비중


### 프로젝트 목표
- MSA 아키텍처 컨셉을 고려한 구현
- 분산 트랜잭션을 위한 분산락 구현
- 대용량 트래픽 처리를 고려한 서비스 구현
- 성능을 고려한 단위/통합 테스트 학습
- CI/CD를 통한 배포 경험과 모니터링 구축


### 기술적 이슈 및 고도화 (문서작업 준비중)
- UUID
- AOP를 이용한 로직 분리
- Redission을 활용한 분산 락
- 푸시 알림 메세지 기능
- DB 실행계획 분석을 통한 SQL 성능 튜닝
- 고가용성을 위한 DB replication
- ngrinder 성능 테스트 
- 로드밸런싱
- CI/CD 환경구성 : Jenkins


### DB Diagram
- 준비중 
