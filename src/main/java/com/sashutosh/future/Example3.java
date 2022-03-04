package com.sashutosh.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Example3 {
    public static void main(String[] args) {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(()->{
            System.out.println("Future started");
            try {
                TimeUnit.SECONDS.sleep(1);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Future is completed");
            return "Future completed";
        });
        try {
            System.out.println("Main started");
            completableFuture.get();
            System.out.println("Main completed");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
