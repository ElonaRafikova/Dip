package tests;
import database.Database;
import database.Page;
import database.TransactionManager;
import database.TransactionSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class Recovery {

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
        transactionManager.startExecutionWithFailure();
    }

    @Test
    public void BalancedTest() {
        transactionManager = new TransactionManager(transactionSet.getBalancedSet(), database, isDisk);
    }
}

