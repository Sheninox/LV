package de.hofmann.LV.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hofmann.LV.datacess.ProductClient;
import de.hofmann.LV.modell.Product;

import java.sql.Date;
import java.time.LocalDate;

@PageTitle("Erstellen")
@Route("create")
public class CreateProductView extends Div {
	private static final long serialVersionUID = 1L;

	private TextField name, duration, quantity;
	private Product product;
	private ProductClient client;
	public CreateProductView() {

		Label title = new Label("Erstellen eines Produktes");

		client = new ProductClient();
		name = new TextField("Nahme: ");
		duration = new TextField("Haltbar Für (in Jahren): ");
		quantity = new TextField("Menge (Nur Zahlen): ");
		DatePicker date = new DatePicker();
		date.setValue(LocalDate.now());

		Button save = new Button("Save", e -> {

			if (name.getValue() == "" || quantity.getValue() == "" || duration.getValue() == ""){
				Notification.show("Überprüfen sie ihre eingaben");
			}else {
				product = new Product();
				product.setName(name.getValue());
				product.setQuantity(Integer.parseInt(quantity.getValue()));
				product.setAdDate(Date.valueOf(date.getValue()));
				product.setDuration(Date.valueOf(date.getValue().plusYears(Integer.parseInt(duration.getValue()))));
				client.createProduct(product);
				this.getUI().ifPresent(ui -> ui.navigate(ListProductView.class));
			}

		});
		VerticalLayout layout = new VerticalLayout(title, name, date, duration, quantity, save);
		this.add(layout);
	}

}
