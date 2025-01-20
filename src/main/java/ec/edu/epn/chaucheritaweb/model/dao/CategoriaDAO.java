package ec.edu.epn.chaucheritaweb.model.dao;

import ec.edu.epn.chaucheritaweb.model.entities.Categoria;
import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CategoriaDAO extends BaseDAO<Categoria> {

    public CategoriaDAO() {
        super(Categoria.class);
    }

    public List<Categoria> listarPorUsuario(Usuario usuario) {
        TypedQuery<Categoria> query = entityManager.createQuery(
            "SELECT c FROM Categoria c WHERE c.usuario = :usuario",
            Categoria.class
        );
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }
}
