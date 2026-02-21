package com.elvira.pages;

import com.elvira.core.config.FrameworkConfig;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class TodoPage {

    private final Page page;
    
    private final Locator todoItems;
    private final Locator activeFilter;
        public TodoPage(Page page) {
        this.page = page;

        this.todoItems = page.locator(".todo-list li");
        this.activeFilter = page.locator("text=Active");
        page.locator("text=Completed");
    }

    public void navigate() {
        page.navigate(FrameworkConfig.baseUrl());
    }

    public void addTodo(String text) {
        Locator input = page.locator(".new-todo");
        input.fill(text);
        input.press("Enter");
    }

    public boolean isTodoVisible(String text) {
    return page.locator("text=" + text).isVisible();
    }

    public void toggleTodo(int index) {
        todoItems.nth(index).locator(".toggle").click();
    }

    public Locator getTodoItems() {
        return todoItems;
    }

    public void clickActiveFilter() {
        page.locator("text=Active");
        activeFilter.click();
    }

    public void clickCompletedFilter() {
        page.getByRole(AriaRole.LINK,
            new
        Page.GetByRoleOptions().setName("Completed")).click();
    }

    public Locator getVisibleTodos() {
        page.locator(".todo-list li");
        return todoItems;
    }

}