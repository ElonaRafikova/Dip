package database;

public class LogRecord {
    public int idTransaction;
    public int idTable;
    public int idTuple;

    public LogRecord(int idTransaction, int idTable, int idTuple) {
        this.idTransaction = idTransaction;
        this.idTable = idTable;
        this.idTuple = idTuple;
    }

    public void print() {
        System.out.println("LogRecord");
        System.out.println(idTransaction + " " + idTable + " " + idTuple);
    }

}
