package nl.rug.search.opr.dao;


import java.io.Serializable;
import java.util.List;

/**
 *
 * @param <T>
 * @param <ID> 
 * @author cm
 */
public interface GenericDaoLocal<T, ID extends Serializable> {

    T getById(ID id);

    List<T> getAll();

    T makePersistent(T entity);

    void makeTransient(T entity);

    List<T> findBy(T example, String... exclude);

}
