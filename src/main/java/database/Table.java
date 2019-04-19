package database;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public int id;
    public List<Tuple> tuples = new ArrayList();

    public Table(int idTable) {
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

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", tuples=" + tuples.toString() +
                '}';
    }

    public void print() {
        System.out.println("Table id " + id);
        for (Tuple tuple : tuples) {
            System.out.println(tuple.toString());
        }
    }
}
