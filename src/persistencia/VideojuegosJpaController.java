/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Jugadores;
import modelo.Logros;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Videojuegos;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author LV1800-00
 */
public class VideojuegosJpaController implements Serializable {
    
    public VideojuegosJpaController() {
        emf =Persistence.createEntityManagerFactory("Videojuegos2PU");
    }

    public VideojuegosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Videojuegos videojuegos) {
        if (videojuegos.getLogrosCollection() == null) {
            videojuegos.setLogrosCollection(new ArrayList<Logros>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugadores idJugador = videojuegos.getIdJugador();
            if (idJugador != null) {
                idJugador = em.getReference(idJugador.getClass(), idJugador.getIdJugador());
                videojuegos.setIdJugador(idJugador);
            }
            Collection<Logros> attachedLogrosCollection = new ArrayList<Logros>();
            for (Logros logrosCollectionLogrosToAttach : videojuegos.getLogrosCollection()) {
                logrosCollectionLogrosToAttach = em.getReference(logrosCollectionLogrosToAttach.getClass(), logrosCollectionLogrosToAttach.getIdLogro());
                attachedLogrosCollection.add(logrosCollectionLogrosToAttach);
            }
            videojuegos.setLogrosCollection(attachedLogrosCollection);
            em.persist(videojuegos);
            if (idJugador != null) {
                idJugador.getVideojuegosCollection().add(videojuegos);
                idJugador = em.merge(idJugador);
            }
            for (Logros logrosCollectionLogros : videojuegos.getLogrosCollection()) {
                Videojuegos oldIdVideojuegoOfLogrosCollectionLogros = logrosCollectionLogros.getIdVideojuego();
                logrosCollectionLogros.setIdVideojuego(videojuegos);
                logrosCollectionLogros = em.merge(logrosCollectionLogros);
                if (oldIdVideojuegoOfLogrosCollectionLogros != null) {
                    oldIdVideojuegoOfLogrosCollectionLogros.getLogrosCollection().remove(logrosCollectionLogros);
                    oldIdVideojuegoOfLogrosCollectionLogros = em.merge(oldIdVideojuegoOfLogrosCollectionLogros);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Videojuegos videojuegos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Videojuegos persistentVideojuegos = em.find(Videojuegos.class, videojuegos.getIdVideojuego());
            Jugadores idJugadorOld = persistentVideojuegos.getIdJugador();
            Jugadores idJugadorNew = videojuegos.getIdJugador();
            Collection<Logros> logrosCollectionOld = persistentVideojuegos.getLogrosCollection();
            Collection<Logros> logrosCollectionNew = videojuegos.getLogrosCollection();
            List<String> illegalOrphanMessages = null;
            for (Logros logrosCollectionOldLogros : logrosCollectionOld) {
                if (!logrosCollectionNew.contains(logrosCollectionOldLogros)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Logros " + logrosCollectionOldLogros + " since its idVideojuego field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idJugadorNew != null) {
                idJugadorNew = em.getReference(idJugadorNew.getClass(), idJugadorNew.getIdJugador());
                videojuegos.setIdJugador(idJugadorNew);
            }
            Collection<Logros> attachedLogrosCollectionNew = new ArrayList<Logros>();
            for (Logros logrosCollectionNewLogrosToAttach : logrosCollectionNew) {
                logrosCollectionNewLogrosToAttach = em.getReference(logrosCollectionNewLogrosToAttach.getClass(), logrosCollectionNewLogrosToAttach.getIdLogro());
                attachedLogrosCollectionNew.add(logrosCollectionNewLogrosToAttach);
            }
            logrosCollectionNew = attachedLogrosCollectionNew;
            videojuegos.setLogrosCollection(logrosCollectionNew);
            videojuegos = em.merge(videojuegos);
            if (idJugadorOld != null && !idJugadorOld.equals(idJugadorNew)) {
                idJugadorOld.getVideojuegosCollection().remove(videojuegos);
                idJugadorOld = em.merge(idJugadorOld);
            }
            if (idJugadorNew != null && !idJugadorNew.equals(idJugadorOld)) {
                idJugadorNew.getVideojuegosCollection().add(videojuegos);
                idJugadorNew = em.merge(idJugadorNew);
            }
            for (Logros logrosCollectionNewLogros : logrosCollectionNew) {
                if (!logrosCollectionOld.contains(logrosCollectionNewLogros)) {
                    Videojuegos oldIdVideojuegoOfLogrosCollectionNewLogros = logrosCollectionNewLogros.getIdVideojuego();
                    logrosCollectionNewLogros.setIdVideojuego(videojuegos);
                    logrosCollectionNewLogros = em.merge(logrosCollectionNewLogros);
                    if (oldIdVideojuegoOfLogrosCollectionNewLogros != null && !oldIdVideojuegoOfLogrosCollectionNewLogros.equals(videojuegos)) {
                        oldIdVideojuegoOfLogrosCollectionNewLogros.getLogrosCollection().remove(logrosCollectionNewLogros);
                        oldIdVideojuegoOfLogrosCollectionNewLogros = em.merge(oldIdVideojuegoOfLogrosCollectionNewLogros);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = videojuegos.getIdVideojuego();
                if (findVideojuegos(id) == null) {
                    throw new NonexistentEntityException("The videojuegos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Videojuegos videojuegos;
            try {
                videojuegos = em.getReference(Videojuegos.class, id);
                videojuegos.getIdVideojuego();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The videojuegos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Logros> logrosCollectionOrphanCheck = videojuegos.getLogrosCollection();
            for (Logros logrosCollectionOrphanCheckLogros : logrosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Videojuegos (" + videojuegos + ") cannot be destroyed since the Logros " + logrosCollectionOrphanCheckLogros + " in its logrosCollection field has a non-nullable idVideojuego field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Jugadores idJugador = videojuegos.getIdJugador();
            if (idJugador != null) {
                idJugador.getVideojuegosCollection().remove(videojuegos);
                idJugador = em.merge(idJugador);
            }
            em.remove(videojuegos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Videojuegos> findVideojuegosEntities() {
        return findVideojuegosEntities(true, -1, -1);
    }

    public List<Videojuegos> findVideojuegosEntities(int maxResults, int firstResult) {
        return findVideojuegosEntities(false, maxResults, firstResult);
    }

    private List<Videojuegos> findVideojuegosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Videojuegos.class));
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

    public Videojuegos findVideojuegos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Videojuegos.class, id);
        } finally {
            em.close();
        }
    }

    public int getVideojuegosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Videojuegos> rt = cq.from(Videojuegos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
