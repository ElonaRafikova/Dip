package database;

public class LogRecord {
    public long idTransaction;
    public long idTable;
    public long idTuple;

    public LogRecord(long idTransaction, long idTable, long idTuple) {
        this.idTransaction = idTransaction;
        this.idTable = idTable;
        this.idTuple = idTuple;
    }

}
