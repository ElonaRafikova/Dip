package database;

import java.util.Random;

public class Tuple {
    private long id;
    private long idTable;
    private Integer key;
    private Integer value;
    Random random = new Random();

    public Tuple(long idTable, int key, int value) {
        this.id = random.nextLong();
        this.idTable = idTable;
        this.setKey(key);
        this.setValue(value);
    }

    private void setKey(int key) {
        this.key = key;
    }

    public Integer getKey() {
        return key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public long getIdTable() {
        return idTable;
    }

    public void setIdTable(long idTable) {
        this.idTable = idTable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
