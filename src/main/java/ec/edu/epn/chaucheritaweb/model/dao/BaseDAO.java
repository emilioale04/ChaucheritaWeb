package ec.edu.epn.chaucheritaweb.model.dao;

public abstract class BaseDAO<T> {

    private final Class<T> entityClass;

    public BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

}