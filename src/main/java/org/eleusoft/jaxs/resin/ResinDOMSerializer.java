package org.eleusoft.jaxs.resin;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

//import com.caucho.xml.XmlPrinter;

import org.eleusoft.jaxs.helpers.AbstractDOMSerializer;



/** 
 * Serializer for DOM nodes based on Resin XmlPrinter.
 * Not thread-safe.
 **/
class ResinDOMSerializer extends AbstractDOMSerializer
{
	ResinDOMSerializer()
	{
	   super();
	}	
//	public void serializeNode(final Node node, 
//	    final Writer out) throws IOException
//	{
//		final XmlPrinter serializer = new XmlPrinter(out);
//		printNode(serializer, node);
//	}
//	public void serializeNode(final Node node, 
//	    final OutputStream out, final String encoding) throws IOException
//	{
//		final XmlPrinter serializer = new XmlPrinter(out);
//		printNode(serializer, node);
//    }
//	
//	private void printNode(final XmlPrinter serializer, 
//	    final Node node) throws IOException
//	{	
//	    serializer.setEncoding(getEncoding());
//		serializer.setMethod(getMethod());
//		serializer.setVersion(getVersion());
//		serializer.setPretty(getPrettyPrint());
//		serializer.setPrintDeclaration(getOmitXMLDeclaration());
//		serializer.printNode(node);
//	}

    protected void serializeNode(Node node, OutputStream out, String encoding) throws IOException
    {
        // TODO Auto-generated method stub
        
    }

    protected void serializeNode(Node node, Writer out) throws IOException
    {
        // TODO Auto-generated method stub
        
    }
	
	 
}
