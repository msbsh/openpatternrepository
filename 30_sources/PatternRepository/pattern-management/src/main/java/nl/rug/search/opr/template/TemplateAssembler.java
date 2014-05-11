/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.rug.search.opr.template;

import nl.rug.search.opr.entities.template.Type;
import nl.rug.search.opr.entities.template.Template;
import nl.rug.search.opr.entities.template.Component;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import nl.rug.search.opr.template.xml.XMLType;
import nl.rug.search.opr.template.xml.XMLMapping;
import nl.rug.search.opr.template.xml.XMLTemplate;
import org.xml.sax.SAXParseException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author cm
 */
public class TemplateAssembler {

    public static final int ERROR_PARSE = 1;
    public static final int ERROR_XML = 2;
    public static final int ERROR_UNUSED = 3;
    public static final int ERROR_MANDATORY = 4;
    public static final int ERROR_REFERENCE = 5;
    public static final int ERROR_UNKOWNTYPE = 6;
    public static final int ERROR_UNKOWN = 10;
    public static final int WARNING_UNUSED = 99;
    public static final String TYPE_ERROR = "error";
    public static final String TYPE_WARNING = "warning";
    private static JAXBContext ctx;
    private static final String XMLPATH = "nl.rug.search.opr.template.xml";


    private static Map<XMLType,Type> componentTypeToType = new EnumMap<XMLType,Type>(XMLType.class);
    static {
        componentTypeToType.put(XMLType.CONSEQUENCES, Type.CONSEQUENCES);
        componentTypeToType.put(XMLType.CONTEXT, Type.CONTEXT);
        componentTypeToType.put(XMLType.DESCRIPTION, Type.DESCRIPTION);
        componentTypeToType.put(XMLType.FORCES, Type.FORCES);
        componentTypeToType.put(XMLType.PROBLEM, Type.PROBLEM);
        componentTypeToType.put(XMLType.RELATIONSHIP, Type.RELATIONSHIP);
        componentTypeToType.put(XMLType.SOLUTION, Type.SOLUTION);
    }



    public static Template xmlToTemplate(String xmlData) {
        Template template = new Template();
        XMLTemplate xml;
                
        try {
            xml = getXmlTemplate(xmlData);
        } catch (JAXBException ex) {
            return null;
        }

        if (!isValid(xml)) return null;

        int order=0;
        List<Component> components = new ArrayList<Component>();
        for (XMLMapping m : xml.getMappings().getMapping()) {
            Component c = new Component();
            c.setDescription(m.getDescription());
            c.setIdentifier(m.getIdentifier());
            c.setMandatory(m.isMandatory());
            c.setName(m.getName());
            c.setType(componentTypeToType.get(m.getType()));
            c.setOrder(order++);
            components.add(c);
        }
        template.setComponents(components);
        return template;
    }

    public static String templateToXml(Template template) {
        throw new NotImplementedException();
    }

    public static  XMLTemplate getXmlTemplate(String xmlData) throws JAXBException {

        XMLTemplate xmlTemplate = null;

        if (xmlData == null || xmlData.length() <= 0) {
            return null;
        }

        if (ctx == null) {
            ctx = JAXBContext.newInstance(XMLPATH);
        }
        Unmarshaller u = ctx.createUnmarshaller();
        Object tmp = u.unmarshal(new StringReader(xmlData));
        if (tmp instanceof XMLTemplate) {
            xmlTemplate = (XMLTemplate) tmp;
        }


        return xmlTemplate;
    }

    public static Map<Message, String> validate(XMLTemplate template) {

        Map<Message, String> messages = new HashMap<Message, String>();
        XMLTemplate tmpl = template;

        //Build mappings map
        Map<String, XMLMapping> mappings = new HashMap<String, XMLMapping>();
        for (XMLMapping m : tmpl.getMappings().getMapping()) {
            mappings.put(m.getIdentifier(), m);
            if ( m.getType() == null ) {
                 messages.put(new Message().setCode(ERROR_UNKOWNTYPE), TYPE_ERROR);
            }
        }

        return messages;
    }

    public static Map<Message, String> validate(String xmlData) {

        Map<Message, String> messages = new HashMap<Message, String>();

        try {
            
            messages = validate(getXmlTemplate(xmlData));

        } catch (JAXBException ex1) {

            if (ex1.getLinkedException() instanceof SAXParseException) {
                SAXParseException sax = (SAXParseException) ex1.getLinkedException();
                messages.put(new Message().setCode(ERROR_PARSE).addDetail(sax.getMessage()).addDetail(sax.getLineNumber() + ""), TYPE_ERROR);
            } else {
                messages.put(new Message().setCode(ERROR_XML).addDetail(ex1.getMessage()), TYPE_ERROR);
            }

        } catch (Exception ex2) {

            messages.put(new Message().setCode(ERROR_UNKOWN), TYPE_ERROR);

        }
        return messages;
    }

    public static boolean isValid(XMLTemplate template) {
        return !validate(template).containsValue(TYPE_ERROR);
    }
}
