package org.eleusoft.jaxs.trax;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

/**
 * A TraX SAX Serializer that must be configured with a {@link TransformerFactory},
 * intended also for subclassing.
 * 
 *
 */
public class ConfigurableTraXSAXSerializer extends
    AbstractTrAXSAXSerializer
{

    private final TransformerHandler identityTransformerHandler;
    /*
     * (non-Javadoc)
     * @see org.eleusoft.jaxs.trax.AbstractTrAXSAXSerializer#getIdentityTransformerHandler()
     */
    protected final TransformerHandler getIdentityTransformerHandler()
    {
        return identityTransformerHandler;
    }
    /**
     * Constructs a {@link ConfigurableTraXSAXSerializer}
     * configuring it with a {@link TransformerFactory}.
     * @param factory A <em>required</em> {@link TransformerFactory}, never null.
     * @throws IllegalArgumentException if ]factory] is <code>null</code>.
     */
    public ConfigurableTraXSAXSerializer(final TransformerFactory factory)
    {
        
        //System.err.println(this.getClass()+ "konf:"+factory);
        if (factory == null)
            throw new IllegalArgumentException("null factory passed");
        try
        {
            if (factory.getFeature(SAXTransformerFactory.FEATURE))
            {
                SAXTransformerFactory saxFactory = (SAXTransformerFactory) factory;
                this.identityTransformerHandler = saxFactory.newTransformerHandler();

            }
            else
            {
                throw new UnsupportedOperationException("The factory [" + factory.getClass()
                    .getName()
                    + "] does not support feature SAXTransformerFactory ["
                    + SAXTransformerFactory.FEATURE
                    + "]");
            }

        }
        catch (TransformerConfigurationException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize TrAXDOMSerializer, " + "TransformerFactory configuration error:"
                + e.getMessage());
        }
    }

    

   
}