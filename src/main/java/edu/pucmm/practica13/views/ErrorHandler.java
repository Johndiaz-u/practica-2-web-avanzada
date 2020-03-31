package edu.pucmm.practica13.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;

import javax.servlet.http.HttpServletResponse;

@Tag(Tag.DIV)
public class ErrorHandler extends Component implements HasErrorParameter<NotFoundException>{
        @Override
        public int setErrorParameter(BeforeEnterEvent event,
                                     ErrorParameter<NotFoundException> parameter) {
            getElement().setText("No existe la vista indica: '"
                    + event.getLocation().getPath() + "'");
            return HttpServletResponse.SC_NOT_FOUND;
        }
}
