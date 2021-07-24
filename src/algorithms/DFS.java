package algorithms;

import Matrix.Index;
import Matrix.*;
import java.util.*;
import java.util.stream.Collectors;

public class DFS<T> {
    protected final ThreadLocal<Stack<Node<T>>> stack = ThreadLocal.withInitial(Stack::new);
    protected final ThreadLocal<Set<Node<T>>> visited = ThreadLocal.withInitial(()->new LinkedHashSet<>()); //The TheadLocal construct allows us to store data that will be accessible only by a specific thread.
    public List<HashSet<Node<T>>> one_groups= new ArrayList<>();
    public List<Node<T>> nodes_value_one= new ArrayList<>();

    protected void threadLocalPush(Node<T> node){
        stack.get().push(node);
    }

    protected Node<T> threadLocalPop(){
        return stack.get().pop();
    }

    /**@
     * From matrix take all the nodes that have the value of one into list
     * check for every node what are the connected group of one with the traverse() method and add to one_groups
     * all the this node's group remove from list of nodes_value_one (for not checking twice)
     * sort the list of one_groups by size
     * @param partofgraph
     * @return one_groups
     */
    public List<HashSet<Node<T>>> getallgroups(Traversable<T> partofgraph){

        List<Node<T>> all_nodes=partofgraph.getallnodes();

        for(Node<T> node: all_nodes){
            if (partofgraph.getValue(node)==1){
                nodes_value_one.add(node); //all nodes value equal to 1
            }
        }
        while (!nodes_value_one.isEmpty()) {
            Node<T> curr_node = nodes_value_one.get(0);
            HashSet<Node<T>> list_of_connected_nodes = this.traverse(partofgraph,curr_node);
            for (Node<T> node : list_of_connected_nodes) {
                nodes_value_one.remove(node); //all visited nodes
            }
            one_groups.add(list_of_connected_nodes);
        }
        one_groups = one_groups.stream().sorted((xs1, xs2) -> xs1.size() - xs2.size()).collect(Collectors.toList());
        return one_groups;
    }

    /*
    Push to stack the current node of our graph from getallgroups()
    While stack is not empty: // there are nodes to handle
        removed = pop operation
        insert to visited set
        invoke getReachableNodes on the removed node
        For each reachable node:
            if the current reachable node is not in finished set && working stack
            push to stack
     */

    public HashSet<Node<T>> traverse(Traversable<T> partOfGraph, Node<T> current_node){
        threadLocalPush(current_node);
        while(!stack.get().isEmpty()) {
            Node<T> node= threadLocalPop();
            visited.get().add(node);
            Collection<Node<T>> reachableNodes = partOfGraph.getReachableNodes(node);
            if (reachableNodes !=null) {
                for (Node<T> singleReachableNode : reachableNodes) {
                    if (!visited.get().contains(singleReachableNode) &&
                            !stack.get().contains(singleReachableNode)) {
                        threadLocalPush(singleReachableNode);
                    }
                }
            }
        }
        HashSet<Node<T>> group = new HashSet<Node<T>>(visited.get());
        visited.get().clear(); //clear for new group of one on matrix
        return group;

    }


    public static void main(String[] args) {
        int[][] source = {
                {1, 0, 1,1},
                {1, 0, 0,0},
                {1, 0, 1,0}
        };
        Matrix source_new=new Matrix(source);
        List<HashSet<Node<Index>>> lists = new ArrayList<>();
        TraversableMatrix traversableMatrix = new TraversableMatrix(source_new);
        DFS<Index> dfs = new DFS<>();
        lists = dfs.getallgroups(traversableMatrix);
        List<HashSet<Node<Index>>> linkedPointsLists_test = new ArrayList<>();
        for(HashSet<Node<Index>> list:lists) {
            list = list.stream().sorted((xs1, xs2) -> Integer.compare(xs1.getData().getRow(), xs2.getData().getRow())).collect(Collectors.toCollection(LinkedHashSet::new));
            linkedPointsLists_test.add(list);
        }
        System.out.println(linkedPointsLists_test);


    }


}