package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionManager {

    private List<Transaction> transactionBuffer = new ArrayList<Transaction>();
    private Database database;
    private Random rand =new Random();
    private static int nowTime = 0;

    public TransactionManager(List<Transaction> transactions, Database database) {
        transactionBuffer.addAll(transactions);
        this.database = database;
    }

    public void addTransaction(Transaction transaction) {
        transactionBuffer.add(transaction);
    }

    public void executeTransactions() {

        for (Transaction transaction : transactionBuffer) {
            start(transaction);
            //execute(transaction);
            //commit(transaction);

        }
        groupCommit();

    }


    public Answer findTupleInDram(Transaction transaction) {
        for (Tuple tuple : database.dram.tuplesBuffer) {
            if (tuple.getIdTable() == transaction.getNumberOfTable() && tuple.getKey() == transaction.getKey()) {

                return new Answer(true, null, tuple);
                //execute
            }
        }
        return new Answer(false, null, null);
    }

    ///for dtt
    public Tuple findTupleInDram(Integer idTuple, int key) {
        for (Tuple tuple : database.dram.tuplesBuffer) {
            if (tuple.getId() == idTuple && tuple.getKey() == key) {

                return tuple;
                //execute
            }
        }
        return null;
    }

    public Answer findTupleInNVRAM(Transaction transaction) {

        for (Page page : database.nvram.pages) {
            if (page.idTable == transaction.getNumberOfTable()) {
                for (Tuple tuple : page.table.tuples) {
                    if (tuple.getKey() == transaction.getKey()) {
                        return new Answer(true, null, tuple.copyOf());

                    }
                }
            }
        }

        //find in database.nvram.tuples
        for (Tuple tuple : database.nvram.tuples) {
            if (tuple.getKey() == transaction.getKey() && tuple.getIdTable() == transaction.getNumberOfTable()) {
                return new Answer(true, null, tuple.copyOf());
            }
        }
        return new Answer(false, null, null);

    }

    public Answer findPageInDisk(Transaction transaction) {
        for (Page page : database.disk.pages) {
            if (page.idTable == transaction.getNumberOfTable()) {
                for (Tuple tuple : page.table.tuples) {
                    if (tuple.getKey() == transaction.getKey()) {
                        return new Answer(true, page.copyOf(), tuple.copyOf());
                        //return page load to nvram
                        //return tuple to dram
                    }
                }
            }
        }

        return new Answer(false, null, null);
        //write empty tuple to dram

    }


    public void start(Transaction transaction) {
        Answer isFoundNvram;
        Answer isFoundDisk;
        Answer foundDram = findTupleInDram(transaction);
        if (foundDram.is) {
            transaction.idTuple = foundDram.tuple.getId();
            execute(transaction);

        } else {
            isFoundNvram = findTupleInNVRAM(transaction);
            if (isFoundNvram.is) {

                transaction.idTuple = isFoundNvram.tuple.getId();
                database.dram.tuplesBuffer.add(isFoundNvram.tuple);
                execute(transaction);
            } else {
                isFoundDisk = findPageInDisk(transaction);
                if (isFoundDisk.is) {

                    transaction.idTuple = isFoundDisk.tuple.getId();
                    database.nvram.pages.add(isFoundDisk.page);
                    database.dram.tuplesBuffer.add(isFoundDisk.tuple);
                    execute(transaction);
                } else {
                    if (transaction.getValue() == -1) {
                        System.out.println("can not read");
                        System.out.println(transaction.toString());
                    } else {
                        database.dram.tuplesBuffer.add(new Tuple(transaction.numberOfTable, transaction.getKey(), transaction.getValue()));
                    }

                }
            }
        }

    }

    public void execute(Transaction transaction) {
        if (transaction.getValue() == -1) {
            System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));
            System.out.println(database.dram.readTuple(transaction.getKey(), transaction.numberOfTable));
            //database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey());
            System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));
            System.out.println(database.dram.readTuple(transaction.getKey(), transaction.numberOfTable));

        } else {
            System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));
            System.out.println(database.dram.readTuple(transaction.getKey(), transaction.numberOfTable));
           //database.disk.pages.get(transaction.numberOfTable).table.writeTuple(transaction.getKey(), transaction.getValue());
            database.dram.writeTuple(transaction.getKey(),transaction.getValue());
            database.logger.addToDtt(transaction);
            System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));
            System.out.println(database.dram.readTuple(transaction.getKey(), transaction.numberOfTable));
        }
        transaction.commitTimestamp = System.nanoTime();
        System.out.println(transaction.toString());

    }

    private void groupCommit() {
        database.flushDTT();
        int cp = nowTime + rand.nextInt(100);
        int cd=cp+100;
        database.flushLog(cp,cd);
        nowTime = cd;
    }

}
