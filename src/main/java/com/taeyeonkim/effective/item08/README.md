## Finalizer와 Cleaner는 피해라

**Finalizer는 예측 불가능하고, 위험하며, 대부분 불필요하다.** 
Finalizer를 쓰게되면 이상하게 동작하기도 하고, 성능도 안좋아지고, 이식성에도 문제가 생길 수 있다.
Finalizer를 유용하게 쓸 수 있는 경우는 극히 드물다.

```java
public class SampleRunner {

    public static void main(String[] args) throws InterruptedException {
        SampleRunner sampleRunner = new SampleRunner();
        sampleRunner.run();
        Thread.sleep(1000L);
    }

    private void run() {
        //run method가 끝나고 finalizerExample가 gc의 대상이 된다는 보장이 없음.
        // gc의 대상은 되지만 바로바로 gc가 일어나지 않음(finalize 가 언제 호출된다는 보장이 없음).
        FinalizerExample finalizerExample = new FinalizerExample();
        finalizerExample.hello();
    }
}

public class FinalizerExample {
    @Override
    protected void finalize() throws Throwable {
        System.out.println("clean up");
    }

    public void hello() {
        System.out.println("hello world");
    }
}
```

딱 두가지 경우
* 안전망 역할로 자원을 반납하고자 하는 경우
* 네이티브 리소스를 정리해야하는 경우.

일단 자바 9 에서는 Finalizer가 `deprecated` 됐으며 `Cleaner`라는게 새로 생겨서 
Finalizer 보다 덜 위험하지만(별도의 쓰레드를 사용하므로), 야전히 예측 불가능하며, 느리고, 일반적으로 불필요하다.


### 단점 1
 * 언제 실행될지 알 수 없다. <br>
 어떤 객체가 더이상 필요 없어진 시점에 그 즉시 finalizer 또는 cleaner가 바로 실행되지 않을 수도 있다.<br>
 그 사이에 시간이 얼마나 걸릴지는 아무도 모른다.<br>
 **따라서 타이밍이 중요한 작업을 절대로 finalizer나 cleaner에서 하면 안된다.**
 
### 단점 2
 * Finalizer는 인스턴스 반납을 지연 시킬 수도 있다. Finalizer 쓰레드는 GC 대상의 우선 순위가 낮아서 언제 실행될지 모른다.<br>
 따라서, Finalizer가 안에 어떤 작업이 있고, 그 작업을 쓰레드가 처리 못해서 대기하고 있다면, 해당 인스턴스는 GC가 되지 않고 계속 쌓이다가 결국엔 OOM이 발생할 수 있다.
 
 * Cleaner는 별도의 쓰레드로 동작하니까 이 부분에 있어서 조금은 나을 수도 있지만, 여전히 해당 쓰레드는 백드라운드에서 동작하고 언제 처리될지 모른다.
 
### 단점 3
 * 즉시 실행되지 않을 수도 있거니와 Finalizer나 Cleaner를 아예 실행하지 않을 수도 있다.<br>
   따라서 **Finalizer나 Cleaner로 저장소 상태를 변경하는 일을 하지 말라.**
   
### 단점 4
 * 심각한 성능 문제도 있다. <br>
 `Autocloseable` 객체를 만들고, `try-with-resource`로 자원 반납을 하는데 걸리는 시간은 12ns 인데 반해,<br>
 `Finalizer`를 사용한 경우에 550ns가 걸렸다. 약 50배가 걸렸다. `Cleaner`를 사용한 경우에는 66ns가 걸렸다.
 
### 단점 5
 * Finalizer 공격이라는 심각한 보안 이슈에도 이용될 수 있다. 
 
## 자원 반납하는 방법
 * 자원 반납이 필요한 클래스 `AutoCloseable` 인터페이스를 구현하고 `try-with-resource`를 사용하거나,<br> 
   클라이언트가 `close`메소드를 명시적으로 호출하는게 정석이다.
```java
public class SampleResource implements AutoCloseable {
    @Override
    public void close() throws Exception {
        System.out.println("close");
    }

    public void hello() {
        System.out.println("hello");
    }
}

public class SampleRunner {

    public static void main(String[] args) throws Exception {
        try (SampleResource sampleResource = new SampleResource()) {
            sampleResource.hello();
        }
    }
}
```

## Finalizer와 Cleaner를 안전망으로 쓰기
 * 자원 반납에 쓸 `close` 메소드를 클라이언트가 호출하지 않았다는 가정하에, 물론 실제로 Finalizer와 Cleaner가 언제 호출될지도 모르지만,
 안하는 것보다는 나으니까 `finalize`에서 `close`를 호출하는 방법이다.
 ```java
    @Override
    protected void finalize() throws Throwable {
        close();
    }
```
 <br>
 실제로 `FileOutputStream`, `ThreadPoolExecutor` 그리고 `java.sql.Connection`에는 안전망으로 동작하는 finalizer가 있다.
 
## native peer 정리할 때 쓰기
```
자바 클래스 -> 네이티브 메소드 호출 -> 네이티브 객체 (native peer)
```
 * 네이티브 객체는 일반적인 객체가 아니라서 GC가 그 존재를 모른다. native peer가 들고 있는 리소스가 중요하지 않은 자원이며 ,성능상
 영향이 크지 않다면 Cleaner나 Finalizer를 사용해서 해당 자원을 반납할 수도 있을 것이다.<br>
 하지만, 중요한 리소스인 경우에는 위에서 본 것처럼 `close`메소드를 사용하는게 좋다.
 
 ## Cleaner 예제 코드 (jdk 9이상)
 ```java
import java.lang.ref.Cleaner;

public class CleanerSample implements AutoCloseable {

    private static final Cleaner CLEANER = Cleaner.create();
    private final CleanerRunner cleanerRunner;
    private final Cleaner.Cleanable cleanable;

    public CleanerSample() {
        cleanerRunner = new CleanerRunner();
        cleanable = CLEANER.register(this, cleanerRunner);
    }

    @Override
    public void close() {
        cleanable.clean();
    }

    public void doSomething() {
        System.out.println("do it");
    }

    private static class CleanerRunner implements Runnable {

        // TODO 여기에 정리할 리소스 전달

        @Override
        public void run() {
            // 여기서 정리
            System.out.println("close");
        }
    }
}
```
 