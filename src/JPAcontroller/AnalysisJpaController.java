/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPAcontroller;

import JPAcontroller.exceptions.NonexistentEntityException;
import JPAcontroller.exceptions.PreexistingEntityException;
import Model.Analysis;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Model.Client;
import Model.Table1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author thi_s
 */
public class AnalysisJpaController implements Serializable {

    public AnalysisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Analysis analysis) throws PreexistingEntityException, Exception {
        if (analysis.getTable1Collection() == null) {
            analysis.setTable1Collection(new ArrayList<Table1>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client clientId = analysis.getClientId();
            if (clientId != null) {
                clientId = em.getReference(clientId.getClass(), clientId.getClientId());
                analysis.setClientId(clientId);
            }
            Collection<Table1> attachedTable1Collection = new ArrayList<Table1>();
            for (Table1 table1CollectionTable1ToAttach : analysis.getTable1Collection()) {
                table1CollectionTable1ToAttach = em.getReference(table1CollectionTable1ToAttach.getClass(), table1CollectionTable1ToAttach.getTableId());
                attachedTable1Collection.add(table1CollectionTable1ToAttach);
            }
            analysis.setTable1Collection(attachedTable1Collection);
            em.persist(analysis);
            if (clientId != null) {
                clientId.getAnalysisCollection().add(analysis);
                clientId = em.merge(clientId);
            }
            for (Table1 table1CollectionTable1 : analysis.getTable1Collection()) {
                table1CollectionTable1.getAnalysisCollection().add(analysis);
                table1CollectionTable1 = em.merge(table1CollectionTable1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnalysis(analysis.getAnalysisId()) != null) {
                throw new PreexistingEntityException("Analysis " + analysis + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Analysis analysis) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Analysis persistentAnalysis = em.find(Analysis.class, analysis.getAnalysisId());
            Client clientIdOld = persistentAnalysis.getClientId();
            Client clientIdNew = analysis.getClientId();
            Collection<Table1> table1CollectionOld = persistentAnalysis.getTable1Collection();
            Collection<Table1> table1CollectionNew = analysis.getTable1Collection();
            if (clientIdNew != null) {
                clientIdNew = em.getReference(clientIdNew.getClass(), clientIdNew.getClientId());
                analysis.setClientId(clientIdNew);
            }
            Collection<Table1> attachedTable1CollectionNew = new ArrayList<Table1>();
            for (Table1 table1CollectionNewTable1ToAttach : table1CollectionNew) {
                table1CollectionNewTable1ToAttach = em.getReference(table1CollectionNewTable1ToAttach.getClass(), table1CollectionNewTable1ToAttach.getTableId());
                attachedTable1CollectionNew.add(table1CollectionNewTable1ToAttach);
            }
            table1CollectionNew = attachedTable1CollectionNew;
            analysis.setTable1Collection(table1CollectionNew);
            analysis = em.merge(analysis);
            if (clientIdOld != null && !clientIdOld.equals(clientIdNew)) {
                clientIdOld.getAnalysisCollection().remove(analysis);
                clientIdOld = em.merge(clientIdOld);
            }
            if (clientIdNew != null && !clientIdNew.equals(clientIdOld)) {
                clientIdNew.getAnalysisCollection().add(analysis);
                clientIdNew = em.merge(clientIdNew);
            }
            for (Table1 table1CollectionOldTable1 : table1CollectionOld) {
                if (!table1CollectionNew.contains(table1CollectionOldTable1)) {
                    table1CollectionOldTable1.getAnalysisCollection().remove(analysis);
                    table1CollectionOldTable1 = em.merge(table1CollectionOldTable1);
                }
            }
            for (Table1 table1CollectionNewTable1 : table1CollectionNew) {
                if (!table1CollectionOld.contains(table1CollectionNewTable1)) {
                    table1CollectionNewTable1.getAnalysisCollection().add(analysis);
                    table1CollectionNewTable1 = em.merge(table1CollectionNewTable1);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = analysis.getAnalysisId();
                if (findAnalysis(id) == null) {
                    throw new NonexistentEntityException("The analysis with id " + id + " no longer exists.");
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
            Analysis analysis;
            try {
                analysis = em.getReference(Analysis.class, id);
                analysis.getAnalysisId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The analysis with id " + id + " no longer exists.", enfe);
            }
            Client clientId = analysis.getClientId();
            if (clientId != null) {
                clientId.getAnalysisCollection().remove(analysis);
                clientId = em.merge(clientId);
            }
            Collection<Table1> table1Collection = analysis.getTable1Collection();
            for (Table1 table1CollectionTable1 : table1Collection) {
                table1CollectionTable1.getAnalysisCollection().remove(analysis);
                table1CollectionTable1 = em.merge(table1CollectionTable1);
            }
            em.remove(analysis);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Analysis> findAnalysisEntities() {
        return findAnalysisEntities(true, -1, -1);
    }

    public List<Analysis> findAnalysisEntities(int maxResults, int firstResult) {
        return findAnalysisEntities(false, maxResults, firstResult);
    }

    private List<Analysis> findAnalysisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Analysis.class));
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

    public Analysis findAnalysis(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Analysis.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnalysisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Analysis> rt = cq.from(Analysis.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
