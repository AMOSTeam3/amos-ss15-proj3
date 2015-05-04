package de.fau.osr.util;

/**
 * @author: Taleh Didover
 *
 * Helper-Class which contains two Object of different type.
 */
public class Pair<L, R> {
    L left;
    R right;

    public Pair(L l, R r) {
        left = l;
        right = r;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }
}
