package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ExpenseControllerTest {

    private final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype());

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    /*** CREATE ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_create_expense_success() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("some description",
                100.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c46322f"));

        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.description").value("some description"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.expenseGroup.name").value("Pera new expense group"));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_create_expense_forbidden() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("some description",
                100.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c46321f"));

        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: ExpenseGroup"));
    }

    @Test
    public void test_create_expense_unauthorized() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("some description",
                100.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c46321f"));

        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_create_expense_when_expense_group_not_found() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("some description",
                100.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c57432e"));

        mockMvc.perform(post("/api/v1/expenses")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("ExpenseGroup not found!"));
    }

    /*** GET ALL ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_all_expenses() throws Exception {
        mockMvc.perform(get("/api/v1/expenses"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    public void test_get_all_expenses_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/expenses"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** GET LAST FEW ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_last_few_for_expense_group() throws Exception {
        mockMvc.perform(get("/api/v1/expense-groups/46f8f3fa-e285-4895-8b9f-29391c46321f/expenses"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_last_few_for_expense_group_access_denied() throws Exception {
        mockMvc.perform(get("/api/v1/expense-groups/57f8f3fa-e285-4895-8b9f-29391c46432e/expenses"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: ExpenseGroup"));
    }

    @Test
    public void test_get_last_few_for_expense_group_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/expense-groups/57f8f3fa-e285-4895-8b9f-29391c46432e/expenses"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** GET BY ID ***/

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_get_by_id() throws Exception {

        mockMvc.perform(get("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46002e"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.amount").value(500.0))
                .andExpect(jsonPath("$.description").value("Mika expense description"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_by_id_forbidden() throws Exception {

        mockMvc.perform(get("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46002e"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void test_get_by_id_unauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46002e"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** PUT ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_put_expense_success() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("New description",
                300.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c46321f"));

        mockMvc.perform(put("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46001e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.description").value("New description"))
                .andExpect(jsonPath("$.amount").value(300.0))
                .andExpect(jsonPath("$.expenseGroup.name").value("Pera Expense group"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_put_expense_not_found() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("New description",
                300.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c46321f"));

        mockMvc.perform(put("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46005e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Expense not found!"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_put_expense_when_expense_group_not_found() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("New description",
                300.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c463fff"));

        mockMvc.perform(put("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46001e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("ExpenseGroup not found!"));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_put_expense_forbidden() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("New description",
                300.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c46321f"));

        mockMvc.perform(put("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46001e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void test_put_expense_unauthorized() throws Exception {

        ExpenseRequestDto newExpense = new ExpenseRequestDto("New description",
                300.0, UUID.fromString("46f8f3fa-e285-4895-8b9f-29391c46321f"));

        mockMvc.perform(put("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46001e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpense)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** DELETE ***/

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_delete_expense_success() throws Exception {

        mockMvc.perform(delete("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46003e"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_delete_expense_not_found() throws Exception {

        mockMvc.perform(delete("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46005e"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_delete_expense_forbidden() throws Exception {

        mockMvc.perform(delete("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46003e"))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    public void test_delete_expense_unauthorized() throws Exception {

        mockMvc.perform(delete("/api/v1/expenses/46f8f3fa-e285-4895-8b9f-29391c46003e"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}
