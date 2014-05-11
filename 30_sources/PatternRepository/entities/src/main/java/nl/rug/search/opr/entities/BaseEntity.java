package nl.rug.search.opr.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Equals compares primary keys if both are non-null.
 * If either is null, then the class must define the fields that determine
 * uniqueness and compare those.
 *
 * hashCode always uses the same fields used in the equals comparison and never
 * the primary key. If the entity is immutable or the fields that are used for
 * the calculation are, then the hashCode could be cached to save the cost of
 * unnecessary recalculation.
 */
@MappedSuperclass
public abstract class BaseEntity<T extends BaseEntity<T>> {

    public abstract Long getId();
    public abstract void setId(Long id);

    @Override
    public final int hashCode() {
        return calculateHashCode(getHashCodeData());
    }

    @Override
    public final boolean equals(final Object other) {

        if (other==null) {
            return false;
        }

        if (this == other) {
            return true;
        }

        if (! (other instanceof BaseEntity)) {
            return false;
        }

        // if pks are both set, compare
        if (getPk() != null) {
            Serializable otherPk = ((BaseEntity) other).getPk();
            if (otherPk != null) {
                return getPk().equals(otherPk);
            }
        }

        //else compare data
        return dataEquals((T) other);
    }

    /**
     * Utility method for <code>equals()</code> methods.
     * @param o1  one object
     * @param o2  another object
     * @return <code>true</code> if they're both <code>null</code> or both equal
     */
    protected static final boolean areEqual(final Object o1, final Object o2) {
        return (o1 == o2) || (o1 != null && o1.equals(o2));
    }

    /**
     * Utility method for <code>equals()</code> methods.
     * @param s1  one string
     * @param s2  another string
     * @param ignoreCase if <code>false</code> do case-sensitive comparison
     * @return <code>true</code> if they're both <code>null</code> or both equal
     */
    protected static final boolean areEqual(final String s1,final String s2,final boolean ignoreCase) {
        // for use in custom equals() methods

        if (s1 == null && s2 == null) {
            return true;
        }

        if (s1 == null || s2 == null) {
            return false;
        }

        return ignoreCase ? s1.equalsIgnoreCase(s2) : s1.equals(s2);
    }

    /**
     * Utility method for <code>equals()</code> methods.
     * Compares to date objects.
     * @param d1  one date
     * @param d2  another date
     * @return <code>true</code> if they're both <code>null</code> or both equal
     */
    protected static final boolean areEqual(final Date d1, final Date d2) {
        // for use in custom equals() methods

        if (d1 == null && d2 == null) {
            return true;
        }

        if (d1 == null || d2 == null) {
            return false;
        }

        return d1.getTime() == d2.getTime();
    }

    /**
     * Utility method for <code>equals()</code> methods.
     * @param f1  one float
     * @param f2  another float
     * @return <code>true</code> if they're equal
     */
    protected static final boolean areEqual(final float f1, final float f2) {
        return Float.floatToIntBits(f1) == Float.floatToIntBits(f2);
    }

    /**
     * Compare data only; null, class, and pk have been checked.
     * @param other the other instance
     * @return <code>true</code> if equal
     */
    protected abstract boolean dataEquals(T other);

    

    protected int calculateHashCode(final Object... values) {
        HashCodeBuilder builder = new HashCodeBuilder();
        for (Object value : values) {
            builder.append(value);
        }
        return builder.toHashCode();
    }

    @Transient
    protected abstract Object[] getHashCodeData();

    @Transient
    public abstract Serializable getPk();

    @Transient
    public abstract T getThis();
}
