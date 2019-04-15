package database;

import java.util.Random;


public class Database {
    private static final int NUMBER_OF_PAGES = 5;
    private static final int NUMBER_OF_TUPLES = 100;
    private static final int RANGE = 1000;
    public Disk disk;
    public NVRAM nvram;
    public DRAM dram;

    public Database() {
        this.disk = new Disk();
        this.nvram = new NVRAM();
        this.dram = new DRAM();
    }

    //еренести в utility
    public void fillDatabase() {
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_PAGES; i++) {
            Page page = new Page();
            for (int j = 0; j < NUMBER_OF_TUPLES; j++) {
                page.addTupleToTable(random.nextInt(RANGE), random.nextInt(RANGE));
            }
            disk.addPage(page);
        }

    }
}
