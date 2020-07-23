package ru.progwards.java2.lib;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractDbTable<K, V> implements IDbTable<K, V> {
    private final Map<K, V> valueMap = new ConcurrentHashMap<>();
    private final Type type;

    public AbstractDbTable(Type type) {
        this.type = type;
        try {
            readAll();
        } catch (IOException e) {
            try {
                saveAll();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public List<V> getAll() {
        return valueMap.values().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<V> select(Predicate<V> check) {
        return valueMap.values().stream().filter(check).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public boolean put(V elem) throws IOException {
        V newElem = valueMap.put(getKey(elem), elem);
        if (newElem == null)
            saveAll();
        return newElem == null;
    }

    @Override
    public V remove(K key) throws IOException {
        V removedElem = valueMap.remove(key);
        if (removedElem != null)
            saveAll();
        return removedElem;
    }

    @Override
    public V findKey(K key) {
        return valueMap.get(key);
    }

    @Override
    public boolean exists(K key) {
        return findKey(key) != null;
    }

    @Override
    public void readAll() throws IOException {
        synchronized (this) {
            valueMap.clear();
            String json = Files.readString(Path.of(DataBase.DB_PATH + getTableName()));
            ArrayList<V> list = new Gson().fromJson(json, type);

            list.forEach(e -> valueMap.put(getKey(e), e));
        }
    }

    @Override
    public void saveAll() throws IOException {
        synchronized (this) {
            String json = new Gson().toJson(getAll());
            Files.writeString(Path.of(DataBase.DB_PATH + getTableName()), json);
        }
    }
}
