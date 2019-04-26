## Try-Finally 대신 Try-with-Resource를 사용하라.
자바 라이브러리에는 `InputStream`, `OutputStream` 그리고 `java.sql.Connection`과 같이 정리(close)가 가능한 리소스가 많은데, 그런 리소스를 사용하는 클라이언트 코드가 보통 리소스 정리를 잘 안하거나 잘못하는 경우가 있다.
```java
public class FirstError extends RuntimeException {
}

```
```java
public class SecondError extends RuntimeException {
}

```
```java
public class MyResource implements AutoCloseable {

    public void doSomething() throws FirstError {
        System.out.println("do Something");
        throw new FirstError();
    }

    @Override
    public void close() throws SecondError {
        System.out.println("Close MyResource");
        throw new SecondError();
    }
}

```
```java
        MyResource myResource = null;
        try {
            myResource = new MyResource();
            myResource.doSomething();
        } finally {
            if (myResource != null) {
                myResource.close();
            }
        }
```
 - 위 코드는 `SecondError`이 출력되고 `FirstError`는 에러가 출력이 안된다. 디버깅이 힘들어진다.
 ```cmd
 do Something
 Close MyResource
 Exception in thread "main" com.taeyeonkim.effective.item09.SecondError
 	at com.taeyeonkim.effective.item09.MyResource.close(MyResource.java:13)
 	at com.taeyeonkim.effective.item09.App.main(App.java:11)
 ```
 - 또한 nested한 `try-catch`를 만들어야하는 경우에는 코드를 보는게 어려워져서 실수할 가능성이 높아진다.
```java
        try (MyResource myResource1 = new MyResource()) {
            myResource1.doSomething();
        }
```
  - `Try-with-Resource`를 사용하게 되면 코드 가독성도 높아지고 에러 출력도 `FirstError` -> `SecondError`로 출력이 된다.
  ```cmd
  do Something
  Close MyResource
  Exception in thread "main" com.taeyeonkim.effective.item09.FirstError
  	at com.taeyeonkim.effective.item09.MyResource.doSomething(MyResource.java:7)
  	at com.taeyeonkim.effective.item09.App.main(App.java:8)
  	Suppressed: com.taeyeonkim.effective.item09.SecondError
  		at com.taeyeonkim.effective.item09.MyResource.close(MyResource.java:13)
  		at com.taeyeonkim.effective.item09.App.main(App.java:9)
  ```