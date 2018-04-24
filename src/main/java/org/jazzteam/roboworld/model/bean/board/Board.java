package org.jazzteam.roboworld.model.bean.board;

import org.jazzteam.roboworld.exception.TaskIsNullException;
import org.jazzteam.roboworld.model.bean.task.Task;

/**
 * The Board is designed to store tasks in the order of addition.
 * In fact, it is just a queue without unnecessary methods.
 * It should not contains {@code null}.
 *
 * @param <T> type of task
 */
public interface Board<T extends Task> {

    /**
     * Inserts the specified task into this board.
     * Returns {@code true} if insertion is successful and {@code false} otherwise.
     *
     * @param task the task to add
     * @return <tt>true</tt> if this board changed as a result of the call
     * @throws TaskIsNullException if the specified task is null
     */
    boolean add(T task);

    /**
     * Retrieves and removes the first task of this board (which longest time in the board),
     * or returns {@code null} if this board is empty.
     *
     * @return the first task of this board, or {@code null} if this board is empty
     */
    T poll();

    /**
     * Retrieves, but does not remove, the first task of this board,
     * or returns {@code null} if this board is empty.
     *
     * @return the first task of this board, or {@code null} if this board is empty
     */
    T get();

    /**
     * Returns the number of elements in this board.  If this board
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this board
     */
    int size();
}
