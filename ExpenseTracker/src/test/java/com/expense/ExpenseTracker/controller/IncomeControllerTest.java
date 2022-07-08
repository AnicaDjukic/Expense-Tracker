package com.expense.ExpenseTracker.controller;

import com.expense.ExpenseTracker.dto.IncomeRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class IncomeControllerTest {

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
    public void test_create_income_success() throws Exception {

        IncomeRequestDto newIncome = new IncomeRequestDto("some description",
                100.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b311e"));

        mockMvc.perform(post("/api/v1/incomes")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newIncome)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.description").value("some description"))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.incomeGroup.name").value("Pera income group"));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_create_income_forbidden() throws Exception {

        IncomeRequestDto newIncome = new IncomeRequestDto("some description",
                100.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b311e"));

        mockMvc.perform(post("/api/v1/incomes")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newIncome)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: IncomeGroup"));
    }

    @Test
    public void test_create_income_unauthorized() throws Exception {

        IncomeRequestDto newIncome = new IncomeRequestDto("some description",
                100.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b311e"));

        mockMvc.perform(post("/api/v1/incomes")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newIncome)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_create_income_when_income_group_not_found() throws Exception {

        IncomeRequestDto newIncome = new IncomeRequestDto("some description",
                100.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b31ee"));

        mockMvc.perform(post("/api/v1/incomes")
                        .contentType(contentType).content(objectMapper.writeValueAsString(newIncome)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("IncomeGroup not found!"));
    }

    /*** GET ALL ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_all_incomes() throws Exception {
        mockMvc.perform(get("/api/v1/incomes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    public void test_get_all_incomes_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/incomes"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** GET LAST FEW ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_last_few_for_income_group() throws Exception {
        mockMvc.perform(get("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e/incomes"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType)).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_get_last_few_for_income_group_access_denied() throws Exception {
        mockMvc.perform(get("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e/incomes"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: IncomeGroup"));
    }

    @Test
    public void test_get_last_few_for_income_group_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/income-groups/d413d87f-6fde-4a5a-aeed-c4b9c50b311e/incomes"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** GET BY ID ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_get_income_by_id() throws Exception {

        mockMvc.perform(get("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b312e"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.amount").value(1000.0))
                .andExpect(jsonPath("$.description").value("Pera income description"));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_get_income_by_id_forbidden() throws Exception {

        mockMvc.perform(get("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b312e"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: Income"));
    }

    @Test
    public void test_get_income_by_id_unauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b312e"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** PUT ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_put_income_success() throws Exception {

        IncomeRequestDto incomeDto = new IncomeRequestDto("New description",
                300.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b311e"));

        mockMvc.perform(put("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b312e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.description").value("New description"))
                .andExpect(jsonPath("$.amount").value(300.0))
                .andExpect(jsonPath("$.incomeGroup.name").value("Pera income group"));
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_put_income_not_found() throws Exception {

        IncomeRequestDto incomeDto = new IncomeRequestDto("New description",
                300.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b311e"));

        mockMvc.perform(put("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b315e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeDto)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_put_income_when_income_group_not_found() throws Exception {

        IncomeRequestDto incomeDto = new IncomeRequestDto("New description",
                300.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b31ee"));

        mockMvc.perform(put("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b312e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeDto)))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("IncomeGroup not found!"));
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_put_income_forbidden() throws Exception {

        IncomeRequestDto incomeDto = new IncomeRequestDto("New description",
                300.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b311e"));

        mockMvc.perform(put("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b312e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeDto)))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: Income"));
    }

    @Test
    public void test_put_income_unauthorized() throws Exception {

        IncomeRequestDto incomeDto = new IncomeRequestDto("New description",
                300.0, UUID.fromString("d413d87f-6fde-4a5a-aeed-c4b9c50b311e"));

        mockMvc.perform(put("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b312e")
                        .contentType(contentType).content(objectMapper.writeValueAsString(incomeDto)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    /*** DELETE ***/

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_delete_income_success() throws Exception {

        mockMvc.perform(delete("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b313e"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "peraperic", password = "pass")
    public void test_delete_income_not_found() throws Exception {

        mockMvc.perform(delete("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b31ee"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "mikamikic", password = "pass")
    public void test_delete_income_forbidden() throws Exception {

        mockMvc.perform(delete("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b312e"))
                .andExpect(status().isForbidden())
                .andDo(print())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.message").value("Access denied to: Income"));
    }

    @Test
    public void test_delete_income_unauthorized() throws Exception {

        mockMvc.perform(delete("/api/v1/incomes/d413d87f-6fde-4a5a-aeed-c4b9c50b313e"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


}
