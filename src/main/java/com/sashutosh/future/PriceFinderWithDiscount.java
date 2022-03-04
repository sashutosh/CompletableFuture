package com.sashutosh.future;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

public class PriceFinderWithDiscount {

    List<Shop> shops = Arrays.asList(new Shop("Flipkart")
            , new Shop("Amazon")
            , new Shop("Myntra")
            , new Shop("Walmart")
            , new Shop("Jabong")
            , new Shop("Amazon US")
            , new Shop("MnS US")
            , new Shop("Nike")
            , new Shop("Pepe"));

    public static void main(String[] args) {

        PriceFinderWithDiscount pfwd = new PriceFinderWithDiscount();

        System.out.println("Started price calculation comparisons");

        long start = System.nanoTime();
        System.out.println(pfwd.findPriceStreams("Handbag"));
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs for streams");

        start = System.nanoTime();
        System.out.println(pfwd.findPricesCompletableFuture("Handbag"));
        retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs for completable futures");
    }

    public List<String> findPriceStreams(String product) {
        return shops.stream()
                .map(shop -> shop.getPriceWithDiscountCode(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

    public List<String> findPricesCompletableFuture(String product) {

        final Executor executor =
                Executors.newFixedThreadPool(Math.min(shops.size(), 100),
                        (Runnable r) -> {
                            Thread t = new Thread(r);
                            t.setDaemon(true);
                            return t;
                        });

        List<CompletableFuture<String>> futures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPriceWithDiscountCode(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() -> Discount.applyDiscount(quote), executor)))
                .collect(toList());
        return futures.stream()
                .map(future -> future.join())
                .collect(toList());

    }

}
