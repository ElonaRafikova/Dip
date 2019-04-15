package database;

import java.util.Random;

public class Page {
    public long id;
    public long idTable;
    public Table table;
    public Random random = new Random();

    public Page() {
        this.id = random.nextLong();
        this.idTable = random.nextLong();
        this.table = new Table(idTable);
    }

    public void addTupleToTable(int key, int value) {
        table.writeTuple(key, value);
    }
}
