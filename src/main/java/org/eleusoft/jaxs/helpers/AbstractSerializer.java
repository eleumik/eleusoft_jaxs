package org.eleusoft.jaxs.helpers;

import java.io.OutputStream;
import java.io.Writer;

import org.eleusoft.jaxs.XMLSerializer;
/** 
 * Abstract base class for XML serializers.
 **/
public abstract class AbstractSerializer implements XMLSerializer
{
	private static final String DEFAULT_ENCODING = "UTF-8";
    
	private Object out;
	private String encoding;
    /**
     *  The default value is "xml".
     */
    private String method = "xml";
    /**
     *  The default value is "1.0"
     */
    private String version = "1.0";
    /**
     *  The default value is no.
     */
    private boolean pretty = false;
    /**
     *  The default value is no.
     */
    private boolean standalone = false;
    /**
     * The xml output method should output an XML declaration 
     * unless the omit-xml-declaration attribute has the value yes. 
     * Default value is false;
     */
    private boolean omitXMLDecl = false;
    
    protected AbstractSerializer()
    {
        super();
    }
    
    
	public final void setVersion(String version)
	{
	    this.version = version;
	}
	public final String getVersion()
	{
	    return version;
	}
	public final void setMethod(String method)
	{
	    this.method = method;
	}
	public final String getMethod()
	{
	    return method;
	}
	
    public final boolean getPrettyPrint()
    {
        return pretty;
    }
	public final void setPrettyPrint(boolean how)
	{
	    this.pretty = how;
	}
	
	public final void setOutputStream(final OutputStream pout)
	{
	    this.out = pout;
	}
    
	public final void setEncoding(String encoding)
	{
	    this.encoding = encoding;
	}
	public final String getEncoding()
	{
	    if (this.encoding==null) return DEFAULT_ENCODING;
	    else return this.encoding;
	}
	public final boolean getOmitXMLDeclaration()
	{
	    return omitXMLDecl;
	}
	public final void setOmitXMLDeclaration(boolean how)
	{
	    omitXMLDecl = how;
	}
	public final void setStandalone(boolean how)
	{
	    standalone = how;
	}
	
	public final boolean getStandalone()
	{
	    return standalone;
	}
	
	
	public final void setWriter(Writer writer)
	{
	    this.out = writer;
	    
	}
	
	protected final OutputStream getOutputStream()
	{
	    if (out instanceof OutputStream) return (OutputStream) out;
	    else return null;
	}
	protected final Writer getWriter()
	{
	    if (out instanceof Writer) return (Writer) out;
	    else return null;
	}
	
	
}
