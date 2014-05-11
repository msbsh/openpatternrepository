package nl.rug.search.opr;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import nl.rug.search.opr.file.FileBean;
import nl.rug.search.opr.file.FileLocal;
import nl.rug.search.opr.pattern.CategoryBean;
import nl.rug.search.opr.pattern.CategoryLocal;
import nl.rug.search.opr.pattern.LicenseBean;
import nl.rug.search.opr.pattern.LicenseLocal;
import nl.rug.search.opr.pattern.PatternBean;
import nl.rug.search.opr.pattern.PatternLocal;
import nl.rug.search.opr.pattern.QualityAttributeBean;
import nl.rug.search.opr.pattern.QualityAttributeLocal;
import nl.rug.search.opr.pattern.TagBean;
import nl.rug.search.opr.pattern.TagLocal;
import nl.rug.search.opr.search.IndexBean;
import nl.rug.search.opr.search.IndexLocal;
import nl.rug.search.opr.search.SearchBean;
import nl.rug.search.opr.search.SearchLocal;

/**
 *
 * @author Georg Fleischer
 */
public class Ejb {

    private static <T, I> I lookUp(Class<T> classType, Class<I> interfaceType) {

        StringBuilder lookUpName = new StringBuilder();
        lookUpName.append("java:global/classes/");
        lookUpName.append(classType.getSimpleName());
        lookUpName.append("!");
        lookUpName.append(interfaceType.getName());

        try {
            Context context = new InitialContext();
            return (I) context.lookup(lookUpName.toString());
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static EMWrapperLocal createEMWrapperBean() {
        return lookUp(EMWrapperBean.class, EMWrapperLocal.class);
    }

    public static CategoryLocal createCategoryBean() {
        return lookUp(CategoryBean.class, CategoryLocal.class);
    }

    public static FileLocal createFileBean() {
        return lookUp(FileBean.class, FileLocal.class);
    }

    public static LicenseLocal createLicenseBean() {
        return lookUp(LicenseBean.class, LicenseLocal.class);
    }

    public static PatternLocal createPatternBean() {
        return lookUp(PatternBean.class, PatternLocal.class);
    }

    public static TagLocal createTagBean() {
        return lookUp(TagBean.class, TagLocal.class);
    }

    public static QualityAttributeLocal createQualityAttributeBean() {
        return (QualityAttributeLocal) lookUp(QualityAttributeBean.class, QualityAttributeLocal.class);
    }

    public static SearchLocal createSearchBean() {
        return (SearchLocal) lookUp(SearchBean.class, SearchLocal.class);
    }

    public static IndexLocal createIndexBean(){
        return (IndexLocal) lookUp(IndexBean.class, IndexLocal.class);
    }
}
