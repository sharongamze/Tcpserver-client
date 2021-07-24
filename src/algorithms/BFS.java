package algorithms;

import Matrix.Index;
import Matrix.*;

import java.util.*;
import java.util.stream.Collectors;

public class BFS<T> {
    protected final ThreadLocal<Queue<List<Node<T>>>> queue = ThreadLocal.withInitial(() -> new LinkedList<>());
    public List<List<Node<T>>> all_paths = new ArrayList<>();

    protected void threadLocalOffer(List<Node<T>> node) {
        queue.get().offer(node);
    }

    protected List<Node<T>> threadLocalPoll() {
        return queue.get().poll();
    }


    /* idea taken from https://www.geeksforgeeks.org/print-paths-given-source-destination-using-bfs/
    create a queue which will store paths
    initialise the queue with first path starting from source

    Now run a loop till queue is not empty
    get the first path from queue
    check if the last_node of this path is destination
       if true then print the path
    else run a loop for all the reachable to the example from (0,0)-[[(0,0), (1,0)], [(0,0), (0,1)], [(0,0), (1,1)]]
    reachable node extracted from path
      if the node is not visited in current path(contains)
         a) create a new path from earlier path and
             append this node
         b) insert this new path to queue
    */
    public void getpaths(Traversable<T> partOfGraph, Node<T> source, Node<T> destinations) {
        List<Node<T>> path = new ArrayList<>();
        path.add(source);
        threadLocalOffer(path);
        while (!queue.get().isEmpty()) {
            path= threadLocalPoll();
            Node<T> last_node_of_path = path.get(path.size() - 1);

            if (last_node_of_path.equals(destinations)) {  // if the index is the destination so add this specific path
                List<Node<T>> new_path = new ArrayList<>(path);
                all_paths.add(new_path);
            }

            Collection<Node<T>> reachable = partOfGraph.getReachableNodes(last_node_of_path);
            for (Node<T> reachable_node : reachable) {
                if (!path.contains(reachable_node)) {
                    List<Node<T>> new_path = new ArrayList<>(path); //take the last path and put it on a new path
                    new_path.add(reachable_node); //add the reachable node of the last node of path
                    threadLocalOffer(new_path); //add to queue of paths
                }
            }
        }
    }

    /**@
     * After getting all the paths possible from source to destination in getpaths()
     * sort by size
     * get the shortest path size from first path and remove all possible paths that are greater than the short path size
     * @return List of the shortest paths
     */
    public List<List<Node<T>>> getShortestPaths() {
        if (all_paths.isEmpty()) {
            System.out.println("There are no paths");
            return all_paths;
        }
        all_paths = all_paths.stream().sorted((xs1, xs2) -> xs1.size() - xs2.size()).collect(Collectors.toList());
        int short_path_size = all_paths.get(0).size();
        all_paths.removeIf(path -> path.size() > short_path_size);
        return all_paths;

    }

    public static void main(String[] args) {
        int[][] subMarine = {
                {1, 1, 0, 0, 0},
                {1, 1, 0, 0, 1},
                {1, 1, 0, 0, 1}
        };
        Matrix source_new=new Matrix(subMarine);
        TraversableMatrix traversableMatrix = new TraversableMatrix(source_new);
        BFS<Index> threadLocalSearch = new BFS<Index>();
        Node<Index> source=new Node<Index>(new Index(0,0));
        Node<Index> destination=new Node<Index>(new Index(2,1));
        threadLocalSearch.getpaths(traversableMatrix,source,destination);
        System.out.println(threadLocalSearch.getShortestPaths());


    }


}

