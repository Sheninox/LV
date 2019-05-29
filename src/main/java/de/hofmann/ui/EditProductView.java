package de.hofmann.ui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hofmann.datacess.ProductClient;
import de.hofmann.modell.Product;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;

@PageTitle("Editiren")
@Route("edit")
public class EditProductView extends Div implements HasUrlParameter<Long> {
	private static final long serialVersionUID = 1L;

	private TextField name, duration, quantity;
	private Product product;
	private Long ID;
	private ProductClient client;

	@Override
	public void setParameter(BeforeEvent beforeEvent, Long aLong) {
		ID = aLong;

		product = client.getProductByID(ID);

		Label title = new Label("Bearbeiten des Produktes mit ID :" + ID);

		name = new TextField("Nahme: ");
		name.setValue(product.getName());

		duration = new TextField("Haltbar Für (in Jahren): ");
		duration.setValue(Integer.toString(getDiffYears(product.getAdDate(), product.getDuration())));

		quantity = new TextField("Menge (Nur Zahlen): ");
		quantity.setValue(Integer.toString(product.getQuantity()));

		DatePicker date = new DatePicker();
		date.setValue(product.getAdDate().toLocalDate());

		Button save = new Button("Save", e -> {

			product = new Product();
			product.setName(name.getValue());
			product.setQuantity(Integer.parseInt(quantity.getValue()));
			product.setAdDate(Date.valueOf(date.getValue()));
			product.setDuration(Date.valueOf(date.getValue().plusYears(Integer.parseInt(duration.getValue()))));
			client.saveProduct(ID, product);
			this.getUI().ifPresent(ui -> ui.navigate(ListProductView.class));

		});
		VerticalLayout layout = new VerticalLayout(title, name, date, duration, quantity, save);
		this.add(layout);

	}

	public EditProductView() {
		client = new ProductClient();

	}

	private int getDiffYears(Date first, Date last) {
		Calendar a = getCalendar(first);
		Calendar b = getCalendar(last);
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
				(a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
			diff--;
		}
		return diff;
	}

	private Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		return cal;
	}


}
