eleusoft_jaxs
=============

Java Xml Serialization package 

Details
-------

Xml Serialization or to be more precise, the serialization to XML of Object Models representing an XML Document at runtime can be achieved in different ways with different libraries and sometimes with different results especially for what regards whitespaces including end of line.

The `eleusoft_jaxs` library aim is to create an abstraction to use different implementations with a single interface.


Implementations
---------------

The following implementations are available for "Xml Serialization" of DOM documents and SAX streams:

- Xerces
- TrAX API
- Xalan
- Saxon (SAX only)
- Resin
- DOM 3 (TODO)

Dependencies
------------

The API package depends only on SAX and DOM packages included in JRE. The full list of dependencies can be found in the [`pom.xml`](https://github.com/eleumik/eleusoft_jaxs/blob/master/pom.xml)

Maven
-----

    <dependency>
       <groupId>org.eleusoft</groupId>
       <artifactId>eleusoft_jaxs</artifactId>
       <version>1.0.0</version>
    </dependency>`


Author
------

Michele Vivoda

