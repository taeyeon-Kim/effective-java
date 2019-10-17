## 인스턴스 수를 통제해야 한다면 readResolve보다는 Enum을 사용하라.

### readResolve
 - `readObject`에 의해 생성된 인스턴스를 다른 인스턴스로 대체 할 수 있다.
 - 새로 생성된 객체의 참조는 유지하지 않으므로 바로 가비지 컬렉션 대상이 된다.
 - `readResolve`를 인스턴스 통제 목적으로 사용한다면 객체 참조 타입 인스턴스 필드는 모두 transient로 선언해야 한다.
 ```java
 //target to serializable
    public class Elvis {
    public static final Elvis INSTANCE = new Elvis();
    private Elvis() { ... }
    public void leaveTheBuilding() { ... }
 }
 ```
 ```java
 // Broken singleton - has nontransient object reference field!
 public class Elvis implements Serializable {
     public static final Elvis INSTANCE = new Elvis();
     private Elvis() { }
     private String[] favoriteSongs = { "Hound Dog", "Heartbreak Hotel" };
     public void printFavorites() {
         System.out.println(Arrays.toString(favoriteSongs));
     }
     private Object readResolve() {
         return INSTANCE;
     }
 }
 ```
 - 문제점
   - 이렇게 하면 세심하게 조작된 스트림이 객체 참조 필드의 내용이 역직렬화되었을 때 원래 역직렬화된 싱글톤에 대한 참조를 "훔치게" 할 수 있다.

```java
// 'steal' class 
public class ElvisStealer implements Serializable {
    static Elvis impersonator;
    private Elvis payload;
    private Object readResolve() {
        // resolve 되기전에 Elvis 인스턴스의 참조를 저장한다.
        impersonator = payload;
        // favoriteSongs의 타입의 객체를 리턴한다.
        return new String[] { "A Fool Such as I" };
    }
    private static final long serialVersionUID = 0;
}

public class ElvisImpersonator {
    private static final Byte[] serializedForm = {진짜 Elvis 인스턴스로는 만들어질 수 없는 바이트 스트림};
    public static void main(String[] args) {
        Elvis elvis = (Elvis) deserialize(serializedForm);
        Elvis impersonator = ElvisStealer.impersonator;
        
        elvis.printFavorites(); // ["Hound Dog", "Heartbreak Hotel"]
        impersonator.printFavorites(); // ["A Fool Such as I"]
    }
}
```

Enum singleton으로 해결.
```java
// Enum singleton - the preferred approach
public enum Elvis {
    INSTANCE;
    private String[] favoriteSongs = { "Hound Dog", "Heartbreak Hotel" };
    public void printFavorites() {
        System.out.println(Arrays.toString(favoriteSongs));
    }
}
```
 따라서 readResolve의 접근성은 중요하니 final 클래스에 readResolve 메소드를 private으로 합니다.
 
### 요약
 - 불변으로 하기 위해선 웬만하면 enum 타입을 사용하라.
 - 만약 이게 불가능하고 인스턴스도 control하고 직렬화도 제공해야한다면, 클래스 인턴스 필드들을 `primitive`, `transient`로 한 뒤  `readResolve`를 제공해라.