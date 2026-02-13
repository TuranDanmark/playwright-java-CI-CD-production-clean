package com.elvira.core;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.ByteArrayInputStream;

public class AllureListener implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

        Page page = BaseTest.getPageStatic();

        if (page != null) {
            byte[] screenshot = page.screenshot();

            Allure.addAttachment(
                    "Screenshot on Failure",
                    new ByteArrayInputStream(screenshot)
            );
        }
    }
}
