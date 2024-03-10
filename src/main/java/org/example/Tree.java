package org.example;

import java.util.HashMap;
import java.util.Map;
import manifold.rt.api.util.Pair;
import manifold.ext.props.rt.api.var;
import org.jetbrains.annotations.NotNull;

public class Tree<V, E> {

    @var private final Map<V, TreeNode<V, E>> rootTreeNodeMap = new HashMap<>();



    @SafeVarargs
    public final void insertPath(Pair<V, E>... pairs) {
        if (pairs.length == 0) {
            return;
        }

        TreeNode<V, E> node = insertRootNodeIfAbsent(pairs[0].first, pairs[0].second);

        for (int i = 1; i < pairs.length; i++) {
            E edge = pairs[i-1].second;
            V newValue = pairs[i].first;
            E newEdge = pairs[i].second;

            node = node.insertNode(edge, newValue, newEdge);
        }
    }

    @NotNull
    private TreeNode<V, E> insertRootNodeIfAbsent(V value, E edge) {

        TreeNode<V, E> node = rootTreeNodeMap.computeIfAbsent(value, v -> new TreeNode<>(value) );

        node.references++;

        node.insertNode(edge);

        return node;

    }


    @SafeVarargs
    public final boolean containsPath(Pair<V, E>... pairs) {
        TreeNode<V, E> current = getNode(pairs[0].first);

        for (int i = 0; current != null && i < pairs.length; i++) {
            current = current.getNode(pairs[i].second);
        }

        return current != null;
    }

    @SafeVarargs
    public final void removePath(Pair<V, E>... pairs) {
        if (!containsPath(pairs)) {
            return;
        }

        TreeNode<V, E> parent = null;
        TreeNode<V, E> node = removeRootNode(pairs[0].first);
        E edge = null;

        for (int pairIndex = 0; node != null && pairIndex < pairs.length; pairIndex++) {
            parent = node;
            edge = pairs[pairIndex].second;
            node = parent.getNode(edge);

            parent.removeNode(edge);
        }

        if (node != null) {
            node.action = false;
        }

    }

    private TreeNode<V, E> removeRootNode(V value) {

        TreeNode<V, E> node = rootTreeNodeMap.get(value);

        node.references--;

        if (node.references == 0) {
            rootTreeNodeMap.remove(value);
        }

        return node;

    }

    public TreeNode<V, E> getNode(V value) {
        return rootTreeNodeMap.get(value);
    }

    public boolean isEmpty() {
        return rootTreeNodeMap.isEmpty();
    }

    public String toString() {
        return rootTreeNodeMap.isEmpty() ? "Empty tree" : rootTreeNodeMap.values().toString();
    }
}
