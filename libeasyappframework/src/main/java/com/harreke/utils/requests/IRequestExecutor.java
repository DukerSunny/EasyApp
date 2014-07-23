package com.harreke.utils.requests;

public interface IRequestExecutor {
    public void cancel();

    public boolean isExecuting();
}