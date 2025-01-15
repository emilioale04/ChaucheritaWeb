package ec.edu.epn.chaucheritaweb.model.dao;

import ec.edu.epn.chaucheritaweb.model.entities.Cuenta;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
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
}
