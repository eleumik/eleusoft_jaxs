package org.eleusoft.jaxs.saxon;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.eleusoft.jaxs.DOMSerializer;
import org.eleusoft.jaxs.helpers.AbstractDOMSerializer;
import org.w3c.dom.Node;

public class SaxonDOMSerializer extends AbstractDOMSerializer implements
    DOMSerializer
{

    protected void serializeNode(Node node, OutputStream out, String encoding) throws IOException
    {
        // TODO Auto-generated method stub

    }

    protected void serializeNode(Node node, Writer out) throws IOException
    {
        // TODO Auto-generated method stub

    }

}
