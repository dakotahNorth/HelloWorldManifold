package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import manifold.ext.props.rt.api.var;

@SuppressWarnings({"PackageVisibleField", "FieldNotUsedInToString"})
public class TreeNode<T, V> {

    public static final TreeNode Leaf = new TreeNode();

    @var T name;
    @var int references;
    @var boolean action;
    @var private Map<V, TreeNode<T, V>> values;

    TreeNode() {
        action = true;
        references = 1;
        values = new HashMap<>();

    }


    TreeNode(T tag) {
        this.name = tag;
        values = new HashMap<>();
    }


    public boolean isLeaf() { return values.isEmpty() == true; }

    public void put(V value, TreeNode<T, V> node) { values.put(value, node); }

    public boolean containsValue(V value) {
        return values.containsKey(value);
    }

    public TreeNode<T, V> getValue(V value) {
        return values.get(value);
    }

    public TreeNode<T, V> removeValue(V value) {
        return values.remove(value);
    }

    public boolean removed() {
        return references == 0;
    }

    public TreeNode<T, V> computeIfAbsent(V v, Function<? super V, ? extends TreeNode<T, V>> mappingFunction) {
        return values.computeIfAbsent(v, mappingFunction);
    }

    @Override
    public String toString() {
        return toString("\t");
    }

    private String toString(String indent) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(name).append("\n");
        for (Entry<V, TreeNode<T, V>> entry : values.entrySet()) {
            sb.append(indent).append(entry.getKey()).append(" : ").append(entry.getValue() == Leaf ? "LEAF" : entry.getValue().toString(indent + "\t")).append('\n');
        }
        return sb.toString();
    }



}