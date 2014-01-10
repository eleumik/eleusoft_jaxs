/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package org.eleusoft.jaxs.helpers;

import java.io.IOException;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

//import javax.xml.XMLConstants;


/** <p>A simple serializer for XML documents.</p>
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."

 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
class XMLWriter implements ContentHandler {
  
  private Writer w;
  private Locator l;
  private java.util.Map delayedPrefixes;
  int curIndent = 0;

  private static final int STATE_OUTSIDE = 0;
  private static final int STATE_IN_START_ELEMENT = 1;
  private static final int STATE_IN_ELEMENT = 2;
  private int state;
  
  boolean indent = true;
  String INDENT_SEPARATOR = "\n";
  String INDENT_STRING = "  ";

  /** Creates a new XMLWriter */
  public XMLWriter() { w = null;}

  

  /** <p>Sets the XMLWriter Writer.</p>
   */
  public void setWriter(Writer pWriter)  {
    w = pWriter;
  }
  /** <p>Returns the XMLWriter Writer.</p>
   */
  public Writer getWriter() {
    return w;
  }

  /** Sets the locator.
   *
   * @param pLocator A locator for use in case of errors
   * @see #getDocumentLocator
   */
  public void setDocumentLocator(Locator pLocator) { l = pLocator; }

  /** Returns the locator
   * @return A locator previously set with setDocumentLocator or null.
   * @see #setDocumentLocator
   */
  public Locator getDocumentLocator() { return l; }

  /**
   * <p>Starts use of a namespace prefix.</p>
   *
   * @param namespaceURI The namespace URI
   * @param prefix The prefix
   * @throws SAXException Not actually thrown, just for compliance to the interface specification.
   */
  public void startPrefixMapping(String prefix, String namespaceURI)
      throws SAXException {
    if (delayedPrefixes == null) {
      delayedPrefixes = new java.util.HashMap();
    }
    if ("".equals(prefix)) {
      if (namespaceURI.equals(prefix)) {
        return;
      }
      prefix = XMLConstants.XMLNS_ATTRIBUTE;
    } else {
      prefix = XMLConstants.XMLNS_ATTRIBUTE + ":" + prefix;
    }
    delayedPrefixes.put(prefix, namespaceURI);
  }

  /** <p>Terminates use of a namespace prefix.</p>
   *
   * @param prefix The prefix being abandoned.
   * @throws SAXException Not actually thrown, just for compliance to the interface specification.
   */
  public void endPrefixMapping(String prefix) throws SAXException {
    if (delayedPrefixes != null) {
      if ("".equals(prefix)) {
        prefix = XMLConstants.XMLNS_ATTRIBUTE;
      } else {
        prefix = XMLConstants.XMLNS_ATTRIBUTE + ":" + prefix;
      }
      delayedPrefixes.remove(prefix);
    }
  }

  /** <p>Starts a document.</p>
   * @throws SAXException Not actually thrown, just for compliance to the interface specification.
   */
  public void startDocument() throws SAXException {
    if (delayedPrefixes != null) {
      delayedPrefixes.clear();
    }
    state = STATE_OUTSIDE;
    curIndent = 0;
  }

  /** <p>This method finishs the handlers action. After calling endDocument you
   * may start a new action by calling startDocument again.</p>
   *
   * @throws SAXException Not actually thrown, just for compliance to the
   *   interface specification.
   */  
  public void endDocument() throws SAXException {}

  /** Calls the character method with the same arguments.
   * @param ch A string of whitespace characters being inserted into the document.
   * @param start The index of the first character.
   * @param length The number of characters.
   * @throws SAXException Thrown in case of an IOException.
   */  
  public void ignorableWhitespace(char[] ch, int start, int length)
      throws SAXException {
    characters(ch, start, length);
  }

  private void stopTerminator() throws java.io.IOException {
    if (state == STATE_IN_START_ELEMENT) {
      if (w != null) {
        w.write('>');
      }
      state = STATE_IN_ELEMENT;
    }
  }

  /** Inserts a string of characters into the document.
   * @param ch The characters being inserted. A substring, to be precise.
   * @param start Index of the first character
   * @param length Number of characters being inserted
   * @throws SAXException Thrown in case of an IOException
   */  
  public void characters(char[] ch, int start, int length) throws SAXException {
    try {
      stopTerminator();
      if (w == null) return;
      int end = start+length;
      for (int i = start;  i < end;  i++) {
        char c = ch[i];
        switch (c) {
          case '&':  w.write("&amp;"); break;
          case '<':  w.write("&lt;");  break;
          case '>':  w.write("&gt;");  break;
          case '\n':
          case '\r':
          case '\t':
             w.write(c); break;
          default:
            if (canEncode(c)) {
              w.write(c);
            } else {
              w.write("&#");
              w.write(Integer.toString(c));
              w.write(";");
            }
            break;
        }
      }
    } catch (IOException e) {
      throw new SAXException(e);
    }
  }

  public boolean canEncode(char c) {
    return true; //c > 31  &&  c < 127;
  }


  /** <p>Terminates an element.</p>
   *
   * @param namespaceURI The namespace URI, if any, or null
   * @param localName The local name, without prefix, or null
   * @param qName The qualified name, including a prefix, or null
   * @throws SAXException Thrown in case of an IOException.
   */
  public void endElement(String namespaceURI, String localName, String qName)
      throws SAXException {
    if (indent) {
      --curIndent;
    }
    if (w != null) {
      try {
        if (state == STATE_IN_START_ELEMENT) {
          w.write("/>");
          state = STATE_OUTSIDE;
        } else {
          if (state == STATE_OUTSIDE) {
            indentMe();
          }
          w.write("</");
          w.write(qName);
          w.write('>');
        }
        state = STATE_OUTSIDE;
      } catch (java.io.IOException e) {
        throw new SAXException(e);
      }
    }
  }

  private void indentMe() throws java.io.IOException {
    if (w != null) {
      if (indent) {
        String s = INDENT_SEPARATOR;
        if (s != null) {
          w.write(s);
        }
        s = INDENT_STRING;
        for (int i = 0;  i < curIndent;  i++) {
          w.write(s);
        }
      }
    }
  }

  private void writeCData(String v) throws java.io.IOException {
    int len = v.length();
    for (int j = 0;  j < len;  j++) {
      char c = v.charAt(j);
      switch (c) {
        case '&':  w.write("&amp;");  break;
        case '<':  w.write("&lt;");   break;
        case '>':  w.write("&gt;");   break;
        case '\'': w.write("&apos;"); break;
        case '"':  w.write("&quot;"); break;
        default:
          if (canEncode(c)) {
            w.write(c);
          } else {
            w.write("&#");
            w.write(Integer.toString(c));
            w.write(';');
          }
          break;
      }
    }
  }

  /** Starts a new element.
   *
   * @param namespaceURI The namespace URI, if any, or null
   * @param localName The local name, without prefix, or null
   * @param qName The qualified name, including a prefix, or null
   * @param attr The element attributes
   * @throws SAXException Thrown in case of an IOException.
   */
  public void startElement(String namespaceURI, String localName, String qName,
                           Attributes attr) throws SAXException {
    try {
      stopTerminator();
      if (indent) {
        if (curIndent > 0) {
          indentMe();
        }
        curIndent++;
      }

      if (w != null) {
        w.write('<');
        w.write(qName);
        if (attr != null) {
          for (int i = attr.getLength();  i > 0;) {
            w.write(' ');
            String name = attr.getQName(--i);
            w.write(name);
            if (delayedPrefixes != null) {
              delayedPrefixes.remove(name);
            }
            w.write("=\"");
            writeCData(attr.getValue(i));
            w.write('"');
          }
        }
        if (delayedPrefixes != null  &&  delayedPrefixes.size() > 0) {
          for (java.util.Iterator iter = delayedPrefixes.entrySet().iterator();
               iter.hasNext();  ) {
            java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
            w.write(' ');
            w.write((String) entry.getKey());
            w.write("=\"");
            w.write((String) entry.getValue());
            w.write('"');
          }
          delayedPrefixes.clear();
        }
      }
      state = STATE_IN_START_ELEMENT;
    } catch (java.io.IOException e) {
      throw new SAXException(e);
    }
  }

  /** Not actually implemented, because I don't know how to skip entities.
   *
   * @param ent The entity being skipped.
   * @throws SAXException Not actually thrown, just for compliance to the interface specification.
   */  
  public void skippedEntity(String ent) throws SAXException {
    throw new SAXException("Don't know how to skip entities");
  }
    
  /** Inserts a processing instruction.
   *
   * @param target The PI target
   * @param data The PI data
   * @throws SAXException Thrown in case of an IOException
   */  
  public void processingInstruction(String target, String data)
      throws SAXException {
    try {
      stopTerminator();
      if (w != null) {
        w.write("<?");
        w.write(target);
        w.write(' ');
        w.write(data);
        w.write("?>");
      }
    } catch (java.io.IOException e) {
      throw new SAXException(e);
    }
  }
}
