package com.alkemy.wallet.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // WARNING : without filters
class UserControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;
  
  // add repository ...

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {}

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void endpointName_status_expectedResult() throws Exception {
    // change status, add info to database
    
    // do the request with headers and body
    String uri = "";
    MvcResult result = mockMvc.perform(get(uri)).andExpect(status().isOk()).andReturn();
    
    // check result
    assertNotNull(result);
    
    fail("Not yet implemented");
  }

}
