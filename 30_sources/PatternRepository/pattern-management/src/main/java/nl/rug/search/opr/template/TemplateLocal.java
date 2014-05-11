package nl.rug.search.opr.template;

import javax.ejb.Local;
import nl.rug.search.opr.dao.GenericDaoLocal;
import nl.rug.search.opr.entities.template.Template;

/**
 *
 * @author cm
 */
@Local
public interface TemplateLocal extends GenericDaoLocal<Template, Long> {

    public void add(Template template);

    public Template getByName(String name);
}






























