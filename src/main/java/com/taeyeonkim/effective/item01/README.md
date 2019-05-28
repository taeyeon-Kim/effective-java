## 생성자 대신 static 팩토리 메소드를 고려해보자.

```java
public class Foo {
    String name;

    public Foo(String name) {
        this.name = name;
    }
    
    // static 팩토리 메소드
    public static Foo withName(String name) {
        return new Foo(name);
    }

    public static void main(String[] args) {
        Foo foo = new Foo("taeyeon");
        Foo foo1 = Foo.withName("taeyeon");
    }
}


```
#### Props
 
 - 이름을 가질 수 있다.
   -  `new Foo("taeyeon")`보다  `Foo.withName("taeyeon")`이 읽기 편하다.
   - 생성자는 시그니처에 제약이 있다. static 팩토리 메소드는 이를 극복할 수 있다.
   ```java
         public Foo(String name) {
             this.name = name;
         }
     
         public Foo(String address) {
             this.address = address;
         }  
   ```
   ```java
       public static Foo withName(String name) {
           Foo foo = new Foo();
           foo.name = name;
           return foo;
       }
   
       public static Foo withAddress(String address) {
           Foo foo = new Foo();
           foo.address = address;
           return foo;
       }
   ```
 - 반드시 새로운 객체를 만들 필요가 없다.
   - 불변(immutable) 클래스인 경우나 매번 새로운 객체를 만들 필요가 없는 경우에 미리 만들어둔 인스턴스 또는 캐시해둔 인스턴스를 반활 할 수 있다.
   ```java
   public static Boolean valueOf(boolean b) {
       return b ? Boolean.TRUE : Boolean.FALSE;
   }
   ```
   
  - 리턴 타입의 하위 타입 인스턴스를 만들 수도 있다. `java.util.collections`가 그 예
    - 클래스에서 만들어줄 객체의 클래스를 선택하는 유연함이 있다. 리턴 타입의 하위 타입의 인스턴스를 만들어줘도 되니까, 리턴 타임은 인터페이스로 지정하고 그 인터페이스의 구현체를 노출되지 않고 해당 구현체의 인스턴스를 만들어 줄 수 있다는 말.
  - 리턴하는 객체의 클래스가 입력 매개변수에 따라 매번 다를 수 있다.
     - 예를 들어 `Enumset`클래스는 생성자 없이 public static 메소드, `allOf()`, `of()`등을 제공한다. 그런 객체 타입은 노출하지 않고 감춰져 있기 때문에 추후에 JDK의 변화에 따라 새로운 타입을 만들거나 기존 타입을 없애도 문제가 되지 않는다.
     ```java
        EnumSet<Color> colors = EnumSet.allOf(Color.class);
        EnumSet<Color> colors1 = EnumSet.of(BLUE, WHITE);
        
        enum Color {
            RED, BLUE, WHITE
        }
     ```
     - 위 두 장점의 예
     ```java
     
    public static Foo getFoo() {
        return foo;
    }

    public static Foo getFoo(boolean flag) {
        return flag ? new Foo() : new BarFoo();
    }

    public static void main(String[] args) {
        Foo foo2 = Foo.getFoo();
        
        Foo foo3 = Foo.getFoo(true);
        Foo foo4 = Foo.getFoo(false);  // BarFoo다.
        
    }
    
    static class BarFoo extends Foo {
        
     ```
     
  - 리턴하는 객체의 클래스가 public static 팩토리 메소드를 작성할 시점에 반드시 존재하지 않아도된다.
     - 위 두 장점과 비슷한 개념이다. 이러한 유연성을 제공하는 static 팩토리 메소드는 `서비스 프로바이더` 프레임워크의 근본이다. `JDBC`를 예로 들고 있다.
     ```java
    public static Foo getFoo() {
        Foo foo =new Foo();
        //뭔짓이든 가능, 다른 인스턴스로 교체가능
        // 뭔가 템플릿 메소드 패턴과 비슷한 느낌이 든다..
        return foo;
    }     
     ```
     
#### Cons

 - public 또는 protected 생성자 없이 static public 메소드만 제공하는 클래스는 상속할 수 없다.
 - 프로그래머가 static 팩토리 메소드를 찾는게 어렵다.
    - 생성자는 javadoc 상단에 모아서 모여주지만 static 팩토리 메소드는 API 문서에서 특별히 다루지 않는다. 따라서 클래스나 인터페이스 문서 상단에 팩토리 메소드에 대한 문서를 제공하는 것이 좋다.
    