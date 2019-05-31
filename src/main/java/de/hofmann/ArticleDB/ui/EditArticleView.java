package de.hofmann.ArticleDB.ui;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hofmann.ArticleDB.dataacess.ArticleClient;
import de.hofmann.ArticleDB.modell.Article;


import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

@PageTitle("Editiren")
@Route("ArticleEditor")
public class EditArticleView extends Div implements HasUrlParameter<Long> {
	private static final long serialVersionUID = 1L;

	private TextField name, duration, EAN;
	private Article product;
	private Long ID;
	private ArticleClient client;

	@Override
	public void setParameter(BeforeEvent beforeEvent, Long aLong) {
		ID = aLong;

		product = client.getArticleByID(ID);

		Label title = new Label("Bearbeiten des Produktes mit ID :" + ID);

		name = new TextField("Nahme: ");
		name.setValue(product.getName());

		EAN = new TextField("Barcoode Nummer: ");
		EAN.setValue(product.getEAN().toString());

		duration = new TextField("Haltbar Für (in Jahren): ");
		duration.setValue(Integer.toString(product.getDuration()));

		Button save = new Button("Save", e -> {

			if (name.getValue() == "" || EAN.getValue() == "" || duration.getValue() == ""){
				Notification.show("Überprüfen sie ihre eingaben");

			}else {
				product = new Article();
				product.setName(name.getValue());
				product.setEAN(Long.parseLong(EAN.getValue()));
				product.setAdDate(Date.valueOf(LocalDate.now()));
				product.setDuration(Integer.parseInt(duration.getValue()));
				client.saveArticle(ID, product);
				this.getUI().ifPresent(ui -> ui.navigate(ListArticleView.class));
			}



		});
		VerticalLayout layout = new VerticalLayout(title, name, duration, EAN, save);
		this.add(layout);

	}

	public EditArticleView() {
		client = new ArticleClient();
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
