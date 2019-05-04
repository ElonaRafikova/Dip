package database;

import java.util.ArrayList;
import java.util.List;

public class DRAM {
    public List<Tuple> tuplesBuffer = new ArrayList<Tuple>();
    public List<DTTRecord> dtt = new ArrayList<DTTRecord>();

    public void print() {
        System.out.println("DRAM");
        for (Tuple tuple : tuplesBuffer) {
            System.out.println(tuple);
        }
    }

    public void writeTuple(int key, int value, long time) {

        for (Tuple tuple : tuplesBuffer) {
            Latency.dramReadLatency();
            if (tuple.getKey() == key) {
                tuple.setValue(value);
                tuple.time = time;
                Latency.dramWriteLatency();
            }
        }

    }

    public Integer readTuple(Integer key, Integer idTable) {
        Latency.dramReadLatency();
        for (Tuple tuple : tuplesBuffer) {
            if (tuple.getKey().equals(key) && tuple.getIdTable() == idTable)
                return tuple.getValue();
        }
        return -1;
    }
}
