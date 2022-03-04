package com.sashutosh.future;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class Example8 {

    public static void main(String[] args) {

        List<CompletableFuture<Double>> pageContentFutures = IntStream.rangeClosed(1,100)
                .mapToObj(x -> calculateRandom(x))
                .collect(toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()]));
        allFutures.join();
    }

    private static CompletableFuture<Double> calculateRandom(int x) {
        System.out.println("Calculating data for "+ x);
        try {
            Thread.sleep((long) (Math.random()*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Completed data for "+ x);
        return CompletableFuture.supplyAsync(() -> Math.random()*x);
    }
}
