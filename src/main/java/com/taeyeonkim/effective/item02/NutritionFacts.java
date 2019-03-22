package com.taeyeonkim.effective.item02;

public class NutritionFacts {
    private int servingSize;
    private int servings;
    private int calories;
    private int sodium;
    private int carbohydrate;
    private int na;

    public NutritionFacts() {
    }

    public NutritionFacts(int servingSize, int servings, int calories, int sodium, int carbohydrate, int na) {
        this.servingSize = servingSize;
        this.servings = servings;
        this.calories = calories;
        this.sodium = sodium;
        this.carbohydrate = carbohydrate;
        this.na = na;
    }

    private NutritionFacts(Builder builder) {
        this.servingSize = builder.servingSize;
        this.servings = builder.servings;
        this.calories = builder.calories;
        this.sodium = builder.sodium;
        this.carbohydrate = builder.carbohydrate;
        this.na = builder.na;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public int getNa() {
        return na;
    }

    public void setNa(int na) {
        this.na = na;
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
