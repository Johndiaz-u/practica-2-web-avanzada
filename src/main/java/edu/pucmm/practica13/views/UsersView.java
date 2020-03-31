package edu.pucmm.practica13.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import edu.pucmm.practica13.data.User;
import edu.pucmm.practica13.payload.Auth.SignUpRequest;
import edu.pucmm.practica13.payload.User.UserResponse;
import edu.pucmm.practica13.repository.UserRepository;
import edu.pucmm.practica13.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route("users")
public class UsersView  extends Div {

    public UsersView(@Autowired UserService userService){

        DataProvider<User, Void> dataProvider;
        User selected;
        Boolean newUser = false;

        MenuView menuView = new MenuView("users");
        add(menuView);

        dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    return userService.getAllUsers().stream();
                },
                query -> {
                    return Math.toIntExact(userService.getAllUsers().stream().count());
                }
        );

        add(new H3("Users"));

        VerticalLayout tableView = new VerticalLayout();
        VerticalLayout newForm = new VerticalLayout();

        Grid<User> table = new Grid<>();
        table.setDataProvider(dataProvider);
        table.addColumn(User::getName).setHeader("Name");
        table.addColumn(User::getUsername).setHeader("Username");
        table.addColumn(User::getEmail).setHeader("Email");
        table.addColumn(new NativeButtonRenderer<User>("Delete", e->{
            Notification.show("Deleting " + e.getUsername());
            userService.deleteUser(e);
            dataProvider.refreshAll();
        })).setHeader("Actions");

        Button button = new Button("NEW", event -> {
            newForm.setVisible(!newForm.isVisible());
            tableView.setVisible(!tableView.isVisible());
        });

        tableView.add(table, button);

        TextField name = new TextField("Name");
        name.setValueChangeMode(ValueChangeMode.EAGER);
        TextField username = new TextField("Username");
        username.setValueChangeMode(ValueChangeMode.EAGER);
        PasswordField password = new PasswordField("Password");
        password.setValueChangeMode(ValueChangeMode.EAGER);
        EmailField email = new EmailField("Email");
        email.setValueChangeMode(ValueChangeMode.EAGER);

        Button addBtn = new Button("ADD", event -> {
            userService.store(new SignUpRequest().setName(name.getValue()).setUsername(username.getValue()).setEmail(email.getValue()).setPassword(password.getValue()));
            dataProvider.refreshAll();
            newForm.setVisible(!newForm.isVisible());
            tableView.setVisible(!tableView.isVisible());
        });

        Button cancelBtn = new Button("CANCEL", event -> {
            newForm.setVisible(!newForm.isVisible());
            tableView.setVisible(!tableView.isVisible());
        });

        newForm.add(name, username, password, email, cancelBtn, addBtn);
        newForm.setVisible(false);

        add(tableView, newForm);

    }

}

