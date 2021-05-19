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
import Model.Analysis;
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
public class Table1JpaController implements Serializable {

    public Table1JpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Table1 table1) throws PreexistingEntityException, Exception {
        if (table1.getAnalysisCollection() == null) {
            table1.setAnalysisCollection(new ArrayList<Analysis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Analysis> attachedAnalysisCollection = new ArrayList<Analysis>();
            for (Analysis analysisCollectionAnalysisToAttach : table1.getAnalysisCollection()) {
                analysisCollectionAnalysisToAttach = em.getReference(analysisCollectionAnalysisToAttach.getClass(), analysisCollectionAnalysisToAttach.getAnalysisId());
                attachedAnalysisCollection.add(analysisCollectionAnalysisToAttach);
            }
            table1.setAnalysisCollection(attachedAnalysisCollection);
            em.persist(table1);
            for (Analysis analysisCollectionAnalysis : table1.getAnalysisCollection()) {
                analysisCollectionAnalysis.getTable1Collection().add(table1);
                analysisCollectionAnalysis = em.merge(analysisCollectionAnalysis);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTable1(table1.getTableId()) != null) {
                throw new PreexistingEntityException("Table1 " + table1 + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Table1 table1) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Table1 persistentTable1 = em.find(Table1.class, table1.getTableId());
            Collection<Analysis> analysisCollectionOld = persistentTable1.getAnalysisCollection();
            Collection<Analysis> analysisCollectionNew = table1.getAnalysisCollection();
            Collection<Analysis> attachedAnalysisCollectionNew = new ArrayList<Analysis>();
            for (Analysis analysisCollectionNewAnalysisToAttach : analysisCollectionNew) {
                analysisCollectionNewAnalysisToAttach = em.getReference(analysisCollectionNewAnalysisToAttach.getClass(), analysisCollectionNewAnalysisToAttach.getAnalysisId());
                attachedAnalysisCollectionNew.add(analysisCollectionNewAnalysisToAttach);
            }
            analysisCollectionNew = attachedAnalysisCollectionNew;
            table1.setAnalysisCollection(analysisCollectionNew);
            table1 = em.merge(table1);
            for (Analysis analysisCollectionOldAnalysis : analysisCollectionOld) {
                if (!analysisCollectionNew.contains(analysisCollectionOldAnalysis)) {
                    analysisCollectionOldAnalysis.getTable1Collection().remove(table1);
                    analysisCollectionOldAnalysis = em.merge(analysisCollectionOldAnalysis);
                }
            }
            for (Analysis analysisCollectionNewAnalysis : analysisCollectionNew) {
                if (!analysisCollectionOld.contains(analysisCollectionNewAnalysis)) {
                    analysisCollectionNewAnalysis.getTable1Collection().add(table1);
                    analysisCollectionNewAnalysis = em.merge(analysisCollectionNewAnalysis);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = table1.getTableId();
                if (findTable1(id) == null) {
                    throw new NonexistentEntityException("The table1 with id " + id + " no longer exists.");
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
            Table1 table1;
            try {
                table1 = em.getReference(Table1.class, id);
                table1.getTableId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The table1 with id " + id + " no longer exists.", enfe);
            }
            Collection<Analysis> analysisCollection = table1.getAnalysisCollection();
            for (Analysis analysisCollectionAnalysis : analysisCollection) {
                analysisCollectionAnalysis.getTable1Collection().remove(table1);
                analysisCollectionAnalysis = em.merge(analysisCollectionAnalysis);
            }
            em.remove(table1);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Table1> findTable1Entities() {
        return findTable1Entities(true, -1, -1);
    }

    public List<Table1> findTable1Entities(int maxResults, int firstResult) {
        return findTable1Entities(false, maxResults, firstResult);
    }

    private List<Table1> findTable1Entities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Table1.class));
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

    public Table1 findTable1(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Table1.class, id);
        } finally {
            em.close();
        }
    }

    public int getTable1Count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Table1> rt = cq.from(Table1.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
