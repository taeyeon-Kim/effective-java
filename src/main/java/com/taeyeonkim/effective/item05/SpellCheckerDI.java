package com.taeyeonkim.effective.item05;

import java.util.List;
import java.util.Objects;

public class SpellCheckerDI {
    private final Lexicon dictionary;

    public SpellCheckerDI(Lexicon dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary);
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Lexicon dictionary = new TestDictionary();
        //Lexicon dictionary = new KoreanDictionary();

        SpellCheckerDI spellCheckerDI = new SpellCheckerDI(dictionary);
    }
}


class TestDictionary implements Lexicon {}