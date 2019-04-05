## 더 이상 쓰지 않는 객체 레퍼런스는 없애자.

### 메모리 직접 관리하는 케이스 
 - 자바에 GC가 있기 때문에 메모리 관리에 대해 신겨쓰지 않아도 될 것 같지만 그렇지 않다. 다음 코드에서 메모리 leak을 찾아보자.
 ```java
 public class Stack {
 
     private Object[] elements;
 
     private int size = 0;
 
     private static final int DEFAULT_INITIAL_CAPACITY = 16;
 
     public Stack() {
         this.elements = new Object[DEFAULT_INITIAL_CAPACITY];
     }
 
     public void push(Object e) {
         this.ensureCapacity();
         this.elements[size++] = e;
     }
 
     public Object pop() {
         if (size == 0) {
             throw new EmptyStackException();
         }
 
         return this.elements[--size]; // 주목!!
     }
 
     /**
      * Ensure space for at least one more element,
      * roughly doubling the capacity each time the array needs to grow.
      */
     private void ensureCapacity() {
         if (this.elements.length == size) {
             this.elements = Arrays.copyOf(elements, 2 * size + 1);
         }
     }
 }
 ```
  pop의 코드를 보면 `return this.elements[--size]`코드는 실제로 해당 element를 꺼내고 지우는게 아니라 size의 int 값만 조절되기 때문에 쓸모 없이 메모리를 차지하고 있는 부분이다.
 
 해당 코드를 개선 해보자.
 ```java
     public Object pop() {
         if (size == 0) {
             throw new EmptyStackException();
         }
 
         Object value = this.elements[--size];
         this.elements[size] = null;
         return value;
     }
 ```
 그렇다고 매번 `this.elements[size] = null;`처럼 null로 설정하는 코드 작성에는 주의하자. 객체를 Null로 설정하는 건 예외적인 상황에서나 하는 것이기 때문이다. 그렇다면 언제해야 할까??? 바로 위 상황처럼  **메모리를 직접 관리 할 때.** 이다.
 Stack 구현체처럼 elements라는 배열을 관리하는 경우에 GC는 어떤 객체가 필요없는 객체인지 알 수 없다. 오직 프로그래머만 알 수 있다. 따라서, 프로그래머가 해당 레퍼런스를 null로 만들어서 GC한테 필요없는 객체들이라고 알려줘야 한다.
 즉, **메모리를 직접 관리하는 클래스는 프로그래머가 메모리 누수를 조심해야 한다.** 
 
 ### 캐시
 ```java
 public class Cache {
 
     public static void main(String[] args) {
         Object key1 = new Object();
         Object value1 = new Object();
 
         //bad
         Map<Object, Object> badCache = new HashMap<>();
         badCache.put(key1, value1);
         //good
         Map<Object, Object> goodCache = new WeakHashMap<>();
         goodCache.put(key1, value1);
     }
 }
 ```
 -캐시를 사용할 때도 메모리 누수 문제를 조심해야 한다. 객체의 레퍼런스를 캐시에 넣어 놓고 캐시를 비우는 것을 잊기 쉽다. `badCache`의 경우는 `key1`이 외부에서 쓸모 없어졌을 경우에도 `badCache`이 계속 `key1`을 참조하고 있기때문에 GC의 대상이 되지 않는다. 이에 대해 여러 가지 해결책이 있지만, 캐시의 키에 대한 레퍼런스가 캐시 밖에서 필요 없어지면 해당 엔트리를 캐시에서 자동으로 비워주는 WeakHashMap을 쓸 수 있다.([Weak 레퍼런스](https://web.archive.org/web/20061130103858/http://weblogs.java.net/blog/enicholas/archive/2006/05/understanding_w.html)를 사용)
  
  또는 특정 시간이 지나면 캐시값이 의미가 없어지는 경우에 백그라운드 쓰레드를 사용하거나, 새로운 엔트리를 추가할 때 부가적인 작업으로 기존 캐시를 비우는 일을 할 것이다. (LinkedHashMap 클래스는 removeEldestEntry라는 메서드를 제공한다.)
  
 ### 콜백
 - 흔하게 메모리 누수가 발생할 수 있는 지점으로 리스너와 콜백이 있다.
 클라이언트 코드가 콜백을 등록할 수 있는 API를 만들고 콜백을 뺄 수 있는 방법을 제공하지 않는다면, 계속해서 콜백이 쌓이기 할 것이다. 이것 역시 WeahHashMap을 사용해서 해결 할 수 있다.
 

메모리 누수는 발견하기 쉽지 않기 때문에 수년간 시스템에 머물러 있을 수도 있다. 실제로, 현재 맡고 있던 서비스에서도 메모리 누수 이슈로 heap dump를 뜨고 누수가 발생하는 곳을 찾으려니 상당 기간이 소요되었다... 따라서 이런 문제를 예방하는 방법을 학습하여 미연에 방지하는 것이 좋은 것 같다. GC가 다 해주지 않는다는 걸 항상 인지하자.