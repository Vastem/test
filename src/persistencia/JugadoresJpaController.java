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
import modelo.Videojuegos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Jugadores;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author LV1800-00
 */
public class JugadoresJpaController implements Serializable {

    public JugadoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jugadores jugadores) {
        if (jugadores.getVideojuegosCollection() == null) {
            jugadores.setVideojuegosCollection(new ArrayList<Videojuegos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Videojuegos> attachedVideojuegosCollection = new ArrayList<Videojuegos>();
            for (Videojuegos videojuegosCollectionVideojuegosToAttach : jugadores.getVideojuegosCollection()) {
                videojuegosCollectionVideojuegosToAttach = em.getReference(videojuegosCollectionVideojuegosToAttach.getClass(), videojuegosCollectionVideojuegosToAttach.getIdVideojuego());
                attachedVideojuegosCollection.add(videojuegosCollectionVideojuegosToAttach);
            }
            jugadores.setVideojuegosCollection(attachedVideojuegosCollection);
            em.persist(jugadores);
            for (Videojuegos videojuegosCollectionVideojuegos : jugadores.getVideojuegosCollection()) {
                Jugadores oldIdJugadorOfVideojuegosCollectionVideojuegos = videojuegosCollectionVideojuegos.getIdJugador();
                videojuegosCollectionVideojuegos.setIdJugador(jugadores);
                videojuegosCollectionVideojuegos = em.merge(videojuegosCollectionVideojuegos);
                if (oldIdJugadorOfVideojuegosCollectionVideojuegos != null) {
                    oldIdJugadorOfVideojuegosCollectionVideojuegos.getVideojuegosCollection().remove(videojuegosCollectionVideojuegos);
                    oldIdJugadorOfVideojuegosCollectionVideojuegos = em.merge(oldIdJugadorOfVideojuegosCollectionVideojuegos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jugadores jugadores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugadores persistentJugadores = em.find(Jugadores.class, jugadores.getIdJugador());
            Collection<Videojuegos> videojuegosCollectionOld = persistentJugadores.getVideojuegosCollection();
            Collection<Videojuegos> videojuegosCollectionNew = jugadores.getVideojuegosCollection();
            List<String> illegalOrphanMessages = null;
            for (Videojuegos videojuegosCollectionOldVideojuegos : videojuegosCollectionOld) {
                if (!videojuegosCollectionNew.contains(videojuegosCollectionOldVideojuegos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Videojuegos " + videojuegosCollectionOldVideojuegos + " since its idJugador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Videojuegos> attachedVideojuegosCollectionNew = new ArrayList<Videojuegos>();
            for (Videojuegos videojuegosCollectionNewVideojuegosToAttach : videojuegosCollectionNew) {
                videojuegosCollectionNewVideojuegosToAttach = em.getReference(videojuegosCollectionNewVideojuegosToAttach.getClass(), videojuegosCollectionNewVideojuegosToAttach.getIdVideojuego());
                attachedVideojuegosCollectionNew.add(videojuegosCollectionNewVideojuegosToAttach);
            }
            videojuegosCollectionNew = attachedVideojuegosCollectionNew;
            jugadores.setVideojuegosCollection(videojuegosCollectionNew);
            jugadores = em.merge(jugadores);
            for (Videojuegos videojuegosCollectionNewVideojuegos : videojuegosCollectionNew) {
                if (!videojuegosCollectionOld.contains(videojuegosCollectionNewVideojuegos)) {
                    Jugadores oldIdJugadorOfVideojuegosCollectionNewVideojuegos = videojuegosCollectionNewVideojuegos.getIdJugador();
                    videojuegosCollectionNewVideojuegos.setIdJugador(jugadores);
                    videojuegosCollectionNewVideojuegos = em.merge(videojuegosCollectionNewVideojuegos);
                    if (oldIdJugadorOfVideojuegosCollectionNewVideojuegos != null && !oldIdJugadorOfVideojuegosCollectionNewVideojuegos.equals(jugadores)) {
                        oldIdJugadorOfVideojuegosCollectionNewVideojuegos.getVideojuegosCollection().remove(videojuegosCollectionNewVideojuegos);
                        oldIdJugadorOfVideojuegosCollectionNewVideojuegos = em.merge(oldIdJugadorOfVideojuegosCollectionNewVideojuegos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = jugadores.getIdJugador();
                if (findJugadores(id) == null) {
                    throw new NonexistentEntityException("The jugadores with id " + id + " no longer exists.");
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
            Jugadores jugadores;
            try {
                jugadores = em.getReference(Jugadores.class, id);
                jugadores.getIdJugador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugadores with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Videojuegos> videojuegosCollectionOrphanCheck = jugadores.getVideojuegosCollection();
            for (Videojuegos videojuegosCollectionOrphanCheckVideojuegos : videojuegosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Jugadores (" + jugadores + ") cannot be destroyed since the Videojuegos " + videojuegosCollectionOrphanCheckVideojuegos + " in its videojuegosCollection field has a non-nullable idJugador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(jugadores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jugadores> findJugadoresEntities() {
        return findJugadoresEntities(true, -1, -1);
    }

    public List<Jugadores> findJugadoresEntities(int maxResults, int firstResult) {
        return findJugadoresEntities(false, maxResults, firstResult);
    }

    private List<Jugadores> findJugadoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jugadores.class));
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

    public Jugadores findJugadores(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jugadores.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jugadores> rt = cq.from(Jugadores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
