package database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static database.Latency.*;


public class Database {
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
    public void fillDatabaseWBL(List<Page> pagesToAdd) {
        nvram.pages.addAll(pagesToAdd);
    }
    public void fillDatabaseMyWBL(List<Page> pagesToAdd) {
        disk.pages.addAll(pagesToAdd);
    }

    public void print() {
        System.out.println("Database");
        dram.print();
        nvram.print();
        disk.print();
    }


    public void flushDTT() {
        for (DTTRecord dttRecord : dram.dtt) {
            dramReadLatency();
            Tuple tupleInDram = findTupleInDram(dttRecord.idTuple, dttRecord.idTable);
            nvramWriteLatency();
            nvram.addTuple(tupleInDram, dttRecord.idTransaction);
        }

    }

    public void flushLog(long cp, long cd) {
        for (DTTRecord dttRecord : dram.dtt) {
            dramReadLatency();
            nvramWriteLatency();
            nvram.logFile.records.add(new LogRecord(dttRecord,cp,cd));
            dramWriteLatency();
        }
        dram.dtt.clear();
    }

    public Tuple findTupleInDram(Integer idTuple, int idTable) {
        for (Tuple tuple : this.dram.tuplesBuffer) {
            dramReadLatency();
            if (tuple.getId() == idTuple && tuple.getIdTable() == idTable) {
                return tuple;
            }
        }
        return null;
    }


}

