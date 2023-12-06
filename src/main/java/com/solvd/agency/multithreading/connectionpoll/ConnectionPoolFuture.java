package com.solvd.agency.multithreading.connectionpoll;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConnectionPoolFuture {
    public static void main(String[] args) {
        final int poolSize = 5;
        final int numberOfThreads = 7;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        ConnectionPool pool = ConnectionPool.getInstance(poolSize);

        CompletableFuture<?>[] futures = new CompletableFuture<?>[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    Connection connection = pool.getConnection();
                    System.out.println(Thread.currentThread().getName() + " started " + connection);

                    TimeUnit.SECONDS.sleep(1);

                    pool.releaseConnection(connection);
                    System.out.println(Thread.currentThread().getName() + " ended " + connection);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, executorService);
        }

        CompletableFuture.allOf(futures).join();

        executorService.shutdown();
    }
}
