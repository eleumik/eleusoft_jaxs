package org.eleusoft.jaxs.saxon;


import javax.xml.transform.TransformerConfigurationException;

import org.eleusoft.jaxs.DOMSerializer;
import org.eleusoft.jaxs.DOMSerializerFactory;
import org.eleusoft.jaxs.SAXSerializer;
import org.eleusoft.jaxs.SAXSerializerFactory;

/** 
 * Factory of DOMSerializer and SAXSerializer based on Saxon
 * from Michael Kay, see <a href='http://saxon.sourceforge.net/'>http://saxon.sourceforge.net/</a>
 * <p>Thread-safe.
 */
public class SaxonSerializerFactory
   implements  SAXSerializerFactory //DOMSerializerFactory dom not impl..
{
    
    public  DOMSerializer createDOMSerializer() 
    {
        return new SaxonDOMSerializer();
    }
    public  SAXSerializer createSAXSerializer()
    {
        return new SaxonSAXSerializer();
    }
     
}
