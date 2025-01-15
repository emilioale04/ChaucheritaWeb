package ec.edu.epn.monederovirtual.model.dao;

public abstract class BaseDAO<T> {

    private final Class<T> entityClass;

    public BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

}