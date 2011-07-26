package com.bumerang.util;

import java.util.LinkedList;

public class DownloadThreadQueue
{
    private final int nThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;
    private static DownloadThreadQueue instance;

    private DownloadThreadQueue(int nThreads)
    {
        this.nThreads = nThreads;
        queue = new LinkedList();
        threads = new PoolWorker[nThreads];

        for (int i=0; i<nThreads; i++) {
            threads[i] = new PoolWorker();
            threads[i].start();
        }
    }
    
    public static DownloadThreadQueue getInstance()
    {
    	if(instance==null)
    	{
    		instance = new DownloadThreadQueue(1);
    		return instance;
    	}
    	else return instance;
    }

    public void execute(Runnable r) {
        synchronized(queue) {
            queue.addLast(r);
            queue.notify();
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            Runnable r;

            while (true) {
                synchronized(queue) {
                    while (queue.isEmpty()) {
                        try
                        {
                            queue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }

                    r = (Runnable) queue.removeFirst();
                }

                // If we don't catch RuntimeException, 
                // the pool could leak threads
                try {
                    r.run();
                }
                catch (RuntimeException e) {
                    // You might want to log something here
                }
            }
        }
    }
}
