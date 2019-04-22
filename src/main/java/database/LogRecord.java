package database;

public class LogRecord {
    public DTTRecord record;
    public int cp;
    public int cd;

    public LogRecord(DTTRecord record,int cp,int cd) {
        this.record=record;
        this.cp=cp;
        this.cd=cd;
    }

    public void print() {
        System.out.println("LogRecord");
        record.print();
        System.out.println(cp+" "+cd);
    }

}
