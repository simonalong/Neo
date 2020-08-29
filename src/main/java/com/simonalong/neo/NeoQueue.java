package com.simonalong.neo;

import com.simonalong.neo.util.ObjectUtil;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**
 * @author shizi
 * @since 2020/8/30 3:31 上午
 */
public class NeoQueue implements Deque<Object> {

    private final Deque<Object> dataDeque = new ConcurrentLinkedDeque<>();

    public static NeoQueue of(Object... kvs) {
        NeoQueue neoQueue = new NeoQueue();
        for (Object kv : kvs) {
            neoQueue.addLast(kv);
        }
        return neoQueue;
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
     * @param <T>    目标对象类型
     * @return 集合类型
     */
    public <T> List<T> getFirstToList(Class<T> tClass) {
        return ObjectUtil.toList(getFirst()).stream().map(r -> ObjectUtil.cast(tClass, r)).collect(Collectors.toList());
    }

    public <T> Set<T> getFirstToSet(Class<T> tClass) {
        return ObjectUtil.toSet(getFirst()).stream().map(r -> ObjectUtil.cast(tClass, r)).collect(Collectors.toSet());
    }

    @Override
    public void addFirst(Object o) {
        dataDeque.addFirst(o);
    }

    @Override
    public void addLast(Object o) {
        dataDeque.addLast(o);
    }

    @Override
    public boolean offerFirst(Object o) {
        return dataDeque.offerFirst(o);
    }

    @Override
    public boolean offerLast(Object o) {
        return dataDeque.offerLast(o);
    }

    @Override
    public Object removeFirst() {
        return dataDeque.removeFirst();
    }

    @Override
    public Object removeLast() {
        return dataDeque.removeLast();
    }

    @Override
    public Object pollFirst() {
        return dataDeque.pollFirst();
    }

    @Override
    public Object pollLast() {
        return dataDeque.pollLast();
    }

    @Override
    public Object getFirst() {
        return dataDeque.getFirst();
    }

    @Override
    public Object getLast() {
        return dataDeque.getLast();
    }

    @Override
    public Object peekFirst() {
        return dataDeque.peekFirst();
    }

    @Override
    public Object peekLast() {
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
    public boolean add(Object o) {
        return dataDeque.add(o);
    }

    @Override
    public boolean offer(Object o) {
        return dataDeque.offer(o);
    }

    @Override
    public Object remove() {
        return dataDeque.remove();
    }

    @Override
    public Object poll() {
        return dataDeque.poll();
    }

    @Override
    public Object element() {
        return dataDeque.element();
    }

    @Override
    public Object peek() {
        return dataDeque.peek();
    }

    @Override
    public void push(Object o) {
        dataDeque.push(o);
    }

    @Override
    public Object pop() {
        return dataDeque.pop();
    }

    @Override
    public boolean remove(Object o) {
        return dataDeque.remove(o);
    }

    @Override
    public boolean addAll(Collection c) {
        return dataDeque.addAll(c);
    }

    @Override
    public void clear() {
        dataDeque.clear();
    }

    @Override
    public boolean equals(Object o) {
        return dataDeque.equals(o);
    }

    @Override
    public int hashCode() {
        return dataDeque.hashCode();
    }

    @Override
    public boolean retainAll(Collection c) {
        return dataDeque.retainAll(c);
    }

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
    public Iterator<Object> iterator() {
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
    public Iterator<Object> descendingIterator() {
        return dataDeque.descendingIterator();
    }
}
