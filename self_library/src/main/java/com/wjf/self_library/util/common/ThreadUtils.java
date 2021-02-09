package com.wjf.self_library.util.common;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadUtils {

    private static final SparseArray<SparseArray<ExecutorService>> TYPE_PRIORITY_POOLS =
            new SparseArray<>();
    private static final Map<BaseTask, ScheduledExecutorService> TASK_SCHEDULED = new HashMap<>();

    private static final byte TYPE_SINGLE = -1;
    private static final byte TYPE_CACHED = -2;
    private static final byte TYPE_IO = -4;
    private static final byte TYPE_CPU = -8;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static Executor sDeliver;

    /**
     * Return whether the thread is the main thread.
     *
     * @return {@code true}: yes<br>
     * {@code false}: no
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Return a thread pool that reuses a fixed number of threads operating off a shared unbounded
     * queue, using the provided ThreadFactory to create new threads when needed.
     *
     * @param size The size of thread in the pool.
     * @return a fixed thread pool
     */
    public static ExecutorService getFixedPool(@IntRange(from = 1) final int size) {
        return getPoolByTypeAndPriority(size);
    }

    /**
     * Return a thread pool that reuses a fixed number of threads operating off a shared unbounded
     * queue, using the provided ThreadFactory to create new threads when needed.
     *
     * @param size     The size of thread in the pool.
     * @param priority The priority of thread in the poll.
     * @return a fixed thread pool
     */
    public static ExecutorService getFixedPool(
            @IntRange(from = 1) final int size, @IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(size, priority);
    }

    /**
     * Return a thread pool that uses a single worker thread operating off an unbounded queue, and
     * uses the provided ThreadFactory to create a new thread when needed.
     *
     * @return a single thread pool
     */
    public static ExecutorService getSinglePool() {
        return getPoolByTypeAndPriority(TYPE_SINGLE);
    }

    /**
     * Return a thread pool that uses a single worker thread operating off an unbounded queue, and
     * uses the provided ThreadFactory to create a new thread when needed.
     *
     * @param priority The priority of thread in the poll.
     * @return a single thread pool
     */
    public static ExecutorService getSinglePool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_SINGLE, priority);
    }

    /**
     * Return a thread pool that creates new threads as needed, but will reuse previously
     * constructed threads when they are available.
     *
     * @return a cached thread pool
     */
    public static ExecutorService getCachedPool() {
        return getPoolByTypeAndPriority(TYPE_CACHED);
    }

    /**
     * Return a thread pool that creates new threads as needed, but will reuse previously
     * constructed threads when they are available.
     *
     * @param priority The priority of thread in the poll.
     * @return a cached thread pool
     */
    public static ExecutorService getCachedPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CACHED, priority);
    }

    /**
     * Return a thread pool that creates (2 * CPU_COUNT + 1) threads operating off a queue which
     * size is 128.
     *
     * @return a IO thread pool
     */
    public static ExecutorService getIoPool() {
        return getPoolByTypeAndPriority(TYPE_IO);
    }

    /**
     * Return a thread pool that creates (2 * CPU_COUNT + 1) threads operating off a queue which
     * size is 128.
     *
     * @param priority The priority of thread in the poll.
     * @return a IO thread pool
     */
    public static ExecutorService getIoPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_IO, priority);
    }

    /**
     * Return a thread pool that creates (CPU_COUNT + 1) threads operating off a queue which size is
     * 128 and the maximum number of threads equals (2 * CPU_COUNT + 1).
     *
     * @return a cpu thread pool for
     */
    public static ExecutorService getCpuPool() {
        return getPoolByTypeAndPriority(TYPE_CPU);
    }

    /**
     * Return a thread pool that creates (CPU_COUNT + 1) threads operating off a queue which size is
     * 128 and the maximum number of threads equals (2 * CPU_COUNT + 1).
     *
     * @param priority The priority of thread in the poll.
     * @return a cpu thread pool for
     */
    public static ExecutorService getCpuPool(@IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CPU, priority);
    }

    /**
     * Executes the given task in a fixed thread pool.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByFixed(
            @IntRange(from = 1) final int size, final BaseTask<T> task) {
        execute(getPoolByTypeAndPriority(size), task);
    }

    /**
     * Executes the given task in a fixed thread pool.
     *
     * @param size     The size of thread in the fixed thread pool.
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByFixed(
            @IntRange(from = 1) final int size,
            final BaseTask<T> task,
            @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(size, priority), task);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param size  The size of thread in the fixed thread pool.
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByFixedWithDelay(
            @IntRange(from = 1) final int size,
            final BaseTask<T> task,
            final long delay,
            final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(size), task, delay, unit);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param size     The size of thread in the fixed thread pool.
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByFixedWithDelay(
            @IntRange(from = 1) final int size,
            final BaseTask<T> task,
            final long delay,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(size, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size   The size of thread in the fixed thread pool.
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(
            @IntRange(from = 1) final int size,
            final BaseTask<T> task,
            final long period,
            final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, 0, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size     The size of thread in the fixed thread pool.
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(
            @IntRange(from = 1) final int size,
            final BaseTask<T> task,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size         The size of thread in the fixed thread pool.
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(
            @IntRange(from = 1) final int size,
            final BaseTask<T> task,
            long initialDelay,
            final long period,
            final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size         The size of thread in the fixed thread pool.
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(
            @IntRange(from = 1) final int size,
            final BaseTask<T> task,
            long initialDelay,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(size, priority), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a single thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeBySingle(final BaseTask<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE), task);
    }

    /**
     * 构建新的线程池
     *
     * @param name     名称
     * @param poolSize 大小
     * @return 线程池对象
     */
    public static ScheduledExecutorService newExecutorService(String name, int poolSize) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        if (poolSize < 1) {
            poolSize = 1;
        }

        UtilsThreadFactory factory = new UtilsThreadFactory(name, Thread.NORM_PRIORITY);
        return new ScheduledThreadPoolExecutor(poolSize, factory);
    }

    /**
     * Executes the given task in a single thread pool.
     *
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeBySingle(
            final BaseTask<T> task, @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task);
    }

    /**
     * Executes the given task in a single thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeBySingleWithDelay(
            final BaseTask<T> task, final long delay, final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE), task, delay, unit);
    }

    /**
     * Executes the given task in a single thread pool after the given delay.
     *
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeBySingleWithDelay(
            final BaseTask<T> task,
            final long delay,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(
            final BaseTask<T> task, final long period, final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE), task, 0, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(
            final BaseTask<T> task,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(
            final BaseTask<T> task, long initialDelay, final long period, final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(
            final BaseTask<T> task,
            long initialDelay,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByCached(final BaseTask<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED), task);
    }

    /**
     * Executes the given task in a cached thread pool.
     *
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCached(
            final BaseTask<T> task, @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED, priority), task);
    }

    /**
     * Executes the given task in a cached thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByCachedWithDelay(
            final BaseTask<T> task, final long delay, final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED), task, delay, unit);
    }

    /**
     * Executes the given task in a cached thread pool after the given delay.
     *
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCachedWithDelay(
            final BaseTask<T> task,
            final long delay,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(
            final BaseTask<T> task, final long period, final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(
            final BaseTask<T> task,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(
            final BaseTask<T> task, long initialDelay, final long period, final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(
            final BaseTask<T> task,
            long initialDelay,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CACHED, priority), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByIo(final BaseTask<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_IO), task);
    }

    /**
     * Executes the given task in an IO thread pool.
     *
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByIo(
            final BaseTask<T> task, @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_IO, priority), task);
    }

    /**
     * Executes the given task in an IO thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByIoWithDelay(
            final BaseTask<T> task, final long delay, final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO), task, delay, unit);
    }

    /**
     * Executes the given task in an IO thread pool after the given delay.
     *
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByIoWithDelay(
            final BaseTask<T> task,
            final long delay,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO, priority), task, delay, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(
            final BaseTask<T> task, final long period, final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, 0, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(
            final BaseTask<T> task,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(
            final BaseTask<T> task, long initialDelay, final long period, final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(
            final BaseTask<T> task,
            long initialDelay,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_IO, priority), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByCpu(final BaseTask<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_CPU), task);
    }

    /**
     * Executes the given task in a cpu thread pool.
     *
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCpu(
            final BaseTask<T> task, @IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CPU, priority), task);
    }

    /**
     * Executes the given task in a cpu thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByCpuWithDelay(
            final BaseTask<T> task, final long delay, final TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU), task, delay, unit);
    }

    /**
     * Executes the given task in a cpu thread pool after the given delay.
     *
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCpuWithDelay(
            final BaseTask<T> task,
            final long delay,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(
            final BaseTask<T> task, final long period, final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(
            final BaseTask<T> task,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(
            final BaseTask<T> task, long initialDelay, final long period, final TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(
            final BaseTask<T> task,
            long initialDelay,
            final long period,
            final TimeUnit unit,
            @IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CPU, priority), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a custom thread pool.
     *
     * @param pool The custom thread pool.
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByCustom(final ExecutorService pool, final BaseTask<T> task) {
        execute(pool, task);
    }

    /**
     * Executes the given task in a custom thread pool after the given delay.
     *
     * @param pool  The custom thread pool.
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByCustomWithDelay(
            final ExecutorService pool,
            final BaseTask<T> task,
            final long delay,
            final TimeUnit unit) {
        executeWithDelay(pool, task, delay, unit);
    }

    /**
     * Executes the given task in a custom thread pool at fix rate.
     *
     * @param pool   The custom thread pool.
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByCustomAtFixRate(
            final ExecutorService pool,
            final BaseTask<T> task,
            final long period,
            final TimeUnit unit) {
        executeAtFixedRate(pool, task, 0, period, unit);
    }

    /**
     * Executes the given task in a custom thread pool at fix rate.
     *
     * @param pool         The custom thread pool.
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCustomAtFixRate(
            final ExecutorService pool,
            final BaseTask<T> task,
            long initialDelay,
            final long period,
            final TimeUnit unit) {
        executeAtFixedRate(pool, task, initialDelay, period, unit);
    }

    /**
     * Cancel the given task.
     *
     * @param task The task to cancel.
     */
    public static void cancel(final BaseTask task) {
        task.cancel();
    }

    private static <T> void execute(final ExecutorService pool, final BaseTask<T> task) {
        executeWithDelay(pool, task, 0, TimeUnit.MILLISECONDS);
    }

    private static <T> void executeWithDelay(
            final ExecutorService pool,
            final BaseTask<T> task,
            final long delay,
            final TimeUnit unit) {
        if (delay <= 0) {
            getScheduledByTask(task).execute(() -> pool.execute(task));
        } else {
            getScheduledByTask(task).schedule(() -> pool.execute(task), delay, unit);
        }
    }

    private static <T> void executeAtFixedRate(
            final ExecutorService pool,
            final BaseTask<T> task,
            long initialDelay,
            final long period,
            final TimeUnit unit) {
        task.isSchedule = true;
        getScheduledByTask(task)
                .scheduleAtFixedRate(() -> pool.execute(task), initialDelay, period, unit);
    }

    private static synchronized ScheduledExecutorService getScheduledByTask(final BaseTask task) {
        ScheduledExecutorService scheduled = TASK_SCHEDULED.get(task);
        if (scheduled == null) {
            UtilsThreadFactory factory = new UtilsThreadFactory("scheduled", Thread.MAX_PRIORITY);
            scheduled = new ScheduledThreadPoolExecutor(1, factory);
            TASK_SCHEDULED.put(task, scheduled);
        }
        return scheduled;
    }

    private static synchronized void removeScheduleByTask(final BaseTask task) {
        ScheduledExecutorService scheduled = TASK_SCHEDULED.get(task);
        if (scheduled != null) {
            TASK_SCHEDULED.remove(task);
            scheduled.shutdownNow();
        }
    }

    private static ExecutorService getPoolByTypeAndPriority(final int type) {
        return getPoolByTypeAndPriority(type, Thread.NORM_PRIORITY);
    }

    private static synchronized ExecutorService getPoolByTypeAndPriority(
            final int type, final int priority) {
        ExecutorService pool;
        SparseArray<ExecutorService> priorityPools = TYPE_PRIORITY_POOLS.get(type);
        if (priorityPools == null) {
            priorityPools = new SparseArray<>();
            pool = createPoolByTypeAndPriority(type, priority);
            priorityPools.put(priority, pool);
            TYPE_PRIORITY_POOLS.put(type, priorityPools);
        } else {
            pool = priorityPools.get(priority);
            if (pool == null) {
                pool = createPoolByTypeAndPriority(type, priority);
                priorityPools.put(priority, pool);
            }
        }
        return pool;
    }

    private static ExecutorService createPoolByTypeAndPriority(final int type, final int priority) {
        switch (type) {
            case TYPE_SINGLE:
                return new ScheduledThreadPoolExecutor(
                        1, new UtilsThreadFactory("single", priority));
            case TYPE_CACHED:
                return new ScheduledThreadPoolExecutor(
                        1, new UtilsThreadFactory("cached", priority));
            case TYPE_IO:
                return new ThreadPoolExecutor(
                        2 * CPU_COUNT + 1,
                        2 * CPU_COUNT + 1,
                        30,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(128),
                        new UtilsThreadFactory("io", priority));
            case TYPE_CPU:
                return new ThreadPoolExecutor(
                        CPU_COUNT + 1,
                        2 * CPU_COUNT + 1,
                        30,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(128),
                        new UtilsThreadFactory("cpu", priority));
            default:
                return new ScheduledThreadPoolExecutor(
                        type, new UtilsThreadFactory("fixed(" + type + ")", priority));
        }
    }

    private static Executor getDeliver() {
        if (sDeliver == null) {
            sDeliver =
                    new Executor() {
                        private final Handler mHandler = new Handler(Looper.getMainLooper());

                        @Override
                        public void execute(@NonNull Runnable command) {
                            mHandler.post(command);
                        }
                    };
        }
        return sDeliver;
    }

    /**
     * Set the deliver.
     *
     * @param deliver The deliver.
     */
    public static void setDeliver(final Executor deliver) {
        sDeliver = deliver;
    }

    public abstract static class SimpleTask extends BaseSimpleTask<String> {

        /**
         * 简单的实现
         */
        public abstract void start();

        @Nullable
        @Override
        public String doInBackground() {
            start();
            return null;
        }
    }

    public abstract static class BaseSimpleTask<T> extends BaseTask<T> {

        @Override
        public void onCancel() {
        }

        @Override
        public void onFail(Throwable t) {
        }

        @Override
        public void onSuccess(@Nullable T result) {
        }
    }

    public abstract static class BaseTask<T> implements Runnable {

        private static final int NEW = 0;
        private static final int COMPLETING = 1;
        private static final int CANCELLED = 2;
        private static final int EXCEPTIONAL = 3;

        private volatile int state = NEW;
        private boolean isSchedule;

        /**
         * 后台运行
         *
         * @return
         * @throws Exception
         */
        @Nullable
        public abstract T doInBackground() throws Exception;

        /**
         * 成功返回
         *
         * @param result
         */
        public abstract void onSuccess(@Nullable T result);

        /**
         * 取消
         */
        public abstract void onCancel();

        /**
         * 错误回调
         *
         * @param t
         */
        public abstract void onFail(Throwable t);

        @Override
        public void run() {
            try {
                final T result = doInBackground();

                if (state != NEW) {
                    return;
                }

                if (isSchedule) {
                    getDeliver().execute(() -> onSuccess(result));
                } else {
                    state = COMPLETING;
                    getDeliver()
                            .execute(
                                    () -> {
                                        onSuccess(result);
                                        removeScheduleByTask(BaseTask.this);
                                    });
                }
            } catch (Exception e) {
                if (state != NEW) {
                    return;
                }

                state = EXCEPTIONAL;
                getDeliver()
                        .execute(
                                () -> {
                                    onFail(e);
                                    removeScheduleByTask(BaseTask.this);
                                });
            }
        }

        public void cancel() {
            if (state != NEW) {
                return;
            }

            state = CANCELLED;
            getDeliver()
                    .execute(
                            () -> {
                                onCancel();
                                removeScheduleByTask(BaseTask.this);
                            });
        }

        public boolean isCanceled() {
            return state == CANCELLED;
        }
    }

    public abstract static class MyBaseTask<T> extends BaseTask<T> {

        private static final int NEW = 0;
        private static final int COMPLETING = 1;
        private static final int CANCELLED = 2;
        private static final int EXCEPTIONAL = 3;

        private volatile int state = NEW;
        private boolean isSchedule;

        public void complete() {
            if (state != NEW) {
                return;
            }

            state = COMPLETING;
        }
    }


    public static final class UtilsThreadFactory extends AtomicLong implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private static final long serialVersionUID = -9209200509960368598L;
        private final String namePrefix;
        private final int priority;

        public UtilsThreadFactory(String prefix, int priority) {
            namePrefix = prefix + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
            this.priority = priority;
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t =
                    new Thread(r, namePrefix + getAndIncrement()) {
                        @Override
                        public void run() {
                            try {
                                super.run();
                            } catch (Exception e) {
                                LogUtils.error(e);
                            }
                        }
                    };
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            t.setPriority(priority);
            return t;
        }
    }
}
