package nl.rug.search.opr.template;

import nl.rug.search.opr.entities.template.Template;
import nl.rug.search.opr.entities.template.Component;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import nl.rug.search.opr.ConfigConstants;
import nl.rug.search.opr.dao.GenericDaoBean;

/**
 *
 * @author cm
 */
@Stateless
public class TemplateBean extends GenericDaoBean<Template, Long> implements TemplateLocal {

    @Override
    public void add(Template template) {

        for (Component c : template.getComponents()) {
            getManager().persist(c);
        }

        makePersistent(template);
    }

    @Override
    public Template getByName(String name) {
        Template template = null;

        try {
            Query q = createQuery(ConfigConstants.QUERY_GET_TEMPLATE_BY_NAME);
                q.setParameter(1, name);

            template = (Template) q.getSingleResult();

        } catch (NoResultException e1) {
        } catch (Exception e2) {
            throw new TemplatingException(e2);
        }

        return template;
    }
    
}
