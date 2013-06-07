package com.daodao.concurrent;

/**
 * Created with IntelliJ IDEA.
 *
 * @author guanpu
 *         Date: 13-5-6
 *         Time: 下午8:57
 *         To change this template use File | Settings | File Templates.
 */
public class Waiter {
    public static boolean shutdown = false;
    public static void main(String[] args) throws InterruptedException {
        Waiter waiter = new Waiter();
        Thread checkingThread = new Thread("SocketSendingTracker") {
            @Override
            public void run() {
                while (!shutdown) {
                    System.out.println("test-----"+System.currentTimeMillis());
                    synchronized (this) {
                        try {
                            wait(5000);
                        }
                        catch (InterruptedException e) {
                        }
                    }
                }
            }
        };
        checkingThread.setDaemon(true);
        checkingThread.start();
        for(int i=0;i<10;i++)  {
             Thread.sleep(1000l);

            synchronized (checkingThread) {
                try {
                    checkingThread.notify();
                }
                catch (IllegalMonitorStateException e) {
                }
            }
        }
        Thread.sleep(50000l);
    }
}
