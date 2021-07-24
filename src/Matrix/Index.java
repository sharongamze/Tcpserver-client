package Matrix;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class Index implements Serializable, Comparable<Index>{
    int row, column;

    // Constructor
    public Index(int oRow, int oColumn){
        this.row = oRow;
        this.column = oColumn;
    }

    @Override
    public String toString(){
        return "(" + row + "," + column + ")";
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return row == index.row &&
                column == index.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public int compareTo(Index o) {
        return Integer.compare(this.getRow(), o.getRow());
    }

    public static void main(String[] args) {
        Index myIndex = new Index(2,2);
        System.out.println(myIndex);
    }


}