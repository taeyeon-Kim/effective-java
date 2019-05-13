## Public 클래스에는 public 필드가 아닌 접근자 필드를 사용하라.
 - package 외부에서 클래스에 접근 할 수 있는 경우 접근자 메서드를 제공하자.
 ```java
    // 접근자와 변경자(mutator) 메서드를 활용해 데이터를 캡슐화한다.
    class Point {
       private double x;
       private double y;
    
       public Point(double x, double y) {
          this.x = x;
          this.y = y;
       }
    
       public double getX() { return x; }
       public void setX(double x) { this.x = x; }
    
       public double getY() { return y; }
       public void setY(double y) { this.y = y; }
    }
 ```
 - 클래스가 package-private이거나 private 중첩 클래스 인 경우 데이터 필드를 노출하는 데 문제가 없습니다.
 - public 클래스의 필드가 불변이라면 직접 노출할 떄의 단점이 조금은 줄어들지만, 여전히 좋은 생각은 아니다.
 ```java
 // 불변 필드를 노출한 public 클래스 - 관여 좋은가?
 public final class Time {
    public final int hour;
    public final int minute;
 
    public Time(int hour, int minute) {
       this.hour = hour;
       this.minute = minute;
    }
 }
 ```