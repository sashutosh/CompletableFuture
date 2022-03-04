package com.sashutosh.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Example1 {

    public static void main(String[] args) {

        CompletableFuture<String> completableFuture = new CompletableFuture();
        System.out.println("Main thread started");
        try {
            //Will block forever since future is not defined
            completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
