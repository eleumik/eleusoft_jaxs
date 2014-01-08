package junit.org.eleusoft.jaxs;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eleusoft.jaxs.SAXSerializerFactory;
import org.xml.sax.SAXException;

public class TestCrLF extends MultiSaxCheckXmlTestCase
{

    protected void doTestSaxSerializer(SAXSerializerFactory f) throws IOException, SAXException, ParserConfigurationException
    {
        checkSerialize(f, "<xml>" + (char)13 + (char)10 + "</xml>");
    }

    
}
