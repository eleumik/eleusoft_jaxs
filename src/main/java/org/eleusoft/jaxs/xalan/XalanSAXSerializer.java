package org.eleusoft.jaxs.xalan;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

import org.apache.xml.serializer.OutputPropertiesFactory;
import org.apache.xml.serializer.Serializer;
import org.apache.xml.serializer.SerializerFactory;
import org.eleusoft.jaxs.helpers.AbstractSAXSerializer;
import org.xml.sax.ContentHandler;
/** 
 * Serializer for SAX events streams based on apache
 * <code>org.apache.xml.serializer.Serializer</code> class,
 * the serializer from Xalan.
 * <p>Note that the <code>org.apache.xml.serializer.Serializer</code>
 * is included only in JDK4 "endorsed" libs,
 * more modern JDK need to have Xalan-Serializer in the classpath:
 * <pre>
 * &lt;dependency>
        &lt;groupId>xalan&lt;/groupId>
        &lt;artifactId>serializer&lt;/artifactId>
        &lt;version>2.7.1&lt;/version>
    &lt;/dependency>
   </pre>
 **/
class XalanSAXSerializer extends AbstractSAXSerializer
{
    private final Properties prop = 
        OutputPropertiesFactory.getDefaultMethodProperties("xml");


    public XalanSAXSerializer()
	{
	    //throw new Error(prop.values().toString());
	    
       
	}	
	
    private Serializer serializer;
	/**
	 * Returns the serializer internal ContentHandler
	 * to be used to receive the SAX events.
	 */
	public ContentHandler asContentHandler() throws IOException
	{
	    
	    String encoding = getEncoding();
	    Writer writer = getWriter();
	    if (writer==null) 
	    {
	        OutputStream os = getOutputStream();
	        if (os==null) throw new IllegalStateException("No output object set (writer or outputstream)");
	        
	        writer = new OutputStreamWriter(os, encoding);
	    }
	    XalanSerializerUtil.configureXMLSerializer(this, prop);
		
		if (serializer==null) 
		    serializer = SerializerFactory.getSerializer(prop);
		serializer.setWriter(writer);
		return serializer.asContentHandler();
	}
		
}
