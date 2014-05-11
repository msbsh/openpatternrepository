package nl.rug.search.opr.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.rug.search.opr.Configuration;

/**
 *
 * @author Christian Manteuffel <cm@notagain.de>
 * @author Martin Verspai <martin@verspai.de>
 * @version 2.0
 */
public abstract class GenericDaoBean<T, ID extends Serializable> implements GenericDaoLocal<T, ID> {

    @PersistenceContext(unitName = "PatternBase")
    private EntityManager em;
    private Class<T> entityType;

    @SuppressWarnings("unchecked")
    public GenericDaoBean() {
        this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public T getById(ID id) {
        T entity = null;

        try {
            entity = getManager().getReference(entityType, id);
        } catch (EntityNotFoundException e) {
        }

        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll() {
        return (List<T>) getManager().createQuery("select e from " + getEntityName() + " as e").getResultList();
    }

    @Override
    public T makePersistent(T entity) {
        try {
            getManager().persist(entity);
            return entity;
        } catch (EntityExistsException ex) {
            return getManager().merge(entity);
        }
    }

    @Override
    public void makeTransient(T entity) {
        entity = getManager().merge(entity);
        getManager().remove(entity);
    }

    @Override
    public List<T> findBy(T example, String... exclude) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EntityManager getManager() {
        if (em == null) {
            throw new IllegalStateException("Entity Manager has not been initialized");
        }
        return em;
    }

    public String getEntityName() {
        String entityName = entityType.getSimpleName();

        if (entityType.isAnnotationPresent(javax.persistence.Table.class)) {
            entityName = entityType.getAnnotation(javax.persistence.Table.class).name();
        }

        return entityName;
    }
    
    protected Query createQuery(String property) {
        return getManager().createQuery((String) Configuration.getInstance().getProperty(property));
    }

    protected Query createNativeQuery(String property) {
        return getManager().createNativeQuery((String) Configuration.getInstance().getProperty(property));
    }
    
}
