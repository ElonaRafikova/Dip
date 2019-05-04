package database;

import java.util.*;

import static database.Latency.*;

import static database.Constants.*;

public class TransactionManager {

    private List<Transaction> transactionBuffer = new ArrayList<Transaction>();
    private Database database;
    private static long nowTimestamp;
    private static long startTimestamp;
    private static long nextGroupCommit;
    private static List<Transaction> doneList = new ArrayList<Transaction>();
    private static CommitGap lastGap=new CommitGap(0,0);
    private static List<Transaction> groupCommit = new ArrayList<Transaction>();
    private boolean isDisk;

    public TransactionManager(List<Transaction> transactions, Database database ,boolean isDisk) {
        transactionBuffer.addAll(transactions);
        this.database = database;
        this.isDisk=isDisk;
    }

    public void startExecution()  {
        startTimestamp = System.nanoTime();
        nextGroupCommit = gap;
        executeTransactions();
    }

    public void executeTransactions()  {
        int i = 0;
        while (i < transactionBuffer.size()) {
            nowTimestamp = System.nanoTime() /startTimestamp;
            if (nowTimestamp < nextGroupCommit) {
                if (isDisk){
                    startWithDisk(transactionBuffer.get(i));
                }
                else {
                    startNoDisk(transactionBuffer.get(i));
                }
                groupCommit.add(transactionBuffer.get(i));
                i++;
                /*if (i == 4) {
                    doFailure();
                    return;
                }*/
            } else {
                System.out.println(groupCommit.size());
                doGroupCommit(groupCommit);

            }
        }
        doGroupCommit(groupCommit);
        System.out.println("All transaction from set is done");
    }

    public void startExecutionWithFailure()  {
        startTimestamp = System.nanoTime();
        nextGroupCommit = gap;
        executeTransactionsWithFailure();
    }

    public void executeTransactionsWithFailure()  {
        int i = 0;
        while (i < transactionBuffer.size()) {
            nowTimestamp = System.nanoTime() /startTimestamp;
            if (nowTimestamp < nextGroupCommit) {
                if (isDisk){
                    startWithDisk(transactionBuffer.get(i));
                }
                else {
                    startNoDisk(transactionBuffer.get(i));
                }
                groupCommit.add(transactionBuffer.get(i));
                i++;
                if (i == FAILURE) {
                    doFailure();
                    return;
                }
            } else {
                System.out.println(groupCommit.size());
                doGroupCommit(groupCommit);

            }
        }
        doGroupCommit(groupCommit);
        System.out.println("All transaction from set is done");
    }



    private void doGroupCommit(List<Transaction> groupCommit) {
        if (!groupCommit.isEmpty()) {
            groupCommit(groupCommit);
            doneList.addAll(groupCommit);
            groupCommit.clear();

        }
        else {
            nextGroupCommit +=gap;
            lastGap.cd=nextGroupCommit;
            nvramWriteLatency();
            database.nvram.logFile.records.add(new LogRecord(new DTTRecord(0,0,0),lastGap.cp,nextGroupCommit));
        }
    }

    private void groupCommit(List<Transaction> groupCommit) {
        long cp = groupCommit.get(groupCommit.size() - 1).commitTimestamp;
        database.flushDTT();
        long cd = System.nanoTime() /startTimestamp + gap;
        nextGroupCommit = cd;
        lastGap.cp=cp;
        lastGap.cd=cd;
        database.flushLog(cp, cd);
        System.out.println(cp + " " + " " + cd);
        System.out.println("end Group Commit");

    }

    private void falseFlushToNVRAM() {
        database.flushDTT();
    }

    public void doFailure()  {
        falseFlushToNVRAM();
        for(int i=0;i<database.dram.tuplesBuffer.size();i++) {
        dramWriteLatency();
        }
        database.dram.tuplesBuffer.clear();
        for(int i=0;i<database.dram.dtt.size();i++) {
            dramWriteLatency();
        }
        database.dram.dtt.clear();
        groupCommit.clear();
        doRecovery();
        continueExecution();
    }


    private void continueExecution()  {
        transactionBuffer.removeAll(doneList);
        long timestamp=System.nanoTime()/startTimestamp;
        if (timestamp>nextGroupCommit) {
            nextGroupCommit=timestamp+gap;
            lastGap.cp=timestamp;
            lastGap.cd=timestamp+gap;
        }
        executeTransactions();
    }

    private void doRecovery() {
        long startRecovery=System.nanoTime();
        System.out.println("gaps");
            System.out.println(lastGap.cp + " " + lastGap.cd);
        garbageCollector();
        System.out.println("recovery time :"+(System.nanoTime()-startRecovery)/Math.pow(10,9));
    }

    private void garbageCollector() {
        recoveryNvram();
    }

    private void recoveryNvram() {
        List<Integer> activeList = new ArrayList<Integer>();
        for (Page page : database.nvram.pages) {
            for (Tuple tuple : page.table.tuples) {
                nvramReadLatency();
                if (checkInGap(tuple, lastGap)) {
                    nvramWriteLatency();
                    tuple.isValid = false;
                    nvramReadLatency();
                    if (tuple.previousVersion != 0)
                        activeList.add(tuple.previousVersion);
                }
            }
        }
        if (!activeList.isEmpty())
            activate(activeList);
    }

    private void activate(List<Integer> activeList) {
        List<Integer> newActiveList = new ArrayList<Integer>();
        for (Integer idTuple : activeList) {
            Tuple tupleWithId = getTupleWithId(idTuple);
            if (checkInGap(tupleWithId, lastGap)) {
                if (tupleWithId.previousVersion != 0)
                    newActiveList.add(tupleWithId.previousVersion);
            }
            else {
                tupleWithId.isValid=true;
            }
        }
        if (!newActiveList.isEmpty())
            activate(newActiveList);
    }

    private Tuple getTupleWithId(Integer idTuple) {
        for (Page page : database.nvram.pages) {
            for (Tuple tuple : page.table.tuples) {
                nvramReadLatency();
                if (tuple.getId() == idTuple)
                    return tuple;
            }
        }
        return null;
    }

    private Boolean checkInGap(Tuple tuple, CommitGap gap) {
        return (tuple.time < gap.cd && tuple.time > gap.cp);
    }


    public Answer findTupleInDram(Transaction transaction)  {
        for (Tuple tuple : database.dram.tuplesBuffer) {
            dramReadLatency();
            if (tuple.getIdTable() == transaction.getNumberOfTable() && tuple.getKey() == transaction.getKey()) {
                return new Answer(true, null, tuple);
            }
        }
        return new Answer(false, null, null);
    }


    public Answer findTupleInNVRAM(Transaction transaction) {

        for (Page page : database.nvram.pages) {
            if (page.idTable == transaction.getNumberOfTable()) {
                for (Tuple tuple : page.table.tuples) {
                    nvramReadLatency();
                    if (tuple.getKey() == transaction.getKey() && tuple.isValid) {
                        return new Answer(true, null, tuple.copyOf());
                    }
                }
            }
        }

        for (Tuple tuple : database.nvram.tuples) {
            nvramReadLatency();
            if (tuple.getKey() == transaction.getKey() && tuple.getIdTable() == transaction.getNumberOfTable() && tuple.isValid) {
                return new Answer(true, null, tuple.copyOf());
            }
        }
        return new Answer(false, null, null);

    }

    public Answer findPageInDisk(Transaction transaction) {
        for (Page page : database.disk.pages) {
            diskReadLatency();
            if (page.idTable == transaction.getNumberOfTable()) {
                for (Tuple tuple : page.table.tuples) {
                    if (tuple.getKey() == transaction.getKey()) {
                        return new Answer(true, page.copyOf(), tuple.copyOf());
                    }
                }
            }
        }

        return new Answer(false, null, null);
    }


    public void startWithDisk(Transaction transaction)  {
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
                dramWriteLatency();
                database.dram.tuplesBuffer.add(isFoundNvram.tuple);
                execute(transaction);
            } else {
                isFoundDisk = findPageInDisk(transaction);
                if (isFoundDisk.is) {
                    transaction.idTuple = isFoundDisk.tuple.getId();
                    nvramReadLatency();
                    nvramWritePageLatency();
                    database.nvram.pages.add(isFoundDisk.page);
                    dramWriteLatency();
                    database.dram.tuplesBuffer.add(isFoundDisk.tuple);
                    execute(transaction);
                } else {
                    if (transaction.getValue() == -1) {
                        System.out.println("can not read");
                        System.out.println(transaction.toString());
                    } else {
                        dramWriteLatency();
                        database.dram.tuplesBuffer.add(new Tuple(transaction.numberOfTable, transaction.getKey(), transaction.getValue()));
                    }

                }
            }
        }

    }
    public void startNoDisk(Transaction transaction)  {
        Answer isFoundNvram;
        Answer foundDram = findTupleInDram(transaction);
        if (foundDram.is) {
            transaction.idTuple = foundDram.tuple.getId();
            execute(transaction);

        } else {
            isFoundNvram = findTupleInNVRAM(transaction);
            if (isFoundNvram.is) {
                transaction.idTuple = isFoundNvram.tuple.getId();
                dramWriteLatency();
                database.dram.tuplesBuffer.add(isFoundNvram.tuple);
                execute(transaction);
            } else {
                if (transaction.getValue() == -1) {
                    System.out.println("can not read");
                    System.out.println(transaction.toString());
                } else {
                    dramWriteLatency();
                    database.dram.tuplesBuffer.add(new Tuple(transaction.numberOfTable, transaction.getKey(), transaction.getValue()));
                }

            }
        }

    }


    public void execute(Transaction transaction)  {
        if (transaction.getValue() == -1) {

            System.out.println(database.dram.readTuple(transaction.getKey(), transaction.numberOfTable));
        } else {
            System.out.println(database.dram.readTuple(transaction.getKey(), transaction.numberOfTable));

            database.dram.writeTuple(transaction.getKey(), transaction.getValue(), System.nanoTime() / startTimestamp);
            database.logger.addToDtt(transaction);
            System.out.println(database.dram.readTuple(transaction.getKey(), transaction.numberOfTable));
        }
        transaction.commitTimestamp = System.nanoTime() /startTimestamp;
        System.out.println(transaction.toString());

    }


}
