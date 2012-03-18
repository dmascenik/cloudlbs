package com.cloudlbs.web.core.gwt;

public class Counter {

    private int count = 0;

    public void reset() {
        count = 0;
    }

    public void incr() {
        count++;
    }

    public int get() {
        return count;
    }
}
