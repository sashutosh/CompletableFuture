package com.sashutosh.future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class PriceFinder{
    List<Shop> shops = Arrays.asList(new Shop("Flipkart")
            ,new Shop("Amazon")
            ,new Shop("Myntra")
            ,new Shop("Walmart")
            ,new Shop("Jabong")
            ,new Shop("Amazon US")
            ,new Shop("MnS US")
            ,new Shop("Nike")
            ,new Shop("Pepe"));

    public List<String> findPriceStreams(String product){
        return shops.stream()
                .map(shop -> String.format("The price at %s, is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    public List<String> findPriceParallelStreams(String product){
        return shops.parallelStream()
                .map(shop -> String.format("The price at %s, is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    public List<String> findPriceSimpleFuture(String product) {

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        //return executorService.submit(() -> calculatePrice(product));
        List<Future<Double>> futures = shops.stream().
                map(shop -> {
                    return executorService.submit(() -> shop.calculatePrice(product));
                }).
                collect(Collectors.toList());
        return futures.stream().
                map(future -> {
                    try {
                        return future.get().toString();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());

    }

    public List<String> findPriceCompletableFuture(String product){

        final Executor executor =
                Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                        (Runnable r) -> {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            return t;
                        });

        List<CompletableFuture<String>> futures  = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("The price at %s, is %.2f", shop.getName(), shop.getPrice(product)),executor))
                .collect(Collectors.toList());

        return futures.stream()
                .map(future -> future.join())
                .collect(Collectors.toList());
    }

}