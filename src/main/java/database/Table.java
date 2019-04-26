package database;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public int id;
    public List<Tuple> tuples = new ArrayList<Tuple>();

    public Table(int idTable) {
        this.id = idTable;
    }

    public void writeTuple(int key, int value, int transactionId, long time) {

        for (Tuple tuple : tuples) {
            if (tuple.getKey() == key) {
                Tuple newTuple = tuple.copyOf();
                newTuple.setRandomId();
                newTuple.setValue(value);
                newTuple.previousVersion = tuple.getId();
                tuple.isValid = false;
                newTuple.committedTransactionId = transactionId;
                newTuple.time = time;
                tuples.add(newTuple);
                return;
            }
        }
        Tuple tuple = new Tuple(id, key, value);
        tuples.add(tuple);

    }

    public Integer readTuple(Integer key) {
        for (Tuple tuple : tuples) {
            if (tuple.getKey().equals(key) && tuple.isValid)
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

    public Table copyOf() {
        Table table=new Table(this.id);
        for (Tuple tuple : tuples) {
            table.tuples.add(tuple.copyOf());
        }
        return table;
    }
}
