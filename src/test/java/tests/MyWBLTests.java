package tests;
import database.Database;
import database.Page;
import database.TransactionManager;
import database.TransactionSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MyWBLTests {

    public static Database database;
    public static TransactionManager transactionManager;
    public static TransactionSet transactionSet;
    private static final boolean isDisk = true;


    @Before
    public void before() {
        database = new Database();
        transactionSet = new TransactionSet();
        List<Page> pages = transactionSet.getInitialDatabase();
        database.fillDatabaseMyWBL(pages);

    }

    @After
    public void after() {
        transactionManager.startExecution();
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
