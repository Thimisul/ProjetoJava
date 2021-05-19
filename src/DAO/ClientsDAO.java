/*lUCAS
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import JPAcontroller.ClientJpaController;
import Model.Client;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author thi_s
 */
public class ClientsDAO {
    private final ClientJpaController objetoJPA;
    private final EntityManagerFactory emf;

    public ClientsDAO() {
        emf = Persistence.createEntityManagerFactory("Projeto2021");
        objetoJPA = new ClientJpaController(emf);
    }
    
    public void add(Client objeto) throws Exception{
        objetoJPA.create(objeto);
    }
    
    public void edit(Client objeto) throws Exception{
        objetoJPA.edit(objeto);
    }
    
    public void remove(int id) throws Exception{
        objetoJPA.destroy(id);
    }
    
    public List<Client> getAllClients(){
        return objetoJPA.findClientEntities();
    }
    
    /**
     *
     * @param objeto
     */
    public void persist (Client objeto){
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