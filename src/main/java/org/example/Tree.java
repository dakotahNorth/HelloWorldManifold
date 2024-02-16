package org.example;

import java.util.HashMap;
import java.util.Map;
import manifold.ext.props.rt.api.var;
import manifold.rt.api.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Tree<T, V> {

    @var private final Map<T, TreeNode<T,V>> rootTreeNodeMap = new HashMap<>();


    @SafeVarargs
    public final void insert(Pair<T, V>... pairs) {

        TreeNode<T, V> rootNode = insertRootNodeIfAbsent(pairs);

        TreeNode<T, V> lastNode = insertInternalNodesIfAbsent(pairs, rootNode);



    }


    @SafeVarargs
    @NotNull
    private TreeNode<T, V> insertRootNodeIfAbsent(Pair<T, V>... pairs) {
        TreeNode<T, V> current = rootTreeNodeMap.computeIfAbsent(pairs[0].first, TreeNode::new);
        current.references++;
        return current;
    }


    private TreeNode<T, V> insertInternalNodesIfAbsent(Pair<T, V>[] pairs, TreeNode<T, V> rootNode) {

        TreeNode<T, V> current = rootNode;
        TreeNode<T, V> previous = null;

        for (int i = 0; i < pairs.length - 1; i++) {
            T nextName = pairs[i + 1].first;
            previous = current;
            current = current.computeIfAbsent(pairs[i].second, v -> new TreeNode<>(nextName));
            current.references++;
        }

        insertLeafNodeIfAbsent(pairs, previous, current);


        return current;

    }

    private static <T, V> void insertLeafNodeIfAbsent(Pair<T, V>[] pairs, TreeNode<T, V> previous, TreeNode<T, V> current) {

        boolean pathEndsInternally = previous != null && previous.isInternal() && !current.isLeaf();
        if (pathEndsInternally) {
            current.action = true;
            return;
        }

        current.put(pairs[pairs.length - 1].second, new TreeNode<>());

    }


    public final boolean contains(Pair<T, V>... pairs) {

        TreeNode<T, V> current = getRootNode(pairs);

        for (int i = 0; current != null && i < pairs.length; i++) {
            current = current.getValue(pairs[i].second);
        }

        return current != null;
    }


    @SafeVarargs
    public final void remove(Pair<T, V>... pairs) {

        if (!contains(pairs)) {
            return;
        }

        TreeNode<T, V> rootNode = removeRootNodeWithoutReferences(pairs);

        if (rootNode.removed()) {
            return;
        }

        removeInternalNodesWithoutReferences(rootNode, pairs);


    }


    @SafeVarargs
    @Nullable
    private TreeNode<T, V> removeRootNodeWithoutReferences(Pair<T, V>... pairs) {

        TreeNode<T, V> currentNode = getRootNode(pairs);

        currentNode.references--;

        if (currentNode.references == 0) {
            rootTreeNodeMap.remove(pairs[0].first);
        }

        return currentNode;
    }


    private @Nullable TreeNode<T, V> getRootNode(Pair<T, V>[] pairs) {
        return pairs.length == 0 ? null : rootTreeNodeMap.get(pairs[0].first);
    }



    @SafeVarargs
    private void removeInternalNodesWithoutReferences(TreeNode<T, V> rootNode, Pair<T, V>... pairs) {

        TreeNode<T, V> currentNode = rootNode;
        TreeNode<T, V> previousNode = null;
        V value = null;


        for (int pairIndex = 0; currentNode != null && pairIndex < pairs.length; pairIndex++) {

            previousNode = currentNode;
            value = pairs[pairIndex].second;

            currentNode = previousNode.getValue(value);
            currentNode.references--;

            removeNodeWithoutReferences(currentNode, previousNode, value);
        }
    }

    private static <T, V> void removeNodeWithoutReferences(TreeNode<T, V> node, TreeNode<T, V> previousNode, V value) {
        boolean removeNode = node.references == 0;
        if (removeNode) {
            previousNode.removeValue(value);
        }
    }



    public TreeNode<T, V> getName(T name) {
        return rootTreeNodeMap.get(name);
    }


    public String toString() {
        return rootTreeNodeMap.isEmpty() ? "Empty tree" : rootTreeNodeMap.values().toString();
    }
}