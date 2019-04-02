package com.taeyeonkim.effective.item05;

import java.util.List;

public class SpellCheckerSingleton {
    private final Lexicon dictionary = new KoreanDictionary();

    private SpellCheckerSingleton() {

    }

    public static final SpellCheckerSingleton INSTANCE = new SpellCheckerSingleton();

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}
