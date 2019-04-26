package com.taeyeonkim.effective.item09;

public class MyResource implements AutoCloseable {

    public void doSomething() throws FirstError {
        System.out.println("do Something");
        throw new FirstError();
    }

    @Override
    public void close() throws SecondError {
        System.out.println("Close MyResource");
        throw new SecondError();
    }
}
