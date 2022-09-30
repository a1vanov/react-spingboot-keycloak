package com.innrate.common.database.model;

/**
 * Represents an object with some unique key.
 *
 * @param <K> type of unique key.
 */
public interface Unique<K> {
    K getKey();
}
