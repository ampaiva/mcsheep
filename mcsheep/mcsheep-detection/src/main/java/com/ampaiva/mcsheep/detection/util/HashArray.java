package com.ampaiva.mcsheep.detection.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//TODO: Make it template and delete IHashArray
public class HashArray implements IHashArray {
    private final HashMap<String, Integer> hash = new HashMap<String, Integer>();
    private final List<String> list = new ArrayList<String>();

    @Override
    public int put(String key) {
        Integer index = hash.get(key);
        if (index != null) {
            return index;
        }
        hash.put(key, hash.size());
        list.add(key);
        return hash.size() - 1;
    }

    @Override
    public String getByIndex(int index) {
        return list.get(index);
    }

    @Override
    public int getByKey(String key) {
        return hash.get(key);
    }

    @Override
    public int size() {
        return hash.size();
    }

    @Override
    public void clear() {
        hash.clear();
        list.clear();
    }
}
