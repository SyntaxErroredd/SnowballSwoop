package me.syntaxerror.snowballswoop.util;

public interface Factory<T, K> {
    K factor(T t);
}
