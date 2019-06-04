package de.hofmann.LV.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hofmann.ArticleDB.dataacess.ArticleClient;
import de.hofmann.ArticleDB.modell.Article;
import de.hofmann.LV.datacess.ProductClient;
import de.hofmann.LV.modell.Product;
import org.apache.commons.lang3.StringUtils;



import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@PageTitle("Lager")
@Route("")
public class ListProductView extends Div {
	private static final long serialVersionUID = 1L;

	private ProductClient Pclient;

	public ListProductView() {

		Pclient = new ProductClient();
		ArticleClient aclient = new ArticleClient();

		Label Title = new Label("Im Lager");

		ComboBox<Article> fast = new ComboBox<>("Artikel auswählen");
		fast.setItems(aclient.getAllArticles());
		fast.setItemLabelGenerator(Article::getName);
		Button fastAccept = new Button("Erstellen");

		TextField fastQnt = new TextField("Menge");

		HorizontalLayout hz = new HorizontalLayout(fast,  fastQnt,fastAccept);

		Button create = new Button("Neuer Eintrag");
		Button databaseAdmin = new Button("Zu allen Registrierten Artikeln");



		List<Product> products = Pclient.getAllProducts();

		Grid<Product> grid = new Grid<>();

		ListDataProvider<Product> dataProvider = new ListDataProvider<>(products);

		grid.setDataProvider(dataProvider);

		Grid.Column<Product> nameCol = grid.addColumn(Product::getName).setHeader("Name");
		Grid.Column<Product> dateCol = grid.addColumn(Product::getAdDate).setHeader("Kaufdatum");
		Grid.Column<Product> durCol = grid.addColumn(Product::getDuration).setHeader("Verfalsdatum");
		Grid.Column<Product> quantCol = grid.addComponentColumn(this::buildQuantButtons).setHeader("Menge");
		Grid.Column<Product> delCol = grid.addComponentColumn(this::buildDeleteButton).setHeader("löschen");
		grid.setSizeFull();
		grid.setHeight("4");

		HeaderRow filterRow = grid.appendHeaderRow();

		TextField NameField = new TextField();
		NameField.addValueChangeListener(event -> dataProvider.addFilter(
				product -> StringUtils.containsIgnoreCase(product.getName(),
						NameField.getValue())));

		NameField.setValueChangeMode(ValueChangeMode.EAGER);

		filterRow.getCell(nameCol).setComponent(NameField);
		NameField.setSizeFull();
		NameField.setPlaceholder("Filter");

		create.addClickListener(e->this.getUI().ifPresent(ui->ui.navigate(CreateProductView.class)));

		grid.addSelectionListener(s-> this.getUI().ifPresent(ui-> ui.navigate(EditProductView.class, s.getFirstSelectedItem().get().getID())));

		databaseAdmin.addClickListener(e->{this.getUI().ifPresent(ui->ui.navigate(de.hofmann.ArticleDB.ui.ListArticleView.class));});

		fastAccept.addClickListener(e->{
			if (fast.getValue() != null){
				Article a = fast.getValue();
				Product p = new Product();
				p.setName(a.getName());
				p.setAdDate(Date.valueOf(LocalDate.now()));
				p.setQuantity(Integer.parseInt(fastQnt.getValue()));
				p.setDuration(Date.valueOf(LocalDate.now().plusYears(a.getDuration())));
				Pclient.createProduct(p);
				UI.getCurrent().getPage().reload();
			}
			fast.getValue();
		});

		VerticalLayout layout = new VerticalLayout(Title, grid, create,hz, databaseAdmin);
		this.add(layout);

		makeNotifivations(products);

	}

	private void makeNotifivations(List<Product> list) {

		list.stream().forEach(p->{

			Instant d1i = Instant.ofEpochMilli(getCalendar(Date.valueOf(LocalDate.now())).getTimeInMillis());
			Instant d2i = Instant.ofEpochMilli(getCalendar(p.getDuration()).getTimeInMillis());

			LocalDateTime startDate = LocalDateTime.ofInstant(d1i, ZoneId.systemDefault());
			LocalDateTime endDate = LocalDateTime.ofInstant(d2i, ZoneId.systemDefault());
			if (ChronoUnit.WEEKS.between(startDate, endDate) <= 0) {
				Notification notification = new Notification("", -1);

				notification.add(new Label("Produkt: "+ p.getName() +" ist Abgelaufen"));
				notification.add(new Button("X", e-> notification.close()));
				notification.open();
			}else if (ChronoUnit.WEEKS.between(startDate, endDate) < 4){
				Notification notification = new Notification("", -1);

				notification.add(new Label("Produkt: "+ p.getName() +" Leuft Bald ab"));
				notification.add(new Button("X", e-> notification.close()));
				notification.open();
			}

		});

	}

	private HorizontalLayout buildQuantButtons(Product product) {

		Label qunt = new Label(Integer.toString(product.getQuantity()));

		Button add = new Button("+", e->{
			Pclient.plusQuant(product);
			qunt.setText(Integer.toString(Integer.parseInt(qunt.getText())+1));
		});

		Button min = new Button("-", e->{
			Pclient.minQuant(product);
			qunt.setText(Integer.toString(Integer.parseInt(qunt.getText())-1));
		});

		return new HorizontalLayout(qunt, add, min);
	}

	private Button buildDeleteButton(Product p) {
		return new Button("DEL", e -> {Pclient.deleteProduct(p);UI.getCurrent().getPage().reload();});
	}

	private Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		return cal;
	}

}
