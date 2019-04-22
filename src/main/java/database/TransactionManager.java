package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionManager {

    private List<Transaction> transactionBuffer = new ArrayList<Transaction>();
    private Database database;
    private Random rand =new Random();

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

    public Answer findTupleInNVRAM(Transaction transaction) {

        for (Page page : database.nvram.pages) {
            if (page.idTable == transaction.getNumberOfTable()) {
                for (Tuple tuple : page.table.tuples) {
                    if (tuple.getKey() == transaction.getKey()) {
                        return new Answer(true, null, tuple.copyOf());

                    }
                    //return tuple to dram
                }
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
            execute(transaction, foundDram.tuple);
            transaction.idTuple = foundDram.tuple.getId();
        } else {
            isFoundNvram = findTupleInNVRAM(transaction);
            if (isFoundNvram.is) {
                database.dram.tuplesBuffer.add(isFoundNvram.tuple);
                execute(transaction, isFoundNvram.tuple);
                transaction.idTuple = isFoundNvram.tuple.getId();
            } else {
                isFoundDisk = findPageInDisk(transaction);
                if (isFoundDisk.is) {
                    database.nvram.pages.add(isFoundDisk.page);
                    database.dram.tuplesBuffer.add(isFoundDisk.tuple);
                    execute(transaction, isFoundDisk.tuple);
                    transaction.idTuple = isFoundDisk.tuple.getId();
                } else {
                    if (transaction.getValue() == -1) {
                        System.out.println("can not read");
                    } else {
                        database.dram.tuplesBuffer.add(new Tuple(transaction.numberOfTable, transaction.getKey(), transaction.getValue()));
                    }

                }
            }
        }

    }

    private Page getPageFromDisk(Transaction transaction) {
        return new Page(1);
    }

    private Tuple getTupleFromNvram(Transaction transaction) {
        return new Tuple(1, 1, 1);
    }


    public void execute(Transaction transaction, Tuple tuple) {
        if (transaction.getValue() == -1) {
            System.out.println(database.dram.readTuple(transaction.getKey()));
            //System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));
            System.out.println(transaction.toString());
            //database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey());
            System.out.println(database.dram.readTuple(transaction.getKey()));
            //System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));

        } else {
            System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));
            System.out.println(database.dram.readTuple(transaction.getKey()));
            System.out.println(transaction.toString());
           //database.disk.pages.get(transaction.numberOfTable).table.writeTuple(transaction.getKey(), transaction.getValue());
            database.dram.writeTuple(transaction.getKey(),transaction.getValue());
            database.logger.addToDtt(transaction, tuple);
            System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));
            System.out.println(database.dram.readTuple(transaction.getKey()));
        }

    }

   /* public void commit(Transaction transaction) {
        database.flushDTT();
        database.logger.commitTransaction(transaction, cp, cd);
    }*/

    private void groupCommit() {
        database.flushDTT();
        int cp=rand.nextInt(1000);
        int cd=cp+100;
        database.flushLog(cp,cd);

        /*for (Transaction transaction : transactionBuffer) {
            database.logger.commitTransaction(transaction,cp,cd);
        }*/
    }

}
