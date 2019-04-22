package tests;


import database.TransactionManager;
import database.TransactionSet;
import database.Tuple;
import org.junit.Before;
import org.junit.Test;

public class SimpleTest extends BaseTest {

    @Before
    public void before() {
        transactionSet = new TransactionSet();
        transactionManager = new TransactionManager(transactionSet.getWriteHeavySet(), database);
    }

    @Test
    public void test(){
        /*for (Page page : database.disk.pages) {
            System.out.println("page");
            for (Tuple tuple : page.table.tuples) {
                System.out.println(tuple.getKey()+""+tuple.getValue());
            }

        }*/

        /*Transaction transaction=new Transaction();
        System.out.println(database.disk.pages.get(0).table.readTuple(2345));
        transaction.write(database.disk.pages.get(0).numberOfTable,2345,3456);
        database.disk.pages.get(0).table.writeTuple(transaction.key,transaction.value);
        System.out.println(database.disk.pages.get(0).table.readTuple(2345));
        database.disk.pages.get(0).table.writeTuple(transaction.key,567);
        System.out.println(database.disk.pages.get(0).table.readTuple(2345));*/
        database.disk.pages.get(0).idTable = 0;
        for (Tuple tuple : database.disk.pages.get(0).table.tuples) {
            tuple.setIdTable(0);
        }
        for (Tuple tuple : database.disk.pages.get(1).table.tuples) {
            tuple.setIdTable(1);
        }
        database.print();
        transactionManager.executeTransactions();
        System.out.println("first set");
        database.print();
        database.nvram.logFile.print();
        transactionManager=new TransactionManager(transactionSet.getReadHeavySet(),database);
        transactionManager.executeTransactions();
        System.out.println("second set");
        database.nvram.logFile.print();
        database.print();





    }
}
