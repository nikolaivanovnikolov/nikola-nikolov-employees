package ui.swing;

import java.util.List;

public interface DataProvider<T> {
    int getTotalRowCount();
    List<T> getRows(int startIndex, int endIndex);
}