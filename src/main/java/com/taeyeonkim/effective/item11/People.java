package com.taeyeonkim.effective.item11;

public class People {
    private String name;
    private int age;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof People)) {
            return false;
        }

        People people = (People) o;
        return (people.age == age) &&
                (people.name != null && people.name.equals(name));
    }

    @Override
    public int hashCode() {
        // 기본 타입 필드라면, Type.hashCode(field)를 수행.
        // field는 equals에 사용된 핵심 필드를 말한다.
        int result = Integer.hashCode(age);
        result = 31 * result + name.hashCode();
        return result;
    }
}