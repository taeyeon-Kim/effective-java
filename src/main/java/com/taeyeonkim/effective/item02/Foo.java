package com.taeyeonkim.effective.item02;

public class Foo {

    public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts(240, 8, 100, 0, 35, 27);

        NutritionFacts cocaCola1 = new NutritionFacts();
        cocaCola1.setCalories(100);
        cocaCola1.setServingSize(240);
        cocaCola1.setServings(8);
        cocaCola1.setCarbohydrate(35);
        cocaCola1.setNa(27);

        NutritionFacts cocaCola2 = new NutritionFacts.Builder(240, 8)
                .calories(100).sodium(35).carbohydrate(27).build();

        NyPizza nyPizza = new NyPizza.Builder(NyPizza.Size.SMALL)
                .addTopping(Pizza.Topping.HAM)
                .addTopping(Pizza.Topping.ONION)
                .build();

        Calzone calzone = new Calzone.Builder()
                .addTopping(Pizza.Topping.HAM)
                .sauceInside(true)
                .build();
    }
}