/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Logros;
import modelo.Videojuegos;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author LV1800-00
 */
public class LogrosJpaController implements Serializable {

    public LogrosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Logros logros) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Videojuegos idVideojuego = logros.getIdVideojuego();
            if (idVideojuego != null) {
                idVideojuego = em.getReference(idVideojuego.getClass(), idVideojuego.getIdVideojuego());
                logros.setIdVideojuego(idVideojuego);
            }
            em.persist(logros);
            if (idVideojuego != null) {
                idVideojuego.getLogrosCollection().add(logros);
                idVideojuego = em.merge(idVideojuego);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Logros logros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Logros persistentLogros = em.find(Logros.class, logros.getIdLogro());
            Videojuegos idVideojuegoOld = persistentLogros.getIdVideojuego();
            Videojuegos idVideojuegoNew = logros.getIdVideojuego();
            if (idVideojuegoNew != null) {
                idVideojuegoNew = em.getReference(idVideojuegoNew.getClass(), idVideojuegoNew.getIdVideojuego());
                logros.setIdVideojuego(idVideojuegoNew);
            }
            logros = em.merge(logros);
            if (idVideojuegoOld != null && !idVideojuegoOld.equals(idVideojuegoNew)) {
                idVideojuegoOld.getLogrosCollection().remove(logros);
                idVideojuegoOld = em.merge(idVideojuegoOld);
            }
            if (idVideojuegoNew != null && !idVideojuegoNew.equals(idVideojuegoOld)) {
                idVideojuegoNew.getLogrosCollection().add(logros);
                idVideojuegoNew = em.merge(idVideojuegoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logros.getIdLogro();
                if (findLogros(id) == null) {
                    throw new NonexistentEntityException("The logros with id " + id + " no longer exists.");
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
            Logros logros;
            try {
                logros = em.getReference(Logros.class, id);
                logros.getIdLogro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logros with id " + id + " no longer exists.", enfe);
            }
            Videojuegos idVideojuego = logros.getIdVideojuego();
            if (idVideojuego != null) {
                idVideojuego.getLogrosCollection().remove(logros);
                idVideojuego = em.merge(idVideojuego);
            }
            em.remove(logros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Logros> findLogrosEntities() {
        return findLogrosEntities(true, -1, -1);
    }

    public List<Logros> findLogrosEntities(int maxResults, int firstResult) {
        return findLogrosEntities(false, maxResults, firstResult);
    }

    private List<Logros> findLogrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Logros.class));
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

    public Logros findLogros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Logros.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Logros> rt = cq.from(Logros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
