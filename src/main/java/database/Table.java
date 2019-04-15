package database;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public long id;
    public List<Tuple> tuples = new ArrayList();

    public Table(long idTable) {
        this.id = idTable;
    }

    public void writeTuple(int key, int value) {
        boolean isKey = false;
        for (Tuple tuple : tuples) {
            if (tuple.getKey() == key) {
                tuple.setValue(value);
                isKey = true;
            }
        }
        if (!isKey) {
            Tuple tuple = new Tuple(id, key, value);
            tuples.add(tuple);
        }
    }

    public Integer readTuple(Integer key) {
        for (Tuple tuple : tuples) {
            if (tuple.getKey().equals(key))
                return tuple.getValue();
        }
        return -1;
    }
}
