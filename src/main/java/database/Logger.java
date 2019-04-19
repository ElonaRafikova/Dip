package database;

public class Logger {

    public static final int ID_TUPLE = 5;
    private Database database;

    public Logger(Database database) {
        this.database = database;
    }

    public void addToDtt(Transaction transaction, Tuple tuple) {
        database.dram.dtt.dtt.add(new LogRecord(transaction.id, transaction.numberOfTable, ID_TUPLE));
        database.dram.dtt.dirtyTuples.add(tuple);
    }

    public void commitTransaction(Transaction transaction) {
        System.out.println("commit:" + transaction.toString());
        database.nvram.logFile.records.add(new LogRecord(transaction.id, transaction.numberOfTable, transaction.idTuple));


    }
}
