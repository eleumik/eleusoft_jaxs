package junit.org.eleusoft.jaxs;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.eleusoft.jaxs.SAXSerializerFactory;
import org.eleusoft.jaxs.saxon.SaxonSerializerFactory;
import org.eleusoft.jaxs.trax.TrAXSerializerFactory;
import org.eleusoft.jaxs.xalan.XalanSerializerFactory;
import org.eleusoft.jaxs.xerces.XercesSerializerFactory;
import org.xml.sax.SAXException;

public abstract class MultiSaxSerializerTestCase extends TestCase
{

    public MultiSaxSerializerTestCase()
    {
        super();
    }

    public MultiSaxSerializerTestCase(String name)
    {
        super(name);
    }

    protected abstract void doTestSaxSerializer(SAXSerializerFactory f) throws IOException,
                                                         SAXException,
                                                         ParserConfigurationException;

    public void testSaxon() throws SAXException,
                                     IOException,
                                     ParserConfigurationException
    {
        doTestSaxSerializer(new SaxonSerializerFactory());
    }

    public void testXerces() throws SAXException,
                                      IOException,
                                      ParserConfigurationException
    {
        doTestSaxSerializer(new XercesSerializerFactory());
    }

    public void testXalan() throws SAXException,
                                     IOException,
                                     ParserConfigurationException
    {
        doTestSaxSerializer(new XalanSerializerFactory());
    }

    public void testTrax() throws SAXException,
                                    IOException,
                                    ParserConfigurationException
    {
        doTestSaxSerializer(new TrAXSerializerFactory());
    }

}