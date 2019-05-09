## clone 재정의는 주의해서 진행하라.
결론부터 얘기하면 아래와 같다.
 - cloneable interface를 확장하지 말자.
 - 상속을 위해 설계된 모든 클래스는 cloneable을 구현하지 말자.
 - final 클래스라면 Cloneable을 구현해도 위험이 크지 않지만, 성능 최적화 관점에서 검토한 후 별다른 문제가 없을 때만 드물게 허용해야한다.
 - 기본 원칙은 `복제 기능은 생성자와 팩토리를 이용하는게 최고`라는 것이다. 단, 배열만은 Clone 메서드 방식이 가장 깔끔한 예외라 할 수 있다.
    - 생성자
    ```java
     public Yum(Yum yum);
    ```
    - 팩토리
    ```java
     public static Yum newInstance(Yum yum);
    ```

### clone 재정의
 1. `super.clone()`을 통해 primitive 값들을 copy한다.
 2. 모든 가변 객체를 copy하고 복체본이 가진 객체 참조 모두가 복사된 객체들을 가리키게한다.(deep copy)

```java
public class Stack {
    private Object[] elements;
    private int size = 0;

    @Override
    public Stack clone() {
        try {
            Stack result = (Stack) super.clone();
            result.elements = elements.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
```
```java
public class HashTable implements Cloneable {
    private Entry[] buckets;

    private static class Entry {
        final Object key;
        Object value;
        Entry next;

        Entry(Object key, Object value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        Entry deepCopy() {
            Entry result = new Entry(key, value, next);
            for (Entry p = result; p.next != null; p = p.next)
                p.next = new Entry(p.next.key, p.next.value, p.next.next);
            return result;
        }

    }

    @Override
    public HashTable clone() {
        try {
            HashTable result = (HashTable) super.clone();
            result.buckets = new Entry[buckets.length];
            for (int i = 0; i < buckets.length; i++)
                if (buckets[i] != null)
                    result.buckets[i] = buckets[i].deepCopy();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
```