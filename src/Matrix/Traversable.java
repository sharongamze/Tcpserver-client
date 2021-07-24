package Matrix;

import Matrix.Node;

import java.util.Collection;
import java.util.List;

/**
 * This interface defines the functionality required for a traversable graph
 */
public interface Traversable<T> {
    public Node<T> getOrigin();
    public Collection<Node<T>>  getReachableNodes(Node<T> someNode);
    public Collection<Node<T>> getNeighbors_nodiagonal(Node<T> node);
    public int getValue(Node<T> index);
    public List<Node<T>> getallnodes();
}