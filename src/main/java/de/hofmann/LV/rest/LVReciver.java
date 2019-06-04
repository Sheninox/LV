package de.hofmann.LV.rest;

import de.hofmann.ArticleDB.dataacess.ArticleClient;
import de.hofmann.ArticleDB.modell.Article;
import de.hofmann.LV.datacess.ProductClient;
import de.hofmann.LV.modell.Product;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.time.LocalDate;


@Path("/rest")
public class LVReciver {


    @POST
    @Path("/{ean}/{qnt}")
    public Response postSrtMsg(@PathParam("ean") String ean,@PathParam("qnt") String qnt){

        ProductClient pClient = new ProductClient();

        ArticleClient aClient = new ArticleClient();

        String output;

        Product p = new Product();

        Article a = aClient.getArticleByEAN(Long.parseLong(ean));

        p.setName(a.getName());
        p.setAdDate(Date.valueOf(LocalDate.now()));
        p.setDuration(Date.valueOf(LocalDate.now().plusYears(a.getDuration())));
        p.setQuantity(Integer.parseInt(qnt));

        pClient.createProduct(p);

        output="Workde";

        return Response.status(200).entity(output).build();
    }

}
