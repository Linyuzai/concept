package com.github.linyuzai.concept.sample.inherit.bug6.d;

import com.github.linyuzai.concept.sample.inherit.bug6.a.Bug6A;
import com.github.linyuzai.concept.sample.inherit.bug6.b.Bug6B;
import com.github.linyuzai.concept.sample.inherit.bug6.c.Bug6C;
import com.github.linyuzai.concept.sample.mapqueue.LinkedBlockingMapQueue;

import java.util.List;

public class Bug6D {

    public Bug6A[] bug6A;

    public SamePkg bug(Bug6B bug6B) {
        System.out.println(new LinkedBlockingMapQueue<>());
        System.out.println(1);
        return new SamePkg();
    }
}
