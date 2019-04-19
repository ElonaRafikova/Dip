package database;

import java.util.Random;

public class Transaction {
    public int id;
    public int numberOfTable;
    public int key;
    public int value;
    public Random random = new Random();
    public int idTuple;

    public Transaction() {
        this.id = random.nextInt(1000);
    }

    public Transaction(int numberOfTable, int key, int value) {
        this.id = random.nextInt(1000);
        this.numberOfTable = numberOfTable;
        this.key = key;
        this.value = value;
    }


    public void setValue(int value) {
        this.value = value;
    }

    public void write(int idTable, int key, int value) {
        setNumberOfTable(idTable);
        setKey(key);
        setValue(value);

    }

    public void read(int idTable, int key) {
        setNumberOfTable(idTable);
        setKey(key);
        setValue(-1);

    }

    public void start() {

    }

    public void execute() {
    }

    public void commit() {
        //логгер
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getNumberOfTable() {
        return numberOfTable;
    }

    public void setNumberOfTable(int numberOfTable) {
        this.numberOfTable = numberOfTable;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return id + " " + numberOfTable + " " + key + " " + value + "";
    }
}
