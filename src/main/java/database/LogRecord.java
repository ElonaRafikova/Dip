package database;

public class LogRecord {
    public DTTRecord record;
    public CommitGap commitGap = new CommitGap();

    public LogRecord(DTTRecord record, long cp, long cd) {
        this.record=record;
        this.commitGap.cp = cp;
        this.commitGap.cd = cd;
    }

    public void print() {
        System.out.println("LogRecord");
        record.print();
        System.out.println(commitGap.cp + " " + commitGap.cd);
    }

}
