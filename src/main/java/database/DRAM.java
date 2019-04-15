package database;

import java.util.ArrayList;
import java.util.List;

public class DRAM {
    public List<Tuple> tuplesBuffer = new ArrayList<Tuple>();
    public List<LogRecord> dtt = new ArrayList<LogRecord>();
}
