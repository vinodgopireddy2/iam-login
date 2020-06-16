package com.intuit.iam.validation.Rules;

public interface Rule<T> {
    void applyRule(T t);
}
