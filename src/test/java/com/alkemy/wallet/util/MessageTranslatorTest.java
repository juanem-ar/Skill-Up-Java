package com.alkemy.wallet.util;

import static org.junit.jupiter.api.Assertions.*;
import java.text.MessageFormat;
import java.util.Locale;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = { "test" })
class MessageTranslatorTest {
  @Autowired
  private MessageTranslator translator;

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {}

  @AfterEach
  void tearDown() throws Exception {}

  @Test
  void translate_WithoutPlaceholder() {
    String code = "for-testing";
    String message = "for-testing";
    String esMessage = "para-pruebas";

    LocaleContextHolder.resetLocaleContext();
    
    assertEquals(message, translator.translate(code));

    LocaleContextHolder.setLocale(Locale.forLanguageTag("es-ES"));

    assertEquals(esMessage, translator.translate(code));
  }

  @Test
  void translate_WithPlaceholder() {
    String code = "for-testing-with-placeholder";
    String message = "for-{0}-with-placeholder";
    String esMessage = "para-{0}-con-placeholder";

    String holder = "testing";
    Object[] args = new Object[] {holder};

    LocaleContextHolder.resetLocaleContext();
    
    assertEquals(MessageFormat.format(message, holder),
        translator.translate(code, args));

    LocaleContextHolder.setLocale(Locale.forLanguageTag("es-ES"));

    assertEquals(MessageFormat.format(esMessage, holder),
        translator.translate(code, args));
  }
}
