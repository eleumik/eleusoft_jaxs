package org.eleusoft.jaxs.helpers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.eleusoft.jaxs.DOMSerializer;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/** 
 * Helper base class for DOMSerializer
 **/
public abstract class AbstractDOMSerializer extends AbstractSerializer 
    implements DOMSerializer
{
	public String toString(Node node) throws IOException
	{
		java.io.StringWriter sw = new java.io.StringWriter();
		_serialize(node, sw);
		return sw.toString();
	}
	/**
	 * Template method for subclasses
	 */
	protected abstract void serializeNode(Node node, 
	    OutputStream out, String encoding) throws IOException;
	/**
	 * Template method for subclasses
	 */
	protected abstract void serializeNode(Node node, 
	    Writer out) throws IOException;
	
	public final void serialize(Node node) throws IOException
	{
	    if (node==null) throw new IllegalArgumentException("null node");
	    final Writer w = getWriter();
	    if (w!=null) _serialize(node, w);
	    else 
	    {
	        final OutputStream os = getOutputStream();
	        // 200902 restored also here as 200801
	        //if (os!=null) serializeNode(node, os, getEncoding());
	        if (os!=null) _serialize(node, os, getEncoding());
            else throw new IllegalStateException("No writer nor output stream set on serializer " + hashCode());
	    }
	}
	
	private final void _serialize(
	    final Node node, 
	    final Writer out) throws IOException
	{
	    // 200801 restored this code..xerces does not support write text nodes..
	    // for output stream ? with encoding?
		if (node instanceof org.w3c.dom.Comment)
		{
			out.write("<!--");
			out.write(xmlEscape(node.getNodeValue()));
			out.write("-->");
		}
		else if (node instanceof Text)
		{
			// TODO escape 1!!!
			out.write(xmlEscape(node.getNodeValue()));
		}
		else
		{		
			serializeNode(node, out);
		}
	}
	
	private final void _serialize(final Node node, 
	    final OutputStream out, 
	    final String encoding) throws IOException
	{
		if (node instanceof org.w3c.dom.Comment)
		{
			final Writer w = new OutputStreamWriter(out, encoding);
			w.write("<!--");
			w.write(xmlEscape(node.getNodeValue()));
			w.write("-->");
		}
		else if (node instanceof Text)
		{
		    // TODO escape 1!!!
			new OutputStreamWriter(out, encoding).write(xmlEscape(node.getNodeValue()));
		}
		else
		{		
			serializeNode(node, out, encoding);
        }
	}
	
	///////////////// copied from org.eleusoft.htmlprint.HTMLEncodeUtil
    
    // deal with ampersands first so we can ignore the ones we add later   
    private static final char[] xmlControlChars = new char[]{'&', '\'', '\"', '<', '>'};
	private static final String[] xmlControlCharsEscaped = new String[]{"&amp;", "&#39;", "&#34;", "&lt;", "&gt;"};
	/**
	 * Escapes text following xml rules.
	 */
	protected static String xmlEscape(String s1)
    {
		return charEscape(s1, xmlControlChars, xmlControlCharsEscaped);
    }
    /**
	 * Escapes text following xml rules (most efficient) .
	 */
	protected static String xmlEscape(char[] chars, int off, int len)
    {
		return charEscape(chars, off, len, xmlControlChars, xmlControlCharsEscaped);
    }
    
	/**
     * Escapes the passed string replacing the characters contained 
     * in the <code>toBeEscaped</code> parameter with the escaped equivalent strings
     * contained in the <code>escapedEquivalents</code> parameter.
     */
	private static String charEscape(String s1, char[] toBeEscaped, String[] escapedEquivalents)
    {
		if (s1==null) throw new IllegalArgumentException("null string to escape passed");
		return charEscape(s1.toCharArray(), 0, s1.length(), toBeEscaped, escapedEquivalents);
	}
	private static String charEscape(char[] s1, int off, int len, char[] toBeEscaped, String[] escapedEquivalents)
    {
		final int toBeEscapedLen	= toBeEscaped.length;
		
		StringBuffer buf	= new StringBuffer((int)(len*1.1));
		
		boolean found;
		char ch;
		
		for (int i=off; i<len; ++i) {
		    ch = s1[i];
		    found = false;
		    for (int index=0;index<toBeEscapedLen && !found;++index)
			{
				if (ch==toBeEscaped[index])
				{
					buf.append(escapedEquivalents[index]);
					found = true;
				}
			}
			if (!found) buf.append(ch);
		}
		//System.out.println("buf.toString()"+buf.toString()+"\nwas:" + s1);
		return buf.toString();
    }
	
	

	
	
}
