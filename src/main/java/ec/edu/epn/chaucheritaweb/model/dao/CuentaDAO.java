package ec.edu.epn.chaucheritaweb.model.dao;

import ec.edu.epn.chaucheritaweb.model.entities.Cuenta;
import ec.edu.epn.chaucheritaweb.model.entities.Movimiento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class CuentaDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ChaucheritaWeb");

    public void crearCuenta(Cuenta cuenta) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al crear la cuenta: " + e.getMessage());
        } finally {
            em.close();
        }
    }


    public List<Cuenta> listarCuentas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cuenta c", Cuenta.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void eliminarCuenta(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Cuenta cuenta = em.find(Cuenta.class, id);
            if (cuenta != null) {
                em.remove(cuenta);
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al eliminar la cuenta: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void actualizarCuenta(Cuenta cuenta) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cuenta);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al actualizar la cuenta: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void realizarMovimiento(Cuenta cuenta, Movimiento movimiento) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (movimiento instanceof ec.edu.epn.chaucheritaweb.model.entities.Ingreso) {
                cuenta.setBalance(cuenta.getBalance().add(movimiento.getValor()));
            } else if (movimiento instanceof ec.edu.epn.chaucheritaweb.model.entities.Egreso) {
                cuenta.setBalance(cuenta.getBalance().subtract(movimiento.getValor()));
            }
            em.merge(cuenta);
            em.persist(movimiento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al realizar el movimiento: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void realizarMovimiento(Cuenta cuenta, Cuenta cuentaDestino, Movimiento movimiento) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (movimiento instanceof ec.edu.epn.chaucheritaweb.model.entities.Transferencia) {
                cuenta.setBalance(cuenta.getBalance().subtract(movimiento.getValor()));
                cuentaDestino.setBalance(cuentaDestino.getBalance().add(movimiento.getValor()));
            }
            em.merge(cuenta);
            em.merge(cuentaDestino);
            em.persist(movimiento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al realizar la transferencia: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}
