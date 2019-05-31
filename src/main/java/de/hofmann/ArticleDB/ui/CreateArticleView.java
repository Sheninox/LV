package de.hofmann.ArticleDB.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hofmann.ArticleDB.dataacess.ArticleClient;
import de.hofmann.ArticleDB.modell.Article;


import java.sql.Date;
import java.time.LocalDate;

@PageTitle("Erstellen")
@Route("ArticleCreator")
public class CreateArticleView extends Div {
	private static final long serialVersionUID = 1L;

	private TextField name, duration, EAN;
	private Article product;
	private ArticleClient client;
	public CreateArticleView() {

		Label title = new Label("Erstellen eines Produktes");

		client = new ArticleClient();
		name = new TextField("Nahme: ");
		duration = new TextField("Haltbar Für (in Jahren): ");
		EAN = new TextField("Barcoode Nummer: ");

		Button save = new Button("Save", e -> {
			if (name.getValue() == "" || EAN.getValue() == "" || duration.getValue() == ""){
				Notification.show("Überprüfen sie ihre eingaben");

			}else {
				product = new Article();
				product.setName(name.getValue());
				product.setEAN(Long.parseLong(EAN.getValue()));
				product.setAdDate(Date.valueOf(LocalDate.now()));
				product.setDuration(Integer.parseInt(duration.getValue()));
				client.createArticle(product);
				this.getUI().ifPresent(ui -> ui.navigate(ListArticleView.class));
			}



		});
		VerticalLayout layout = new VerticalLayout(title, name, duration, EAN, save);
		this.add(layout);
	}

}
