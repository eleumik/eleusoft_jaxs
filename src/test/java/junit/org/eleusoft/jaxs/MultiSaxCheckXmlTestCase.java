package junit.org.eleusoft.jaxs;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;

import org.eleusoft.jaxs.SAXSerializer;
import org.eleusoft.jaxs.SAXSerializerFactory;
import org.xml.sax.SAXException;

public abstract class MultiSaxCheckXmlTestCase extends
    MultiSaxSerializerTestCase
{

    public MultiSaxCheckXmlTestCase()
    {
        super();
    }

    public MultiSaxCheckXmlTestCase(String name)
    {
        super(name);
    }
    
    protected final void checkSerialize(SAXSerializerFactory f, String string) throws IOException, SAXException, ParserConfigurationException
    {
        checkSerialize(f, string, string);
    }
    protected void configure(SAXSerializer s)
    {
        s.setOmitXMLDeclaration(true);
        
    }
    protected final void checkSerialize(SAXSerializerFactory f,
        String exp,
        String xmlString) throws IOException, SAXException, ParserConfigurationException
        {
        checkSerialize(f, new String[]{exp}, xmlString);
        }
    protected final void checkSerialize(SAXSerializerFactory f,
        String exp,
        String exp2,
        String xmlString) throws IOException, SAXException, ParserConfigurationException
    {
        checkSerialize(f, new String[]{exp,exp2}, xmlString);
    }
    protected final void checkSerialize(SAXSerializerFactory f,
        String[] exp,
        String xmlString) throws IOException, SAXException, ParserConfigurationException
    {
    
        SAXSerializer s = f.createSAXSerializer();
        configure(s);
        //s.setStandalone(false);
        final StringWriter sw = new StringWriter();
        s.setWriter(sw);
        org.xml.sax.ContentHandler c = s.asContentHandler();
        JAXP.parse(xmlString, c);
        String xml = sw.toString();
        // this removes a \n after <?xml ?>
        if (xml.startsWith("<?xml"))
        {
            int i = xml.indexOf("?>", 3);
            int ch = xml.charAt(i+2);
            if (ch==10) {
                final StringBuffer sb = new StringBuffer();
                sb.append(xml.substring(0, i+2));
                sb.append(xml.substring(i+3));
                xml = sb.toString(); 
            }
        }
        boolean ok = false;
        for(int i=0,len=exp.length;i<len;i++)
        {
            if (exp[i]!=null && exp[i].equals(xml)) 
            {
                ok = true;
                break;
            }
        }
        if (ok)
        {
            // ok
        } 
        else
        {
            if (exp.length==1) assertEquals(exp[0], xml);
            
            final StringBuffer sb = new StringBuffer();
            sb.append(xml +  "\n\n is None of expected: \n");
            for(int i=0,len=exp.length;i<len;i++)
            {
                sb.append(exp[i] + "\n");
            }
            fail(sb.toString());
            
        }

    }

     
}