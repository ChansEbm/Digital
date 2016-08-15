package com.szbb.pro.entity;

public class TxBuilder {
    private int s;
    private String i;

    public TxBuilder setS(int s) {
        this.s = s;
        return this;
    }

    public TxBuilder setI(String i) {
        this.i = i;
        return this;
    }

    public Tx createTx() {
        return new Tx(s, i);
    }
}