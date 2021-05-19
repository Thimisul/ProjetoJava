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
import Model.Type;
import Model.Analysis;
import Model.Client;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author thi_s
 */
public class ClientJpaController implements Serializable {

    public ClientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Client client) throws PreexistingEntityException, Exception {
        if (client.getAnalysisCollection() == null) {
            client.setAnalysisCollection(new ArrayList<Analysis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Type typeId = client.getTypeId();
            if (typeId != null) {
                typeId = em.getReference(typeId.getClass(), typeId.getTypeId());
                client.setTypeId(typeId);
            }
            Collection<Analysis> attachedAnalysisCollection = new ArrayList<Analysis>();
            for (Analysis analysisCollectionAnalysisToAttach : client.getAnalysisCollection()) {
                analysisCollectionAnalysisToAttach = em.getReference(analysisCollectionAnalysisToAttach.getClass(), analysisCollectionAnalysisToAttach.getAnalysisId());
                attachedAnalysisCollection.add(analysisCollectionAnalysisToAttach);
            }
            client.setAnalysisCollection(attachedAnalysisCollection);
            em.persist(client);
            if (typeId != null) {
                typeId.getClientCollection().add(client);
                typeId = em.merge(typeId);
            }
            for (Analysis analysisCollectionAnalysis : client.getAnalysisCollection()) {
                Client oldClientIdOfAnalysisCollectionAnalysis = analysisCollectionAnalysis.getClientId();
                analysisCollectionAnalysis.setClientId(client);
                analysisCollectionAnalysis = em.merge(analysisCollectionAnalysis);
                if (oldClientIdOfAnalysisCollectionAnalysis != null) {
                    oldClientIdOfAnalysisCollectionAnalysis.getAnalysisCollection().remove(analysisCollectionAnalysis);
                    oldClientIdOfAnalysisCollectionAnalysis = em.merge(oldClientIdOfAnalysisCollectionAnalysis);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClient(client.getClientId()) != null) {
                throw new PreexistingEntityException("Client " + client + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Client client) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client persistentClient = em.find(Client.class, client.getClientId());
            Type typeIdOld = persistentClient.getTypeId();
            Type typeIdNew = client.getTypeId();
            Collection<Analysis> analysisCollectionOld = persistentClient.getAnalysisCollection();
            Collection<Analysis> analysisCollectionNew = client.getAnalysisCollection();
            if (typeIdNew != null) {
                typeIdNew = em.getReference(typeIdNew.getClass(), typeIdNew.getTypeId());
                client.setTypeId(typeIdNew);
            }
            Collection<Analysis> attachedAnalysisCollectionNew = new ArrayList<Analysis>();
            for (Analysis analysisCollectionNewAnalysisToAttach : analysisCollectionNew) {
                analysisCollectionNewAnalysisToAttach = em.getReference(analysisCollectionNewAnalysisToAttach.getClass(), analysisCollectionNewAnalysisToAttach.getAnalysisId());
                attachedAnalysisCollectionNew.add(analysisCollectionNewAnalysisToAttach);
            }
            analysisCollectionNew = attachedAnalysisCollectionNew;
            client.setAnalysisCollection(analysisCollectionNew);
            client = em.merge(client);
            if (typeIdOld != null && !typeIdOld.equals(typeIdNew)) {
                typeIdOld.getClientCollection().remove(client);
                typeIdOld = em.merge(typeIdOld);
            }
            if (typeIdNew != null && !typeIdNew.equals(typeIdOld)) {
                typeIdNew.getClientCollection().add(client);
                typeIdNew = em.merge(typeIdNew);
            }
            for (Analysis analysisCollectionOldAnalysis : analysisCollectionOld) {
                if (!analysisCollectionNew.contains(analysisCollectionOldAnalysis)) {
                    analysisCollectionOldAnalysis.setClientId(null);
                    analysisCollectionOldAnalysis = em.merge(analysisCollectionOldAnalysis);
                }
            }
            for (Analysis analysisCollectionNewAnalysis : analysisCollectionNew) {
                if (!analysisCollectionOld.contains(analysisCollectionNewAnalysis)) {
                    Client oldClientIdOfAnalysisCollectionNewAnalysis = analysisCollectionNewAnalysis.getClientId();
                    analysisCollectionNewAnalysis.setClientId(client);
                    analysisCollectionNewAnalysis = em.merge(analysisCollectionNewAnalysis);
                    if (oldClientIdOfAnalysisCollectionNewAnalysis != null && !oldClientIdOfAnalysisCollectionNewAnalysis.equals(client)) {
                        oldClientIdOfAnalysisCollectionNewAnalysis.getAnalysisCollection().remove(analysisCollectionNewAnalysis);
                        oldClientIdOfAnalysisCollectionNewAnalysis = em.merge(oldClientIdOfAnalysisCollectionNewAnalysis);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = client.getClientId();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
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
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getClientId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            Type typeId = client.getTypeId();
            if (typeId != null) {
                typeId.getClientCollection().remove(client);
                typeId = em.merge(typeId);
            }
            Collection<Analysis> analysisCollection = client.getAnalysisCollection();
            for (Analysis analysisCollectionAnalysis : analysisCollection) {
                analysisCollectionAnalysis.setClientId(null);
                analysisCollectionAnalysis = em.merge(analysisCollectionAnalysis);
            }
            em.remove(client);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
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

    public Client findClient(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
