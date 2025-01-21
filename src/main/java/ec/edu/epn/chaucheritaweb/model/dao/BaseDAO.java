package ec.edu.epn.chaucheritaweb.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
        try {
            entityManager.getTransaction().begin(); // Start transaction
            entityManager.persist(entity);
            entityManager.getTransaction().commit(); // Commit transaction
        } catch (Exception e) {
            entityManager.getTransaction().rollback(); // Rollback transaction on error
            throw e;
        }
    }

    /**
     * Updates the given entity in the database.
     *
     * @param entity entity to be updated
     */
    public void actualizar(T entity) {
        try {
            entityManager.getTransaction().begin(); // Start transaction
            entityManager.merge(entity);
            entityManager.getTransaction().commit(); // Commit transaction
        } catch (Exception e) {
            entityManager.getTransaction().rollback(); // Rollback transaction on error
            throw e;
        }
    }

    /**
     * Removes the entity from the database based on primary key.
     *
     * @param id primary key of the entity to remove
     */
    public void eliminar(int id) {
        try {
            entityManager.getTransaction().begin(); // Start transaction
            T entity = entityManager.find(entityClass, id);
            if (entity != null) {
                entityManager.remove(entity);
            }
            entityManager.getTransaction().commit(); // Commit transaction
        } catch (Exception e) {
            entityManager.getTransaction().rollback(); // Rollback transaction on error
            throw e;
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

    /**
     * Closes the EntityManager when the DAO is no longer needed.
     */
    public void cerrar() {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
