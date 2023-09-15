package com.example.gurbocoursework;

import com.example.gurbocoursework.Model.Clearance;
import com.example.gurbocoursework.Repositories.ClearanceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-info-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-info-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("admin")
public class ClearanceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClearanceRepository operationRepository;
    @Test
    public void testOperationRepository() throws Exception {
        List<Clearance> operations = new ArrayList<>();
        operationRepository.findAll().forEach(operations::add);
        int expectedResult = 3;
        assertEquals(expectedResult, operations.size());
    }
    @Test
    public void testFilterOperations() throws Exception {
        this.mockMvc.perform(get("/operations/sort").param("type", "Прибытие"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='operation-item']").nodeCount(1));
    }
}
