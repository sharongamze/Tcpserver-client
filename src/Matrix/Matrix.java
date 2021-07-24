package Matrix;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Matrix implements Serializable {

    int[][] primitiveMatrix;

    public Matrix(int[][] oArray){
        primitiveMatrix = Arrays
                .stream(oArray)
                .map(int[]::clone)
                .toArray(int[][]::new);
    }
    public void printMatrix(){
        for (int[] row : primitiveMatrix) {
            String s = Arrays.toString(row);
            System.out.println(s);
        }
    }

    public List<Index> getAllNodes() {
        List<Index> allNodes = new LinkedList<>();
        for (int i = 0; i <this.primitiveMatrix.length ; i++){
            for (int j = 0; j <this.primitiveMatrix[0].length ; j++) {
                    Index index= new Index(i,j);
                    allNodes.add(index);
            }
        }
        return allNodes;
    }

    public final int[][] getPrimitiveMatrix() {
        return primitiveMatrix;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : primitiveMatrix) {
            stringBuilder.append(Arrays.toString(row));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public Collection<Index> getNeighbors(final Index index){
        Collection<Index> list = new ArrayList<>();
        int extracted = -1;
        try {
            extracted = primitiveMatrix[index.row + 1][index.column];
            list.add(new Index(index.row + 1, index.column));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            extracted = primitiveMatrix[index.row][index.column + 1];
            list.add(new Index(index.row, index.column + 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            extracted = primitiveMatrix[index.row - 1][index.column];
            list.add(new Index(index.row - 1, index.column));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            extracted = primitiveMatrix[index.row][index.column - 1];
            list.add(new Index(index.row, index.column - 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            extracted = primitiveMatrix[index.row - 1][index.column - 1];
            list.add(new Index(index.row - 1, index.column - 1));
        } catch (ArrayIndexOutOfBoundsException outOfBounds) {
        }

        try {
            extracted = primitiveMatrix[index.row + 1][index.column + 1];
            list.add(new Index(index.row + 1, index.column + 1));
        } catch (ArrayIndexOutOfBoundsException outOfBounds) {
        }

        try {
            extracted = primitiveMatrix[index.row + 1][index.column - 1];
            list.add(new Index(index.row + 1, index.column - 1));
        } catch (ArrayIndexOutOfBoundsException outOfBounds) {
        }

        try {
            extracted = primitiveMatrix[index.row - 1][index.column + 1];
            list.add(new Index(index.row- 1, index.column + 1));
        } catch (ArrayIndexOutOfBoundsException outOfBounds) {
        }
        return list;
    }

    public Collection<Index> getNeighbors_notdiagonal(final Index index){
        Collection<Index> list = new ArrayList<>();
        int extracted = -1;
        try{
            extracted = primitiveMatrix[index.row+1][index.column];
            list.add(new Index(index.row+1,index.column));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row][index.column+1];
            list.add(new Index(index.row,index.column+1));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row-1][index.column];
            list.add(new Index(index.row-1,index.column));
        }catch (ArrayIndexOutOfBoundsException ignored){}
        try{
            extracted = primitiveMatrix[index.row][index.column-1];
            list.add(new Index(index.row,index.column-1));
        }catch (ArrayIndexOutOfBoundsException ignored){}

        return list;
    }


    public int getValue(Index index) {
        return primitiveMatrix[index.row][index.column];
    }

    public Collection<Index> getReachables(Index index) {
        ArrayList<Index> filteredIndices = new ArrayList<>();
        this.getNeighbors(index).stream().filter(i-> getValue(i)==1)
                .map(neighbor->filteredIndices.add(neighbor)).collect(Collectors.toList());
        if (filteredIndices.isEmpty()) {
            return null;
        }
        return filteredIndices;
    }

    public static void main(String[] args) {
        int[][] source = {
                {1, 0, 0},
                {1, 0, 1},
                {0, 1, 1}
        };
        Matrix matrix = new Matrix(source);
        matrix.printMatrix();
        System.out.println(matrix.getNeighbors(new Index(1,1)));
        System.out.println(matrix.getReachables(new Index(1,0)));
    }
}