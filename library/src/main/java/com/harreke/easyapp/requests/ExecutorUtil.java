package com.harreke.easyapp.requests;

import com.harreke.easyapp.requests.executors.Executor;

/**
 * Created by 启圣 on 2015/6/24.
 */
public class ExecutorUtil {
    public static boolean isFree(Executor<?> executor) {
        return executor == null || !executor.isExecuting();
    }
}
