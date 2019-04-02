package com.taeyeonkim.effective.item05;

import java.util.List;

public class SpellChecker {
    private static final Lexicon dictionary = new KoreanDictionary();

    private SpellChecker() {
        // Noninstantiable
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }
}

interface Lexicon {
}

class KoreanDictionary implements Lexicon {
}