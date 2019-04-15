package database;

import java.util.Random;

public class Transaction {
    public long id;
    public long idTable;
    public int key;
    public int value;
    public Random random = new Random();

    public Transaction() {
        this.id = random.nextLong();
    }

    public Transaction(long idTable, int key, int value) {
        this.id = random.nextLong();
        this.idTable = idTable;
        this.key = key;
        this.value = value;
    }


    public void setValue(int value) {
        this.value = value;
    }

    public void write(long idTable, int key, int value) {
        setIdTable(idTable);
        setKey(key);
        setValue(value);

    }

    public void read(long idTable, int key) {
        setIdTable(idTable);
        setKey(key);
        setValue(-1);

    }

    public void start() {

    }

    public void execute() {
        //найти данные
        //изменить
    }

    public void commit() {
        //логгер
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdTable() {
        return idTable;
    }

    public void setIdTable(long idTable) {
        this.idTable = idTable;
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
}
