package com.changolaxtra.tools.annotator.view.factory;

import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public final class LayoutFactory {

  private LayoutFactory(){}

  public static VerticalLayout getVerticalLayout() {
    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setWidthFull();
    verticalLayout.setHeightFull();
    verticalLayout.setAlignItems(Alignment.START);
    verticalLayout.setJustifyContentMode(JustifyContentMode.START);
    verticalLayout.setPadding(true);

    return verticalLayout;
  }

  public static HorizontalLayout getHorizontalLayout() {
    final HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.setWidthFull();
    horizontalLayout.setAlignItems(Alignment.START);
    horizontalLayout.setJustifyContentMode(JustifyContentMode.START);
    horizontalLayout.setPadding(true);

    return horizontalLayout;
  }

}
