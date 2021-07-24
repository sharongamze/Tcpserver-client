package Matrix;

import Matrix.Index;
import Matrix.Matrix;
import Matrix.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements adapter/wrapper/decorator design pattern
 */
public class TraversableMatrix implements Traversable<Index> {
    protected final Matrix matrix;
    protected Index startIndex;

    public TraversableMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public Index getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Index startIndex) {
        this.startIndex = startIndex;
    }

    public int getValue(Node<Index> index) {
        Index getindex= index.getData();
        return this.matrix.getValue(getindex);
    }

    public Collection<Node<Index>> getNeighbors_nodiagonal(Node<Index> someNode){
        List<Node<Index>> neighbors = new ArrayList<Node<Index>>();
        assert someNode.getData() != null;
        for(Index index: this.matrix.getNeighbors_notdiagonal(someNode.getData()) ){
            neighbors.add(new Node<Index>(index));
        }
        return neighbors;
    }

    public List<Node<Index>> getallnodes(){
        List<Node<Index>> allnodes = new ArrayList<Node<Index>>();
        for(Index index: this.matrix.getAllNodes()) {
           {
                allnodes.add(new Node<Index>(index));
            }
        }
        return allnodes;

    }


    @Override
    public Node<Index> getOrigin() throws NullPointerException{
        if (this.startIndex == null) throw new NullPointerException("start index is not initialized");
        return new Node<>(this.startIndex);

    }

    @Override
    public Collection<Node<Index>> getReachableNodes(Node<Index> someNode) {
        List<Node<Index>> filteredIndices = new ArrayList<>();
        assert someNode.getData() != null;
        this.matrix.getNeighbors(someNode.getData()).stream().filter(i-> matrix.getValue(i)==1)
                .map(neighbor->filteredIndices.add(new Node<Index>(neighbor))).collect(Collectors.toList());
        if (filteredIndices.isEmpty()) {
            return null;
        }
        return filteredIndices;
    }

    @Override
    public String toString() {
        return matrix.toString();
    }
}