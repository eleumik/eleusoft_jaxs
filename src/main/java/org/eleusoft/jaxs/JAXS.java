package org.eleusoft.jaxs;

import java.util.Properties;
/** 
 * Entry point for xml serialization.
 * 
 * <p>This class is configured with a property 
 * file in META-INF/services,
 * that may contain the following properties:
 * <dl class=dl>
 * <dt>org.eleusoft.jaxs.SAXSerializerFactory</dt>
 * <dd>Name of the {@link org.eleusoft.jaxs.SAXSerializerFactory} implementation</dd>
 * <dt>org.eleusoft.jaxs.DOMSerializerFactory</dt>
 * <dd>Name of the {@link org.eleusoft.jaxs.DOMSerializerFactory} implementation</dd>
 * <dt>debug</dt>
 * <dd>If "<code>true</code>" the implementation names are printed
 * at loading time to the system out.</dd>

 * </dl>
 * <p>Example configuration:
 * <pre class='code'>
 * <em>#org.eleusoft.jaxs.DOMSerializerFactory=org.eleusoft.jaxs.xerces.XercesSerializerFactory
 * #org.eleusoft.jaxs.SAXSerializerFactory=org.eleusoft.jaxs.trax.TrAXSerializerFactory
 * #org.eleusoft.jaxs.DOMSerializerFactory=org.eleusoft.jaxs.xerces.XercesSerializerFactory
 * #org.eleusoft.jaxs.SAXSerializerFactory=org.eleusoft.jaxs.xerces.XercesSerializerFactory
 * #org.eleusoft.jaxs.SAXSerializerFactory=org.eleusoft.jaxs.saxon.SaxonSerializerFactory</em>
 * org.eleusoft.jaxs.SAXSerializerFactory=org.eleusoft.jaxs.xalan.XalanSerializerFactory
 * debug=true
 * </pre>
 **/
public class JAXS
{
    private static final boolean debug = Boolean.getBoolean(JAXS.class.getName() + ".debug");

    private static String DEFAULT_DOMSERIALIZERFACTORY_CLASSNAME = 
        "org.eleusoft.jaxs.trax.TrAXSerializerFactory";
    private static String DEFAULT_SAXSERIALIZERFACTORY_CLASSNAME = 
         "org.eleusoft.jaxs.trax.TrAXSerializerFactory";
        
    private static DOMSerializerFactory domSerializerFactory;
    private static SAXSerializerFactory saxSerializerFactory;
    private static RuntimeException e;
    static 
    {
        try
        {
            final Properties props = new Properties();
                props.setProperty(DOMSerializerFactory.class.getName(),
                    DEFAULT_DOMSERIALIZERFACTORY_CLASSNAME);
                props.setProperty(SAXSerializerFactory.class.getName(),
                    DEFAULT_SAXSERIALIZERFACTORY_CLASSNAME);
            PropertiesLoader.loadProperties(JAXS.class, props, debug);
        
            final PropertiesLoader.ProviderLoader loader = 
                new PropertiesLoader.ProviderLoader(JAXS.class, false, debug);
        
            String domFactory = System.getProperty(DOMSerializerFactory.class.getName());
            //System.out.println("dom factory:" + domFactory);
            if (domFactory==null) domFactory = props.getProperty(DOMSerializerFactory.class.getName());
            if (domFactory==null) 
                throw new RuntimeException("No configuration for [" +
                    DOMSerializerFactory.class.getName() + "]");
            domSerializerFactory = (DOMSerializerFactory)loader.load(domFactory);
            if (domSerializerFactory==null) throw new RuntimeException("Could not load class:" + domFactory);
        
            String saxFactory = System.getProperty(SAXSerializerFactory.class.getName());
            if (saxFactory==null) saxFactory = props.getProperty(SAXSerializerFactory.class.getName());
            if (saxFactory==null) 
                throw new RuntimeException("No configuration for [" +
                    SAXSerializerFactory.class.getName() + "]");
            saxSerializerFactory = (SAXSerializerFactory)loader.load(saxFactory);
            if (saxSerializerFactory==null) throw new RuntimeException("Could not load class:" + saxFactory);
        
            if (debug || "true".equals(props.getProperty("debug"))) showInfo();
        }
        catch(RuntimeException re)
        {
            re.printStackTrace();
            e = re;
        }
            
        
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
        
        
        
    }
    private static void line()
    {
        System.out.println("-------------");
    }     
	
}
