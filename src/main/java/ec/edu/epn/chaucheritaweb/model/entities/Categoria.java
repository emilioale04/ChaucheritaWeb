package ec.edu.epn.chaucheritaweb.model.entities;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
@Table(name = "categoria") // Nombre de la tabla en la base de datos
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento en la base de datos
    private Integer id;

    @Column(name = "nombre_categoria")
    private String nombreCategoria;

    @ManyToOne 
    @JoinColumn(name = "id_usuario", nullable = false) // Clave foránea en la tabla "categoria"
    private Usuario usuario;

    public Categoria() {}

    public Categoria(Integer id, String nombreCategoria, Usuario usuario) {
        super();
        this.id = id;
        this.nombreCategoria = nombreCategoria;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
