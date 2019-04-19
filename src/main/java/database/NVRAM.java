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

    public void addTuple(Tuple dirtyTuple) {
        boolean isPage = false;
        for (Page page : pages) {
            if (page.idTable == dirtyTuple.getIdTable()) {
                page.addTupleToTable(dirtyTuple.getKey(), dirtyTuple.getValue());
                isPage = true;
            }

        }
        if (!isPage)
            tuples.add(dirtyTuple);

    }
}
