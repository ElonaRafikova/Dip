package database;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Database {
    private static final int NUMBER_OF_PAGES = 5;
    private static final int NUMBER_OF_TUPLES = 10;
    private static final int RANGE = 1000;
    public Disk disk;
    public NVRAM nvram;
    public DRAM dram;
    public Map<Integer, Integer> mapPageTable;
    public Logger logger;

    public Database() {
        this.disk = new Disk();
        this.nvram = new NVRAM();
        this.dram = new DRAM();
        mapPageTable = new HashMap<Integer, Integer>();
        this.logger = new Logger(this);

    }

    //перенести в utility
    public void fillDatabase() {
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_PAGES; i++) {
            Page page = new Page(i);
            if (i == 0) {
                page.addTupleToTable(0, 55);
                page.addTupleToTable(1, 11);
                page.addTupleToTable(2, 22);
                page.addTupleToTable(3, 33);
                page.addTupleToTable(4, 44);
                //disk.addPage(page);
            }
            for (int j = 0; j < NUMBER_OF_TUPLES; j++) {
                page.addTupleToTable(random.nextInt(RANGE), random.nextInt(RANGE));
            }
            disk.addPage(page);

        }
    }

    public void print() {
        System.out.println("Database");
        dram.print();
        nvram.print();
        disk.print();
    }


    public void flushDTT() {
        for (Tuple dirtyTuple : dram.dtt.dirtyTuples) {
            nvram.addTuple(dirtyTuple);
        }
        dram.dtt.dirtyTuples.clear();

    }
}

