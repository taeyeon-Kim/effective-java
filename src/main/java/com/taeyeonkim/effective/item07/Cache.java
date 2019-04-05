package com.taeyeonkim.effective.item07;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class Cache {

    public static void main(String[] args) {
        Object key1 = new Object();
        Object value1 = new Object();

        //bad
        Map<Object, Object> badCache = new HashMap<>();
        badCache.put(key1, value1);
        //good
        Map<Object, Object> goodCache = new WeakHashMap<>();
        goodCache.put(key1, value1);
    }
}
