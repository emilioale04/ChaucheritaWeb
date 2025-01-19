package ec.edu.epn.chaucheritaweb.model.entities;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("TRANSFERENCIA")
public class Transferencia extends Movimiento {
    
	private static final long serialVersionUID = 1L;
	@ManyToOne
    @JoinColumn(name = "cuenta_destino_id")
    private Cuenta cuentaDestino;

    public Transferencia() {
        super();
    }

    public Transferencia(int id, BigDecimal valor, String concepto, Date fecha, 
                        Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        super(id, valor, concepto, fecha, cuentaOrigen);
        this.cuentaDestino = cuentaDestino;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }
}