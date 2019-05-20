## 변경 가능성을 최소화해라.
- 클래스는 꼭 필요한 경우가 아니라면 불편이어야 한다.
- 불변으로 만들 수 없는 클래스라도 변경할 수 있는 부분을 최소한으로 줄이자.
- 다른 합당한 이유가 없다면 private final이여야 한다.
- 생성자는 불변식 설정이 모두 완료된, 초기화가 완벽히 끝난 상태의 객체를 생성해야 한다.

### Immutable Class
 - Immutable class는 단순하게 instance를 수정할 수 없는 class입니다.
 
#### Immutable Class 예들
 - String
 - Boxed primitive class들
 - BigInteger, BigDecimal
 
#### Immutable Class의 장점.
 - 디자인, 구현, 사용이 쉽다.
 - 오류가 발생하기 적고, 보다 안전하다.
 
#### Immutable Class를 만들기 위한 규칙
 1. 객체의 상태를 변경하는 메서드(변경자)를 제공하지 않는다.
 2. 클래스를 확장할 수 없도록 한다.
 3. 모든 필드를 final로 선언한다. 
 4. 모든 필드를 private으로 선언한다.
 5. 자신 외에는 내부의 가변 컴포넌트에 접근할 수 없도록 한다.


```java
package com.taeyeonkim.effective.item17;

public final class Complex {
    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    // 생성자 대신한 static 팩토리 방법.
    // private Complex(double re, double im) {
    //    this.re = re;
    //    this.im = im;
    // }
    //
    // public static Complex valueOf(double re, double im) {
    //    return new Complex(re, im);
    // }

    public double getRealPart() {
        return re;
    }

    public double getImaginaryPart() {
        return im;
    }

    public Complex add(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    public Complex subtract(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    public Complex multiply(Complex c) {
        return new Complex(re * c.re - im * c.im, re * c.im + im * c.re);
    }

    public Complex divide(Complex c) {
        double tmp = c.re * c.re + c.im * c.im;
        return new Complex((re * c.re + im * c.im) / tmp, (im * c.re - re * c.im) / tmp);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Complex)) {
            return false;
        }

        Complex c = (Complex) o;
        return Double.compare(im, c.im) == 0 && Double.compare(re, c.re) == 0;
    }

    @Override
    public int hashCode() {
        int result = 17 + hashDouble(re);
        result = 31 * result + hashDouble(im);
        return result;
    }

    private int hashDouble(double val) {
        long longBits = Double.doubleToLongBits(re);
        return (int) (longBits ^ (longBits >>> 32));
    }

    @Override
    public String toString() {
        return "(" + re + " + " + im + "i)";
    }
}
```

#### immutable object의 장단점.
##### 장점
 1. immutable object는 단순하다.
    - 생성된 시점의 상태를 파괴될 때까지 그대로 간직한다.
 2. immutable object는 근본적으로 thread-safe하여 따로 동기화할 필요 없다.
 3. immutable object는 안심하고 공유할 수 있다. 따라서 불변 클래스라면 한번 만든 인스턴스를 최대한 활용하기를 권한다.
    ```java
    public static final Complex ZERO = new Complex(0, 0);
    public static final Complex ONE  = new Complex(1, 0);
    public static final Complex I    = new Complex(0, 1);
    ``` 
 4. immutable object는 자유롭게 공유할 수 있음은 물론, 불변 객체끼리는 내부 데이터를 공유할 수 있다.
 5. 객체를 만들 떄 다른 불변 객체들을 구성요소로 사용하면 이점이 많다. (예를 들면 map의 key, set의 원소)
 6. immutable object는 그 자체로 실패 원자성을 제공한다.
##### 단점
 1. immutable object의 유일한 단점은 값이 다름녀 반드시 독립된 객체로 만들어야 한다는 것.

