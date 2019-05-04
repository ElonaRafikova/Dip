package database;

import static database.Constants.*;

public class Latency {

    static public void dramReadLatency(){
        try {
            Thread.sleep(0, DRAM_READ_LATENCY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static public void dramWriteLatency(){
        try {
            Thread.sleep(0, DRAM_WRITE_LATENCY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static public void nvramReadLatency(){
        try {
            Thread.sleep(0, NVRAM_READ_LATENCY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static public void nvramWriteLatency(){
        try {
            Thread.sleep(0, NVRAM_WRITE_LATENCY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static public void diskReadLatency(){
        try {
            Thread.sleep(0, DISK_READ_LATENCY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static public void diskWriteLatency(){
        try {
            Thread.sleep(0, DISK_WRITE_LATENCY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void nvramWritePageLatency() {
        try {
            Thread.sleep(0, NVRAM_PAGE_WRITE_LATENCY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
