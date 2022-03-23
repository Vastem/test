/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package videojuegos2;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.*;

/**
 *
 * @author LV1800-00
 */
public class Videojuegos2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    //Ejemplo 1 Criteria
    EntityManagerFactory mg = Persistence.createEntityManagerFactory("Videojuegos2PU");
    EntityManager em= mg.createEntityManager();
    em.getTransaction().begin();
    CriteriaBuilder cb= em.getCriteriaBuilder();
    CriteriaQuery<Videojuegos> criteria = cb.createQuery(Videojuegos.class);
    TypedQuery<Videojuegos> query = em.createQuery(criteria);
    
    List<Videojuegos> juegos = query.getResultList();
    for(Videojuegos v: juegos){
        System.out.println(v);
        
    }
    em.getTransaction().commit();
        
    //Ejemplo 2 Criteria
     System.out.println("");
     em.getTransaction().begin();
     
     Root<Videojuegos> root = criteria.from(Videojuegos.class);
     criteria = criteria.select(root).where(cb.like(root.get("nombre"),"%valorant%"));
     TypedQuery<Videojuegos> query2 = em.createQuery(criteria);
     List<Videojuegos> juegos2 = query2.getResultList();
    for(Videojuegos v: juegos2){
        System.out.println(v);
        
    }
    
    em.getTransaction().commit();
       
    // Ejemplo 1 JPQL
    System.out.println("");
    em.getTransaction().begin();
    String jpqlQuery = "SELECT v FROM Videojuegos v";
    TypedQuery<Videojuegos> queryjpql = em.createQuery(jpqlQuery,Videojuegos.class);
    
   juegos = queryjpql.getResultList();
   for(Videojuegos v: juegos){
        System.out.println(v);
        
    }
    em.getTransaction().commit();
    
    // Ejemplo 2 JPQL
        System.out.println("");
    em.getTransaction().begin();
    String jpqlQuery2 = "SELECT v FROM Videojuegos v WHERE v.nombre LIKE :nombre";
    TypedQuery<Videojuegos> queryjpql2 = em.createQuery(jpqlQuery2,Videojuegos.class);
    queryjpql2.setParameter("nombre", "%valorant%");
    List<Videojuegos> juegosjpql = queryjpql2.getResultList();
   for(Videojuegos v: juegosjpql){
        System.out.println(v);
        
    }
    em.getTransaction().commit();
    
    
    //Ejemplo 3 JPQL
    System.out.println("");
    em.getTransaction().begin();
    jpqlQuery2 = "SELECT v FROM Videojuegos v WHERE v.nombre LIKE :nombre AND v.rating = :rating";
    queryjpql2 = em.createQuery(jpqlQuery2,Videojuegos.class);
    queryjpql2.setParameter("nombre","%valorant%");
    queryjpql2.setParameter("rating",9);
    juegosjpql = queryjpql2.getResultList();
   for(Videojuegos v: juegosjpql){
        System.out.println(v);
   }   
    em.getTransaction().commit();
  
    
    //Ejemplo 4 JPQL
    System.out.println("");
    em.getTransaction().begin();
    jpqlQuery2 = "SELECT v.nombre FROM Videojuegos v WHERE v.rating > :rating";
    TypedQuery<String >queryString = em.createQuery(jpqlQuery2,String.class);
    queryString.setParameter("rating", 8);
    List<String> juegoz = queryString.getResultList();
   for(String v: juegoz){
        System.out.println(v);
   }   
    em.getTransaction().commit();
    
    //Ejemplo 5 JPQL
//    System.out.println("");
//    em.getTransaction().begin();
//    jpqlQuery2 = "SELECT v.nombre FROM Videojuegos v WHERE v.rating > :rating";
    
    }
    
}
