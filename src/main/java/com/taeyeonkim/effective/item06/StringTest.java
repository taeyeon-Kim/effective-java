package com.taeyeonkim.effective.item06;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class StringTest {

    private static final Pattern PATTERN = Pattern.compile("정규식");

    static boolean isMatches(String s) {
        return PATTERN.matcher(s).matches();
    }

    public static void main(String[] args) {
        Map<String, String> menu = new HashMap<>();
        menu.put("pizza", "domino");
        menu.put("hamburger", "McDonald");

        Set<String> name1 = menu.keySet();
        Set<String> name2 = menu.keySet();

        //서로 참조되어 있음
        name1.remove("pizza"); // name1에서 pizza를 삭제
        System.out.println(name2.size()); // name2에서도 삭제
        System.out.println(menu.size()); // menu에서도 삭제
    }
}
