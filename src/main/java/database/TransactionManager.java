package database;

import java.util.*;

public class TransactionManager {

    private List<Transaction> transactionBuffer = new ArrayList<Transaction>();
    private Database database;
    private Random rand =new Random();
    private static int nowTime = 0;
    private static long nowTimestamp;
    private static long gap = 1000000;
    private static long startTimestamp;
    private static long nextGroupCommit;
    private static List<Transaction> doneList = new ArrayList<Transaction>();
    private static Set<CommitGap> gaps = new HashSet<CommitGap>();
    private static List<Transaction> groupCommit = new ArrayList<Transaction>();

    public TransactionManager(List<Transaction> transactions, Database database) {
        transactionBuffer.addAll(transactions);
        this.database = database;
    }

    public void addTransaction(Transaction transaction) {
        transactionBuffer.add(transaction);
    }

    public void startExecution() {
        startTimestamp = System.nanoTime();
        nextGroupCommit = gap;
        executeTransactions();
    }

    public void executeTransactions() {
        //nowTimestamp=System.nanoTime();
        // startTimestamp=System.nanoTime();
        //System.out.println(startTimestamp);

        int i = 0;
        while (i < transactionBuffer.size()) {
            // long n=System.nanoTime();
            //long nt=n- startTimestamp;
            //System.out.println(n);
            //System.out.println(nt);
            //System.out.println(nowTimestamp);
            nowTimestamp = System.nanoTime() - startTimestamp;
            if (nowTimestamp < nextGroupCommit) {
                start(transactionBuffer.get(i));
                groupCommit.add(transactionBuffer.get(i));
                i++;
                if (i == 4) {
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
            System.out.println("done: " + doneList.size());
        }
        nextGroupCommit += gap;
    }

    private void groupCommit(List<Transaction> groupCommit) {
        System.out.println("group commit");
        long cp = groupCommit.get(groupCommit.size() - 1).commitTimestamp;
        database.flushDTT();
        long cd = System.nanoTime() - startTimestamp + gap;
        nextGroupCommit = cd;
        database.flushLog(cp, cd);
        System.out.println(cp + " " + " " + cd);
        System.out.println("end Group Commit");

    }

    private void falseFlushToNVRAM(List<Transaction> groupCommit) {
        System.out.println("false flush");
        database.flushDTT();
        database.nvram.print();
    }

    public void doFailure() {
        falseFlushToNVRAM(groupCommit);
        database.dram.print();
        System.out.println("failure");
        database.dram.dtt.clear();
        database.dram.tuplesBuffer.clear();
        groupCommit.clear();
        doRecovery();
        database.print();
        continueExecution();
    }


    private void continueExecution() {
        transactionBuffer.removeAll(doneList);//from log
        System.out.println("next commit " + nextGroupCommit);
        System.out.println("continui" + (System.nanoTime() - startTimestamp));
        //nextGroupCommit+=gap;
        System.out.println("next commit " + nextGroupCommit);
        executeTransactions();
    }

    private void doRecovery() {
        gaps.add(database.nvram.getLastCommitGap());
        System.out.println("gaps");
        for (CommitGap commitGap : gaps) {
            System.out.println(commitGap.cp + " " + commitGap.cd);
        }
        garbageCollector();
    }

    private void garbageCollector() {
        recoveryNvram();
        //recoveryDisk();
    }

    private void recoveryDisk() {
    }

    private void recoveryNvram() {
        List<Integer> activeList = new ArrayList<Integer>();
        for (Page page : database.nvram.pages) {
            for (Tuple tuple : page.table.tuples) {
                if (checkInGap(tuple, gaps)) {
                    tuple.isValid = false;
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
            if (checkInGap(tupleWithId, gaps)) {
                tupleWithId.isValid = false;
                if (tupleWithId.previousVersion != 0)
                    newActiveList.add(tupleWithId.previousVersion);
            }
        }
        if (!newActiveList.isEmpty())
            activate(newActiveList);
    }

    private Tuple getTupleWithId(Integer idTuple) {
        for (Page page : database.nvram.pages) {
            for (Tuple tuple : page.table.tuples) {
                if (tuple.getId() == idTuple)
                    return tuple;
            }
        }
        return null;
    }

    private Boolean checkInGap(Tuple tuple, Set<CommitGap> gaps) {
        for (CommitGap commitGap : gaps) {
            if (tuple.committedTransactionId < commitGap.cd && tuple.committedTransactionId > commitGap.cp)
                return true;
        }
        return false;
    }

    private void groupCommit() {
        database.flushDTT();
        int cp = nowTime + rand.nextInt(100);
        int cd = cp + 100;
        database.flushLog(cp, cd);
        nowTime = cd;

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
                    if (tuple.getKey() == transaction.getKey() && tuple.isValid) {
                        return new Answer(true, null, tuple.copyOf());

                    }
                }
            }
        }

        //find in database.nvram.tuples
        for (Tuple tuple : database.nvram.tuples) {
            if (tuple.getKey() == transaction.getKey() && tuple.getIdTable() == transaction.getNumberOfTable() && tuple.isValid) {
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
            database.dram.writeTuple(transaction.getKey(), transaction.getValue(), System.nanoTime() - startTimestamp);
            database.logger.addToDtt(transaction);
            System.out.println(database.disk.pages.get(transaction.numberOfTable).table.readTuple(transaction.getKey()));
            System.out.println(database.dram.readTuple(transaction.getKey(), transaction.numberOfTable));
        }
        transaction.commitTimestamp = System.nanoTime() - startTimestamp;
        System.out.println(transaction.toString());

    }


}
