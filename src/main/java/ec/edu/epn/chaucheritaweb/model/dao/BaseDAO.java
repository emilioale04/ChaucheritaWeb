package ec.edu.epn.chaucheritaweb.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public abstract class BaseDAO<T> {

    protected EntityManager entityManager;

    private final Class<T> entityClass;

    public BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ChaucheritaWeb");
        this.entityManager = emf.createEntityManager();
    }

    /**
     * Persists the given entity in the database.
     *
     * @param entity entity to be persisted
     */
    public void crear(T entity) {
        entityManager.persist(entity);
    }

    /**
     * Updates the given entity in the database.
     *
     * @param entity entity to be updated
     * @return the updated entity
     */
    public T actualizar(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * Removes the entity from the database based on primary key.
     *
     * @param id primary key of the entity to remove
     */
    public void eliminar(int id) {
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
        }
    }

    /**
     * Finds an entity by its primary key.
     *
     * @param id primary key of the entity
     * @return the found entity instance or null if the entity does not exist
     */
    public T findById(int id) {
        return entityManager.find(entityClass, id);
    }

    /**
     * Returns all entities of type T.
     *
     * @return a list of all entities
     */
    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> rootEntry = cq.from(entityClass);
        CriteriaQuery<T> all = cq.select(rootEntry);
        return entityManager.createQuery(all).getResultList();
    }
}
