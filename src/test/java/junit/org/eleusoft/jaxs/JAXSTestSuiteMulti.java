package junit.org.eleusoft.jaxs;

import junit.framework.Test;
import junit.framework.TestSuite;

public class JAXSTestSuiteMulti extends TestSuite
{

	public static Test suite() throws Exception {
	    JAXSTestSuiteMulti suite = new JAXSTestSuiteMulti();
	    suite.addTestSuite(TestOmitXml.class);
	    suite.addTestSuite(TestSpace.class);
	    suite.addTestSuite(TestSpaces.class);
        suite.addTestSuite(TestTab.class);
        suite.addTestSuite(TestCrLF.class);
        suite.addTestSuite(TestLF.class);
        suite.addTestSuite(TestEntity13.class);
        suite.addTestSuite(TestNoOmitXml.class);
        suite.addTestSuite(TestNoOmitXmlStandaloneNo.class);
        suite.addTestSuite(TestNoOmitXmlStandaloneYes.class);
            

	    return suite;
	}


}