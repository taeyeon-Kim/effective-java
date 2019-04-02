package com.taeyeonkim.effective.item05;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SpellCheckerSupplier {
    private final Lexicon dictionary;

    public SpellCheckerSupplier(Supplier<Lexicon> dictionary) {
        this.dictionary = Objects.requireNonNull(dictionary.get());
    }

    public static boolean isValid(String word) {
        throw new UnsupportedOperationException();
    }

    public static List<String> suggestions(String typo) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        Lexicon lexicon = new TestDictionary();
        SpellCheckerSupplier spellCheckerSupplier = new SpellCheckerSupplier(() -> lexicon);
    }
}
