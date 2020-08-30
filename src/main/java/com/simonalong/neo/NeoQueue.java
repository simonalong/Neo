package com.simonalong.neo;

import com.simonalong.neo.util.ObjectUtil;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/8/30 3:31 上午
 */
public class NeoQueue<T> implements Deque<T> {

    private final Deque<T> dataDeque = new ConcurrentLinkedDeque<>();

    @SafeVarargs
    public static <E> NeoQueue<E> of(E... kvs) {
        NeoQueue<E> neoQueue = new NeoQueue<>();
        for (E kv : kvs) {
            neoQueue.addLast(kv);
        }
        return neoQueue;
    }

    public Queue<T> getQueue() {
        return dataDeque;
    }

    public List<T> toList() {
        return new LinkedList<>(dataDeque);
    }

    /**
     * 获取字符
     *
     * @return 字符，如果是字符串，则返回字符串的第一个字符
     */
    public Character getFistToChar() {
        return ObjectUtil.toChar(getFirst());
    }

    /**
     * 返回值为String类型的值
     *
     * @return String类型的值
     */
    public String getFistToString() {
        return ObjectUtil.toStr(getFirst());
    }

    /**
     * 获取值类型为Boolean的值
     *
     * @param key map中对应的key
     * @return 若值为true或者TRUE，则返回true，否则其他任何值都返回false，包括false和null
     */
    public Boolean getFirstToBoolean(String key) {
        return ObjectUtil.toBoolean(getFirst());
    }

    public Byte getFirstToByte() {
        return ObjectUtil.toByte(getFirst());
    }

    public Short getFirstToShort() {
        return ObjectUtil.toShort(getFirst());
    }

    public Integer getFirstToInteger() {
        return ObjectUtil.toInt(getFirst());
    }

    public Long getFirstToLong() {
        return ObjectUtil.toLong(getFirst());
    }

    public Double getFirstToDouble() {
        return ObjectUtil.toDouble(getFirst());
    }

    public Float getFirstToFloat() {
        return ObjectUtil.toFloat(getFirst());
    }

    /**
     * 获取value并转换为指定的类型的list
     *
     * @param tClass 目标对象的类，对于有些类型不是这个类的，只要能转换到这个类也是可以的，比如原先存的是{@code List<String>}，取的时候只要String可以转为Integer，那么这个tClass可以为Integer.class
     * @param <E>    目标对象类型
     * @return 集合类型
     */
    public <E> List<E> getFirstToList(Class<E> tClass) {
        return ObjectUtil.toList(getFirst()).stream().map(r -> ObjectUtil.cast(tClass, r)).collect(Collectors.toList());
    }

    public <E> Set<E> getFirstToSet(Class<E> tClass) {
        return ObjectUtil.toSet(getFirst()).stream().map(r -> ObjectUtil.cast(tClass, r)).collect(Collectors.toSet());
    }

    @SuppressWarnings("all")
    @Override
    public NeoQueue<T> clone() {
        NeoQueue neoQueue = NeoQueue.of();
        neoQueue.addAll(this.getQueue());
        return neoQueue;
    }

    @Override
    public void addFirst(T o) {
        dataDeque.addFirst(o);
    }

    @Override
    public void addLast(T o) {
        dataDeque.addLast(o);
    }

    @Override
    public boolean offerFirst(T o) {
        return dataDeque.offerFirst(o);
    }

    @Override
    public boolean offerLast(T o) {
        return dataDeque.offerLast(o);
    }

    @Override
    public T removeFirst() {
        return dataDeque.removeFirst();
    }

    @Override
    public T removeLast() {
        return dataDeque.removeLast();
    }

    @Override
    public T pollFirst() {
        return dataDeque.pollFirst();
    }

    @Override
    public T pollLast() {
        return dataDeque.pollLast();
    }

    @Override
    public T getFirst() {
        return dataDeque.getFirst();
    }

    @Override
    public T getLast() {
        return dataDeque.getLast();
    }

    @Override
    public T peekFirst() {
        return dataDeque.peekFirst();
    }

    @Override
    public T peekLast() {
        return dataDeque.peekLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return dataDeque.removeFirstOccurrence(o);
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return dataDeque.removeLastOccurrence(o);
    }

    @Override
    public boolean add(T o) {
        return dataDeque.add(o);
    }

    @Override
    public boolean offer(T o) {
        return dataDeque.offer(o);
    }

    @Override
    public T remove() {
        return dataDeque.remove();
    }

    @Override
    public T poll() {
        return dataDeque.poll();
    }

    @Override
    public T element() {
        return dataDeque.element();
    }

    @Override
    public T peek() {
        return dataDeque.peek();
    }

    @Override
    public void push(T o) {
        dataDeque.push(o);
    }

    @Override
    public T pop() {
        return dataDeque.pop();
    }

    @Override
    public boolean remove(Object o) {
        return dataDeque.remove(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection c) {
        return dataDeque.addAll(c);
    }

    @Override
    public void clear() {
        dataDeque.clear();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object o) {
        if(o instanceof NeoQueue) {
            Queue queue = ((NeoQueue)o).getQueue();
            return dataDeque.equals(queue);
        }
        return dataDeque.equals(o);
    }

    @Override
    public int hashCode() {
        return dataDeque.hashCode();
    }

    @SuppressWarnings("all")
    @Override
    public boolean retainAll(Collection c) {
        return dataDeque.retainAll(c);
    }

    @SuppressWarnings("all")
    @Override
    public boolean removeAll(Collection c) {
        return dataDeque.removeAll(c);
    }

    @Override
    public boolean containsAll(Collection c) {
        return dataDeque.containsAll(c);
    }

    @Override
    public boolean contains(Object o) {
        return dataDeque.contains(o);
    }

    @Override
    public int size() {
        return dataDeque.size();
    }

    @Override
    public boolean isEmpty() {
        return dataDeque.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return dataDeque.iterator();
    }

    @Override
    public Object[] toArray() {
        return dataDeque.toArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] toArray(Object[] a) {
        return dataDeque.toArray(a);
    }

    @Override
    public Iterator<T> descendingIterator() {
        return dataDeque.descendingIterator();
    }
}
