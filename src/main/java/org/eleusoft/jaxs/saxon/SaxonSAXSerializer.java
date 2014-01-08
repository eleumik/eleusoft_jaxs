package org.eleusoft.jaxs.saxon;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.TransformerHandler;

import net.sf.saxon.Configuration;

import org.eleusoft.jaxs.trax.AbstractTrAXSAXSerializer;
import org.eleusoft.jaxs.trax.ConfigurableTraXSAXSerializer;

/** 
 * Serializer for SAX events based on SAXON.
 * Not thread-safe.
 * 
 * FIXME not working!!!!
 **/
class SaxonSAXSerializer extends AbstractTrAXSAXSerializer
{

    private TransformerHandler t;

    public SaxonSAXSerializer() 
    {
        try
        {
            this.t = new net.sf.saxon.TransformerFactoryImpl(new Configuration()).newTransformerHandler();
        }
        catch (TransformerConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get an identity TransformerHandler" +
                "from [new net.sf.saxon.TransformerFactoryImpl().newTransformerHandler()],"+
                "message:" +e.getMessage());
        }
    }
    protected TransformerHandler getIdentityTransformerHandler()
    {
        return t;
    }
    
    

    
    

}
