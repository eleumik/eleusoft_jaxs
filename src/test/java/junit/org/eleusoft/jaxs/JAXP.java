package junit.org.eleusoft.jaxs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.PropertyResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eleusoft.xml.coll.ArrayStack;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;




/**
 * JAXP <em>parsing utilities</em>.
 * <p>Retrieving from JAXP a parser
 * or a builder is a quite expensive operation.
 * For this reason this class caches parsers
 * and document builders for
 * better performance.
 * The price is less flexibility since the
 * parser creation and the parsing process
 * cannot be controlled in the same fine way
 * is enabled by the JAXP api.
 * <p>Parser created from this class are configured
 * by default with these settings:
 * <ul>
 * <li>Namespace Aware - <code>true</code>
 * <li>Validating - <code>false</code>
 * <li>Coalescing (DOM only) - <code>false</code>
 * </ul>
 * <p>These default settings can be changed in the property
 * file <code>org.eleusoft.xml.jaxp.JAXP</code> placed
 * in a <code>META-INF/services</code> in the classpath.
 * The property file is obtained by this class using
 * {@link PropertyResourceBundle#getBundle(String)}
 * and passing <code>META-INF/services/org.eleusoft.xml.jaxp.JAXP</code>.
 * <p>Available properties:
 * <ul>
 * <li>dom.cache.size (int, default 10)
 * <li>sax.cache.size (int, default 10)
 * <li>dom.validate (boolean, default false)
 * <li>sax.validate (boolean, default false)
 * <li>dom.namespaces (boolean, default true)
 * <li>sax.namespaces (boolean, default true)
 * <li>dom.coalescing (boolean, default false)
 * <li>dom.expandentityreferences (boolean, default true)
 * </ul>
 * <p>Default <a href='http://www.saxproject.org/apidoc/org/xml/sax/package-summary.html#package_description'>
 * features</a> can be set using this format:
 * <pre class=code>
 * dom.feature.<em>[featurename]</em>=<em>[booleanvalue]</em>
 * </pre>
 * where <em>[featurename]</em> is the name of the feature, for example:
 * <code>namespace-prefixes</code>, in this case:
 * <pre class=code>
 * dom.feature.namespace-prefixes=true
 * </pre>
 * <p>Note that in this case we are setting a standard feature,
 * that has prefix <code>http://xml.org/sax/features/</code>,
 * in case we want to set a feature with an other prefix
 * say <code>http://example.org/sax/features/fast-index</code> we can add the
 * following:
 * <pre class=code>
 * dom.feature.fast-index=true
 * dom.feature.fast-index.prefix=http://example.org/sax/features/
 * </pre>
 *
 *
 *
 * <p>Typical usage to retrieve a DOM Document:</p>
 * <pre class=code>
 * Document doc = JAXP.getDocument(file);
 * ..do document operations...
 *
 * or in the most complete form:
 *
 * Document doc = JAXP.getDocument(inputSource, errorHandler, entityResolver);
 * </pre>
 * <p>Typical usage to parse an xml document generating sax events:</p>
 * <pre class=code>
 * ContentHandler myContentHandler = ...(obtain handler)..
 * JAXP.parse(file, myContentHandler);
 *
 * or a more complete form:
 *
 * JAXP.parse(file, myContentHandler, errorHandler, entityResolver);
 * </pre>
 * <p>The most complete form let the client specific also an optional LexicalHandler
 * and an XMLFilter. The XMLFilter will be configured with the actual parser
 * calling its {@link XMLFilter#setParent(XMLReader)} method.
 *
 **/
public class JAXP
{

	private static final DefaultHandler NULL_DEFAULT_HANDLER = new DefaultHandler();
	/**
	 * An helper singleton ContentHandler that does not perform
	 * any operation on sax event calls.
	 */
 	public static final ContentHandler NULL_CONTENT_HANDLER = NULL_DEFAULT_HANDLER;
	/**
	 * An helper singleton ErrorHandler that performs standard
	 * operations on sax error event calls. (no-op on warning and error,
	 * re-throw exception on fatalError).
	 * <p>
	 */
 	public static final ErrorHandler NULL_ERROR_HANDLER = NULL_DEFAULT_HANDLER;
	/**
	 * An helper singleton EntityResolver that does not resolve
	 * any entity. Returns always null from <code>resolveEntity</code>
	 * @see org.xml.sax.EntityResolver#resolveEntity
	 */
 	public static final EntityResolver NULL_ENTITY_RESOLVER = NULL_DEFAULT_HANDLER;

	private final static ArrayStack buildersCache = new ArrayStack();
	private final static ArrayStack parsersCache = new ArrayStack();

	private static int SIZE_CACHE_BUILDERS = 10;
	private static int SIZE_CACHE_PARSERS = 10;
	private static boolean SAX_VALIDATE = false;
	private static boolean SAX_NAMESPACES = true;
	private static boolean DOM_VALIDATE = false;
	private static boolean DOM_NAMESPACES = true;
	private static boolean DOM_COALESCING = false;
    private static boolean DOM_EXPANDENTITYREFERENCES = true;
    private static boolean CACHE_DEBUG = false;

	private static final int DEF_CACHE_SIZE = 10;
	
	private static final boolean debug = Boolean.getBoolean(JAXP.class.getName() + ".debug");

	static {
	
	    if (debug)
	    {
	        info("****************");
	        info("* Eleusoft JAXP");
	        info("****************");
            
	    }
	    info("* org.eleusoft.xml.jaxp.JAXP - loading config.");
        try
		{
			ClassMetaInfLoader props = new ClassMetaInfLoader(JAXP.class);
			DOM_VALIDATE = props.getBool("dom.validate", DOM_VALIDATE);
			SAX_VALIDATE = props.getBool("sax.validate", SAX_VALIDATE);
			DOM_NAMESPACES = props.getBool("dom.namespaces", DOM_NAMESPACES);
			SAX_NAMESPACES = props.getBool("sax.namespaces", SAX_NAMESPACES);
			DOM_COALESCING = props.getBool("dom.coalescing", DOM_COALESCING);
			DOM_EXPANDENTITYREFERENCES = props.getBool("dom.expandentityreferences", DOM_EXPANDENTITYREFERENCES);
			CACHE_DEBUG = debug ||  props.getBool("cache.debug", false);
			if (CACHE_DEBUG) info("Eleusoft JAXP Cache Debug ON");
			SIZE_CACHE_BUILDERS = props.getInt("dom.cache.size", DEF_CACHE_SIZE);
			SIZE_CACHE_PARSERS = props.getInt("sax.cache.size", DEF_CACHE_SIZE);
			if (SIZE_CACHE_BUILDERS<=0) SIZE_CACHE_BUILDERS = DEF_CACHE_SIZE;
			if (SIZE_CACHE_PARSERS<=0) SIZE_CACHE_PARSERS = DEF_CACHE_SIZE;

			info("* org.eleusoft.xml.jaxp.JAXP - Config loaded.");
            info("* org.eleusoft.xml.jaxp.JAXP - Cache:" + SIZE_CACHE_BUILDERS + " builders and " +  SIZE_CACHE_PARSERS + " parsers");
		}
		catch(java.util.MissingResourceException ex)
		{
			error("JAXP::loadConfiguration() - no properties found.", ex);
			
		}
    	catch(Exception ex)
		{
			error("JAXP::loadConfiguration() - failed to load properties file", ex);
			
		}

	}
	private static void error(String string, Exception ex)
    {
	    System.err.println("eleusoft.JAXP - ERROR - " + string);
	    ex.printStackTrace();
	    
    }
    private static void info(String string)
    {
        if (debug) System.out.println("eleusoft.JAXP - " + string);
        
    }
    private JAXP(){}


    ////////////////////// BEGIN DOM


 	/**
	 * From DocumentBuilderFactory JAVADOC:
	 * An implementation of the DocumentBuilderFactory class
	 * is NOT guaranteed to be thread safe.
	 * It is up to the user application to make sure about the use
	 * of the DocumentBuilderFactory from more than one thread.
	 * Alternatively the application can have one instance of
	 * the DocumentBuilderFactory per thread.
	 * An application can use the same instance of the factory
	 * to obtain one or more instances of the DocumentBuilder
	 * provided the instance of the factory isn't being used
	 * in more than one thread at a time.
	 **/
	private static final DocumentBuilderFactory getFactory()
	{
		 final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 // Specifies that the parser produced by this code
		 // will provide support for XML namespaces.
		 // By default the value of this is set to false
		 factory.setNamespaceAware(DOM_NAMESPACES);
		 // Specifies that the parser produced by this code
		 // will validate documents as they are parsed.
		 // By default the value of this is set to false.
		 factory.setValidating(DOM_VALIDATE);
		 // Specifies that the parser produced by this code
		 // will convert CDATA nodes to Text nodes and
		 // append it to the adjacent (if any) text node.
		 // NOTE: CDATA must stay CDATA
		 factory.setCoalescing(DOM_COALESCING);
		 return factory;
	}
	/**
	 * Serves a DocumentBuilder obtained through javax.xml.parsers API.
	 * The returned  builder can be used from only one thread
	 * for loading multiple documents.
	 * <p>Use this method when you need to load
	 * multiple documents (from the same thread).
	 * @throws ParserConfigurationException when a parser could not be obtained
	 * from {@link javax.xml.parsers.DocumentBuilderFactory#newDocumentBuilder()}
	 * for configuration issues.
	 * From newDocumentBuilder throws ParserConfigurationException  doc:
	 * <p><code>if a DocumentBuilder cannot be created which satisfies the configuration requested.</code>
	 */
	public static final DocumentBuilder newBuilder()
	    throws ParserConfigurationException
	{
		try
		{
			return getFactory().newDocumentBuilder();
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}
	/**
	 * Returns the currently used DOMImplementation class name.
	 */
	public static String getDOMImplementationClassName()
	{
        return getFactory().getClass().toString();
	}

	/**
	 * Creates a new, empty DOM Document.
	 * @return an always not null DOM Document
     */
	public static Document newDocument()
	    throws ParserConfigurationException
    {
        final DocumentBuilder builder = aquireBuilder();
		try
		{
			return builder.newDocument();
		}
		finally
		{
			releaseBuilder(builder);
		}
	}

 	/**
	 * Builds a DOM Document from an InputStream.
	 * @param stream the not null InputStream
	 * @return an always not null DOM Document
	 */
	public static Document getDocument(final InputStream stream)
	    throws IOException, SAXException, ParserConfigurationException
	{
		return getDocument(new InputSource(stream));
 	}
 	/**
	 * Builds a DOM Document from the passed java.lang.String.
	 * @param xml the not null String
	 * @return an always not null DOM Document
	 */
	public static Document getDocument(final String xml)
	    throws IOException, SAXException, ParserConfigurationException
    {
		final Reader is = new StringReader(xml);
		return getDocument(is);
    }
 	/**
	 * Builds a DOM Document from the passed java.net.URL.
	 * @param url the not null URL
	 * @return an always not null DOM Document
	 */
	public static Document getDocument(final URL url)
	    throws IOException, SAXException, ParserConfigurationException
    {
		final InputSource is = new InputSource(url.openStream());
		is.setSystemId(url.toString()); //not toExternalForm that uses ftp:// ??
		return getDocument(is);
    }

 	/**
	 * Builds a DOM Document from a Reader.
     * @param reader the not null Reader
	 * @return an always not null DOM Document
     */
	public static Document getDocument(final Reader reader)
	    throws IOException, SAXException, ParserConfigurationException
	{
		return getDocument(new InputSource(reader));
 	}
 	/**
	 * Builds a DOM Document from a File.
	 * @param file the not null File
	 * @return an always not null DOM Document
	 */
	public static Document getDocument(final File file)
	    throws IOException, SAXException, ParserConfigurationException
	{
		final InputSource is = getInputSource(file);
		return getDocument(is);
 	}

 	/**
	 * Builds a DOM Document from an InputSource.
	 * @param is the not null InputSource
	 * @return an always not null DOM Document
	 */
	public static Document getDocument(final InputSource is)
	    throws IOException, SAXException, ParserConfigurationException
    {
        return getDocument(is, null, null);
	}
	/**
	 * <b>Builds a DOM Document</b> from an InputSource and
	 * optional(s) ErrorHandler and EntityResolver
	 * @param is the not null InputSource
	 * @param resolver the optional EntityResolver
	 * @param errorHandler the optional ErrorHandler
	 * @return an always not null DOM Document
	 */
	public static Document getDocument(final InputSource is,
	    final ErrorHandler errorHandler, final EntityResolver resolver)
	    throws IOException, SAXException, ParserConfigurationException
    {
		final DocumentBuilder builder = aquireBuilder();
		try
		{
		    if (errorHandler!=null) builder.setErrorHandler(errorHandler);
		    if (resolver!=null) builder.setEntityResolver(resolver);
			return builder.parse(is);
		}
		finally
		{
		    // note: in JAXP1.3 is possible to set to null
		    // see http://java.sun.com/j2se/1.5.0/docs/guide/xml/jaxp/JAXP-Compatibility_150.html
		    builder.setErrorHandler(NULL_ERROR_HANDLER);
			builder.setEntityResolver(NULL_ENTITY_RESOLVER);
			releaseBuilder(builder);

		}
 	}

 	///////////////////////// END DOM

 	/**
 	 * Builds an InputSource from a <code>java.io.File</code> using
 	 * {@link File#toURL()} to retrieve the SystemId.
 	 * <p>Note that File.toExternalForm() is not used since it
 	 * can generate urls like <code>ftp://[computername]/..</code>,
 	 * TODO must check why and when.
 	 * @param file the not-null file to build the input source
	 * @return an always not null InputSource
 	 */
 	public static InputSource getInputSource(File file) throws IOException
 	{
 	    InputSource is = new InputSource(new FileInputStream(file));
 	    // Note: toExternalForm on file:// creates a FTP://
		is.setSystemId(file.toURL().toString());
		return is;
 	}

 	///////////////////////// BEGIN SAX

 	/**
	 * Serves a SAXParser obtained through javax.xml.parsers API.
	 * <p>Impl. note:An implementation of the SAXParserFactory class
	 * is NOT guaranteed to be thread safe so a new factory must be created
	 * for each method call.
	 * @throws ParserConfigurationException when a parser could not be obtained
	 * from {@link #newSAXParser()} for configuration issues:
	 *
	 * @throws FactoryConfigurationError when the factory
	 * implementation is not available or cannot
	 * be instantiated (is an {@link Error} subclass)
     * @throws ParserConfigurationException
     * if a parser cannot be created which satisfies the requested configuration
	 */
	public static SAXParser newSAXParser() throws ParserConfigurationException, SAXException
    {
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(SAX_NAMESPACES);
		factory.setValidating(SAX_VALIDATE);
		final SAXParser parser = factory.newSAXParser();
		return parser;
	}
	public static String getSAXImplementationClassName()
	{
	    // TODO here ?? error or null ??
		return SAXParserFactory.newInstance().getClass().getName();
	}

	public static void parse(final File file, final ContentHandler ch)
	    throws SAXException,
	        IOException,
	        ParserConfigurationException
 	{
 		final InputSource is = getInputSource(file);
		parse(is, ch, null, null);
	}
	public static void parse(final Reader reader, final ContentHandler ch)
	    throws SAXException,
	        IOException,
	        ParserConfigurationException
 	{
 		final InputSource is = new InputSource(reader);
		parse(is, ch, null, null);
	}
	public static void parse(final String xml, final ContentHandler ch)
	    throws SAXException,
	        IOException,
	        ParserConfigurationException
 	{
 		final InputSource is = new InputSource(new StringReader(xml));
		parse(is, ch, null, null);
	}
	public static void parse(final URL url, final ContentHandler ch)
	    throws SAXException,
	        IOException,
	        ParserConfigurationException
 	{
 		final InputSource is = new InputSource(url.openStream());
 		// TODO net.URL encodes BAD!!!!! do it wiht URI!!!
 		// NOTE: URL.toString calls toExternalForm (is the same)
 		// Note also that toString of URL is expensive..
 		// is possible to use a cache (not here)
 		// http://www.ja-sig.org/issues/browse/CAS-400
 		is.setSystemId(url.toString());
		parse(is, ch, null, null);
	}

    public static void parse(final InputSource is, final ContentHandler ch)
        throws SAXException,
            IOException,
            ParserConfigurationException
 	{
 		parse(is, ch, null, null);
 	}

	/**
	 * Parses an xml document with SAX, the xml is retrieved from
	 * the passed InputSource, events are risen to the passed
	 * ContentHandler; this version allows configuration of the used XMLReader with
	 * an optionally passed ErrorHandler and EntityResolver.
	 * @param is the not null sax InputSource
	 * @param ch the not null sax ContentHandler
	 * @param resolver the optional EntityResolver
	 * @param errorHandler the optional ErrorHandler
	 * @throws IOException when an I/O error occurs.
	 * @throws SAXException when the parser raised an error during parsing
	 * @throws ParserConfigurationException when a parser could not be obtained
	 * from {@link #newSAXParser()} for configuration issues.
	 */
 	public static void parse(final InputSource is,
 		final ContentHandler ch,
 		final ErrorHandler errorHandler,
 		final EntityResolver resolver)
 		throws SAXException,
 		    IOException,
 		    ParserConfigurationException
 	{
 	    parse(is, ch, errorHandler, resolver, null, null);
 	}
 	/**
	 * Parses an xml document with SAX, the xml is retrieved from
	 * the passed InputSource, events are risen to the passed
	 * ContentHandler and LexicalHandler;
	 * this version allows configuration of the used XMLReader with
	 * an optionally passed ErrorHandler and EntityResolver.
	 * <p>The optional XMLFilter when passed, will be used
	 * as target XMLReader, setting to it as parent the actual parser
	 * instance.
	 * @param is the not null sax InputSource
	 * @param ch the not null sax ContentHandler
	 * @param resolver the optional EntityResolver
	 * @param errorHandler the optional ErrorHandler
	 * @throws IOException when an I/O error occurs.
	 * @throws SAXException when the parser raised an error during parsing
	 * @throws ParserConfigurationException when a parser could not be obtained
	 * from {@link #newSAXParser()} for configuration issues.
	 */
 	public static void parse(final InputSource is,
 		final ContentHandler ch,
 		final ErrorHandler errorHandler,
 		final EntityResolver resolver,
 		final LexicalHandler lexicalHandler,
 		final XMLFilter filter)
 		throws SAXException,
 		    IOException,
 		    ParserConfigurationException
 	{
 		if (ch==null) throw new IllegalArgumentException("Null content handler");
 		final SAXParser parser = aquireParser();
 		try
 		{
 			final XMLReader readerImpl = parser.getXMLReader();
 			//readerImpl.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
 		    final XMLReader reader;
 		    if (filter==null) reader = readerImpl;
 		    else
 		    {
 		        filter.setParent(readerImpl);
 		        reader = filter;
 		    }
			try
			{
			    if (lexicalHandler!=null)
			    {
			        setLexicalHandler(reader, lexicalHandler);
			    }
				reader.setContentHandler(ch);
				if (errorHandler!=null) reader.setErrorHandler(errorHandler);
				if (resolver!=null) reader.setEntityResolver(resolver);
				reader.parse(is);
			}
			finally
			{
				reader.setContentHandler(NULL_CONTENT_HANDLER);
				reader.setErrorHandler(NULL_ERROR_HANDLER);
				reader.setEntityResolver(NULL_ENTITY_RESOLVER);
				if (lexicalHandler!=null)
			    {
			        setLexicalHandler(reader, null);
			    }

			}
		}
		finally
		{
			releaseParser(parser);
		}
 	}
 	private static void setLexicalHandler(final XMLReader reader, final LexicalHandler l)
 	{
 	    try
	    {
	        reader.setProperty("http://xml.org/sax/properties/lexical-handler", l);
	    }
	    catch(SAXException se)
	    {
	        se.printStackTrace();
	    }
 	}
 	private static void releaseBuilder(final DocumentBuilder builder)
	{
		synchronized(buildersCache)
		{
			final int size = buildersCache.size();
			if (size < SIZE_CACHE_BUILDERS)
			{
				buildersCache.push(builder);
				//System.out.println("Builder pushed, now:" + buildersCache.size());

			}
		}
	}

	private static DocumentBuilder aquireBuilder()
	    throws ParserConfigurationException //IOException, org.xml.sax.SAXException
	{
		DocumentBuilder builder = null;
		synchronized(buildersCache)
		{
			if (!buildersCache.empty())
			{
				builder =  (DocumentBuilder)buildersCache.pop();
				if (CACHE_DEBUG) info("Builder acquired from cache, now in cache:" + buildersCache.size());
			}
		    if(builder==null)
		    {
		    	builder = newBuilder();
		    	if(CACHE_DEBUG)info("EJAXP - Builder created because now cache size:" + buildersCache.size());
		    }
		}
		return builder;

	}

	static void releaseParser(final SAXParser parser)
	{
	    synchronized(parsersCache)
		{
			final int size = parsersCache.size();
			if (size < SIZE_CACHE_PARSERS)
			{
				parsersCache.push(parser);
				if (CACHE_DEBUG) info("Parser released in cache, now in cache:" + parsersCache.size());
			}
			else if (CACHE_DEBUG) info("Parser not release in cache, that is over size, now :" + parsersCache.size());
		}
	}

	static SAXParser aquireParser() throws ParserConfigurationException, SAXException
	{
		SAXParser parser = null;
		synchronized(parsersCache)
		{
			if (!parsersCache.empty())
			{
				parser =  (SAXParser)parsersCache.pop();
				if (CACHE_DEBUG) info("Parser acquired from cache, now in cache:" + parsersCache.size());
			}
            
		}
		if(parser==null)
		{
			parser = newSAXParser();
	         if(CACHE_DEBUG)info("EJAXP - SAX Parser created because now cache size:" + buildersCache.size());
	         
		}
		return parser;

	}


}
