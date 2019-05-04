package tests;
import database.Database;
import database.Page;
import database.TransactionManager;
import database.TransactionSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class WBLTests {

    public static Database database;
    public static TransactionManager transactionManager;
    public static TransactionSet transactionSet;
    private static final boolean isDisk = false;


    @Before
    public void before() {
        database = new Database();
        transactionSet = new TransactionSet();
        List<Page> pages = transactionSet.getInitialDatabase();
        database.fillDatabaseWBL(pages);

    }

    @After
    public void after() {
        long start=System.nanoTime();
        transactionManager.startExecution();
        System.out.println("time: "+(System.nanoTime()-start)/Math.pow(10,9));
    }

    @Test
    public void WriteHeavyTest() {
        transactionManager = new TransactionManager(transactionSet.getWriteHeavySet(), database, isDisk);
    }
    @Test
    public void ReadHeavyTest() {
        transactionManager = new TransactionManager(transactionSet.getReadHeavySet(), database, isDisk);
    }
    @Test
    public void BalancedTest() {
        transactionManager = new TransactionManager(transactionSet.getBalancedSet(), database, isDisk);
    }
}
