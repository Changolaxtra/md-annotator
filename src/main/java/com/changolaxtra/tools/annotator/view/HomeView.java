package com.changolaxtra.tools.annotator.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route("")
@RouteAlias("home")
public class HomeView extends Div {
  public HomeView() {
    setText("Hello world");
  }

}
