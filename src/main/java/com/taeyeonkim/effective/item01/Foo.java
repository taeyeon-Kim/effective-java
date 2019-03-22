package com.taeyeonkim.effective.item01;

import java.util.EnumSet;

import static com.taeyeonkim.effective.item01.Foo.Color.BLUE;
import static com.taeyeonkim.effective.item01.Foo.Color.WHITE;

public class Foo {
    String name;
    String address;

    public Foo() {
    }

    private static final Foo foo = new Foo();

    public static Foo withName(String name) {
        Foo foo = new Foo();
        foo.name = name;
        return foo;
    }

    public static Foo withAddress(String address) {
        Foo foo = new Foo();
        foo.address = address;
        return foo;
    }

    public static Foo getFoo() {
        Foo foo =new Foo();
        //뭔짓이든 가능, 다른 인스턴스로 교체가능
        // 뭔가 템플릿 메소드 패턴과 비슷한 느낌이 든다..
        return foo;
    }

    public static Foo getFoo(boolean flag) {
        return flag ? new Foo() : new BarFoo();
    }

    public static void main(String[] args) {
        Foo foo2 = Foo.getFoo();

        Foo foo3 = Foo.getFoo(true);
        Foo foo4 = Foo.getFoo(false);  // BarFoo다.

        EnumSet<Color> colors = EnumSet.allOf(Color.class);
        EnumSet<Color> colors1 = EnumSet.of(BLUE, WHITE);
    }

    static class BarFoo extends Foo {

    }

    enum Color {
        RED, BLUE, WHITE
    }
}
