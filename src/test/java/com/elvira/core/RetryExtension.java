package com.elvira.core;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class RetryExtension implements TestExecutionExceptionHandler {

    private static final int MAX_RETRIES = 2;

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        int currentRetry = getRetryCount(context);

        if (currentRetry < MAX_RETRIES) {
            incrementRetryCount(context);
            System.out.println("Retrying test: " + context.getDisplayName()
                    + " | attempt: " + (currentRetry + 1));
            throw throwable; // JUnit перезапустит
        }

        throw throwable;
    }

    private int getRetryCount(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrDefault(context.getUniqueId(), Integer.class, 0);
    }

    private void incrementRetryCount(ExtensionContext context) {
        context.getStore(ExtensionContext.Namespace.GLOBAL)
                .put(context.getUniqueId(), getRetryCount(context) + 1);
    }
}
