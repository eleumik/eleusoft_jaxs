package junit.org.eleusoft.jaxs;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eleusoft.jaxs.SAXSerializer;
import org.eleusoft.jaxs.SAXSerializerFactory;
import org.xml.sax.SAXException;

public class TestNoOmitXml extends MultiSaxCheckXmlTestCase
{

    protected void doTestSaxSerializer(SAXSerializerFactory f) throws IOException, SAXException, ParserConfigurationException
    {
        checkSerialize(f, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml/>", "<xml/>");
    }

    protected void configure(SAXSerializer s)
    {
        super.configure(s);
        s.setOmitXMLDeclaration(false);
    }
    
}
