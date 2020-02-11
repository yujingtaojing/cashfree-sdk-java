package com.cashfree.lib.utils;

public class ImmutablePair<K, V> implements Pair<K, V> {
  private K k;

  private V v;

  public ImmutablePair(K k, V v) {
    this.k = k;
    this.v = v;
  }


  @Override
  public K getLeft() {
    return k;
  }

  @Override
  public V getRight() {
    return v;
  }

  @Override
  public String toString() {
    return "{" + k.toString() + ", " + v.toString() + "}";
  }
}
