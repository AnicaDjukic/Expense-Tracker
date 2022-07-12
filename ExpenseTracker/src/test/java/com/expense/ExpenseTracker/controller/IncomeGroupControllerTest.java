package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.IncomeGroupRequestDto;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class IncomeGroupControllerTest {

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
    public void test_create_income_group_success() throws Exception {

        IncomeGroupRequestDto newIncomeGroup = new IncomeGroupRequestDto("New Income group", "Some description");

        mockMvc.perform(post("/api/v1/income-groups")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newIncomeGroup)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name").value("New Income group"))
                .andExpect(jsonPath("$.description").value("Some description"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_create_income_group_conflict() throws Exception {

        IncomeGroupRequestDto incomeGroupDto = new IncomeGroupRequestDto("Pera income group", "Some description");

        mockMvc.perform(post("/api/v1/income-groups")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeGroupDto)))
                .andExpect(status().isConflict())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("IncomeGroup with name: Pera income group already exists!"));
    }

    @Test
    public void test_create_income_group_unauthorized() throws Exception {

        IncomeGroupRequestDto newIncomeGroup = new IncomeGroupRequestDto("New income group", "Some description");

        mockMvc.perform(post("/api/v1/income-groups")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newIncomeGroup)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** GET ALL ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_all_income_groups() throws Exception {

        mockMvc.perform(get("/api/v1/income-groups"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content.[*].name").value(hasItem("Pera income group")));
    }

    @Test
    public void test_get_all_income_groups_unauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/income-groups"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** GET BY ID ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_income_group_by_id() throws Exception {

        mockMvc.perform(get("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.description").value("Pera income group description"))
                .andExpect(jsonPath("$.name").value("Pera income group"));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_get_income_group_by_id_forbidden() throws Exception {

        mockMvc.perform(get("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: IncomeGroup"));
    }

    @Test
    public void test_get_income_group_by_id_unauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** UPDATE ***/

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_update_income_group_success() throws Exception {

        IncomeGroupRequestDto incomeGroup = new IncomeGroupRequestDto("New name for income group", "Some new description");

        mockMvc.perform(put("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b322e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeGroup)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name").value("New name for income group"))
                .andExpect(jsonPath("$.description").value("Some new description"));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_update_income_group_not_found() throws Exception {

        IncomeGroupRequestDto incomeGroup = new IncomeGroupRequestDto("New name for income group", "Some new description");

        mockMvc.perform(put("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b3eee")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeGroup)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("IncomeGroup not found!"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_update_income_group_forbidden() throws Exception {

        IncomeGroupRequestDto incomeGroup = new IncomeGroupRequestDto("New name for income group", "Some new description");

        mockMvc.perform(put("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b322e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeGroup)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: IncomeGroup"));
    }

    @Test
    public void test_update_income_group_unauthorized() throws Exception {

        IncomeGroupRequestDto incomeGroup = new IncomeGroupRequestDto("New name for income group", "Some new description");

        mockMvc.perform(put("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b322e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeGroup)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** DELETE ***/

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_delete_income_group_success() throws Exception {

        mockMvc.perform(delete("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b322e"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_delete_income_group_forbidden() throws Exception {

        mockMvc.perform(delete("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: IncomeGroup"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_delete_income_group_which_contains_incomes() throws Exception {

        mockMvc.perform(delete("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("It's not possible to delete income group which contains incomes."));
    }

    @Test
    public void test_delete_income_group_unauthorized() throws Exception {

        mockMvc.perform(delete("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


}
