package com.taeyeonkim.effective.item12;

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
        int result = Integer.hashCode(age);
        result = 31 * result + name.hashCode();
        return result;
    }

    /**
     * People 객체에 대한 field들을 리턴합니다.
     * 형식은 아래와 같습니다.
     * "이름-나이"
     */
    @Override
    public String toString() {
        return String.format("%s-%d", name, age);
    }
}