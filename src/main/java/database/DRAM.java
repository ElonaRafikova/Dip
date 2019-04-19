package database;

import java.util.ArrayList;
import java.util.List;

public class DRAM {
    public List<Tuple> tuplesBuffer = new ArrayList<Tuple>();
    //public List<LogRecord> dtt = new ArrayList<LogRecord>();
    public DTT dtt = new DTT();

    public void print() {
        System.out.println("DRAM");
        for (Tuple tuple : tuplesBuffer) {
            System.out.println(tuple);
        }
    }
}
