package database;

import java.util.Random;

import static database.Constants.*;
public class Page {
    public int number;
    public int idTable;
    public Table table;
    public Random random = new Random();

    public Page(int numberOfPage) {
        this.number = numberOfPage;
        this.idTable = random.nextInt(RANGE);
        this.table = new Table(idTable);
    }

    public void addTupleToTable(Tuple tuple, int idTransaction) {
        table.writeTuple(tuple.getKey(), tuple.getValue(), idTransaction, tuple.time);
    }


    public void print() {
        System.out.println("Page " + number + " " + idTable);
        table.print();
    }
    public Page(){

    }

    public Page copyOf() {
        Page page=new Page();
        page.idTable=this.idTable;
        page.number=this.number;
        page.table=this.table.copyOf();
        return page;

    }
}
