package by.roboworld.model.bean.board;

import by.roboworld.exception.Constants;
import by.roboworld.model.bean.task.Task;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class encapsulates the {@linkplain LinkedBlockingQueue} class because
 * it based on linked nodes, orders elements FIFO (first-in-first-out) and
 * implements the "two lock queue" algorithm. This is ideal for storing tasks
 * in a multithreaded application.
 *
 * @param <T> type of task
 */
public class TaskBoard<T extends Task> implements Board<T> {
    /** The queue for store tasks. */
    private final BlockingQueue<T> tasks = new LinkedBlockingQueue<>();

    /**
     * Inserts the specified task at the tail of this board with the lock on
     * the entry. Returns {@code true} upon success and {@code false} otherwise.
     *
     * @param task added task
     * @return {@code true} upon success and {@code false} otherwise.
     * @throws NullPointerException if the specified task is null
     */
    public boolean add(T task) {
        Objects.requireNonNull(task, Constants.TASK_IS_NULL);
        return tasks.offer(task);
    }

    /**
     * Retrieves and removes the first task of this board with lock on read or
     * returns {@code null} if this board is empty.
     *
     * @return the first task of this board, or {@code null} if this board is
     *         empty
     */
    public T poll() {
        return tasks.poll();
    }

    /**
     * Retrieves, but does not remove, the first task of this board with lock
     * on read, or returns {@code null} if this board is empty.
     *
     * @return the first task of this board, or {@code null} if this board is
     *         empty
     */
    public T get() {
        return tasks.peek();
    }

    /**
     * Returns the number of elements in this board.  If this board
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this board
     */
    public int size() {
        return tasks.size();
    }

}
