package com.sashutosh.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Example7 {

    //Combining two independent futures using thenCombine
    public static void main(String[] args) {
        CompletableFuture<Double> weightInKg = CompletableFuture.supplyAsync(() -> {

            try {
                System.out.println("Retrieving weight ");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 62.5;
        });

        CompletableFuture<Double> heightInCm = CompletableFuture.supplyAsync(() -> {

            System.out.println("Retrieving height ");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 180.0;
        });
        CompletableFuture<Double> future = weightInKg.thenCombine(heightInCm, (weight, height) -> {
            System.out.println("Calculating BMI");
            return weight / height;
        });
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
