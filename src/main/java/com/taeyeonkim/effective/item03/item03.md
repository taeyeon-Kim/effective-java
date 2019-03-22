## private 생성자 또는 enum 타입을 사용해서 싱글톤을 만들 것.
 - 오직 한 인스턴스만 만드는 클래스를 **싱글톤**
 - 보통 함수 같은 stateless 객체 또는 본질적으로 유일한 시스템 컴포넌트를 그렇게 만든다.
 - 싱글 톤을 사용하는 클라이언트 코드를 테스트하는게 어렵다.


### final 필드
```java
public class Singleton1 {
    public static final Singleton1 instance = new Singleton1();

    private Singleton1() {

    }
}
```
 - 리플렉션을 사용해서 private 생성자를 호출하는 방법을 제외하면 생성자는 오직 최초 한번만 호출되고 Elvis는 싱글톤이 된다.
 - 리플렉션의 방법을 막고자 생성자 안에서 카운팅하거나 flag를 이용해서 예외를 던지게 해서 막을 수 있다.
 
##### 장점
static 팩토리 메소드를 사용하는 방법에 비해 더 명확하고 더 간단하다.

### static 팩토리 메소드
```java
public class Singleton2 {
    private static final Singleton2 instance = new Singleton2();

    private Singleton2() {

    }

    public static Singleton2 getInstance() {
        return instance;
    }
}
```

##### 장점

 - API(getInstance)를 변경하지 않고도 싱글톤으로 쓸지 안쓸지 변경할 수 있다. 처음엔 싱글톤으로 쓰다가 나중엔 쓰레드당 새 인스턴스를 만든다는 등 클라이언트 코드를 고치지 않고도 변경할 수 있다.


### 직렬화 (Serialization)
 - **final 필드**, **static 팩토리 메소드** 방법 모두, 직렬화에 사용한다면 역직렬화할 때마다 `public static final Singleton1 instance = new Singleton1();`이 실행되면서 같은 타입의 인스턴스가 여러개 생성 될 수 있다.
 - 이를 해결하기 위해 모든 인스턴스 필드에 `transient`를 추가하고 `readResolve`메소드를 다음과 같이 구현하면 된다.
 - [참고](https://www.oracle.com/technetwork/articles/java/javaserial-1536170.html)
 ```java
 private Object readResolve() {
    return INSTANCE;
 }
 ```
 
### Enum
 ```java
public enum Singleton3 {
    INSTANCE;
}
```
 - 직렬화/역직렬화 할 때 코딩으로 문제를 해결할 필요도 없고, 리플렉션으로 호출되는 문제도 고민할 필요없는 Enum 방법이다.
 - 최선의 방법이지만 이 방법은 다른 상위 클래스를 상속할 수 없다.(interface는 구현 가능)


#### 결론: Spring bean은 default가 싱글톤이다. 하지만 전체 application에서 싱글톤이 아니라 ApplcationContext안에서 싱글톤이다.