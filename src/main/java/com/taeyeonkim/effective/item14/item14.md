## Comparable을 구현할지 고민하라.
Comparable 을 구현한다는 것은 그 클래스의 인스턴스들에는 순서가 있음을 뜻한다.
##### Comparable interface
```java
public interface Comparable<T> {
    public int compareTo(T o);
}
```
[compareTo()](https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html#compareTo-T-)
 - 객체가 주어진 객체보다 작으면 음의 정수, 같으면 0, 크면 양의 정수를 반환한다.
 - `sgn(x.compareTo(y)) == -sgn(y.compare- To(x))` 대칭적이어야한다.
 - `(x.compareTo(y) > 0 && y.compareTo(z) > 0)`이면 `x.compareTo(z) > 0` 추이성을 보장한다.
 - `x.compareTo(y) == 0`이면 `sgn(x.compareTo(z)) == sgn(y.compareTo(z))`
 - `(x.compareTo(y) == 0) == (x.equals(y))`
 
##### compareTo 구현시 주의 사항.
필드의 값을 비교할 때 `<`와 `>` 연산자는 쓰지말아야 한다. 그 대신 기본 타입 클래스가 제공하는 정적 compare 메소드나 Comparator 인터페이스가 제공하는 비교자 생성 메서드를 사용해야한다. (성능이 개선됨)
```java
import java.util.Comparator;

import static java.util.Comparator.comparingInt;

public class PhoneNumber implements Comparable<PhoneNumber> {
    private final short areaCode, lineNum, prefix;
    private static final Comparator<PhoneNumber> COMPARATOR = comparingInt((PhoneNumber pn) -> pn.areaCode).
            thenComparingInt(pn -> pn.prefix).
            thenComparingInt(pn -> pn.lineNum);

    public PhoneNumber(int areaCode, int lineNum, int prefix) {
        this.areaCode = (short) areaCode;
        this.lineNum = (short) lineNum;
        this.prefix = (short) prefix;
    }

    @Override
    public int compareTo(PhoneNumber o) {
        return COMPARATOR.compare(this, o);
    }
}
```