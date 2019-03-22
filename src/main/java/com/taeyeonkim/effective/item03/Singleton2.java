package com.taeyeonkim.effective.item03;

public class Singleton2 {
    private static final Singleton2 instance = new Singleton2();

    private Singleton2() {

    }

    public static Singleton2 getInstance() {
        return instance;
    }
}
