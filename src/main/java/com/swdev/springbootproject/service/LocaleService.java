package com.swdev.springbootproject.service;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class LocaleService {

  private static final String TMDB_GERMAN = "de-DE";
  private static final String TMDB_ENGLISH = "en-US";

  public String getCurrentLanguage() {
    Locale locale = LocaleContextHolder.getLocale();
    return "de".equals(locale.getLanguage()) ? TMDB_GERMAN : TMDB_ENGLISH;
  }
}
