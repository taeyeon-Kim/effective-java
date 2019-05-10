## 클래스와 멤버의 접근 권한을 최소화하라.

### Information Hiding or Encapsulation의 장점
 - 시스템 개발속도를 높인다. 여러 컴포넌트를 병렬로 개발할 수 있기 떄문이다.
 - 시스템 관리비용을 낮춘다. 각 컴포넌트를 더 빨리 파악하여 디버깅할 수 있고, 다른 컴포넌트로 교체하는 부담도 적기 때문이다.
 - 정보 은닉 자체가 성능을 높여주진 않지만 성능 최적화에 도움을 준다.
 - 소프트웨어의 재사용성을 높인다.
 - 큰 시스템을 제작하는 난이도를 낮춰준다. 시스템 전체가 아직 완성되지 않은 상태에서도 개별 컴포넌트의 동작을 검증할 수 있기 때문이다.

### 접근제한자
자바에서는 접근제한자를 제대로 활용하여 정보은닉을 해야한다.<br>
기본 원칙은 **모든 클래스와 멤버의 접근성을 가능한 한 좁혀야한다.**

| Modifier    | Class | Package | Subclass | World |
|-------------|-------|---------|----------|-------|
| public      | Y     | Y       | Y        | Y     |
| protected   | Y     | Y       | Y        | N     |
| no modifier | Y     | Y       | N        | N     |
| private     | Y     | N       | N        | N     |

### Public Class
 - Public class의 Instance field들은 되도록이면 public이면 안된다.
    - 가변 필드를 갖는 클래스는 thread-safe하지 않다.
    - 대부분은 private으로 하자.
    

### 상수 필드(Public static final fields)
```java
// Exception
public static final String CLASS_NAME = "Point";

// Potential security hole!
public static final Thing[] VALUES =  { ... };

// Alternative #1: Private array and add a public immutable list
private static final Thing[] PRIVATE_VALUES = { ... };
public static final List<Thing> VALUES = Collections.unmodifiableList(Arrays.asList(PRIVATE_VALUES));

// Alternative #2: Private array and add a public method that returns a copy of a private array
private static final Thing[] PRIVATE_VALUES = { ... };
public static final Thing[] values() {
   return PRIVATE_VALUES.clone();
}
```
