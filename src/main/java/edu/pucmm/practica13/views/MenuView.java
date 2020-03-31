package edu.pucmm.practica13.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.AppLayoutMenu;
import com.vaadin.flow.component.applayout.AppLayoutMenuItem;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.charts.Chart;

import java.util.Random;

@Route("menu")
public class MenuView extends Div {


    public MenuView(String selected){
        AppLayout appLayout = new AppLayout();
        appLayout.setBranding(new H3("PROGRAMACIÓN WEB - PRÁCTICA 3"));
        AppLayoutMenu menu = appLayout.createMenu();

        AppLayoutMenuItem dashboardTab = new AppLayoutMenuItem("Dashboard", "dashboard");
        AppLayoutMenuItem usersTab = new AppLayoutMenuItem("Users", "users");
        AppLayoutMenuItem devicesTab = new AppLayoutMenuItem("Devices", "devices");

        menu.addMenuItems(dashboardTab, usersTab, devicesTab);

        switch (selected){
            case "dashboard":
                dashboardTab.getStyle().set("color", "#1676f3");
                break;
            case "users":
                usersTab.getStyle().set("color", "#1676f3");
                break;
            case "devices":
                devicesTab.getStyle().set("color", "#1676f3");
                break;
        }

        add(appLayout);
    }

}

//public class SplineUpdatingEachSecond extends AbstractChartExample {
//
//    @Override public void initDemo() {
//        final Random random = new Random();
//
//        final Chart chart = new Chart();
//
//        final Configuration configuration = chart.getConfiguration();
//        configuration.getChart().setType(ChartType.SPLINE);
//        configuration.getTitle().setText("Live random data");
//
//        XAxis xAxis = configuration.getxAxis();
//        xAxis.setType(AxisType.DATETIME);
//        xAxis.setTickPixelInterval(150);
//
//        YAxis yAxis = configuration.getyAxis();
//        yAxis.setTitle(new AxisTitle("Value"));
//
//        configuration.getTooltip().setEnabled(false);
//        configuration.getLegend().setEnabled(false);
//
//        final DataSeries series = new DataSeries();
//        series.setPlotOptions(new PlotOptionsSpline());
//        series.setName("Random data");
//        for (int i = -19; i <= 0; i++) {
//            series.add(new DataSeriesItem(System.currentTimeMillis() + i * 1000, random.nextDouble()));
//        }
//
//        configuration.setSeries(series);
//
//        runWhileAttached(chart, () -> {
//            final long x = System.currentTimeMillis();
//            final double y = random.nextDouble();
//            series.add(new DataSeriesItem(x, y), true, true);
//        }, 1000, 1000);
//
//        add(chart);
//    }
//}