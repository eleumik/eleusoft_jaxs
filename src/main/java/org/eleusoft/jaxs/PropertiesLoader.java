//package com.eleusoft.form.helpers;
package org.eleusoft.jaxs;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Properties;
import java.net.URL;
import java.io.IOException;
import java.io.InputStream;

/**
 * NOTE: copied from com.eleusoft.form.helpers.PropertiesLoader
 */
final class PropertiesLoader
{
    // check http://www.javaworld.com/cgi-bin/mailto/x_java.cgi
    // for evolution..


    public static Properties loadProperties (
        final Class serviceClass, 
        final  Properties props,
        final boolean debug)
	{
	    ClassLoader loader;
		try
		{
			loader = Thread.currentThread().getContextClassLoader();
		}
		catch(Exception e)
		{
			loader = serviceClass.getClassLoader();
		}
		catch(Error e)
		{
			loader = serviceClass.getClassLoader();
		}
		return loadProperties(serviceClass, loader, props, debug);
	}

	public static Properties loadProperties (final Class serviceClass, boolean debug)
	{
	    return loadProperties(serviceClass, new Properties(), debug);
    }
    public static Properties loadProperties (final Class serviceClass,
        final ClassLoader loader, boolean debug)
    {
        return loadProperties(serviceClass, loader, new Properties(), debug);
    }
    /**
     * Gets the properties for the passed class.
     * All the files with as name the Class.getName() value
     * is searched in the class path under the
     * META-INF/services/ path.
     * If at least one file with at least one property
     * is found, an Hashtable of properties is returned,
     * otherwise null is returned.
     * @param map a map of default values.
     */
    public static Properties loadProperties (final Class serviceClass,
        final ClassLoader loader, final Properties defsmap, boolean debug)
	{
	    if (serviceClass==null) throw new IllegalArgumentException("service class is null");
	    if (defsmap==null) throw new IllegalArgumentException("map is null");
	    if (loader==null) throw new IllegalArgumentException("loader is null");
	    final String service = serviceClass.getName();
		Enumeration resEnum;
		final String resURL = "META-INF/services/" + service;

		try
		{
			resEnum = loader.getResources(resURL); // java 1.2
		}
		catch(final Error e) // final paranoia
		{
		    final Object res = loader.getResource(resURL); // java 1.1
		    if (res!=null)
		    {
		        final Vector vector = new Vector();
		        vector.add(res);
			    resEnum = vector.elements();
			}
			else resEnum = null;
		}
		catch(final IOException e)
		{
			resEnum = null;
		}
		final Properties map = new Properties();

		if (resEnum!=null)
		{
		    final Properties worker = new Properties();

			while(resEnum.hasMoreElements())
			{
				InputStream is = null; // out-of-block for finally
				URL url = null; // out-of-block for exception handling
				try
				{
					url = (URL)resEnum.nextElement();
					if (debug)info("load config at " + url);
					is = url.openStream();
					worker.load(is);
					final Enumeration names = worker.propertyNames();
					while(names.hasMoreElements())
					{
					    final String name = (String)names.nextElement();
					    final String value = worker.getProperty(name);
						if (!map.containsKey(name))
                        {						
						    if (debug)info("Put in [" + name +"] value:" + value);
						    map.put(name, value);
						}
						
					}
				}
				catch(final IOException ioe)
				{
				    log("Error loading props at " + url);
				    ioe.printStackTrace();
				}
				finally
				{
				    if (is!=null) try
				    {
				        is.close();
				    }
				    catch(final IOException ioe)
				    {
				        log("Error closing stream of props at " + url);
				        ioe.printStackTrace();
				    }
				}

			}
			defsmap.putAll(map);
			final int size = defsmap.size();
			if (size==0)
			{
				if (debug)info("No properties for service:" +  service);
				return null;
		    }
		    else return defsmap;


		}
		else
		{
		    if (debug)info("No property files for service [" +  service + "]");
		    return null;
	}
	}

    private static final void log(final String msg)
    {
        System.err.println("* PropLoader:" + msg);
    }
    private static final void info(final String msg)
    {
         System.out.println("* PropLoader:" + msg);
    }
    

	/**
	 * Helper inner class of {@link PropertiesLoader}
	 * used to obtain instances of services providers.
	 */
	public static final class ProviderLoader
	{
	    private boolean reuseRefs = true;
        private boolean checkInstanceOf = true;
        // TODO not used ??
	    private ClassLoader loader;
	    private final Class serviceClass;
	    private final Vector results;
	    private final StringBuffer error = new StringBuffer();
	    private final Hashtable classNameToResultMap;
        private final boolean debug;

	    public ProviderLoader(final Class serviceClass,
	        final boolean checkInstanceOf, final boolean debug)
	    {
	        this(serviceClass, debug);
	        this.checkInstanceOf = checkInstanceOf;
	    }

	    public ProviderLoader(final Class serviceClass, final boolean debug)
	    {
	        if (serviceClass==null) throw new IllegalArgumentException("service class is null");
	        this.debug = debug;
	        try
		    {
		    	loader = Thread.currentThread().getContextClassLoader();
		    }
		    catch(Error e)
		    {
		    	loader = serviceClass.getClassLoader();
		    }
		    this.serviceClass = serviceClass;
		    results = new Vector();
		    classNameToResultMap = new Hashtable();
	    }
	    public String getError()
	    {
	        return error.toString();
	    }
	    private final void log(final String msg)
	    {
	        System.err.println("* PropLoader:" + msg);
	        error.append(msg);
	        error.append('\n');
	    }

	    /**
	     * Returns the loaded instances or
	     * null if no instances loaded.
	     * All the results are assured to be instances of the
	     * Class passed in the constructor.
	     */
	    public Vector getResults()
	    {
	        return (results.size()==0) ? null : results;
	    }
	   /**
	     * Returns whether there is
	     * at least one result.
	     */
	    public boolean hasResults()
	    {
	        return (results.size()>0);
	    }

	    /**
	     * Sets whether passed classes that are
	     * already loaded have a new instance created
	     * (pass false) or reuse previously created instances
	     * (pass true [default]).
	     */
	    public void setReuseReferences(final boolean how)
	    {
	        reuseRefs = how;
	    }
	    /**
	     * Loads a class instance and adds it to the results.
	     * By default if a class name has already been passed
	     * the same instance will be returned. Use setReuseReferences(false)
	     * to have a new instance created each time.
	     * @return An instance of the passed clazz, that must be
	     *  castable to the service class passed in the constructor.
	     *
	     */
	    public Object load(final String clazz)
	    {
	        error.setLength(0);
	        if (clazz.trim().length()!=0)
		    {
		        Throwable error = null;
		    	if (reuseRefs)
		    	{
		    	    final Object test = classNameToResultMap.get(clazz);
		    	    if (test!=null) return test;
		    	}
		    	try
		    	{
		    		//log("loading:" + clazz);
		    	    final Object obj =
		    			serviceClass.getClassLoader().loadClass(clazz).newInstance();
		    		if (!checkInstanceOf || serviceClass.isInstance(obj))
		    		{
		    			results.add(obj);
		    			classNameToResultMap.put(clazz, obj);
		    			if (debug)info("loaded:" + clazz);
		    			return obj;
		    		}
		    		else log(clazz + " [not an instanceof] " + serviceClass.getName());
		    	}
		    	catch (ClassNotFoundException e) { error =e;}
		    	catch (ClassCastException e) {error =e; }
		    	catch (InstantiationException e) { error =e;}
		    	catch (IllegalAccessException e) { error =e;}
		    	catch (LinkageError e) { error =e;}
		    	log("error for:" + clazz);
		    	if (error!=null)
		    	{
		    	    log("Exception message:" + error.getMessage());
		    	    log("Exception trace:" + printStackTrace(error));
		    	    error.printStackTrace();
		    	}

		    }
		    return null;
		}
	}

	private static String printStackTrace(final Throwable e)
	{
	    final java.io.StringWriter sw = new java.io.StringWriter();
	    final java.io.PrintWriter p=new java.io.PrintWriter(sw);
        e.printStackTrace(p);
		//e.printStackTrace();
		p.flush();
		return sw.toString();

	}
}
