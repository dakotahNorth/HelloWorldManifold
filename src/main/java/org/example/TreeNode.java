package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import manifold.ext.props.rt.api.var;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"PackageVisibleField", "FieldNotUsedInToString"})
public class TreeNode<V, E> {

    @var V value;
    @var int references;
    @var boolean action;
    @var private Map<E, TreeNode<V, E>> edges;


    TreeNode() {
        action = true;
        references++;
        edges = new HashMap<>();

    }


    TreeNode(V value) {
        this.value = value;
        edges = new HashMap<>();
    }


    TreeNode<V, E> insertNode(E edge, V newValue, E newEdge) {

        if (isEndOfPath(edge)) {
            return swapEndOfPathNode(edge, newValue, newEdge);
        }

        return insertInternalNode(edge, newValue, newEdge);

    }


    @NotNull
    private TreeNode<V, E> swapEndOfPathNode(E edge, V newValue, E newEdge) {
        var node = edges.get(edge);
        node.value = newValue;
        node.insertNode(newEdge);
        return node;
    }


    private boolean isEndOfPath(E edge) {
        return edges.get(edge) != null && edges.get(edge).value == null;
    }


    @NotNull
    TreeNode<V, E> insertInternalNode(E parentEdge, V value, E edge) {

        TreeNode<V, E> node = edges.computeIfAbsent(parentEdge, v -> new TreeNode<>(value));
        node.references++;
        node.insertNode(edge);
        return node;

    }


    TreeNode<V, E> insertNode(E edge) {

        boolean shorterPathEndedOnExistingNode = getNode(edge) != null;

        if (shorterPathEndedOnExistingNode) {
            action = true;
            return this;
        }

        return swapEndOfPathNode(edge);

    }


    @NotNull
    private TreeNode<V, E> swapEndOfPathNode(E edge) {

        TreeNode<V, E> node = new TreeNode<>();
        edges.put(edge, node);
        return node;
    }


    public boolean hasEdge(E edge) {
        return edges.containsKey(edge);
    }


    public TreeNode<V, E> getNode(E edge) {
        return edges.get(edge);
    }


    void removeNode(E edge) {

        var node = edges.get(edge);

        node.references--;

        if (node.references == 0) {
            edges.remove(edge);
        }

    }

    public boolean isLeaf() { return edges.isEmpty(); }

    public boolean isInternal() { return !edges.isEmpty(); }

    @Override
    public String toString() {
        return toString("\t");
    }
    private String toString(String indent) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(value).append("\n");
        for (Entry<E, TreeNode<V, E>> entry : edges.entrySet()) {
            sb.append(indent).append(entry.getKey()).append(" : ").append(entry.getValue().toString(indent + "\t")).append('\n');
        }
        return sb.toString();
    }



}