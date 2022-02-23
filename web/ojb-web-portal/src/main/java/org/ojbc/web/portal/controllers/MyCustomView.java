package org.ojbc.web.portal.controllers;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

@Component("myCustomView")
public class MyCustomView implements View {

  @Override
  public String getContentType() {
      return "text/html";
  }

  @Override
  public void render(Map<String, ?> map, HttpServletRequest httpServletRequest,
                     HttpServletResponse httpServletResponse) throws Exception {
      PrintWriter writer = httpServletResponse.getWriter();
      writer.write("msg rendered in MyCustomView: " + map.get("msg"));
  }
}