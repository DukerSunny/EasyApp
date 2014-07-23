package com.harreke.utils.requests;

public interface IRequestCallback<RESULT> {
    public void onFailure();

    public void onSuccess(RESULT result);
}