package com.alkemy.wallet.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MessageTranslator {
  private final MessageSource messageSource;

  public String translate(String code, Object[] args) {
    return messageSource.getMessage(code, args,
        LocaleContextHolder.getLocale());
  }

  public String translate(String code) {
    return messageSource.getMessage(code, null,
        LocaleContextHolder.getLocale());
  }

}
