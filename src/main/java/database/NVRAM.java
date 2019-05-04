package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants.RANGE;

public class NVRAM {
    public List<Page> pages = new ArrayList<Page>();
    public LogFile logFile = new LogFile();
    public List<Tuple> tuples = new ArrayList<Tuple>();

    public NVRAM() {
    }
    public void print() {
        System.out.println("NVRAM");
        for (Page page : pages) {
            page.print();
        }
    }

    public void addTuple(Tuple dirtyTuple, int idTransaction) {

        for (Page page : pages) {
            Latency.nvramReadLatency();
            if (page.idTable == dirtyTuple.getIdTable()) {
                Latency.nvramWriteLatency();
                page.addTupleToTable(dirtyTuple, idTransaction);
                return;
            }

        }
        Latency.nvramWriteLatency();
        tuples.add(dirtyTuple.copyOf());

    }
}
