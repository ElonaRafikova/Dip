package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionSet {
    private static final int number = 1000;
    private static Random random = new Random();

    public List<Transaction> getWriteHeavySet() {
        List<Transaction> set = new ArrayList<Transaction>();
        for (int i = 0; i < number * 9 / 10; i++) {
            Transaction tr = new Transaction();
            tr.write(1, random.nextInt(1000), random.nextInt(1000));
            set.add(tr);
        }
        for (int i = 0; i < number * 1 / 10; i++) {
            Transaction tr = new Transaction();
            tr.read(1, random.nextInt(1000));
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
