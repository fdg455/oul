package com.oushangfeng.ounews;

import org.junit.Test;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        print();
    }

    private void print() {
        if (new Object() {
            @Override
            public boolean equals(Object obj) {
                System.out.println("a");
                return super.equals(obj);
            }
        }.equals(1)) {
            System.out.println("a");
        } else {
            System.out.println("b");
        }
    }

}