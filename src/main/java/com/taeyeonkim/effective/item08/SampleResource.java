package com.taeyeonkim.effective.item08;

public class SampleResource implements AutoCloseable {
    @Override
    public void close() throws Exception {
        System.out.println("close");
    }

    public void hello() {
        System.out.println("hello");
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}
