## 생성자 매개변수가 많은 경우에 빌더 사용을 고려해볼 것
 - static 팩토리 메소드와 public 생성자 모두 매개변수가 많이 필요한 경우에 불편해진다
 
 ```java
public class NutritionFacts {
    private int servingSize;
    private int servings;
    private int calories;
    private int sodium;
    private int carbohydrate;
}
```


#### 해결책 1: 생성자
```java
NutritionFacts cocaCola
    = new NutritionFacts(240, 8, 100, 0, 35, 27);
```
 - 불필요한 필드 값은 0과 같은 기본 값을 넘기긴다. 이러한 코드는 동작에 이상은 없지만 작성하기도 어렵고 읽기 불편하다.
 
#### 해결책 2: 자바빈
 - 아무런 매개 변수를 받지 않는 생성자를 사용해서 인스턴스를 만들고, setter를 사용해서 필요한 필드만 설정할 수 있다.
```java
NutritionFacts cocaCola1 = new NutritionFacts();
cocaCola1.setCalories(100);
cocaCola1.setServingSize(240);
cocaCola1.setServings(8);
cocaCola1.setCarbohydrate(35);
cocaCola1.setNa(27)
```
 - 코드는 명확하나 장황하다....
 - 최종적인 인스턴스를 만들기까지 여러번의 호출을 거쳐야 하기 때문에 자바빈이 중간에 사용되는 경우 안정적이지 않은 상태로 사용될 여지가 있다.
 - setter와 getter때문에 불편 클래스로 만들 수 없다.
 - 쓰레드 안정성을 보장(freezing)하려면 추가적인 cost가 필요하다.

#### 해결책 3: 빌더
 - 신축적인 생성자와 자바빈을 사용할 때 얻을 수 있었던 가독성을 모두 취할 수 있는 대안이 빌더 패턴.

 - 빌더의 생성자나 메소드에서 유효성을 확인을 할 수도 있다.
 - 여러 매개 변수를 혼합해서 확인해야하는 경우 에는 `build()` 메소드에서 호출하는 생성자에서 할 수 있다.
 
 ```java
 public class NutritionFacts {
     private int servingSize;
     private int servings;
     private int calories;
     private int sodium;
     private int carbohydrate;
     private int na;
     
     private NutritionFacts(Builder builder) {
         this.servingSize = builder.servingSize;
         this.servings = builder.servings;
         this.calories = builder.calories;
         this.sodium = builder.sodium;
         this.carbohydrate = builder.carbohydrate;
         this.na = builder.na;
     }
     
    public static class Builder {
        // 필수 parameter
        private int servingSize;
        private int servings;

        // 옵션 parameter 초기 값을 준다.
        private int calories = 0;
        private int sodium = 0;
        private int carbohydrate = 0;
        private int na = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int calories) {
            this.calories = calories;
            return this;
        }

        public Builder sodium(int sodium) {
            this.sodium = calories;
            return this;
        }

        public Builder carbohydrate(int carbohydrate) {
            this.carbohydrate = carbohydrate;
            return this;
        }

        public Builder na(int na) {
            this.na = na;
            return this;
        }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }
}
```
```java
NutritionFacts cocaCola2 = new NutritionFacts.Builder(240, 8)
                .calories(100).sodium(35).carbohydrate(27).build();
```

 - 클래스 계층 구조를 잘 활용할 수 있다. 추상 빌더를 가지고 있는 추상 클래스를 만들고 하위 클래스에서는 추상 클래스를 상속받으며 각 하위 클래스용 빌더도 추상 빌더를 상속받아 만들 수 있다.
 
 ```java
public abstract class Pizza {

    public enum Topping {
        HAM, MUSHROOM, ONION
    }

    final EnumSet<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings;
    }
}
```

```java
public class NyPizza extends Pizza {

    public enum Size {
        SMALL, MEDIUM, LARGE
    }

    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }
}
```

```java
public class Calzone extends Pizza {
    private final Boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false;

        public Builder sauceInside(boolean sauceInside) {
            this.sauceInside = sauceInside;
            return this;
        }

        @Override
        public Calzone build() {
            return new Calzone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private Calzone(Builder builder) {
        super(builder);
        sauceInside = builder.sauceInside;
    }
}
```

```java
NyPizza nyPizza = new NyPizza.Builder(NyPizza.Size.SMALL)
        .addTopping(Pizza.Topping.HAM)
        .addTopping(Pizza.Topping.ONION)
        .build();

Calzone calzone = new Calzone.Builder()
        .addTopping(Pizza.Topping.HAM)
        .sauceInside(true)
        .build();
```

#### 결론: Lombok의 @Builder를 고려하자.
 