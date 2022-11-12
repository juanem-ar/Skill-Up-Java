package com.alkemy.wallet.controller;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.ECurrency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.repository.IAccountRepository;
import com.alkemy.wallet.repository.IUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // (addFilters = false) // WARNING : without filters
public class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IAccountRepository accountRepository;
    @Autowired
    private IUserRepository userRepository;

    private String uri = "/accounts";

    @BeforeAll
    static void setUpBeforeClass() throws Exception {}

    @AfterAll
    static void tearDownAfterClass() throws Exception {}

    @BeforeEach
    void setUp() throws Exception {}

    @AfterEach
    void tearDown() throws Exception {}


    @Test
    @Transactional
    void listAccountsByUser_NoAuthenticated_Unauthorized() throws Exception {

        User user1 = new User();

        user1.setFirstName("user");
        user1.setLastName("test");
        user1.setEmail("user@email.com");
        user1.setPassword("password");
        userRepository.save(user1);

        Account acc1 = new Account();

        Double transactionLimit = 1000.0;
        acc1.setUser(user1);
        acc1.setCurrency(ECurrency.USD);
        acc1.setBalance(0.0);
        acc1.setTransactionLimit(transactionLimit);
        acc1.setDeleted(false);
        accountRepository.save(acc1);

        mockMvc.perform(get(uri+"/1")).andExpect(status().isUnauthorized());
    }
}
