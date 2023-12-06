package com.solvd.agency.multithreading.threads;

import com.solvd.agency.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterThread extends Thread {
    public static final Logger logger = LogManager.getLogger(Main.class);
    private String filePath;
    private String content;
    private final Object fileLock;


    public FileWriterThread(String filePath, String content, Object fileLock) {
        this.filePath = filePath;
        this.content = content;
        this.fileLock = fileLock;
    }

    @Override
    public void run() {
        synchronized (fileLock) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(content);
                writer.newLine();
                logger.info("Written content: " + content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}