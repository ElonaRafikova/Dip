package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionSet {
    private static final int number = 5;
    private static Random random = new Random();

    public List<Transaction> getWriteHeavySet() {

        /*List<Transaction> set = new ArrayList<Transaction>();
        for (int i = 0; i < id * 9 / 10; i++) {
            Transaction tr = new Transaction();
            tr.write(random.nextInt(5), random.nextInt(1000), random.nextInt(1000));
            set.add(tr);
        }
        for (int i = 0; i < id * 1 / 10; i++) {
            Transaction tr = new Transaction();
            tr.read(random.nextInt(5), random.nextInt(1000));
            set.add(tr);
        }
        return set;
         */
        List<Transaction> set = new ArrayList<Transaction>();
        for (int i = 0; i < 4; i++) {
            Transaction tr = new Transaction();
            tr.write(0, i, random.nextInt(1000));
            set.add(tr);
        }
        for (int i = 0; i < 1; i++) {
            Transaction tr = new Transaction();
            tr.read(0, 2);
            set.add(tr);
        }
        return set;
    }

    public List<Transaction> getReadHeavySet() {
        List<Transaction> set = new ArrayList<Transaction>();
        return set;
    }

    public List<Transaction> getBalancedSet() {
        List<Transaction> set = new ArrayList<Transaction>();
        return set;
    }
}
