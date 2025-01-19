package ec.edu.epn.chaucheritaweb.model.entities;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "categoria") // Nombre de la tabla en la base de datos
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento en la base de datos
    private Integer id;

    @Column(name = "nombre_categoria")
    private String nombreCategoria;

    @Column(name = "descripcion")
    private String descripcion;

    public Categoria() {}

    public Categoria(Integer id, String nombreCategoria, String descripcion) {
        super();
        this.id = id;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

}

