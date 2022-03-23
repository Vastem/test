/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author LV1800-00
 */
@Entity
@Table(name = "videojuegos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Videojuegos.findAll", query = "SELECT v FROM Videojuegos v")
    , @NamedQuery(name = "Videojuegos.findByIdVideojuego", query = "SELECT v FROM Videojuegos v WHERE v.idVideojuego = :idVideojuego")
    , @NamedQuery(name = "Videojuegos.findByNombre", query = "SELECT v FROM Videojuegos v WHERE v.nombre = :nombre")
    , @NamedQuery(name = "Videojuegos.findByRating", query = "SELECT v FROM Videojuegos v WHERE v.rating = :rating")})
public class Videojuegos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_videojuego")
    private Integer idVideojuego;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "rating")
    private int rating;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idVideojuego")
    private Collection<Logros> logrosCollection;
    @JoinColumn(name = "id_jugador", referencedColumnName = "id_jugador")
    @ManyToOne(optional = false)
    private Jugadores idJugador;

    public Videojuegos() {
    }

    public Videojuegos(Integer idVideojuego) {
        this.idVideojuego = idVideojuego;
    }

    public Videojuegos(Integer idVideojuego, String nombre, int rating) {
        this.idVideojuego = idVideojuego;
        this.nombre = nombre;
        this.rating = rating;
    }

    public Integer getIdVideojuego() {
        return idVideojuego;
    }

    public void setIdVideojuego(Integer idVideojuego) {
        this.idVideojuego = idVideojuego;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @XmlTransient
    public Collection<Logros> getLogrosCollection() {
        return logrosCollection;
    }

    public void setLogrosCollection(Collection<Logros> logrosCollection) {
        this.logrosCollection = logrosCollection;
    }

    public Jugadores getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Jugadores idJugador) {
        this.idJugador = idJugador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVideojuego != null ? idVideojuego.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Videojuegos)) {
            return false;
        }
        Videojuegos other = (Videojuegos) object;
        if ((this.idVideojuego == null && other.idVideojuego != null) || (this.idVideojuego != null && !this.idVideojuego.equals(other.idVideojuego))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idVideojuego +", " + nombre;
    }
    
}
