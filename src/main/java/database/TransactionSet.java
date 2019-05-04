package database;

import java.util.*;

import static database.Constants.*;


public class TransactionSet {
    private static Random random = new Random();
    public static List<Pair> keys= new ArrayList<Pair>();

    public List<Page> getInitialDatabase(){
        List<Page> pages=new ArrayList<Page>();
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_PAGES; i++) {
            Page page = new Page(i);
            for (int j = 0; j < NUMBER_OF_TUPLES; j++) {
                int k=random.nextInt(RANGE);
                page.addTupleToTable(new Tuple(k,random.nextInt(RANGE)), random.nextInt(RANGE));
                if(j% NUMBER_OF_TRANS ==0){
                    keys.add(new Pair(k,page.idTable));
                }
            }
            pages.add(page);
        }
        return  pages;
    }

    public List<Transaction> getWriteHeavySet() {

        return getTransactions(90);
    }

    public List<Transaction> getReadHeavySet() {
        return getTransactions(10);
    }

    public List<Transaction> getBalancedSet() {
        return getTransactions(50);
    }

    private List<Transaction> getTransactions(int i2) {
        List<Transaction> set = new ArrayList<Transaction>();
        int a = keys.size() / 100 * i2;
        for (int i = 0; i < a; i++) {
            Transaction tr = new Transaction();
            tr.write(keys.get(i).idTable, keys.get(i).key, random.nextInt(RANGE));
            set.add(tr);
        }
        for (int i = a; i < keys.size(); i++) {
            Transaction tr = new Transaction();
            tr.read(keys.get(i).idTable, keys.get(i).key);
            set.add(tr);
        }
        Collections.shuffle(set);
        return set;
    }
}
