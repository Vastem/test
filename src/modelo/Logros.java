/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author LV1800-00
 */
@Entity
@Table(name = "logros")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logros.findAll", query = "SELECT l FROM Logros l")
    , @NamedQuery(name = "Logros.findByIdLogro", query = "SELECT l FROM Logros l WHERE l.idLogro = :idLogro")
    , @NamedQuery(name = "Logros.findByNombre", query = "SELECT l FROM Logros l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "Logros.findByPuntos", query = "SELECT l FROM Logros l WHERE l.puntos = :puntos")})
public class Logros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_logro")
    private Integer idLogro;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "puntos")
    private int puntos;
    @JoinColumn(name = "id_videojuego", referencedColumnName = "id_videojuego")
    @ManyToOne(optional = false)
    private Videojuegos idVideojuego;

    public Logros() {
    }

    public Logros(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public Logros(Integer idLogro, String nombre, int puntos) {
        this.idLogro = idLogro;
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public Integer getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public Videojuegos getIdVideojuego() {
        return idVideojuego;
    }

    public void setIdVideojuego(Videojuegos idVideojuego) {
        this.idVideojuego = idVideojuego;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idLogro != null ? idLogro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Logros)) {
            return false;
        }
        Logros other = (Logros) object;
        if ((this.idLogro == null && other.idLogro != null) || (this.idLogro != null && !this.idLogro.equals(other.idLogro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Logros[ idLogro=" + idLogro + " ]";
    }
    
}
