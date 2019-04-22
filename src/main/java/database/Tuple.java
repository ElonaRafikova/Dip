package database;

import java.util.Random;

public class Tuple {
    private int id;
    private int idTable;
    private Integer key;
    private Integer value;
    Random random = new Random();

    public Tuple(int idTable, int key, int value) {
        this.id = random.nextInt(1000);
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
        return id + " " + idTable + " " + key + " " + value + "";
    }

    public Tuple() {
    }

    public Tuple copyOf(){
        Tuple tuple=new Tuple();
        tuple.id=this.id;
        tuple.idTable=this.idTable;
        tuple.key=this.key;
        tuple.value=this.value;
        return  tuple;

    }
}
