package nl.rug.search.opr;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.LockModeType;
import javax.persistence.FlushModeType;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Martin Verspai <martin@verspai.de>
 * @author Georg Fleisher <fleischer.georg@gmail.com>
 * @date Nov 11, 2010
 */
@Stateless
public class EMWrapperBean implements EMWrapperLocal {
    
    @PersistenceContext(unitName="PatternBase")
    private EntityManager em;

    @Override
    public Object persist(Object entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public <T> List<T> persist(List<T> entities){
        for (T entity : entities){
            em.persist(entity);
        }
        return entities;
    }

    @Override
    public <T> T merge(T entity) {
        return em.merge(entity);
    }

    @Override
    public void remove(Object entity) {
        em.remove(entity);
    }

    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        return em.find(entityClass, primaryKey);
    }

    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        return em.getReference(entityClass, primaryKey);
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushMode) {
        em.setFlushMode(flushMode);
    }

    @Override
    public FlushModeType getFlushMode() {
        return em.getFlushMode();
    }

    @Override
    public void lock(Object entity, LockModeType lockMode) {
        em.lock(entity, lockMode);
    }

    @Override
    public void refresh(Object entity) {
        em.refresh(entity);
    }

    @Override
    public void clear() {
        em.clear();
    }

    @Override
    public boolean contains(Object entity) {
        return em.contains(entity);
    }

    @Override
    public Query createQuery(String qlString) {
        return em.createNamedQuery(qlString);
    }

    @Override
    public Query createNamedQuery(String name) {
        return em.createNamedQuery(name);
    }

    @Override
    public Query createNativeQuery(String sqlString) {
        return em.createNativeQuery(sqlString);
    }

    @Override
    public Query createNativeQuery(String sqlString, Class resultClass) {
        return em.createNativeQuery(sqlString, resultClass);
    }

    @Override
    public Query createNativeQuery(String sqlString, String resultSetMapping) {
        return em.createNativeQuery(sqlString, resultSetMapping);
    }

    @Override
    public void joinTransaction() {
        em.joinTransaction();
    }

    @Override
    public Object getDelegate() {
        return em.getDelegate();
    }

    @Override
    public void close() {
        em.close();
    }

    @Override
    public boolean isOpen() {
        return em.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return em.getTransaction();
    }

    @Override
    public void queryExecuteUpdate(String string) {
        Query q = em.createQuery(string);
        q.executeUpdate();
    }

    @Override
    public List queryGetResultList(String sqlString){

        Query q = em.createQuery(sqlString);
        return q.getResultList();
    }

    @Override
    public <T> List<T> getAllEntities(Class<T> entityType) {
        Query q = em.createQuery("select c from " + entityType.getSimpleName() + " c");
        return q.getResultList();
    }
    
}
