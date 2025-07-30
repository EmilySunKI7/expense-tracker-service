package services;

import managers.BookManager;
import managers.CategoryManager;
import models.Book;
import models.Expense;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ExpensesTest {
    static Expenses expense;

    @BeforeAll
    static void init() {
        System.setProperty("config.file.path", "/app.properties");
        expense = new Expenses(new BookManager(new CategoryManager()));
    }

    @Test
    void getAnExpenseById() throws IOException {

        Expense returnExp = expense.getAnExpenseById(1);
        assertEquals(1, returnExp.id);
    }

    @Test
    void getAllTest() throws IOException {

        Book returnBook = expense.getAll();
        assertEquals(6, returnBook.getExpenses().size());
    }

}