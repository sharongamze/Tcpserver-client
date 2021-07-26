package TcpServer;

import Matrix.*;
import Matrix.Node;
import Matrix.TraversableMatrix;
import algorithms.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class MatrixIHandler implements IHandler {
    boolean doWork = true;

    @Override
    public void handle(InputStream fromClient, OutputStream toClient)
            throws IOException, ClassNotFoundException {

        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);

        while(doWork){
            switch(objectInputStream.readObject().toString()) {
                case "Task one": {
                    System.out.println("\nTask one:");
                    int[][] client_matrix = (int[][]) objectInputStream.readObject();
                    if (client_matrix != null) {
                        TraversableMatrix myTraversableMatrix = new TraversableMatrix(new Matrix(client_matrix));
                        DFS<Index> dfs = new DFS<>();
                        List<HashSet<Node<Index>>> groups = dfs.getallgroups(myTraversableMatrix);
                        List<HashSet<Node<Index>>> groups_sorted = new ArrayList<>();
                        Comparator<Node<Index>> compare_row = (xs1, xs2) -> Integer.compare(xs1.getData().getRow(), xs2.getData().getRow());
                        Comparator<Node<Index>> compare_col = (xs1, xs2) -> Integer.compare(xs1.getData().getColumn(), xs2.getData().getColumn());
                        Comparator<Node<Index>> compareByFull = compare_row.thenComparing(compare_col);
                        for(HashSet<Node<Index>> list_of_nodes: groups) {
                            list_of_nodes = list_of_nodes.stream().sorted(compareByFull).collect(Collectors.toCollection(LinkedHashSet::new));
                            groups_sorted.add(list_of_nodes);
                        }
                        List<HashSet<Node<Index>>> groups_of_ones = new ArrayList<HashSet<Node<Index>>>(groups_sorted);
                        objectOutputStream.writeObject(groups_of_ones);
                    }
                    break;
                }

                case "Task two": {
                    System.out.println("\nTask two:");
                    int[][] client_matrix = (int[][]) objectInputStream.readObject();
                    if (client_matrix.length > 50 || client_matrix[0].length > 50) { //check input as asked in task
                        throw new IOException();
                    }
                    else {
                        TraversableMatrix myTraversableMatrix = new TraversableMatrix(new Matrix(client_matrix));
                        Index source_index = (Index) objectInputStream.readObject();
                        Index destination_index = (Index) objectInputStream.readObject();
                        Node<Index> source = new Node<>(source_index);
                        Node<Index> destination = new Node<>(destination_index);
                        BFS<Index> shortest_path = new BFS<>();
                        shortest_path.getpaths(myTraversableMatrix, source, destination);
                        List<List<Node<Index>>> shortest_paths_final = shortest_path.getShortestPaths();
                        objectOutputStream.writeObject(shortest_paths_final);
                    }
                    break;
                }
                case "Task three": {
                    System.out.println("\nTask three:");
                    int[][] client_matrix = (int[][]) objectInputStream.readObject();
                    if (client_matrix != null) {
                        Matrix source_matrix=new Matrix(client_matrix);
                        TraversableMatrix traversableMatrix = new TraversableMatrix(source_matrix);
                        DFS<Index> dfs = new DFS<>();
                        List<HashSet<Node<Index>>> groups= dfs.getallgroups(traversableMatrix);
                        List<List<Node<Index>>> groups_sorted = new ArrayList<>();
                        Comparator<Node<Index>> compare_row = (xs1, xs2) -> Integer.compare(xs1.getData().getRow(), xs2.getData().getRow());
                        Comparator<Node<Index>> compare_col = (xs1, xs2) -> Integer.compare(xs1.getData().getColumn(), xs2.getData().getColumn());
                        Comparator<Node<Index>> compareByFull = compare_row.thenComparing(compare_col);
                        for (HashSet<Node<Index>> list_of_nodes : groups) {
                            list_of_nodes = list_of_nodes.stream().sorted(compareByFull).collect(Collectors.toCollection(LinkedHashSet::new));
                            groups_sorted.add((List.copyOf(list_of_nodes))); //to change to list
                        }
                        Submarine submarine=new Submarine();
                        submarine.setMatrix(source_matrix);
                        List<List<Node<Index>>> groups_sorted_sub = new ArrayList<List<Node<Index>>>(groups_sorted);
                        int how_many_submarines= submarine.submarinesGame(groups_sorted_sub);
                        List<List<Node<Index>>> submarines= submarine.getSubmarines();
                        objectOutputStream.writeObject(how_many_submarines);
                        objectOutputStream.writeObject(submarines);
                    }
                    break;
                }
                case "Task four":{
                    System.out.println("\nTask four:");
                    int[][] client_matrix = (int[][]) objectInputStream.readObject();
                    if (client_matrix != null) {
                        TraversableMatrix myTraversableMatrix = new TraversableMatrix(new Matrix(client_matrix));
                        Index source_index = (Index) objectInputStream.readObject();
                        Index destination_index = (Index) objectInputStream.readObject();
                        Node<Index> source = new Node<>(source_index);
                        Node<Index> destination = new Node<>(destination_index);
                        MinCost<Index> min_cost_path=new MinCost<>();
                        min_cost_path.getpaths(myTraversableMatrix,source,destination);
                        min_cost_path.calculate_cost();
                        HashMap<List<Node<Index>>, Integer>  min_cost_path_final = min_cost_path.get_path_and_cost();
                        objectOutputStream.writeObject(min_cost_path_final);
                    }

                   break;
                }
                case "stop server": {
                    System.out.println("Stopping server!");
                    doWork = false;
                    break;
                }

            }
        }
    }
}