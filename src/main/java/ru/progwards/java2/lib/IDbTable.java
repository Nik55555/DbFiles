package ru.progwards.java2.lib;

import java.util.List;
import java.util.function.Predicate;

public interface IDbTable<K, V> {
    String getTableName();

    List<V> getAll();
    List<V> select(Predicate<V> check);
    K getKey(V elem);
    boolean put(V elem) throws Exception;
    V remove(K key) throws Exception;
    V findKey(K key);
    boolean exists(K key);

    void readAll() throws Exception;
    void saveAll() throws Exception;
}
