package com.taeyeonkim.effective.item09;

public class App {
    public static void main(String[] args) {
        MyResource myResource = null;
        try {
            myResource = new MyResource();
            myResource.doSomething();
        } finally {
            if (myResource != null) {
                myResource.close();
            }
        }

        try (MyResource myResource1 = new MyResource()) {
            myResource1.doSomething();
        }
    }
}
