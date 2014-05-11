package nl.rug.search.opr.webservices;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import nl.rug.search.opr.search.api.ResultList;
import nl.rug.search.opr.webservices.types.PatternDTO;

/**
 *
 * @author cm
 */
public class GenerateSchema {

    public static void main(String[] args) throws JAXBException, IOException {
        final File baseDir = new File("./web");
        class MySchemaOutputResolver extends SchemaOutputResolver {

            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                return new StreamResult(new File(baseDir, "OPRWebServiceTypes.xsd"));
            }
        }

        JAXBContext context = JAXBContext.newInstance(ResultList.class, PatternDTO.class);
        context.generateSchema(new MySchemaOutputResolver());
    }
}
