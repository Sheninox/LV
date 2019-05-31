package de.hofmann.ArticleDB.dataacess;

import de.hofmann.ArticleDB.modell.Article;
import de.hofmann.LV.modell.Product;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;

public class ArticleClient {

    private EntityManager em;

    public ArticleClient() {
        em = Persistence.createEntityManagerFactory("articles").createEntityManager();
    }

    public List<Article> getAllArticles(){
        return em.createQuery("select a from Article a").getResultList();
    }

    public void createArticle(@NotNull Article p){
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }

    public void saveArticle(@NotNull Long ID, @NotNull Article p){
        Article old = em.find(Article.class, ID);
        em.getTransaction().begin();
        old.setAdDate(p.getAdDate());
        old.setName(p.getName());
        old.setEAN(p.getEAN());
        old.setDuration(p.getDuration());
        em.getTransaction().commit();
    }

    public Article getArticleByID(@NotNull Long ID){
        return em.find(Article.class, ID);
    }

    public void deleteProduct(@NotNull Article p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }
}
