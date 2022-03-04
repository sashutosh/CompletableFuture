package com.sashutosh.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Example2 {
    public static void main(String[] args) {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(()->{
            try {
                System.out.println("Future started");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Future is completed");
        });

        System.out.println("Main started");
        try {
            completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Main completed");
    }
}
