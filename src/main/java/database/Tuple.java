package database;

import java.util.Random;

public class Tuple {
    private int id;
    private int idTable;
    public int previousVersion;
    public int committedTransactionId;
    public long time;
    public Boolean isValid;
    private int key;
    private int value;
    Random random = new Random();

    public Tuple(int idTable, int key, int value) {
        this.id = random.nextInt(1000);
        this.idTable = idTable;
        this.setKey(key);
        this.setValue(value);
        this.isValid = true;
    }

    public Tuple(int key, int value) {
        this.key = key;
        this.value = value;

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

    public void setIdTable(int idTable) {
        this.idTable = idTable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return id + " " + idTable + " " + key + " " + value + " " + isValid + " " + previousVersion + " " + committedTransactionId + " " + time;
    }

    public Tuple() {
    }

    public Tuple copyOf(){
        Tuple tuple=new Tuple();
        tuple.id=this.id;
        tuple.idTable=this.idTable;
        tuple.key=this.key;
        tuple.value=this.value;
        tuple.isValid = this.isValid;
        tuple.previousVersion = this.previousVersion;
        tuple.committedTransactionId = this.committedTransactionId;
        tuple.time = this.time;
        return  tuple;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        return id == tuple.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setRandomId() {
        this.id = random.nextInt(1000);
    }
}
