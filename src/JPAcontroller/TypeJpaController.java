/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPAcontroller;

import JPAcontroller.exceptions.NonexistentEntityException;
import JPAcontroller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Client;
import Model.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author thi_s
 */
public class TypeJpaController implements Serializable {

    public TypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Type type) throws PreexistingEntityException, Exception {
        if (type.getClientCollection() == null) {
            type.setClientCollection(new ArrayList<Client>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Client> attachedClientCollection = new ArrayList<Client>();
            for (Client clientCollectionClientToAttach : type.getClientCollection()) {
                clientCollectionClientToAttach = em.getReference(clientCollectionClientToAttach.getClass(), clientCollectionClientToAttach.getClientId());
                attachedClientCollection.add(clientCollectionClientToAttach);
            }
            type.setClientCollection(attachedClientCollection);
            em.persist(type);
            for (Client clientCollectionClient : type.getClientCollection()) {
                Type oldTypeIdOfClientCollectionClient = clientCollectionClient.getTypeId();
                clientCollectionClient.setTypeId(type);
                clientCollectionClient = em.merge(clientCollectionClient);
                if (oldTypeIdOfClientCollectionClient != null) {
                    oldTypeIdOfClientCollectionClient.getClientCollection().remove(clientCollectionClient);
                    oldTypeIdOfClientCollectionClient = em.merge(oldTypeIdOfClientCollectionClient);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findType(type.getTypeId()) != null) {
                throw new PreexistingEntityException("Type " + type + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Type type) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Type persistentType = em.find(Type.class, type.getTypeId());
            Collection<Client> clientCollectionOld = persistentType.getClientCollection();
            Collection<Client> clientCollectionNew = type.getClientCollection();
            Collection<Client> attachedClientCollectionNew = new ArrayList<Client>();
            for (Client clientCollectionNewClientToAttach : clientCollectionNew) {
                clientCollectionNewClientToAttach = em.getReference(clientCollectionNewClientToAttach.getClass(), clientCollectionNewClientToAttach.getClientId());
                attachedClientCollectionNew.add(clientCollectionNewClientToAttach);
            }
            clientCollectionNew = attachedClientCollectionNew;
            type.setClientCollection(clientCollectionNew);
            type = em.merge(type);
            for (Client clientCollectionOldClient : clientCollectionOld) {
                if (!clientCollectionNew.contains(clientCollectionOldClient)) {
                    clientCollectionOldClient.setTypeId(null);
                    clientCollectionOldClient = em.merge(clientCollectionOldClient);
                }
            }
            for (Client clientCollectionNewClient : clientCollectionNew) {
                if (!clientCollectionOld.contains(clientCollectionNewClient)) {
                    Type oldTypeIdOfClientCollectionNewClient = clientCollectionNewClient.getTypeId();
                    clientCollectionNewClient.setTypeId(type);
                    clientCollectionNewClient = em.merge(clientCollectionNewClient);
                    if (oldTypeIdOfClientCollectionNewClient != null && !oldTypeIdOfClientCollectionNewClient.equals(type)) {
                        oldTypeIdOfClientCollectionNewClient.getClientCollection().remove(clientCollectionNewClient);
                        oldTypeIdOfClientCollectionNewClient = em.merge(oldTypeIdOfClientCollectionNewClient);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = type.getTypeId();
                if (findType(id) == null) {
                    throw new NonexistentEntityException("The type with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Type type;
            try {
                type = em.getReference(Type.class, id);
                type.getTypeId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The type with id " + id + " no longer exists.", enfe);
            }
            Collection<Client> clientCollection = type.getClientCollection();
            for (Client clientCollectionClient : clientCollection) {
                clientCollectionClient.setTypeId(null);
                clientCollectionClient = em.merge(clientCollectionClient);
            }
            em.remove(type);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Type> findTypeEntities() {
        return findTypeEntities(true, -1, -1);
    }

    public List<Type> findTypeEntities(int maxResults, int firstResult) {
        return findTypeEntities(false, maxResults, firstResult);
    }

    private List<Type> findTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Type.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Type findType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Type.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Type> rt = cq.from(Type.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
