package edu.pucmm.practica13.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import edu.pucmm.practica13.data.DeviceMessage;
import edu.pucmm.practica13.services.DeviceMessageServices;
import edu.pucmm.practica13.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

@Push
@Route("dashboard")
public class DashboardView extends Div {

    private FeederThread thread;
    DataProvider<DeviceMessage, Void> dataProvider;

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        add(new Span("Waiting for updates"));

        // Start the data feed thread
        thread = new FeederThread(attachEvent.getUI(), this, dataProvider);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
        thread.interrupt();
        thread = null;
    }

    public DashboardView(@Autowired DeviceService deviceService, @Autowired DeviceMessageServices deviceMessageService){

        VerticalLayout layout = new VerticalLayout();

        MenuView menuView = new MenuView("dashboard");
        menuView.getStyle().set("margin-bottom", "20px");

        Select<String> select = new Select<>();
        select.setItems(deviceService.getAllDevicesIdAndNames());
        select.setPlaceholder("Device");
        select.setLabel("Select a device");

        Chart chart = new Chart(ChartType.SPLINE);

        Configuration configuration = chart.getConfiguration();
        configuration.getTitle().setText("Temperature and Humidity vs Time");
        configuration.getxAxis().setType(AxisType.DATETIME);
        configuration.getxAxis().setTitle("Time");
        configuration.getyAxis().setTitle("Value");

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    if(select.getValue() == null){
                        return new ArrayList<DeviceMessage>().stream();
                    }else{
                        Long id = Long.parseLong(select.getValue().split(" ")[0]);
                        Long deviceId = deviceService.find(id).getId();
                        return deviceMessageService.getAllDeviceMessagesByDevice(deviceId).stream();
                    }
                },
                query -> {
                    Long deviceId = deviceService.find(Long.parseLong(select.getValue().split(" ")[0])).getId();
                    return Math.toIntExact(deviceMessageService.getAllDeviceMessagesByDevice(deviceId).stream().count());
                }
        );

        select.addValueChangeListener(e -> {
            dataProvider.refreshAll();
        });

        DataProviderSeries<DeviceMessage> tempData = new DataProviderSeries<>(dataProvider, DeviceMessage::getTemperature);
        tempData.setId("Temperature");
        tempData.setName("Temperature");
        DataProviderSeries<DeviceMessage> humData = new DataProviderSeries<>(dataProvider, DeviceMessage::getHumidity);
        humData.setId("Humidity");
        humData.setName("Humidity");

        configuration.setSeries(tempData, humData);

        layout.add(menuView, select, chart);
        layout.setPadding(true);
        layout.setMargin(true);
        layout.setSpacing(true);
        add(layout);


    }

    private static class FeederThread extends Thread {
        private final UI ui;
        private final DashboardView view;
        DataProvider<DeviceMessage, Void> dataProvider;

        private int count = 0;

        public FeederThread(UI ui, DashboardView view, DataProvider<DeviceMessage, Void> dataProvider) {
            this.ui = ui;
            this.view = view;
            this.dataProvider = dataProvider;
        }

        @Override
        public void run() {
            try {
                while (count < 10) {
                    Thread.sleep(500);
                    ui.access(() -> dataProvider.refreshAll());
                }

                ui.access(() -> {
                    view.add(new Span("Done updating"));
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
