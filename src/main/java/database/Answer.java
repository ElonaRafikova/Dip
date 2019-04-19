package database;

public class Answer {
    public Boolean is;
    public Page page;
    public Tuple tuple;

    public Answer(Boolean is, Page page, Tuple tuple) {
        this.is = is;
        this.page = page;
        this.tuple = tuple;
    }
}
