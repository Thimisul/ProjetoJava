package DAO;

import JPAcontroller.UserJpaController;
import Model.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author thi_s
 */
public class UsersDAO {
    private final UserJpaController objetoJPA;
    private final EntityManagerFactory emf;

    public UsersDAO() {
        emf = Persistence.createEntityManagerFactory("ProjetoPU");
        objetoJPA = new UserJpaController(emf);
    }
    
    public void add(User objeto) throws Exception{
        objetoJPA.create(objeto);
    }
    
    public void edit(User objeto) throws Exception{
        objetoJPA.edit(objeto);
    }
    
    public void remove(int id) throws Exception{
        objetoJPA.destroy(id);
    }
   
    
    public List<User> getAllUsers(){
        return objetoJPA.findUserEntities();
    }
    
    public void persist (User objeto){
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
    
    public User userLogin(String name, String password){
        User login = objetoJPA.login(name, password);
        System.out.println( "DAO   -    " + login.getEmail());
        return login;
        }
}