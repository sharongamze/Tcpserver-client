package algorithms;

import Matrix.Index;
import Matrix.*;

import java.util.*;

public class MinCost<T> {

    protected final ThreadLocal<Queue<List<Node<T>>>> queue = ThreadLocal.withInitial(() -> new LinkedList<>());
    public List<List<Node<T>>> all_paths = new ArrayList<>();
    public HashMap<List<Node<T>>,Integer> min_cost_path=new HashMap<>();
    public HashMap<List<Node<T>>,Integer> min_path_final=new HashMap<>();
    public Traversable<T> partOfGraph_new;

    protected void threadLocalOffer(List<Node<T>> node) { queue.get().offer(node); }

    protected List<Node<T>> threadLocalPoll() {
        return queue.get().poll();
    }


    // take the neighbors(not diagonal neighbors) from last node in path- similar function in bfs class
    public void getpaths(Traversable<T> partOfGraph, Node<T> source, Node<T> destinations) {
        partOfGraph_new=partOfGraph;
        List<Node<T>> path = new ArrayList<>();
        path.add(source);
        threadLocalOffer(path);

        while (!queue.get().isEmpty()) {
            path = threadLocalPoll();
            Node<T> last_node_of_path = path.get(path.size() - 1);

            // if the index is the destination so add this specific path
            if (last_node_of_path.equals(destinations)) {
                List<Node<T>> new_path = new ArrayList<>(path);
                all_paths.add(new_path);
            }

            Collection<Node<T>> reachable = partOfGraph.getNeighbors_nodiagonal(last_node_of_path);
            for (Node<T> neighbor_node : reachable) {
                if (!path.contains(neighbor_node)) {
                    List<Node<T>> new_path = new ArrayList<>(path); //take the last path and put it on a new path
                    new_path.add(neighbor_node); //add the reachable nodes of the last node of path
                    threadLocalOffer(new_path); //add to queue of paths
                }
            }
        }
    }

   // from getpaths() calculate the cost of paths
    public void calculate_cost(){
        int sum;
        for(List<Node<T>> list: all_paths){
            sum=0;
            for(Node<T> index:list){
                int value = partOfGraph_new.getValue(index);
                sum += value;
            }
            min_cost_path.put(list,sum); //put the path and it's cost
        }
    }

    /**@
     * after calculating the cost of paths, sort by cost with helper function sortByValue()
     * get the minimum cost value from first path on list
     * keep only the minimum cost paths - loop until not the minimum cost
     * @return hashmap of paths and the minimum cost
     */
    public HashMap<List<Node<T>>, Integer> get_path_and_cost() {
        min_cost_path = sortByValue(min_cost_path);
        int number= (int) min_cost_path.values().toArray()[0];
        for (Map.Entry<List<Node<T>>, Integer> path : min_cost_path.entrySet()){
            if (path.getValue()==number){
                min_path_final.put(path.getKey(),path.getValue());
            }
            else{
                break;
            }
        }
        return  min_path_final;
    }


    public HashMap<List<Node<T>>, Integer> sortByValue(HashMap<List<Node<T>>, Integer> cost_hm)
    {
        List<Map.Entry<List<Node<T>>, Integer> > list_cost = new LinkedList<Map.Entry<List<Node<T>>, Integer> >(cost_hm.entrySet());
        list_cost.sort((i1, i2) -> i1.getValue().compareTo(i2.getValue()));
        HashMap<List<Node<T>>, Integer> temp_cost = new LinkedHashMap<List<Node<T>>, Integer>(); // put data from sorted list to hashmap
        for (Map.Entry<List<Node<T>>, Integer> path_cost : list_cost) {
            temp_cost.put(path_cost.getKey(), path_cost.getValue());
        }
        return temp_cost;
    }


    public static void main(String[] args) {
        int[][] test_task_4 = {
                {100,100,100},
                {500,900,300}
        };

        Matrix source_new=new Matrix(test_task_4);
        TraversableMatrix traversableMatrix = new TraversableMatrix(source_new);
        MinCost<Index> mincostfind = new MinCost<Index>();
        Node<Index> source=new Node<Index>(new Index(1,0));
        Node<Index> destination=new Node<Index>(new Index(1,2));
        mincostfind.getpaths(traversableMatrix,source,destination);
        mincostfind.calculate_cost();
        HashMap<List<Node<Index>>, Integer> my= new HashMap<>();
        my = mincostfind.get_path_and_cost();
        my.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

    }
}
