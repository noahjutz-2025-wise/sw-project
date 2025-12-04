package com.swdev.springbootproject.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Genre {
  ACTION(28),
  ADVENTURE(12),
  ANIMATION(16),
  COMEDY(35),
  CRIME(80),
  DOCUMENTARY(99),
  DRAMA(18),
  FAMILY(10751),
  FANTASY(14),
  HISTORY(36),
  HORROR(27),
  MUSIC(10402),
  MYSTERY(9648),
  ROMANCE(10749),
  SCIFI(878),
  THRILLER(53),
  WESTERN(37);

  private final int genreId;
}
