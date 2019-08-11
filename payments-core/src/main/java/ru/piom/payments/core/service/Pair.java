package ru.piom.payments.core.service;

import lombok.Data;

@Data
public class Pair<A, B> {
    A first;
    B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
}
