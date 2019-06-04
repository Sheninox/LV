package de.hofmann.ArticleDB.rest;

import de.hofmann.ArticleDB.dataacess.ArticleClient;
import de.hofmann.ArticleDB.modell.Article;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.time.LocalDate;

@Path("/rest")
public class ArticleReciver {



    @POST
    @Path("/{ean}/{name}/{dur}")
    public Response postSrtMsg(@PathParam("ean") String ean,
                               @PathParam("name") String name,
                               @PathParam("dur") String dur){

        ArticleClient client = new ArticleClient();

        String output;

        Article a = new Article();

        a.setEAN(Long.parseLong(ean));
        a.setName(name);
        a.setDuration(Integer.parseInt(dur));
        a.setAdDate(Date.valueOf(LocalDate.now()));

        client.createArticle(a);

        output="Workde";

        return Response.status(200).entity(output).build();
    }
    @POST
    @Path("/check/{ean}")
    public Response postSrtMsg(@PathParam("ean") String ean){

        ArticleClient client = new ArticleClient();

        String output;

        boolean failure = false;

        try {
            client.getArticleByEAN(Long.parseLong(ean));
        }catch (javax.persistence.NoResultException e){
            failure = true;
        }

        if(failure){
            output="n";
            return Response.status(200).entity(output).build();
        }else {
            output="y";
            return Response.status(200).entity(output).build();
        }

    }

}
