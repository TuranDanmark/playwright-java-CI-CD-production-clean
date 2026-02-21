package com.elvira.tests;

import com.elvira.core.base.BaseTest;
import com.elvira.pages.TodoPage;
import com.elvira.utils.TestListener;
import io.qameta.allure.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@ExtendWith(TestListener.class)
@Epic("Todo App")
@Feature("Todo Management")
public class TodoTest extends BaseTest {

@Tag("smoke")
@Test
@Story("User can add todo")
@Description("Assert that user can add a new todo item")
@Severity(SeverityLevel.CRITICAL)
void userCanAddTodoItem() {
TodoPage todoPage = new TodoPage(getPage());
todoPage.navigate();
todoPage.addTodo("Learn Playwright");
assertThat(todoPage.getTodoItems()).hasCount(1);
}

@Tag("smoke")
@Test
@Story("User can add todo")
@Description("Assert that user can add a new todo item")
@Severity(SeverityLevel.CRITICAL)
void userCanAddTodo() {

    TodoPage todoPage = new TodoPage(getPage());
    todoPage.navigate();

    todoPage.addTodo("Buy milk");

    assertThat(page.locator("text=Buy milk")).isVisible();
}

@Tag("smoke")
@Test
@Story("User can add todo")
@Description("Assert that user can add a new todo item")
@Severity(SeverityLevel.CRITICAL)
void userCanCompleteTodo() {
    TodoPage todoPage = new TodoPage(getPage());
    todoPage.navigate();

    todoPage.addTodo("Task 1");
    todoPage.toggleTodo(0);

    assertThat(todoPage.getTodoItems().first()).hasClass("completed");

}

@Tag("smoke")
@Test
@Story("User can add todo")
@Description("Verify that user can add a new todo item")
@Severity(SeverityLevel.CRITICAL)
void userCanFilterCompletedTasks() {

    TodoPage todoPage = new TodoPage(getPage());

    todoPage.navigate();

    todoPage.addTodo("Active Task");
    todoPage.addTodo("Completed Task");

    todoPage.toggleTodo(1); // отмечаем вторую как выполненную

    todoPage.clickCompletedFilter();


    assertThat(todoPage.getVisibleTodos()).hasCount(1);
    assertThat(todoPage.getVisibleTodos().first()).hasText("Completed Task");
}

}
