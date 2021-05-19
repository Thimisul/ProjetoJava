package DAO;

import JPAcontroller.Table1JpaController;
import Model.Table1;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author thi_s
 */
public class Table1DAO {
    private final Table1JpaController objetoJPA;
    private final EntityManagerFactory emf;

    public Table1DAO() {
        emf = Persistence.createEntityManagerFactory("Projeto2021");
        objetoJPA = new Table1JpaController(emf);
    }
    
    public void add(Table1 objeto) throws Exception{
        objetoJPA.create(objeto);
    }
    
    public void edit(Table1 objeto) throws Exception{
        objetoJPA.edit(objeto);
    }
    
    public void remove(int id) throws Exception{
        objetoJPA.destroy(id);
    }
    
    public List<Table1> getAllTable1s(){
        return objetoJPA.findTable1Entities();
    }
    
    public void persist (Table1 objeto){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try{
            em.persist(objeto);
            em.getTransaction().commit();
        }catch (Exception e){
            e.printStackTrace();
            em.getTransaction().rollback();
        }finally{
            em.close();
        }
    }
    
}