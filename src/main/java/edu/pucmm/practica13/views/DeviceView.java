package edu.pucmm.practica13.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import edu.pucmm.practica13.data.Device;
import edu.pucmm.practica13.data.DeviceMessage;
import edu.pucmm.practica13.services.DeviceMessageServices;
import edu.pucmm.practica13.services.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@SuppressWarnings("Duplicates")
@Route("devices")
public class DeviceView extends Div implements HasUrlParameter<String> {
    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceMessageServices deviceMessageService;

    @Override
    public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
        if (parameter.isEmpty()) {
            setText("NO DEVICE ID FOUND.");
        } else {

            Device device = deviceService.find(Long.parseLong(parameter));
            DataProvider<DeviceMessage, Void> dataProvider;

            VerticalLayout layout = new VerticalLayout();

            //MENU
            MenuView menuView = new MenuView("devices");
            menuView.getStyle().set("margin-bottom", "20px");
            //END MENU

            //DATE PICKER
            Div dates = new Div();
            Div message = new Div();

            DatePicker startDatePicker = new DatePicker();
            startDatePicker.setLabel("Start");
            startDatePicker.setValue(LocalDate.now());
            DatePicker endDatePicker = new DatePicker();
            endDatePicker.setLabel("End");
            endDatePicker.setValue(LocalDate.now().plus(7, ChronoUnit.DAYS));
            //END DATE PICKER


            Long deviceId = device.getId();

            NumberField stepperField = new NumberField("Page");
            stepperField.setValue(1d);
            stepperField.setMin(1);
            stepperField.setMax(deviceMessageService.getAllDeviceMessagesByDevice(deviceId).stream().count()/8);
            stepperField.setHasControls(true);

            //TABLE
            dataProvider = DataProvider.fromCallbacks(
                    query -> {
                        query.getLimit();
                        query.getOffset();
                        int offset = stepperField.getValue().intValue();
                        int limit = 8;
                        LocalDate start = startDatePicker.getValue();
                        LocalDate end = endDatePicker.getValue();
                        return deviceMessageService.getAllDeviceMessagesPaginated(deviceId, start, end, offset, limit).stream();
                    },
                    query -> {
                        query.getLimit();
                        query.getOffset();
                        int offset = stepperField.getValue().intValue();
                        int limit = 8;
                        LocalDate start = startDatePicker.getValue();
                        LocalDate end = endDatePicker.getValue();
                        return Math.toIntExact(deviceMessageService.getAllDeviceMessagesPaginated(deviceId, start, end, offset, limit).stream().count());
                    }
            );

            stepperField.addValueChangeListener(e -> {
                dataProvider.refreshAll();
            });

            VerticalLayout tableView = new VerticalLayout();

            Grid<DeviceMessage> table = new Grid<>();
            table.setDataProvider(dataProvider);
            table.addColumn(DeviceMessage::getId).setHeader("id");
            table.addColumn(DeviceMessage::getTemperature).setHeader("Temperature");
            table.addColumn(DeviceMessage::getHumidity).setHeader("Humidity");
            table.addColumn(DeviceMessage::getDate).setHeader("Date");

            tableView.add(table,stepperField);
            //END TABLE

            startDatePicker.addValueChangeListener(eventDate -> {
                LocalDate selectedDate = eventDate.getValue();
                LocalDate endDate = endDatePicker.getValue();
                if (selectedDate != null) {
                    endDatePicker.setMin(selectedDate.plusDays(1));
                    if (endDate == null) {
                        endDatePicker.setOpened(true);
                        message.setText("Select the ending date");
                    } else {
                        message.setText(
                                "Selected:\nFrom " + selectedDate.toString()
                                        + " to " + endDate.toString());
                    }
                    dataProvider.refreshAll();
                } else {
                    endDatePicker.setMin(null);
                    message.setText("Select the starting date");
                }
            });

            endDatePicker.addValueChangeListener(eventDate -> {
                LocalDate selectedDate = eventDate.getValue();
                LocalDate startDate = startDatePicker.getValue();
                if (selectedDate != null) {
                    startDatePicker.setMax(selectedDate.minusDays(1));
                    if (startDate != null) {
                        message.setText(
                                "Selected:\nFrom " + startDate.toString()
                                        + " to " + selectedDate.toString());
                    } else {
                        message.setText("Select the starting date");
                    }
                    dataProvider.refreshAll();
                } else {
                    startDatePicker.setMax(null);
                    if (startDate != null) {
                        message.setText("Select the ending date");
                    } else {
                        message.setText("No date is selected");
                    }
                }
            });

            dates.add(startDatePicker, endDatePicker, message);
            layout.add(menuView, dates);
            layout.add(tableView);
            layout.setPadding(true);
            layout.setMargin(true);
            layout.setSpacing(true);
            add(layout);
        }
    }
}
