# 백엔드 기초: 제목 검증과 자동 테스트

Java 21, Gradle 8.14.6, JUnit 5로 만드는 작은 연습 프로젝트입니다.
Spring, 데이터베이스, HTTP 서버는 사용하지 않습니다. 입력 검증과 테스트에 집중합니다.

## 준비와 실행

JDK 21이 필요합니다. 터미널에서 `java -version`과 `javac -version`을 확인하세요.
프로젝트 폴더에서 실행합니다. Gradle을 따로 설치하지 않아도 됩니다.

Windows PowerShell:

```powershell
.\gradlew.bat test
```

Windows 명령 프롬프트(cmd):

```bat
gradlew.bat test
```

macOS/Linux (JDK 21 필요):

```sh
sh gradlew test
```

첫 실행은 Gradle과 JUnit을 내려받으므로 인터넷이 필요합니다.
성공하면 `BUILD SUCCESSFUL`이 표시됩니다.
결과 보고서는 `build/reports/tests/test/index.html`에 생성됩니다.
다시 테스트를 실행하려면 `.\gradlew.bat test --rerun-tasks`를 사용하세요.

## 규칙

`new TaskTitleValidator().validate(title)`은 다음과 같이 동작합니다.

- `null`, 빈 문자열 `""`, 공백뿐인 문자열은 `IllegalArgumentException`을 던집니다.
- 정상 제목은 통과합니다.
- 앞뒤 공백은 허용하지만, 반환값은 `strip()`으로 앞뒤 공백을 제거한 문자열입니다.
- 제목 중간의 공백은 유지합니다. 예: `"  자바 공부하기  "` → `"자바 공부하기"`.
- `isBlank()`와 `strip()`은 Java의 `Character.isWhitespace` 기준을 사용합니다. 모든 특수 공백 문자가 포함되는 것은 아닙니다.

## 코드 읽기

1. `String title`: 메서드로 들어오는 입력값입니다.
2. `title == null`: 문자열 자체가 없는지 확인합니다. `null`은 빈 문자열과 다릅니다.
3. `||`: 둘 중 하나만 참이어도 참입니다. 왼쪽이 참이면 오른쪽은 실행하지 않습니다.
   그래서 `null`에 `isBlank()`를 호출하는 오류를 피합니다.
4. `title.isBlank()`: 빈 문자열 또는 공백뿐인 문자열인지 확인합니다.
5. `throw new IllegalArgumentException(...)`: 잘못된 입력이라는 신호를 보내고 정상 반환을 중단합니다.
6. `return title.strip()`: 앞뒤 공백을 제거한 문자열을 돌려줍니다. 원래 문자열은 수정하지 않습니다.

## 테스트 읽기

`@Test`는 JUnit이 실행할 테스트 메서드라는 표시입니다.
각 테스트는 입력 하나와 기대하는 결과를 확인합니다.

- `assertEquals(기대값, 실제값)`: 두 값이 같으면 통과합니다.
  예: `assertEquals("자바 공부하기", validator.validate("자바 공부하기"))`.
  두 번째 인자는 실제 메서드 실행 결과입니다.
- `assertThrows(예외타입.class, 실행할 코드)`: 지정한 예외가 발생해야 통과합니다.
  예: `assertThrows(IllegalArgumentException.class, () -> validator.validate(""))`.
  `() -> ...`는 JUnit이 실행하도록 코드를 전달하는 람다입니다.
  예외가 없거나 다른 종류의 예외가 발생하면 실패합니다.

테스트 5개: 정상 제목, 빈 문자열, 공백만 있는 문자열, `null`, 앞뒤 공백 제거.

## 파일과 도구의 역할

- `src/main/java/.../TaskTitleValidator.java`: 실제 검증 규칙.
- `src/test/java/.../TaskTitleValidatorTest.java`: 규칙을 확인하는 테스트.
- `build.gradle`: Java 21 설정, JUnit 의존성, 테스트 실행 방식.
- `settings.gradle`: 프로젝트 이름.
- `gradlew`, `gradlew.bat`, `gradle/wrapper/`: 정해진 Gradle 버전을 실행하는 공식 Wrapper.
- `.gitignore`: 빌드 결과와 캐시를 제외하는 규칙.

Java는 코드를 실행하고, Gradle은 컴파일·의존성 다운로드·테스트 실행을 관리하며,
JUnit은 기대값과 실제 동작을 비교합니다. 이 프로젝트에는 실행용 `main` 메서드가 없습니다.

## 실패를 직접 확인하는 연습

`isBlank()`를 잠깐 `isEmpty()`로 바꾸고 테스트를 다시 실행하면,
공백만 있는 제목 테스트가 실패해야 합니다. `isEmpty()`는 길이 0만 확인하기 때문입니다.
연습 후에는 반드시 `isBlank()`로 복원하고 테스트가 모두 통과하는지 확인하세요.

## Gradle 출처와 무결성

공식 배포 목록: https://gradle.org/releases/
공식 체크섬: https://gradle.org/release-checksums/#8.14.6
배포 URL: https://services.gradle.org/distributions/gradle-8.14.6-bin.zip

다운로드 ZIP의 SHA-256을 공식 값과 대조했습니다. Wrapper 설정에도 이 값을 고정합니다.

```text
7988ed071b2a07900e2ec715fca15c6ab72bce6db433af301fa7fa7e9407bd24
```

## 실제 확인 결과

GitHub Actions 검증: https://github.com/zeroinmars/backend-basics-java/actions/runs/38361584784

학습 블로그: https://velog.io/@zeroinmars-dev21/posts (첫 글은 검토 전 임시저장 상태)


- JDK 21.0.11에서 실제 프로젝트 소스의 클래스 파일을 생성했습니다.
- 독립 실행형 JUnit Platform Console 1.11.0(JUnit Jupiter 5.11.0)으로 테스트 5개를 실행했습니다: 5개 성공, 0개 실패, 종료 코드 0.
- 전달 파일이 아닌 임시 복사본에서만 `isBlank()`를 `isEmpty()`로 바꿨습니다: 4개 성공, 공백 제목 테스트 1개 실패, 종료 코드 1.
- 원본 `isBlank()` 소스로 다시 실행했습니다: 5개 성공, 0개 실패, 종료 코드 0. 전달 소스는 변경하지 않았습니다.

환경 제한: `javac`는 클래스 파일 생성 후 리소스 정리에서 `Cannot close compiler resources`로 종료 코드 3을 반환했습니다. 최초 로그에는 JAR의 `toRealPath`에 대한 `AccessDeniedException`이 기록되어 있습니다. 생성된 클래스를 이용한 위 JUnit 실행은 성공했지만, 컴파일 명령 전체의 정상 종료까지 확인한 것은 아닙니다.

Gradle 테스트는 이 환경의 DNS/파일시스템 제한 때문에 실행되지 못했습니다. Wrapper 다운로드는 `UnknownHostException: services.gradle.org`, 직접 Gradle 실행은 초기화 중 `IOException: 액세스가 거부되었습니다`로 막혔습니다. 독립 JUnit 결과를 Gradle 성공으로 간주하지 않습니다. Gradle 설정의 JUnit 버전은 5.11.4이므로, 위 독립 실행 결과와 버전도 다릅니다.

`.github/workflows/test.yml`은 저장소에 올렸을 때 Ubuntu와 Java 21에서 `sh gradlew test`를 실행합니다. 읽기 전용 권한만 사용하며 별도 자격 증명을 요구하지 않습니다. GitHub에 커밋한 뒤 CI에서 compileJava, compileTestJava, test가 실행되었고, 테스트 5개 모두 PASSED 및 BUILD SUCCESSFUL을 확인했습니다.

공식 Gradle 배포 ZIP 및 생성된 Wrapper JAR의 SHA-256을 공식 값과 대조했습니다. 독립 JUnit JAR도 Maven Central의 SHA-256과 일치했습니다. 빌드 결과와 `.gradle/` 캐시는 전달하지 않습니다.
