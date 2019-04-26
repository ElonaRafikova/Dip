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
            int idTransaction = 0;
            if (i == 0) {
                page.addTupleToTable(new Tuple(0, 55), idTransaction);
                page.addTupleToTable(new Tuple(1, 11), idTransaction);
                page.addTupleToTable(new Tuple(2, 22), idTransaction);
                page.addTupleToTable(new Tuple(3, 33), idTransaction);
                page.addTupleToTable(new Tuple(4, 44), idTransaction);
                //disk.addPage(page);
            }
            if (i == 1) {
                page.addTupleToTable(new Tuple(0, 555), idTransaction);
                page.addTupleToTable(new Tuple(1, 111), idTransaction);
                page.addTupleToTable(new Tuple(2, 222), idTransaction);
                page.addTupleToTable(new Tuple(3, 333), idTransaction);
                page.addTupleToTable(new Tuple(4, 444), idTransaction);
                //disk.addPage(page);
            }
            for (int j = 0; j < NUMBER_OF_TUPLES; j++) {
                page.addTupleToTable(new Tuple(random.nextInt(RANGE), random.nextInt(RANGE)), idTransaction);
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
        for (DTTRecord dttRecord : dram.dtt) {
            Tuple tupleInDram = findTupleInDram(dttRecord.idTuple, dttRecord.idTable);
            //tupleInDram.committedTransactionId=dttRecord.idTransaction;
            nvram.addTuple(tupleInDram, dttRecord.idTransaction);
        }
        // dram.dtt.clear();

        /*for (Tuple dirtyTuple : dram.dtt.dirtyTuples) {
            nvram.addTuple(dirtyTuple);
        }*/
        // dram.dtt.dirtyTuples.clear();

    }

    public void flushLog(long cp, long cd) {
        for (DTTRecord dttRecord : dram.dtt) {
            nvram.logFile.records.add(new LogRecord(dttRecord,cp,cd));
        }
        dram.dtt.clear();
    }

    public Tuple findTupleInDram(Integer idTuple, int idTable) {
        for (Tuple tuple : this.dram.tuplesBuffer) {
            if (tuple.getId() == idTuple && tuple.getIdTable() == idTable) {
                return tuple;
                //execute
            }
        }
        return null;
    }


}

