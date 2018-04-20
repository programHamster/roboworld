package org.jazzteam.roboworld.model.bean.board;

public interface Board<T> {
    boolean add(T element);
    T poll();
    T get();
    int size();
}
