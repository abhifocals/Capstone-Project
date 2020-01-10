package com.focals.myreddit.network;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SubExecutors {

    private static Object LOCK = new Object();
    private static SubExecutors instance;
    private final Executor networkIO;


    public SubExecutors(Executor networkIO) {
        this.networkIO = networkIO;
    }

    public static SubExecutors getInstance() {

        synchronized (LOCK) {

            if (instance == null) {
                instance = new SubExecutors(Executors.newFixedThreadPool(3));
            }

        }

        return instance;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

}
