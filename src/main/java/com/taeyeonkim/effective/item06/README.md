## 불필요한 객체를 만들지 말자.
 - 기능적으로 동일한 객체를 만드는 대신 객체 하나를 재사용하는 것이 대부분 적절하다. 
 
### 문자열 객체 생성

자바의 문자열, String을 new로 생성하면 항상 새로운 객체를 만들게 된다. 다음과 같이 String 객체를 생성하는 것이 올바르다.

```java
String s = "taeyeonkim";
String s = new String("asd"); // 하지말자
```

문자열 리터럴을 재사용하기 때문에 해당 자바 가상 머신에 동일한 문자열 리터럴이 존재한다면 그 리터럴을 재사용한다.

### static 팩토리 메소드 사용하기

자바 9에서 deprecated 된 `Boolean(String)` 대신 `Boolean.valueOf(String)` 같은 static 팩토리 메소드를 사용할 수 있다. 생성자는 반드시 새로운 객체를 만들어야 하지만 팩토리 메소드는 그렇지 않다.

### 무거운 객체는 재사용하자.
 - 만드는데 메모리나 시간이 오래 걸리는 객체 "비싼 객체"를 반복적으로 만들어야 한다면 캐시해두고 재사용할 수 있는지 고려하는 것이 좋다.
 
예를 들어, `String.matches`를 보자.
`String.matchees`는 string이 정규 표현식에 매치가 되는지 확인하는 방법이긴 하지만 서능이 중요한 상황에서 반복적으로 사용하기에는 적절하지 않다. 
왜냐하면, `String.matchees`는 내부적으로 `Pattern` 객체를 만들어 쓰는데 해당 객체는 비싼 객체다. 그러므로 성능을 개선하려면 `Pattern` 객체를 만들어 재사용하는 것이 좋다.
```java
//String.matchees의 구현체
public boolean matches(String var1) {
    return Pattern.matches(var1, this);
}
```
 - 개선
   ```java
   private static final Pattern PATTERN = Pattern.compile("정규식");
   
       static boolean isMatches(String s) {
           return PATTERN.matcher(s).matches();
       
   ```
   
### 어댑터
 - 불변 객체인 경우에 안정하게 재사용하는 것이 매우 명확하다. 하지만 꼭 그런 것많은 아니다. 어댑터를 예로 보면, 어댑터는 인터페이스를 통해서 뒤에 있는 객체로 연결해주는 개체라 여러개 만들 필요가 없다.
 
```java
public static void main(String[] args) {
    Map<String, String> menu = new HashMap<>();
    menu.put("pizza", "domino");
    menu.put("hamburger", "McDonald");
    
    Set<String> name1 = menu.keySet();
    Set<String> name2 = menu.keySet();
    
    //서로 참조되어 있음
    name1.remove("pizza"); // name1에서 pizza를 삭제
    System.out.println(name2.size()); // name2에서도 삭제
    System.out.println(menu.size()); // menu에서도 삭제
}
```

### 오토박싱을 피하자.
 - 불필요한 객체를 생성하는 또 다른 방법으로 오토박싱이 있다. 오토박싱은 프리미티브 타입과 박스 타입을 섞어 쓸 수 있게 해주고 박싱과 언박싱을 자동으로 해준다.
```java
public class AutoBoxing {
    public static void main(String[] args) {
        Long sum = 0L;

        for (long i = 0; i <= Integer.MAX_VALUE; i++) {
            sum += i; // Long과 long을 자동으로
        }

        System.out.println(sum);
    }
}
```
위 코드에서 `sum` 변수의 타입을 `Long`으로 만들었기 때문에 불필요한 `Long`객체를 2의 31 제곱개 만큼 만들게 된다. 만약 타입을 `long` 프리미티브 타입으로 바꾸면 약 10배 이상의 차이가 난다.
**불필요한 오토박싱을 피하려면 박스 타입(Integer, Long, ...)보단 프리미티브 타입을 사용해야한다.**


### 결론
 - 객체를 만드는 것이 무조건 비싸고 가급적이면 피해야 한다는 오해를 가지지 말자. 특히 [Defensive copying](http://www.javapractices.com/topic/TopicAction.do?Id=15)를 해야하는 경우에도 객체를 재사용하면 심각한 버그와 보안성에 문제가 생긴다. 객체를 생성하면 그저 스타일과 성능에 영향이 있다는 것만 어느정도 인지한 상태에서 코딩을 하자.