package com.solvd.agency.multithreading.connectionpoll;


import com.solvd.agency.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ConnectionImplementation implements Runnable {

    public static final Logger logger = LogManager.getLogger(Main.class);


    private final ConnectionPool connectionPool;

    public ConnectionImplementation(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void run() {
        try {
            Connection connection = connectionPool.getConnection();
            logger.info(Thread.currentThread().getName() + " started " + connection);

            Thread.sleep(1000);

            connectionPool.releaseConnection(connection);
            System.out.println(Thread.currentThread().getName() + " stopped " + connection);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        runThreads();
    }

    public static void runThreads() {
        List<Thread> threads = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance(5);

        for (int i = 0; i < 7; i++) {
            Thread thread = new Thread(new ConnectionImplementation(pool));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}



