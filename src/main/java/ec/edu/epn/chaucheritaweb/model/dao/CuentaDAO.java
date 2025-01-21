package ec.edu.epn.chaucheritaweb.model.dao;

import ec.edu.epn.chaucheritaweb.model.entities.*;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CuentaDAO extends BaseDAO<Cuenta> {

    public CuentaDAO() {
        super(Cuenta.class);
    }

    public List<Cuenta> findByUsuario(Usuario usuario) {
        TypedQuery<Cuenta> query = entityManager.createQuery(
            "SELECT c FROM Cuenta c WHERE c.usuario = :usuario",
            Cuenta.class
        );
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }

    public void realizarMovimiento(Cuenta cuenta, Movimiento movimiento) {
        if(movimiento instanceof Ingreso) {
            cuenta.setBalance(cuenta.getBalance().add(movimiento.getValor()));
        } else if(movimiento instanceof Egreso) {
            cuenta.setBalance(cuenta.getBalance().subtract(movimiento.getValor()));
        }
        entityManager.getTransaction().begin();
        entityManager.merge(cuenta);
        entityManager.persist(movimiento);
        entityManager.getTransaction().commit();
    }

    public void realizarMovimiento(Cuenta cuenta, Cuenta cuentaDestino, Movimiento movimiento) {
        if (movimiento instanceof Transferencia) {
            cuenta.setBalance(cuenta.getBalance().subtract(movimiento.getValor()));
            cuentaDestino.setBalance(cuenta.getBalance().add(movimiento.getValor()));
        }
        entityManager.getTransaction().begin();
        entityManager.merge(cuenta);
        entityManager.merge(cuentaDestino);
        entityManager.persist(movimiento);
        entityManager.getTransaction().commit();
    }
}
