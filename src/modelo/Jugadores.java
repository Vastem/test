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
@Table(name = "jugadores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Jugadores.findAll", query = "SELECT j FROM Jugadores j")
    , @NamedQuery(name = "Jugadores.findByIdJugador", query = "SELECT j FROM Jugadores j WHERE j.idJugador = :idJugador")
    , @NamedQuery(name = "Jugadores.findByNombre", query = "SELECT j FROM Jugadores j WHERE j.nombre = :nombre")
    , @NamedQuery(name = "Jugadores.findByCorreo", query = "SELECT j FROM Jugadores j WHERE j.correo = :correo")})
public class Jugadores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_jugador")
    private Integer idJugador;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "correo")
    private String correo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idJugador")
    private Collection<Videojuegos> videojuegosCollection;
    
    String juan = "EldenRing>>>";

    public String getJuan() {
        return juan;
    }

    public void setJuan(String juan) {
        this.juan = juan;
    }

    public Jugadores() {
    }

    public Jugadores(Integer idJugador) {
        this.idJugador = idJugador;
    }

    public Jugadores(Integer idJugador, String nombre, String correo) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.correo = correo;
    }

    public Integer getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(Integer idJugador) {
        this.idJugador = idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @XmlTransient
    public Collection<Videojuegos> getVideojuegosCollection() {
        return videojuegosCollection;
    }

    public void setVideojuegosCollection(Collection<Videojuegos> videojuegosCollection) {
        this.videojuegosCollection = videojuegosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJugador != null ? idJugador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jugadores)) {
            return false;
        }
        Jugadores other = (Jugadores) object;
        if ((this.idJugador == null && other.idJugador != null) || (this.idJugador != null && !this.idJugador.equals(other.idJugador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Jugadores[ idJugador=" + idJugador + " ]";
    }
    
}
