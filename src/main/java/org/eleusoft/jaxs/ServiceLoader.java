package org.eleusoft.jaxs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

class ServiceLoader
{

    public Object getService(Class serviceClass, String[] defaultProviders)
    {
        final ArrayList providers = new ArrayList();

        // Not needed anymore !
        // System.setProperty("org.apache.commons.logging.Log",
        // "org.apache.commons.logging.impl.SimpleLog");
        try
        {
            // for junit
            final String clazz = System.getProperty(serviceClass.getName());
            if (clazz != null) providers.add(clazz);
        }
        catch (SecurityException se)
        {
            // ignore
        }

        final InputStream is = JAXS.class.getResourceAsStream("/META-INF/services/" + serviceClass.getName());
        if (is != null)
        {
            BufferedReader br = null;
            try
            {
                br = new BufferedReader(new InputStreamReader(is));
                String line = br.readLine();
                while (line != null)
                {
                    String tl = line.trim();
                    if (tl.length() == 0 || tl.startsWith("#"))
                    {
                        // skip comment or empty
                    }
                    else
                    {
                        providers.add(tl);
                    }
                    line = br.readLine();
                }
            }
            catch (IOException e)
            {
                // ignore
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    if (br != null)
                        br.close();
                    else
                        is.close();
                }
                catch (IOException e)
                {
                    // ignore.
                }
            }
        }
        for(int i=0,len=defaultProviders.length;i<len;i++)
        {
            providers.add(defaultProviders[i]);
        }
        
        final Iterator i = providers.iterator();
        Object temp = null;
        while (temp == null && i.hasNext())
        {
            String provider = (String) i.next();
            try
            {
                temp = Class.forName(provider).newInstance();
                if (serviceClass.isAssignableFrom(temp.getClass())==false)
                    throw new ClassCastException("Not a " + serviceClass + " but " + temp.getClass());
                break;
            }
            catch (Throwable t)
            {
                t.printStackTrace();

            }
        }

        if (temp == null)
            throw new RuntimeException("No URI provider available");

        return temp;
    }
}