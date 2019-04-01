package com.taeyeonkim.effective.item04;

public class Item04 {
    public static void main(String[] args) {
        // UtilClass utilClass = new UtilClass(); 생성자를 막고자 abstract
        AnotherClass anotherClass = new AnotherClass();
        System.out.println(anotherClass.getName());

        UtilClass.getName();
    }
}
