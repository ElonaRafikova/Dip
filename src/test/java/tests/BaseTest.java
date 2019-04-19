package tests;

import database.Database;
import database.TransactionManager;
import database.TransactionSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class BaseTest {

    public static Database database;
    public static TransactionManager transactionManager;
    public static TransactionSet transactionSet;

    @BeforeClass
    public static void beforeClass() {
        database = new Database();
        database.fillDatabase();
    }

    @Before
    public void before() {

    }

    @After
    public void after() {

    }

    @AfterClass
    public static void afterClass() {

    }

}
