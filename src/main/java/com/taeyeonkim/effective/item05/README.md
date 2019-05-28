## 리소스를 엮을 때는 의존성 주입을 선호하라.

### 부적절한 구현

- 아래 코드에서 `new KoreanDictionary()`와 같은 객체 하나면 상관이 없지만, 만약 English, Japanese와 같이 다른 리소스를 사용해야한다면 static 유틸 클래스와 싱글톤을 사용하는 것은 부적절하다.

#### static 유틸 클래스
 - 부적절한 static 유틸리티 사용 예 - 유연하지 않고 테스트 할 수 없다.
```java
public class SpellChecker {
    private static final Lexicon dicationry = new KoreanDicationry();

    private SpellChecker() {
        // Noninstantiable
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}

interface Lexicon {
}

class KoreanDicationry implements Lexicon {
}
```

#### 싱글톤으로 구현하기
 - 부적절한 싱글톤 사용 예 - 유연하지 않고 테스트 할 수 없다.
```java
public class SpellCheckerSingleton {
    private final Lexicon dictionary = new KoreanDictionary();

    private SpellCheckerSingleton() {

    }

    public static final SpellCheckerSingleton INSTANCE = new SpellCheckerSingleton();

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}

```


### 적절한 구현
#### 의존성 주입
```java
public class SpellCheckerDI {
    private final Lexicon dictionary;

    public SpellCheckerDI(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}
```
 - 의존성 주입은 생성자, 스택틱 팩토리(item01) 그리고 빌더(item02)에도 적용할 수 있다.
 - 의존성 주입을 통해 필요할 때 원하는 리소스를 갈아 낄수 있다.
 ```java
 //테스트시에 TestDictionary만 생성하고 사용, application에서는 KoreanDictionary만 생성해서 사용
 Lexicon dictionary = new TestDictionary();
 Lexicon dictionary = new KoreanDictionary();
         
 SpellCheckerDI spellCheckerDI = new SpellCheckerDI(dictionary);
 ```
 
#### JDK8 Supplier를 이용한 의존 주입
```java
public class SpellCheckerSupplier {
    private final Lexicon dictionary;

    public SpellCheckerSupplier(Supplier<Lexicon> dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary.get());
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Lexicon lexicon = new TestDictionary();
        SpellCheckerSupplier spellCheckerSupplier = new SpellCheckerSupplier(() -> lexicon);
    }
}

```

### 결론
 - 의존성 주입을 통해 유연함과 테스트가 용이해지지만, 의존성이 많은 큰 프로젝트 에서는 코드가 장황해지고 복잡해질 수 있다. 이럴때는 스프링같은 프레임워크를 사용해서 해결하자.
 - 의존하는 리소스에 따라 행동이 달라지는 클래스를 만들 때는 싱글톤이나 스태틱을 사용하지말고 의존성 주입을 통해 유연함, 재사용성, 테스트 용이성을 향상 시켜라. 
 - 스프링한테 감사하자.