package com.taeyeonkim.effective.item14;

import java.util.Comparator;

import static java.util.Comparator.comparingInt;

public class PhoneNumber implements Comparable<PhoneNumber> {
    private final short areaCode, lineNum, prefix;
    private static final Comparator<PhoneNumber> COMPARATOR = comparingInt((PhoneNumber pn) -> pn.areaCode).
            thenComparingInt(pn -> pn.prefix).
            thenComparingInt(pn -> pn.lineNum);

    public PhoneNumber(int areaCode, int lineNum, int prefix) {
        this.areaCode = (short) areaCode;
        this.lineNum = (short) lineNum;
        this.prefix = (short) prefix;
    }

    @Override
    public int compareTo(PhoneNumber o) {
        return COMPARATOR.compare(this, o);
    }
}