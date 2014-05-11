package nl.rug.search.opr.entities;

import java.io.Serializable;

/**
 *
 * @param <T>
 * @author Christian Manteuffel <cm@notagain.de>
 */
public abstract class AmbiguousEntity<T extends AmbiguousEntity<T>> extends BaseEntity<T> {

    @Override
    public abstract Long getId();

    @Override
    public abstract void setId(Long id);

    
    @Override
    protected boolean dataEquals(T other) {
        return false;
    }

    @Override
    protected Object[] getHashCodeData() {
        return new Object[]{};
    }

    @Override
    public abstract Serializable getPk();

    
    @Override
    public abstract T getThis();


}
