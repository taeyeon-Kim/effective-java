## Item 87: 커스텀 직렬화 형태를 고려해보라.

### 커스텀 직렬화를 사용하기 위해선..
 - 첫째로, 커스텀 직렬화가 적절한지 고민해보라. 

어떤 객체의 기본 직렬화 형태는 그 객체를 루트로 하는 객체 그래프의 물리적 모습을 나름 효율적으로 인코딩한다. 
다시 말해, 객체가 포함한 데이터들과 그 객체가 포함한 데이터들과 그 객체에서부터 시작해 접근할 수 있는 모든 객체를 담아내며, 심지어 이 객체들이 연결된 위상까지 기술한다.

-  객체의 물리적 표현과 논리적 표현이 같다면 기본 직렬화 형태라도 무방하다.
```java
// 기본 직렬화의 좋은 케이스
public class Name implements Serializable {
    /**
    * Last name. Must be non-null.
    * @serial
    */
    private final String lastName;
    /**
    * First name. Must be non-null.
    * @serial
    */
    private final String firstName;
    /**
    * Middle name, or null if there is none.
    * @serial
    */
    private final String middleName;
// ... 
}
```

- 불변식 보장과 보안을 위해 readObject 메서드를 제공해야 할 때가 많다.

- public API는 반드시 문서화해라.
    - `@serial`태그로 기술한 내용은 API 문서에서 직렬화 형태를 설명하는 특별한 페이지에 기록된다.

- 객체의 물리적 표현과 논리적 표현의 차이가 클 때 기본 직렬화 형태를 사용하면 크게 네가지 문제가 생긴다.

    - 공개 API가 현재의 내부 표현 방식에 영구히 묶인다.
    - 너무 많은 공간을 차지할 수 있다.
        - 직렬화된 형태가 너무 커져서 디스크에 저장하거나 네트워크로 전송하는 속도가 느려진다.
    - 시간이 너무 많이 걸릴 수 있다.
    - stack overflow를 일으킬 수 있다.
 - 객체의 논리전 상 태와 무관한 필드라고 확실할 때만 transient 한정자를 생략해야한다. 만약 커스텀 직렬화 형태를 사용한다면, 대부분의(혹은 모든) 인스턴스 필드를 transient로 선언해야한다.

기본 직렬화를 사용한다면 transient 필드들은 역직렬화될 때, [기본 값](https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.12.5)으로 초기화됨을 잊지말자. <br/>
기본 값을 그대로 사용해서는 안된다면 readObject 메서드에서 defaultReadObject를 호출한 다음, 해당 필드를 원하는 값으로 복원하자. (item 88)<br/>
혹은 그 값을 처음 사용할 때 초기화 하는 방법도 있다.(item 83)


 - 기본 직렬화 사용 여부와 상관없이 객체의 전체 상태를 읽는 메서드에 적용해야하는 동기화 메커니즘을 직렬화에도 적용해야 한다.
```java
// 기본 직렬화를 사용하는 동기화된 클래스를 위한 writeObject 메서드
private synchronized void writeObject(ObjectOutputStream s) throws IOException {
    s.defaultWriteObject();
}
```

 - 어떤 직렬화 형태를 택하든 직렬화 가능 클래스 모두에 직렬 버전 UID를 명시적으로 부여하자.
```java
    private static final long serialVersionUID = <무작위로 고른 long 값>;
``` 
 - 구버전으로 직렬화된 인스턴스들과의 호환성을 끊으려는 경우를 제외하고는 직렬 버전 UID를 절대 수정하지 말자.
 
 
### 요약
 - Object의 논리적 상태에 대한 적절한 설명이 있는 경우에만 기본 직렬화 양식을 사용하십시오. 그렇지 않으면 객체를 적절하게 설명하는 사용자 정의 직렬화 양식을 설계하자.
 - 공개 메서드를 설계하는 만큼 클래스의 직렬화 양식을 설계하는데 시간을 써라. 왜냐하면 영구히 직렬화를 호환되게 해줘야하기 때문이다.