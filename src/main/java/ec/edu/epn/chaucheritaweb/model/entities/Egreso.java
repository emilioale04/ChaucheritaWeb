package ec.edu.epn.chaucheritaweb.model.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
public class Egreso extends Movimiento {
    
	private static final long serialVersionUID = 1L;

	public Egreso() {
        super();
    }

    public Egreso(int id, BigDecimal valor, String concepto, LocalDateTime fecha, Cuenta cuenta) {
        super(id, valor, concepto, fecha, cuenta);
    }
}