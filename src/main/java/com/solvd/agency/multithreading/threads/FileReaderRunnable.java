package com.solvd.agency.multithreading.threads;

import com.solvd.agency.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderRunnable implements Runnable {

    public static final Logger logger = LogManager.getLogger(Main.class);
    private String filePath;
    private final Object fileLock;


    public FileReaderRunnable(String filePath, Object fileLock) {
        this.filePath = filePath;
        this.fileLock = fileLock;
    }

    @Override
    public void run() {
        synchronized (fileLock) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("Read line: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}