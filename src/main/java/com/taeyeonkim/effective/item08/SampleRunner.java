package com.taeyeonkim.effective.item08;

public class SampleRunner {

    public static void main(String[] args) throws Exception {
        try (SampleResource sampleResource = new SampleResource()) {
            sampleResource.hello();
        }
    }

    private void run() {
        //run method가 끝나고 finalizerExample가 gc의 대상이 된다는 보장이 없음.
        // gc의 대상은 되지만 바로바로 gc가 일어나지 않음(finalize 가 언제 호출된다는 보장이 없음).
        FinalizerExample finalizerExample = new FinalizerExample();
        finalizerExample.hello();
    }
}
