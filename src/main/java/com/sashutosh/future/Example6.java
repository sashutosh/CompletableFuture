package com.sashutosh.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Example6 {
    //Combining completable futures one future is dependent on another using thenCompose
    public static CompletableFuture<String> getUsersDetail(String userId) {
        return CompletableFuture.supplyAsync(() -> "User"+userId);
    }
    public static CompletableFuture<Double> getCreditRating(String user) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Calculating credit rating for user " +user);
            return Math.random();
        });
    }
    public static void main(String[] args) {

        CompletableFuture<Double> future = getUsersDetail("1").thenCompose(Example6::getCreditRating);
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
