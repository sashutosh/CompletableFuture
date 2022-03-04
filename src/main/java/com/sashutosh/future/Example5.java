package com.sashutosh.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Example5 {
    public static void main(String[] args) {
        try {
            CompletableFuture.supplyAsync(()->{
                System.out.println("Future started");
                try {
                    TimeUnit.SECONDS.sleep(1);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Future is completed");
                return "Future completed";
            }).thenAccept(s-> System.out.println("Previous future response is " + s)).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }
}
