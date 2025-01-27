package ec.edu.epn.chaucheritaweb.model.dao;

import ec.edu.epn.chaucheritaweb.model.entities.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class UsuarioDAO extends BaseDAO<Usuario> {
    public UsuarioDAO() {
        super(Usuario.class);
    }
    
    public Usuario autenticar(String usuario, String clave) {
    	try {
            TypedQuery<Usuario> query = entityManager.createQuery(
                    "SELECT u FROM Usuario u "
                    + "WHERE u.usuario = :usuario "
                    + "AND u.clave = :clave",
                    Usuario.class
            );
            query.setParameter("usuario", usuario);
            query.setParameter("clave", clave);

            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Usuario registrar(String nombre, String usuario, String clave) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setUsuario(usuario);
        nuevoUsuario.setClave(clave);

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(nuevoUsuario);
            entityManager.getTransaction().commit();
            return nuevoUsuario;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}