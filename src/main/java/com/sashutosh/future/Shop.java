package com.sashutosh.future;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Shop {
    private final Random random = new Random();
    private final String shopName;

    public Shop(String name) {
        this.shopName = name;
    }

    static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private static void doSomeThing() {

        new Thread(() -> {
            try {
                System.out.println("Some long running computation in separate thread");
                Thread.sleep(100);
                System.out.println("Finished computation in separate thread");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {

        Shop shop = new Shop("Random");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsyncCompletableFuture("Dummy");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime);
        //Future<Double> futurePriceException = shop.getPriceAsyncCompletableFutureThrowsException("Dummy1");
        doSomeThing();
        try {
            Double price = futurePrice.get();
            System.out.println("The price of item is " + price);
            //futurePriceException.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");

        PriceFinder pf = new PriceFinder();
        start = System.nanoTime();
        System.out.println(pf.findPriceStreams("Handbag"));
        retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs for streams");

        start = System.nanoTime();
        System.out.println(pf.findPriceParallelStreams("Handbag"));
        retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs for parallel streams");

        start = System.nanoTime();
        System.out.println(pf.findPriceSimpleFuture("Handbag"));
        retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs for simple futures");

        start = System.nanoTime();
        System.out.println(pf.findPriceCompletableFuture("Handbag"));
        retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs for CompletableFutures");


    }


    public String getName() {
        return shopName;
    }

    public double getPrice(String product) {
        return calculatePrice(product);
    }

    public String getPriceWithDiscountCode(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[
                random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", shopName, price, code);
    }

    public Future<Double> getPriceAsyncFuture(String product) {

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        return executorService.submit(() -> calculatePrice(product));
    }

    public Future<Double> getPriceAsyncCompletableFuture(String product) {
        System.out.println("Started price calculation");
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {

            System.out.println("Async price calculation");
            double price = calculatePrice(product);
            futurePrice.complete(price);

        }).start();
        return futurePrice;
    }

    public Future<Double> getPriceAsyncCompletableFutureConcise(String product) {
        System.out.println("Started price calculation with supply");
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    public Future<Double> getPriceAsyncCompletableFutureThrowsException(String product) {
        System.out.println("Started price calculation");
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                System.out.println("Async price calculation");
                double price = calculatePriceWithException(product);
                futurePrice.complete(price);
            } catch (Exception exception) {
                futurePrice.completeExceptionally(exception);
            }
        }).start();
        return futurePrice;
    }

    private double calculatePriceWithException(String product) throws Exception {
        calculatePrice(product);
        throw new Exception("Price calculation failed, remote service is down");
    }

    public double calculatePrice(String product) {
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

}
