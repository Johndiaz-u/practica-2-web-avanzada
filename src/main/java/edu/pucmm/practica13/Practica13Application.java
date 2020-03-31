package edu.pucmm.practica13;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.pucmm.practica13.controller.AuthController;
import edu.pucmm.practica13.data.Device;
import edu.pucmm.practica13.data.DeviceMessage;
import edu.pucmm.practica13.payload.Auth.LoginRequest;
import edu.pucmm.practica13.payload.Auth.SignUpRequest;
import edu.pucmm.practica13.repository.DeviceMessageRepository;
import edu.pucmm.practica13.repository.DeviceRepository;
import edu.pucmm.practica13.repository.UserRepository;
import edu.pucmm.practica13.security.JwtTokenProvider;
import edu.pucmm.practica13.services.AsynchronousAlarmService;
import edu.pucmm.practica13.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;

@SpringBootApplication
public class Practica13Application {

	public static void main(String[] args) {
		SpringApplication.run(Practica13Application.class, args);
	}

	@Route("")
	public static class MainView  extends VerticalLayout {

		@Autowired
		AuthController authController;

		@Autowired
		JwtTokenProvider jwtTokenProvider;

		public MainView (){

			VerticalLayout layout = new VerticalLayout();
			LoginForm component = new LoginForm();
			component.addLoginListener(e -> {
				try{
					ResponseEntity<?> response = authController.authenticateUser(
							new LoginRequest()
									.setUsernameOrEmail(e.getUsername())
									.setPassword(e.getPassword()));
					Notification.show("Bienvendido " + e.getUsername());
					UI.getCurrent().getPage().executeJavaScript("window.location.replace('/dashboard');");
//					MenuView menu = new MenuView();
//					layout.add(menu);
					layout.remove(component);
				}catch (Exception ex){
					component.setError(true);
					throw ex;
				}
			});

			String jwt = (String) VaadinSession.getCurrent().getAttribute("user");

			if (jwt == null) {
				layout.add(component);
			} else {
				layout.remove(component);
				UI.getCurrent().getPage().executeJavaScript("window.location.replace('/dashboard');");
			}
			layout.setPadding(true);
			layout.setMargin(true);
			layout.setSpacing(true);
			add(layout);



		}
	}

	public static final Logger LOG = LoggerFactory.getLogger(BootStrap.class);

	@Component
	static class BootStrap{
		@Autowired
		UserRepository userRepository;

		@Autowired
		UserService userService;

		@Autowired
		DeviceRepository deviceRepository;

		@Autowired
		DeviceMessageRepository deviceMessageRepository;

		@Autowired
		AsynchronousAlarmService asynchronousAlarmService;

		@PostConstruct
		public void init(){
			LOG.info("Saving User ADMIN...");
			userService.store(new SignUpRequest().setName("Administrator").setEmail("admin@admin.com").setPassword("admin123").setUsername("admin"));
			LOG.info("User ADMIN Saved");

			LOG.info("Saving Device DEV-01...");
			Device device1 = new Device().setName("DEV-01").setAlarmTime(2);
			deviceRepository.save(device1);
			LOG.info("Device DEV-01 Saved");

			LOG.info("Saving Device DEV-02...");
			Device device2 = new Device().setName("DEV-02").setAlarmTime(3);
			deviceRepository.save(device2);
			LOG.info("Device DEV-02 Saved");
			LOG.info("Saving Messages for DEV-01...");
			for(int i=1; i<=30; i++){
				DeviceMessage deviceMessage = new DeviceMessage().setDevice(device1).setDate(new Timestamp(System.currentTimeMillis())).setHumidity((double)10*i).setTemperature((double)15*i);
				deviceMessageRepository.save(deviceMessage);
				LOG.info("Message: Temp: " + deviceMessage.getTemperature() + ", Hum: " + deviceMessage.getHumidity() + " Saved");
			}
			LOG.info("Device DEV-01 messages Saved");

			asynchronousAlarmService.executeAsynchronously();

		}
	}
}