package database;

import java.util.ArrayList;
import java.util.List;

public class Disk {
    public List<Page> pages = new ArrayList();

    public void addPage(Page page) {
        pages.add(page);
    }
}
