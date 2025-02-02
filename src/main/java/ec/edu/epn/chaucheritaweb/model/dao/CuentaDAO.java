package ec.edu.epn.chaucheritaweb.model.dao;

import ec.edu.epn.chaucheritaweb.model.entities.*;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CuentaDAO extends BaseDAO<Cuenta> {

    public CuentaDAO() {
        super(Cuenta.class);
    }

    public void create(Cuenta cuenta) {
        entityManager.getTransaction().begin();
        entityManager.persist(cuenta);
        entityManager.getTransaction().commit();
    }


    public Cuenta read(Long id) {
        return entityManager.find(Cuenta.class, id);
    }


    public void update(Cuenta cuenta) {
        entityManager.getTransaction().begin();
        entityManager.merge(cuenta);
        entityManager.getTransaction().commit();
    }


    public void delete(Long id) {
        entityManager.getTransaction().begin();
        Cuenta cuenta = entityManager.find(Cuenta.class, id);
        if (cuenta != null) {
            entityManager.remove(cuenta);
        }
        entityManager.getTransaction().commit();
    }


    public List<Cuenta> findByUsuario(Usuario usuario) {
        TypedQuery<Cuenta> query = entityManager.createQuery(
                "SELECT c FROM Cuenta c WHERE c.usuario = :usuario", Cuenta.class);
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }


    public void realizarMovimiento(Cuenta cuenta, Movimiento movimiento) {
        entityManager.getTransaction().begin();

        if (movimiento instanceof Ingreso) {
            cuenta.setBalance(cuenta.getBalance().add(movimiento.getValor()));
        } else if (movimiento instanceof Egreso) {
            cuenta.setBalance(cuenta.getBalance().subtract(movimiento.getValor()));
        }

        entityManager.merge(cuenta);
        entityManager.persist(movimiento);
        entityManager.getTransaction().commit();
    }


    public void realizarMovimiento(Cuenta cuentaOrigen, Cuenta cuentaDestino, Transferencia movimiento) {
        entityManager.getTransaction().begin();

        cuentaOrigen.setBalance(cuentaOrigen.getBalance().subtract(movimiento.getValor()));
        cuentaDestino.setBalance(cuentaDestino.getBalance().add(movimiento.getValor()));

        entityManager.merge(cuentaOrigen);
        entityManager.merge(cuentaDestino);
        entityManager.persist(movimiento);

        entityManager.getTransaction().commit();
    }
}
