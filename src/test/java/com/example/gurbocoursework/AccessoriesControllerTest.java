package com.example.gurbocoursework;

import com.example.gurbocoursework.Model.Accessories;
import com.example.gurbocoursework.Repositories.AccessoriesRepository;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-info-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-info-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("admin")
public class AccessoriesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccessoriesRepository accessoriesRepository;
    @Test
    public void testAccessories() throws Exception {
        this.mockMvc.perform(get("/accessories"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='accessories-item']").nodeCount(3));
    }
    @Test
    public void testAccessoriesRepository() throws Exception {
        List<Accessories> accessories = new ArrayList<>();
        accessoriesRepository.findAll().forEach(accessories::add);
        int expectedResult = 3;
        assertEquals(expectedResult, accessories.size());
    }
    @Test
    public void testAccessoriesFilter() throws Exception {
        this.mockMvc.perform(get("/accessories/sort").param("type", "Процессоры"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='accessories-item']").nodeCount(1));
    }
    @Test
    public void validAccessoriesTest() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/accessories/add")
                .param("type", "Процессоры")
                .param("name", "Процессор2")
                .param("cell", "1")
                .param("price", "-300")
                .param("weight", "-2")
                .with(csrf());
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("//form/span").exists());
    }
    @Test
    public void addAccessoriesTest() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/accessories/add")
                .param("type", "Процессоры")
                .param("name", "Процессор2")
                .param("cell", "1")
                .param("price", "300")
                .param("weight", "2")
                .with(csrf());
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("//div[@id='accessories-item']").nodeCount(4));
    }
    @Test
    public void removeConnectedAccessories() throws Exception {
        this.mockMvc.perform(post("/accessories/11/remove").with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("//p[@class='validation-error']").exists());
    }
    @Test
    public void removeAccessories() throws Exception {
        this.mockMvc.perform(post("/accessories/12/remove").with(csrf()))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("//div[@id='accessories-item']").nodeCount(2));
    }

}
