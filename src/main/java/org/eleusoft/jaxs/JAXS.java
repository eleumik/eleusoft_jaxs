package org.eleusoft.jaxs;

import org.xml.sax.ContentHandler;


/** 
 * Entry point for xml serialization.
 * 
 * <p>Default implementations for SAX and DOM are 
 * {@link org.eleusoft.jaxs.trax.TrAXSerializerFactory}
 * 
 * <p>This class is optionally configured with two 
 * files in <code>META-INF/services</code> in the classpath:
 * <ol>
 * <li><code>META-INF/services/org.eleusoft.jaxs.DOMSerializer</code>
 * <li><code>META-INF/services/org.eleusoft.jaxs.SAXSerializer</code>
 * </ol>
 * that may contain a list of implementations for each line.

 * </dl>
 * <p>Example configuration:
 * <p>File <code>META-INF/services/org.eleusoft.jaxs.DOMSerializer</code>
 * <pre class='code'>
 * org.eleusoft.jaxs.xerces.XercesSerializerFactory
 * org.eleusoft.jaxs.trax.TrAXSerializerFactory
 * </pre>
 * <p>File <code>META-INF/services/org.eleusoft.jaxs.SAXSerializer</code>
 * <pre class='code'>
 * org.eleusoft.jaxs.xalan.XalanSerializerFactory
 * org.eleusoft.jaxs.xerces.XercesSerializerFactory
 * org.eleusoft.jaxs.saxon.SaxonSerializerFactory</em>
 * </pre>
 **/
public class JAXS
{
    private static final boolean debug = Boolean.getBoolean(JAXS.class.getName() + ".debug");

    
    private static DOMSerializerFactory domSerializerFactory;
    private static SAXSerializerFactory saxSerializerFactory;
    private static RuntimeException e;
    
    static
    {
        String DEFAULT_DOMSERIALIZERFACTORY_CLASSNAME = 
            "org.eleusoft.jaxs.trax.TrAXSerializerFactory";
        String DEFAULT_SAXSERIALIZERFACTORY_CLASSNAME = 
             "org.eleusoft.jaxs.trax.TrAXSerializerFactory";
        
        ServiceLoader sl = new ServiceLoader();
        domSerializerFactory = (DOMSerializerFactory) sl.getService(DOMSerializerFactory.class, new String[]{
            DEFAULT_DOMSERIALIZERFACTORY_CLASSNAME
        });
        saxSerializerFactory = (SAXSerializerFactory) sl.getService(SAXSerializerFactory.class, new String[]{
            DEFAULT_SAXSERIALIZERFACTORY_CLASSNAME
        });
        
        if (debug) showInfo();
        
    }
    
    
    

    private JAXS(){}
    /**
     * Retrieves a new DOMSerializer.
     * The DOMSerializer is not thread safe.
     * @return a new DOMSerializer
     */
    public static DOMSerializer newDOMSerializer()
    {
        if (e!=null) throw e;
        //return new org.eleusoft.jaxs.resin.ResinDOMSerializer();
		//return new org.eleusoft.jaxs.xerces.XercesDOMSerializer();
		//return new org.eleusoft.jaxs.trax.TrAXDOMSerializer();
		//return new TrAXDOMSerializer();
		return domSerializerFactory.createDOMSerializer();
    }
    /**
     * Retrieves a new SAXSerializer.
     * The SAXSerializer is not thread safe.
     * @return a new SAXSerializer
     */
    public static SAXSerializer newSAXSerializer()
    {
		if (e!=null) throw e;
        return saxSerializerFactory.createSAXSerializer();
    	//return new org.eleusoft.jaxs.xerces.XercesSAXSerializer();
    	//return new org.eleusoft.jaxs.xalan.XalanSAXSerializer();
    }
    
    /**
     * This method shows info about the configured 
     * providers to the System.out.
     */
    public static void main(String[] params)
    {
        // Just show infos
        showInfo();
    }
    private static void showInfo()
    {
        System.out.println('\n');
        line();
        System.out.println("Eleusoft JAXS");
        line();
        System.out.println("SAX:" + newSAXSerializer().getClass().getName());
        System.out.println("DOM:" + newDOMSerializer().getClass().getName());
        line();
        ClassLoader loader;
		try
		{
			loader = Thread.currentThread().getContextClassLoader();
		}
		catch(Exception e)
		{
		    e.printStackTrace();
			loader = JAXS.class.getClassLoader();
		}
		catch(Error e)
		{
			e.printStackTrace();
			loader = JAXS.class.getClassLoader();
		}
        System.out.println("Classloader:" + loader);
        if (loader!=null) 
        {
            System.out.println("Parent Classloader:" + loader.getParent());
        }
        
        System.out.println("Classloader of SAX Serializer:" + newSAXSerializer().getClass().getClassLoader());
        System.out.println("Classloader of JAXP ContentHandler:" + ContentHandler.class.getClassLoader());
        //System.out.println("Classloader of java.lang.String:" + new String().getClass().getClassLoader());
               
        line();
        
        
    }
    private static void line()
    {
        System.out.println("-------------");
    }     
	
}
