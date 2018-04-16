package org.jazzteam.roboworld.model.bean.board;

public interface Board<T> {
    void add(T element);
    T poll();
    T get();
    int size();
}
