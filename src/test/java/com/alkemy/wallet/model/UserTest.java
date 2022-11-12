package com.alkemy.wallet.model;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import com.alkemy.wallet.repository.IUserRepository;

@DataJpaTest
@ActiveProfiles(profiles = {"test"})
class UserTest {
  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  private User user;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {
    user = new User();
    user.setFirstName("first name");
    user.setLastName("last name");
    user.setEmail("elemail@mail.com");
    user.setPassword("password");
  }

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void firstName_Null_ThrowException() {
    user.setFirstName(null);

    assertThrows(ConstraintViolationException.class,
        () -> testEntityManager.persist(user));
  }

  @Test
  void lastName_Null_ThrowException() {
    user.setLastName(null);

    assertThrows(ConstraintViolationException.class,
        () -> testEntityManager.persist(user));
  }

  @Test
  void email_Null_ThrowException() {
    user.setEmail(null);

    assertThrows(ConstraintViolationException.class,
        () -> testEntityManager.persist(user));
  }

  @Test
  void password_Null_ThrowException() {
    user.setPassword(null);

    assertThrows(ConstraintViolationException.class,
        () -> testEntityManager.persist(user));
  }

  @Test
  void accountId_HasAccountId_UserCanGetAccountId() {
    user = testEntityManager.persist(user);

    Account account = new Account();
    account.setCurrency(ECurrency.ARS);
    account.setTransactionLimit(100.0);
    account.setBalance(.0);
    account.setUser(user);
    account = testEntityManager.persist(account);

    user.addAccount(account);
    user = testEntityManager.persist(user);

    Optional<User> result = userRepository.findById(user.getId());

    assertTrue(result.isPresent());
    assertEquals(account.getId(),
        result.get().getAccounts().get(0).getId());
  }

  @Test
  void softDelete_DeleteAndRecover_NotFoundAndFound() {
    user = testEntityManager.persist(user);
    assertEquals(1, userRepository.findAll().size());

    EntityManager entityManager =
        testEntityManager.getEntityManager();
    Session session = entityManager.unwrap(Session.class);
    Filter filter = session.enableFilter("deletedUserFilter");
    filter.setParameter("isDeleted", false);

    userRepository.delete(user);
    assertEquals(0, userRepository.findAll().size());

    filter.setParameter("isDeleted", true);
    assertEquals(1, userRepository.findAll().size());
  }

  @Test
  void softDelete_Delete_NotFound() {
    user = testEntityManager.persist(user);

    userRepository.delete(user);

    EntityManager entityManager =
        testEntityManager.getEntityManager();
    Session session = entityManager.unwrap(Session.class);
    Filter filter = session.enableFilter("deletedUserFilter");
    filter.setParameter("isDeleted", false);

    assertEquals(0, userRepository.findAll().size());
  }

  @Test
  void creationDate_Update_NotChange() {
    user = testEntityManager.persist(user);

    Timestamp creationDate = user.getCreationDate();

    user.setFirstName("nuevo first date");
    user = testEntityManager.persistAndFlush(user);

    assertNotNull(creationDate);
    assertEquals(creationDate, user.getCreationDate());
  }

  @Test
  void updateDate_Update_Change() {
    user = userRepository.save(user);
    user.setFirstName("update first name");
    user.setLastName("update last name");
    user = testEntityManager.persistAndFlush(user);

    Optional<User> updateUser =
        userRepository.findById(user.getId());

    assertNotNull(updateUser.get().getUpdateDate());
    assertNotEquals(user.getCreationDate(),
        updateUser.get().getUpdateDate());
  }
}
