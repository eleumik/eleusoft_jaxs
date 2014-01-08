package org.eleusoft.jaxs.trax;

import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.eleusoft.jaxs.helpers.AbstractSAXSerializer;
import org.xml.sax.ContentHandler;

public abstract class AbstractTrAXSAXSerializer extends AbstractSAXSerializer
{

    /**
     * Subclasses only constructor.
     */
    protected AbstractTrAXSAXSerializer()
    {
        super();
    }
    /**
     * Returns the identity {@link TransformerHandler} for this
     * {@link AbstractTrAXSAXSerializer} subclass.
     * @return A {@link TransformerHandler}, never null.
     */
    protected abstract TransformerHandler getIdentityTransformerHandler();
    
    /*
     * (non-Javadoc)
     * @see org.eleusoft.jaxs.SAXSerializer#asContentHandler()
     */
    public final ContentHandler asContentHandler()
    {
        final TransformerHandler identityTransformerHandler = 
            getIdentityTransformerHandler();
        TrAXSerializerUtil.configure(identityTransformerHandler.getTransformer(),
            this);
        final StreamResult result = new StreamResult();
        final java.io.Writer w = getWriter();
        if (w != null)
        {
            result.setWriter(w);
        }
        else
        {
            java.io.OutputStream os = getOutputStream();
            if (os == null)
                throw new IllegalStateException("No output object set (writer or outputstream)");

            // writer = new OutputStreamWriter(os, encoding);
            if (os != null)
            {
                result.setOutputStream(os);

            }
        }
        identityTransformerHandler.setResult(result);
        return identityTransformerHandler;
    }

}