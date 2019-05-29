package de.hofmann.datacess;

import de.hofmann.modell.Product;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class ProductClient {

    private EntityManager em;

    public ProductClient() {
        String PERSISTENCE_UNIT_NAME = "products";
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }

    public List<Product> getAllProducts(){
        Query q = em.createQuery("select p from Product p");
        return q.getResultList();
    }
    public void createProduct(@NotNull Product p){
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
    }
    public void saveProduct(@NotNull Long ID, @NotNull Product p){
        Product old = em.find(Product.class, ID);
        em.getTransaction().begin();
        old.setAdDate(p.getAdDate());
        old.setName(p.getName());
        old.setQuantity(p.getQuantity());
        old.setDuration(p.getDuration());
        em.getTransaction().commit();
    }
    public Product getProductByID(@NotNull Long ID){
        return em.find(Product.class, ID);
    }

    public void deleteProduct(@NotNull Product p) {
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
    }

    public void plusQuant(@NotNull Product product) {
        Product old = em.find(Product.class, product.getID());
        em.getTransaction().begin();
        old.setQuantity(product.getQuantity()+1);
        em.getTransaction().commit();
    }

    public void minQuant(@NotNull Product product) {
        Product old = em.find(Product.class, product.getID());
        em.getTransaction().begin();
        old.setQuantity(product.getQuantity()-1);
        em.getTransaction().commit();
    }
}