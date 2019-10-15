## readObject 메서드는 방어적으로 작성하라.

## readObject 작성하기
readObject는 직접적 혹은 간접적으로 override 가능한 메서드를 호출해서는 안됩니다.
```java
// 방어적 복사를 사용하는 불변 클래스
public final class Period {
    private final Date start;
    private final Date end;
    /**
    * @param start the beginning of the period
    * @param end the end of the period; must not precede start
    * @throws IllegalArgumentException if start is after end
    * @throws NullPointerException if start or end is null
    */
    public Period(Date start, Date end) {
        this.start = new Date(start.getTime());
        this.end = new Date(end.getTime());
        if (this.start.compareTo(this.end) > 0) {
            throw new IllegalArgumentException(start + " after " + end);
        }
    }
    public Date start () { return new Date(start.getTime()); }
    public Date end () { return new Date(end.getTime()); }
    public String toString() { return start + " - " + end; }
    // ...
}

```

1, readObject 메서드가 defaultReadObject를 호출한 다음 역직렬화된 객체가 유효한지 검사해야 한다.
```java
// readObject method with validity checking - insufficient!
private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();
    // Check that our invariants are satisfied
    if (start.compareTo(end) > 0) {
        throw new InvalidObjectException(start +" after "+ end);
    }
}
```

 * ObjectInputStream에서 Period 인스턴스를 읽은 후 스트림 끝에 추가된 '악의적인 객체 참조'를 읽어 Period 객체의 내부 정보를 얻을 수 있다. period 인스턴스는 더는 불변이 아니게 된 것이다.
 ```java
 public class MutablePeriod {
     //Period 인스턴스
     public final Period period;
     
     //시작 시각 필드 - 외부에서 접근할 수 없어야 한다.
     public final Date start;
     //종료 시각 필드 - 외부에서 접근할 수 없어야 한다.
     public final Date end;
     
     public MutablePeriod() {
         try {
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectArrayOutputStream out = new ObjectArrayOutputStream(bos);
             
             //유효한 Period 인스턴스를 직렬화한다.
             out.writeObject(new Period(new Date(), new Date()));
             
             /**
              * 악의적인 '이전 객체 참조', 즉 내부 Date 필드로의 참조를 추가한다.
              * 상세 내용은 자바 객체 직렬화 명세의 6.4절을 참고
              */
             byte[] ref = {0x71, 0, 0x7e, 0, 5}; // 참조 #5
             bos.write(ref); // 시작 start 필드 참조 추가
             ref[4] = 4; //참조 #4
             bos.write(ref); // 종료(end) 필드 참조 추가
             
             // Period 역직렬화 후 Date 참조를 훔친다.
             ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
             period = (Period) in.readObject();
             start = (Date) in.readObject();
             end = (Date) in.readObject();
         } catch (IOException | ClassNotFoundException e) {
             throw new AssertionError(e);
         }
     }
 }
 
 public static void main(String[] args) {
     MutablePeriod mp = new MutablePeriod();
     Period p = mp.period;
     Date pEnd = mp.end;
     
     //시간 되돌리기
     pEnd.setYear(78);
     System.out.println(p); // Wed Nov 22 00:21:29 PST 2017 - Wed Nov 22 00:21:29 PST 1978
     
     //60년대로 회귀
     pEnd.setYear(60);
     System.out.println(p); // Wed Nov 22 00:21:29 PST 2017 - Wed Nov 22 00:21:29 PST 1969
 }
 ```
 Period의 readObject 메서드를 방어적 복사를 충분히 하지 않은데 있다.

2, private 불변 속성을 포함한 직렬화 가능한 모든 불변 class들은 반드시 readObject 메서드에서 방어적 복사를 해줘야한다.
```java
// readObject method with defensive copying and validity checking
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        // Defensively copy our mutable components
        start = new Date(start.getTime());
        end = new Date(end.getTime());
        // Check that our invariants are satisfied
        if (start.compareTo(end) > 0)
        throw new InvalidObjectException(start +" after "+ end);
    }
```

3, 2를 위해 final을 제거하자.

* 대안으로 serialization proxy pattern (Item 90)이 있다. 


## 요약
readObject 메서드를 작성할 때는 언제나 public 생성자를 만든다고 생각하고 만들어야 한다.

 - private이어야 하는 객체 참조 필드는 각 필드가 가리키는 객체를 방어적으로 복사하라. 불변 클래스 내의 가변 요소가 여기 속한다. 
 - 모든 불변식을 검사하여 어긋나는 게 발견되면 InvalidObjectException을 던진다.
   방어적 복사 다음에는 반드시 불변식 검사가 뒤따라야 한다.
 - 역직렬화 후 객체 그래프 전체의 유효성을 검사해야 한다면 ObjectInputValidation 인터페이스를 사용하라
 - 직접적이든 간접적이든 readObject메서드에서 재정의 가능한 메서드를 호출해서는 안된다.
