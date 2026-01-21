package com.swdev.springbootproject.component.converter;

import com.swdev.springbootproject.entity.BookmarkStatus;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class QueryParamToBookmarkStatusConverter implements Converter<String, BookmarkStatus> {

  @Override
  public @Nullable BookmarkStatus convert(String source) {
    return switch (source) {
      case "watch-later" -> BookmarkStatus.WATCH_LATER;
      case "in-progress" -> BookmarkStatus.IN_PROGRESS;
      case "done" -> BookmarkStatus.DONE;
      case "abandoned" -> BookmarkStatus.ABANDONED;
      default -> throw new IllegalStateException("Unexpected value: " + source);
    };
  }
}
