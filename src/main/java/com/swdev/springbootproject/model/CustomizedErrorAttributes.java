package com.swdev.springbootproject.model;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class CustomizedErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(
      WebRequest webRequest, ErrorAttributeOptions options) {
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

    // Customize the error attributes
    errorAttributes.put("locale", webRequest.getLocale().toString());
    errorAttributes.put("customMessage", "This is a custom error message");

    Throwable error = getError(webRequest);
    if (error != null) {
      errorAttributes.put("exception", error.getClass().getName());
      errorAttributes.put("message", error.getMessage());
    }

    return errorAttributes;
  }
}
