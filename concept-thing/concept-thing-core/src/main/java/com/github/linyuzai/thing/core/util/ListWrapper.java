package com.github.linyuzai.thing.core.util;

import lombok.NonNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface ListWrapper<E> extends List<E> {

    List<E> unwrap();

    @Override
    default int size() {
        return unwrap().size();
    }

    @Override
    default boolean isEmpty() {
        return unwrap().isEmpty();
    }

    @Override
    default boolean contains(Object o) {
        return unwrap().contains(o);
    }

    @NonNull
    @Override
    default Iterator<E> iterator() {
        return unwrap().iterator();
    }

    @NonNull
    @Override
    default Object[] toArray() {
        return unwrap().toArray();
    }

    @NonNull
    @Override
    default <T> T[] toArray(@NonNull T[] a) {
        return unwrap().toArray(a);
    }

    @Override
    default boolean add(E e) {
        return unwrap().add(e);
    }

    @Override
    default boolean remove(Object o) {
        return unwrap().remove(o);
    }

    @Override
    default boolean containsAll(@NonNull Collection<?> c) {
        return unwrap().containsAll(c);
    }

    @Override
    default boolean addAll(@NonNull Collection<? extends E> c) {
        return unwrap().addAll(c);
    }

    @Override
    default boolean addAll(int index, @NonNull Collection<? extends E> c) {
        return unwrap().addAll(index, c);
    }

    @Override
    default boolean removeAll(@NonNull Collection<?> c) {
        return unwrap().removeAll(c);
    }

    @Override
    default boolean retainAll(@NonNull Collection<?> c) {
        return unwrap().retainAll(c);
    }

    @Override
    default void clear() {
        unwrap().clear();
    }

    @Override
    default E get(int index) {
        return unwrap().get(index);
    }

    @Override
    default E set(int index, E element) {
        return unwrap().set(index, element);
    }

    @Override
    default void add(int index, E element) {
        unwrap().add(index, element);
    }

    @Override
    default E remove(int index) {
        return unwrap().remove(index);
    }

    @Override
    default int indexOf(Object o) {
        return unwrap().indexOf(o);
    }

    @Override
    default int lastIndexOf(Object o) {
        return unwrap().lastIndexOf(o);
    }

    @NonNull
    @Override
    default ListIterator<E> listIterator() {
        return unwrap().listIterator();
    }

    @NonNull
    @Override
    default ListIterator<E> listIterator(int index) {
        return unwrap().listIterator(index);
    }

    @NonNull
    @Override
    default List<E> subList(int fromIndex, int toIndex) {
        return unwrap().subList(fromIndex, toIndex);
    }

    @Override
    default void replaceAll(UnaryOperator<E> operator) {
        unwrap().replaceAll(operator);
    }

    @Override
    default void sort(Comparator<? super E> c) {
        unwrap().sort(c);
    }

    @Override
    default Spliterator<E> spliterator() {
        return unwrap().spliterator();
    }

    @Override
    default boolean removeIf(Predicate<? super E> filter) {
        return unwrap().removeIf(filter);
    }

    @Override
    default Stream<E> stream() {
        return unwrap().stream();
    }

    @Override
    default Stream<E> parallelStream() {
        return unwrap().parallelStream();
    }

    @Override
    default void forEach(Consumer<? super E> action) {
        unwrap().forEach(action);
    }
}
