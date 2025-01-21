package ec.edu.epn.chaucheritaweb.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
@Table(name = "cuenta")
public class Cuenta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Cuenta() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
