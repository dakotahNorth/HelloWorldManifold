package org.example;

import manifold.ext.props.rt.api.var;

public class TreePath<T, V> {

    @var T name;
    @var V value;

    public TreePath(T name, V value) {
        this.name = name;
        this.value = value;

    }

}


