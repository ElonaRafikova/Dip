package database;

class Constants {

    static final int NUMBER_OF_TRANS = 1000; //каждая 1000 т е  100 транз
    static final int FAILURE = 50; //середина
    static final int DRAM_READ_LATENCY = 1;
    static final int DRAM_WRITE_LATENCY = 1;
    static final int NVRAM_READ_LATENCY = 1;
    static final int NVRAM_WRITE_LATENCY = 2;
    static final int DISK_READ_LATENCY = 416;
    static final int DISK_WRITE_LATENCY = 5000;
    static final int NVRAM_PAGE_WRITE_LATENCY=10000;
    static final int NUMBER_OF_PAGES = 20;
    static final int NUMBER_OF_TUPLES = 5000;
    static final int RANGE = 10000;
    static long gap = 5000;
}
