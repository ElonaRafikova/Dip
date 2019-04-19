package database;

import java.util.ArrayList;
import java.util.List;

public class LogFile {
    List<LogRecord> records = new ArrayList<LogRecord>();

    public void print() {
        System.out.println("LogFile");
        for (LogRecord record : records) {
            record.print();

        }
    }
}
