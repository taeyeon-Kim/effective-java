## 직렬화 인스턴스 대신 직렬화 프록시 사용을 검토하라.

### serialization proxy pattern.

### 작성하는 법.

1. 바깥 클래스의 논리적 상태를 정밀하게 표현하는 중첩 클래스를 설계해 private static으로 선언한다.
   - 중첩 클래스의 생성자는 단 하나여야 하며, 바깥 클래스를 매개변수로 받아야 한다.
   - 이 생성자는 단순히 인수로 넘어온 인스턴스의 데이터를 복사한다.

```java
// Serialization proxy for Period class
private static class SerializationProxy implements Serializable {
    private final Date start;
    private final Date end;
    SerializationProxy(Period p) {
        this.start = p.start;
        this.end = p.end;
    }
    private static final long serialVersionUID = 234098243823485285L;
}
```
2.바깥 클래스에 다음의 `writeReplace` 메서드를 추가한다.
 - 불변식을 훼손하고자하는 공격을 막기위해서, `readObject` 메서드를 바깥 클래스에 추가한다.
 
```java
// writeReplace method for the serialization proxy pattern
private Object writeReplace() {
    return new SerializationProxy(this);
}
```

```java
// readObject method for the serialization proxy pattern
private void readObject(ObjectInputStream stream) throws InvalidObjectException {
    throw new InvalidObjectException("Proxy required");
}
```

3.바깥 클래스와 논리적으로 동일한 인스턴스를 반환하는 `readResolve` 메서드를 SerializationProxy 클래스에 추가한다.
```java
// readResolve method for Period.SerializationProxy
private Object readResolve() {
    return new Period(start, end); // Uses public constructor
}
```

### note
- 악질적인 직렬화 공격에 의해 어떤 field가 손상될 수 있는지 파악할 필요도 없고, 역 직렬화의 일부인 유효성 검사를 명시 적으로 수행하지 않아도 된다.
- The serialization proxy pattern의 한계.
  1. 클라이언트가 멋대로 확장할 수 있는 클래스에는 적용할 수 없다.
  2. 객체 그래프에 순환이 있는 클래스에도 적용할 수 없다.

### 요약
클라이언트가 확장할 수 없는 클래스에서 `readObject` 또는 `writeObject` 메소드를 작성해야 할 때마다 serialization proxy pattern을 고려하자.
