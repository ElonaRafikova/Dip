package database;

public class Logger {

    private Database database;

    public Logger(Database database) {
        this.database = database;
    }

    public void addToDtt(Transaction transaction) {
        database.dram.dtt.add(new DTTRecord(transaction.id, transaction.numberOfTable, transaction.idTuple));
    }

    public void commitTransaction(Transaction transaction, int cp, int cd) {
        //System.out.println("commit:" + transaction.toString());
        //database.nvram.logFile.records.add(new DTTRecord(transaction.id, transaction.numberOfTable, transaction.idTuple),cp,cd);


    }
}
