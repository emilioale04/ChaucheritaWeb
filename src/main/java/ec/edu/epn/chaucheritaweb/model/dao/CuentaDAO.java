package ec.edu.epn.chaucheritaweb.model.dao;

import ec.edu.epn.chaucheritaweb.model.entities.*;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.math.BigDecimal;


public class CuentaDAO extends BaseDAO<Cuenta> {

    public CuentaDAO() {
        super(Cuenta.class);
    }


    public List<Cuenta> listarCuentas(Usuario usuario) {
        String jpql = "SELECT c FROM Cuenta c WHERE c.usuario = :usuario";
        TypedQuery<Cuenta> query = entityManager.createQuery(jpql, Cuenta.class);
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }



    public void create(Usuario usuario, String nombre, BigDecimal balance) {
        entityManager.getTransaction().begin();

        Cuenta cuenta = new Cuenta();
        cuenta.setNombre(nombre);
        cuenta.setBalance(balance);
        cuenta.setUsuario(usuario);

        entityManager.persist(cuenta);
        entityManager.getTransaction().commit();
    }


    public Cuenta read(Long id) {
        return entityManager.find(Cuenta.class, id);
    }


    public void update(Long cuentaId, String nombre, BigDecimal balance) {
        entityManager.getTransaction().begin();

        Cuenta cuenta = entityManager.find(Cuenta.class, cuentaId);

        if (cuenta != null) {
            cuenta.setNombre(nombre);
            cuenta.setBalance(balance);

            entityManager.merge(cuenta);
        }

        entityManager.getTransaction().commit();
    }


    public boolean delete(Integer id) {
        try {

            entityManager.getTransaction().begin();
            Cuenta cuenta = entityManager.find(Cuenta.class, id);
            if (cuenta != null) {
                entityManager.remove(cuenta);
                entityManager.getTransaction().commit();
                return true;
            } else {
                entityManager.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        }
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
