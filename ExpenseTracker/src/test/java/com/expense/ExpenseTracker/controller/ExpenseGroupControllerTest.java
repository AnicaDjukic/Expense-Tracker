package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.ExpenseGroupRequestDto;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ExpenseGroupControllerTest {

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
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_create_expense_group_success() throws Exception {

        ExpenseGroupRequestDto newExpenseGroup = new ExpenseGroupRequestDto("New Expense group", "Some description");

        mockMvc.perform(post("/api/v1/expense-groups")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpenseGroup)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name").value("New Expense group"))
                .andExpect(jsonPath("$.description").value("Some description"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_create_expense_group_conflict() throws Exception {

        ExpenseGroupRequestDto newExpenseGroup = new ExpenseGroupRequestDto("Pera Expense group", "Some description");

        mockMvc.perform(post("/api/v1/expense-groups")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpenseGroup)))
                .andExpect(status().isConflict())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("ExpenseGroup with name: Pera Expense group already exists!"));
    }

    @Test
    public void test_create_expense_group_unauthorized() throws Exception {

        ExpenseGroupRequestDto newExpenseGroup = new ExpenseGroupRequestDto("New Expense group", "Some description");

        mockMvc.perform(post("/api/v1/expense-groups")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpenseGroup)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** GET ALL ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_all_expense_groups() throws Exception {

        mockMvc.perform(get("/api/v1/expense-groups"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content.[*].name").value(hasItem("Pera Expense group")));
    }

    @Test
    public void test_get_all_expense_groups_unauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/expense-groups"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** GET BY ID ***/

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_get_expense_group_by_id() throws Exception {

        mockMvc.perform(get("/api/v1/expense-groups/57f8f3fa-e285-4895-8b9f-29391c46432e"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.description").value("Mika expense group description"))
                .andExpect(jsonPath("$.name").value("Mika expense group"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_expense_group_by_id_forbidden() throws Exception {

        mockMvc.perform(get("/api/v1/expense-groups/57f8f3fa-e285-4895-8b9f-29391c46432e"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: ExpenseGroup"));
    }

    @Test
    public void test_get_expense_group_by_id_unauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/expense-groups/57f8f3fa-e285-4895-8b9f-29391c46432e"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** UPDATE ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_update_expense_group_success() throws Exception {

        ExpenseGroupRequestDto newExpenseGroup = new ExpenseGroupRequestDto("Pera new expense group", "Some new description");

        mockMvc.perform(put("/api/v1/expense-groups/46f8f3fa-e285-4895-8b9f-29391c46322f")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpenseGroup)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name").value("Pera new expense group"))
                .andExpect(jsonPath("$.description").value("Some new description"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_update_expense_group_not_found() throws Exception {

        ExpenseGroupRequestDto newExpenseGroup = new ExpenseGroupRequestDto("New name for expense group", "Some new description");

        mockMvc.perform(put("/api/v1/expense-groups/46f8f3fa-e285-4895-8b9f-29391c463fff")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpenseGroup)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("ExpenseGroup not found!"));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_update_expense_group_forbidden() throws Exception {

        ExpenseGroupRequestDto newExpenseGroup = new ExpenseGroupRequestDto("New name for expense group", "Some new description");

        mockMvc.perform(put("/api/v1/expense-groups/46f8f3fa-e285-4895-8b9f-29391c46322f")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpenseGroup)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: ExpenseGroup"));
    }

    @Test
    public void test_update_expense_group_unauthorized() throws Exception {

        ExpenseGroupRequestDto newExpenseGroup = new ExpenseGroupRequestDto("New name for expense group", "Some new description");

        mockMvc.perform(put("/api/v1/expense-groups/46f8f3fa-e285-4895-8b9f-29391c46322f")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newExpenseGroup)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** DELETE ***/

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_delete_expense_group_success() throws Exception {

        mockMvc.perform(delete("/api/v1/expense-groups/57f8f3fa-e285-4895-8b9f-29391c46433e"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_delete_expense_group_forbidden() throws Exception {

        mockMvc.perform(delete("/api/v1/expense-groups/57f8f3fa-e285-4895-8b9f-29391c46432e"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: ExpenseGroup"));
    }

    @Test
    public void test_delete_expense_group_unauthorized() throws Exception {

        mockMvc.perform(delete("/api/v1/expense-groups/57f8f3fa-e285-4895-8b9f-29391c46433e"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}
