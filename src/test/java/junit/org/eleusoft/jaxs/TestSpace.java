package junit.org.eleusoft.jaxs;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eleusoft.jaxs.SAXSerializerFactory;
import org.xml.sax.SAXException;

public class TestSpace extends MultiSaxCheckXmlTestCase
{

    protected void doTestSaxSerializer(SAXSerializerFactory f) throws IOException, SAXException, ParserConfigurationException
    {
        checkSerialize(f, "<xml> </xml>");
    }

    
}
