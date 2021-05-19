package DAO;

import JPAcontroller.AnalysisJpaController;
import Model.Analysis;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author thi_s
 */
public class AnalysisDAO {
    private final AnalysisJpaController objetoJPA;
    private final EntityManagerFactory emf;

    public AnalysisDAO() {
        emf = Persistence.createEntityManagerFactory("Projeto2021");
        objetoJPA = new AnalysisJpaController(emf);
    }
    
    public void add(Analysis objeto) throws Exception{
        objetoJPA.create(objeto);
    }
    
    public void edit(Analysis objeto) throws Exception{
        objetoJPA.edit(objeto);
    }
    
    public void remove(int id) throws Exception{
        objetoJPA.destroy(id);
    }
    
    public List<Analysis> getAllAnalysises(){
        return objetoJPA.findAnalysisEntities();
    }
    
    public void persist (Analysis objeto){
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