package com.taeyeonkim.effective.item06;

public class AutoBoxing {
    public static void main(String[] args) {
        Long sum = 0L;

        for (long i = 0; i <= Integer.MAX_VALUE; i++) {
            sum += i; // Long과 long을 자동으로
        }

        System.out.println(sum);
    }
}
