package de.hofmann.ArticleDB.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hofmann.ArticleDB.dataacess.ArticleClient;
import de.hofmann.ArticleDB.modell.Article;
import de.hofmann.LV.modell.Product;
import de.hofmann.LV.ui.ListProductView;
import org.apache.commons.lang3.StringUtils;


import java.util.List;

@PageTitle("Liste Aller Registriten Artikel")
@Route("ArticleList")
public class ListArticleView extends Div {
	private static final long serialVersionUID = 1L;

	private ArticleClient client;

	public ListArticleView() {

		client = new ArticleClient();

		Button create = new Button("Neuer eintrag");

		Button toProduct = new Button("Zrurük zum Lager");
		Label Title = new Label("Alle Registriten Artikel: ");


		//Grid And Data Provider Initzialisation
		List<Article> articles = client.getAllArticles();
		Grid<Article> grid = new Grid<>();
		ListDataProvider<Article> dataProvider = new ListDataProvider<>(articles);
		grid.setDataProvider(dataProvider);

		//Make Grid Columns
		Grid.Column<Article> nameCol = grid.addColumn(Article::getName).setHeader("Name");
		Grid.Column<Article> dateCol = grid.addColumn(Article::getAdDate).setHeader("Hinzugefügt");
		Grid.Column<Article> durCol = grid.addColumn(Article::getDuration).setHeader("Haltbarkeit");
		Grid.Column<Article> eanCol = grid.addColumn(Article::getEAN).setHeader("Barcoode");
		Grid.Column<Article> delCol = grid.addComponentColumn(this::buildDeleteButton).setHeader("löschen");
		grid.setSizeFull();
		grid.setHeight("4");

		HeaderRow filterRow = grid.appendHeaderRow();

		// NAME FILTER ROW
		TextField NameField = new TextField();
		NameField.addValueChangeListener(event -> dataProvider.addFilter(
				product -> StringUtils.containsIgnoreCase(product.getName(),
						NameField.getValue())));

		NameField.setValueChangeMode(ValueChangeMode.EAGER);

		filterRow.getCell(nameCol).setComponent(NameField);
		NameField.setSizeFull();
		NameField.setPlaceholder("Filter");

		// EAN FILTER ROW
		TextField EANField = new TextField();
		EANField.addValueChangeListener(event -> dataProvider.addFilter(
				product -> StringUtils.containsIgnoreCase(product.getEAN().toString(),
						EANField.getValue())));

		EANField.setValueChangeMode(ValueChangeMode.EAGER);

		filterRow.getCell(eanCol).setComponent(EANField);
		EANField.setSizeFull();
		EANField.setPlaceholder("Filter");

		create.addClickListener(e->this.getUI().ifPresent(ui->ui.navigate(CreateArticleView.class)));

		toProduct.addClickListener(e->this.getUI().ifPresent(ui->ui.navigate(ListProductView.class)));

		grid.addSelectionListener(s-> this.getUI().ifPresent(ui-> ui.navigate(EditArticleView.class, s.getFirstSelectedItem().get().getID())));

		VerticalLayout layout = new VerticalLayout(Title, grid, create, toProduct);
		this.add(layout);

	}

	private Button buildDeleteButton(Article p) {
		return new Button("DEL", e -> {client.deleteProduct(p);UI.getCurrent().getPage().reload();});
	}

}
