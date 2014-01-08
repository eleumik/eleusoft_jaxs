package junit.org.eleusoft.jaxs;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;


import org.eleusoft.jaxs.SAXSerializer;
import org.eleusoft.jaxs.SAXSerializerFactory;
import org.eleusoft.xml.jaxp.JAXP;
import org.xml.sax.SAXException;

public class TestEntity13 extends MultiSaxCheckXmlTestCase
{

    protected void doTestSaxSerializer(SAXSerializerFactory f) throws IOException, SAXException, ParserConfigurationException
    {
        checkSerialize(f, new String[]{
            "<xml>&#13;</xml>",
            "<xml>&#xD;</xml>",
            "<xml>&#xd;</xml>"
        }, "<xml>&#13;</xml>");
    }

    
}
