package database;

public class DTTRecord {
    public int idTransaction;
    public int idTable;
    public int idTuple;

    public DTTRecord(int idTransaction, int idTable, int idTuple) {
        this.idTransaction = idTransaction;
        this.idTable = idTable;
        this.idTuple = idTuple;
    }

    public void print() {
        System.out.println("DTTRecord");
        System.out.println(idTransaction + " " + idTable + " " + idTuple);
    }

}
