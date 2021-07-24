package algorithms;
import Matrix.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Submarine {
    public Matrix matrix;
    public int num_of_submarines,min_row, max_row, min_col, max_col;
    public List<List<Node<Index>>> submarines= new ArrayList<>();

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public List<List<Node<Index>>> getSubmarines() {
        return submarines; //get all submarines
    }

    /**@
     * Take all groups of "ones" with class dfs and check by checkIfSubmarine() method if it is in shape of submarine
     * add +1 if it is submarine
     * add all submarines to the list of submarines
     * @param all_one_groups
     * @return num of submarins
     */
    public int submarinesGame(List<List<Node<Index>>> all_one_groups) {
        num_of_submarines = 0;

        for (List<Node<Index>> list : all_one_groups) {
            if (checkifsubmarine(list) ) {
               num_of_submarines++;
               submarines.add(list);
            }
        }
        return num_of_submarines;
    }

    //if it has a shape of square or rectangle
    /* [min_row][min_col]...[min_row][max_col]
                .         .          .
                .         .          .
                .         .          .

       [max_row][min_col]...[max_row][max_col]
   */
    public Boolean checkifsubmarine(List<Node<Index>> list_of_one)  {
        if(list_of_one.size() < 2){
            return false;
        }
        min_row = list_of_one.get(0).getData().getRow(); //assumed after sort
        max_row = list_of_one.get(list_of_one.size() - 1).getData().getRow();

        min_col = list_of_one.get(0).getData().getColumn();
        max_col = list_of_one.get(0).getData().getColumn();
        for(Node<Index> index: list_of_one){
            if(index.getData().getColumn() <= min_col){
                min_col=index.getData().getColumn();
            } else if(index.getData().getColumn() > max_col) {
                max_col= index.getData().getColumn();
            }
        }

        for (int i = min_row; i <= max_row; i++) {
            for (int j = min_col; j <= max_col; j++) {
                Index index=new Index(i,j);
                if (matrix.getValue(index) == 0) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void main(String[] args) throws IOException {
        int[][] source = {
                {1, 0, 0,0,0},
                {1, 0, 0,1,0},
                {1, 0, 0,1,1}

        };

        Matrix source_new=new Matrix(source);
        List<HashSet<Node<Index>>> lists;
        TraversableMatrix traversableMatrix = new TraversableMatrix(source_new);
        DFS<Index> threadLocalSearch = new DFS<>();
        lists = threadLocalSearch.getallgroups(traversableMatrix);
        List<List<Node<Index>>> Lists_test = new ArrayList<>();
        for(HashSet<Node<Index>> list:lists) {
            Comparator<Node<Index>> new_com= (xs1, xs2) -> Integer.compare(xs1.getData().getRow(), xs2.getData().getRow());
            Comparator<Node<Index>> new_com_new= (xs1, xs2) -> Integer.compare(xs1.getData().getColumn(), xs2.getData().getColumn());
            Comparator<Node<Index>> compareByFull = new_com.thenComparing(new_com_new);
            list = list.stream().sorted(compareByFull).collect(Collectors.toCollection(LinkedHashSet::new)); //sorting by size
            Lists_test.add(List.copyOf(list));
        }
        Submarine submarine=new Submarine();
        submarine.setMatrix(source_new);
        System.out.println(submarine.submarinesGame(Lists_test));
        System.out.println(submarine.getSubmarines());
    }

}
