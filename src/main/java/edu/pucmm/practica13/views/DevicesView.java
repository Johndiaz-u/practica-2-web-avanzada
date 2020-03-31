package edu.pucmm.practica13.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import edu.pucmm.practica13.data.Device;
import edu.pucmm.practica13.data.User;
import edu.pucmm.practica13.payload.Auth.SignUpRequest;
import edu.pucmm.practica13.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("devices")
public class DevicesView extends Div {

    public DevicesView(@Autowired DeviceService deviceService){

        DataProvider<Device, Void> dataProvider;

        MenuView menuView = new MenuView("devices");
        add(menuView);

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return deviceService.getAllDevices().stream();
                },
                query -> {
                    return Math.toIntExact(deviceService.getAllDevices().stream().count());
                }
        );

        add(new H3("Devices"));

        VerticalLayout tableView = new VerticalLayout();
        VerticalLayout newForm = new VerticalLayout();

        Grid<Device> table = new Grid<>();
        table.setDataProvider(dataProvider);
        table.addColumn(Device::getName).setHeader("Name");
        table.addColumn(Device::getAlarmTime).setHeader("Alarm Time");
        table.addColumn(new NativeButtonRenderer<Device>("Delete", e->{
            Notification.show("Deleting " + e.getName());
            deviceService.deleteDevice(e);
            dataProvider.refreshAll();
        })).setHeader("Actions");
        table.addColumn(new NativeButtonRenderer<Device>("View", e->{
            UI.getCurrent().getPage().executeJavaScript("window.open('/devices/" + e.getId() + "');");
        })).setHeader("Actions");

        Button button = new Button("NEW", event -> {
            newForm.setVisible(!newForm.isVisible());
            tableView.setVisible(!tableView.isVisible());
        });

        tableView.add(table, button);

        TextField name = new TextField("Name");
        name.setValueChangeMode(ValueChangeMode.EAGER);
        NumberField alarmTime = new NumberField("Alarm Time");
        alarmTime.setValueChangeMode(ValueChangeMode.EAGER);

        Button addBtn = new Button("ADD", event -> {
            deviceService.store(new Device().setName(name.getValue()).setAlarmTime(alarmTime.getValue().intValue()));
            dataProvider.refreshAll();
            newForm.setVisible(!newForm.isVisible());
            tableView.setVisible(!tableView.isVisible());
        });

        Button cancelBtn = new Button("CANCEL", event -> {
            newForm.setVisible(!newForm.isVisible());
            tableView.setVisible(!tableView.isVisible());
        });

        newForm.add(name, alarmTime, cancelBtn, addBtn);
        newForm.setVisible(false);

        add(tableView, newForm);

    }
}

