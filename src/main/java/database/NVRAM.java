package database;

import java.util.ArrayList;
import java.util.List;

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
            if (page.idTable == dirtyTuple.getIdTable()) {
                page.addTupleToTable(dirtyTuple, idTransaction);
                return;
            }

        }
        tuples.add(dirtyTuple.copyOf());

    }

    public CommitGap getLastCommitGap() {
        return logFile.records.get(logFile.records.size() - 1).commitGap;
    }
}
