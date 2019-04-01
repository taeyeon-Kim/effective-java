package com.taeyeonkim.effective.item04;

public class UtilClass {

    // 유틸 클래스라 인스턴스를 만들지 못하게 막았습니다.
    private UtilClass() {
        throw new AssertionError();
    }

    public static String getName() {
        return "taeyeonkim";
    }
}

