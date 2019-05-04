package database;

public class Logger {

    private Database database;

    public Logger(Database database) {
        this.database = database;
    }

    public void addToDtt(Transaction transaction) {
        Latency.dramWriteLatency();
        database.dram.dtt.add(new DTTRecord(transaction.id, transaction.numberOfTable, transaction.idTuple));
    }

}
