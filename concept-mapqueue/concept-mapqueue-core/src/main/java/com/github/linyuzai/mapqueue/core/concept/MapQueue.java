package com.github.linyuzai.mapqueue.core.concept;

import java.util.*;

public interface MapQueue<K, V> {

    Map<K, V> map();

    Queue<V> queue();
}
