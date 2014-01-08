<xsl:stylesheet version='1.0' xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- NOTE: put here only template with name -->
<xsl:output method="html"/>

<!-- 
	helper to print a separator not visible when there the css files are read 
	usage (x copy an paste):
			
			<xsl:call-template name='separator'/>
-->
<xsl:template name='separator'> <span class='nocss'> | </span></xsl:template>
<!-- 
	helper to print a separator for links
	usage (x copy an paste):
			
			<xsl:call-template name='lsep'/>
-->
<xsl:template name='lsep'><span> | </span></xsl:template>
<!-- 
	Call this template inside an <A NAME> element to print dummy comment content.
	In order to work good everywhere a <A NAME='..'> should have something inside,
	like is done in javadoc; the usual choice is a comment with a space inside but
	- 	xalan was/is? giving null pointer printing an empty comment inside <A>
    So I used this <xsl:text disable-output-escaping> but now..
	- 	Mozilla shows the comment when doing browser transformation of xml with xslt.
		When it reads the html file done using xalan it does not display it,
		so probably is broken the mozilla xslt that escapes the comment also if 
		disable-output-escaping is set...mah
	usage (x copy an paste):
			
			<a name='{@name}'><xsl:call-template name='anamecontent'/></a>
--> 
<xsl:template name='anamecontent'><xsl:text disable-output-escaping='yes'>&lt;!-- --></xsl:text></xsl:template>

<!--
	[printHTMLHeadDefaultContent] named template 
	can be used to print some default content for the HTML Head.
	It prints:
	* CSS stylesheet HTML include (default, screen and print)
	* A default style for when there are no stylesheet (files) available
	  Its purpose is also to have defined the DOM-CSS-rule objects 
	  when there are no CSS files, since the 
	  *class reference declaration on an element* is not
	  enough to create a DOM-CSS-rule object for that name: 
	  the style needs also to be defined in a CSS; rule objects are
	  used for toggle-visibility-of-css-class scripting 

	In general the aim of the css designed is to have decent 
	view also on style-less browsers and on printer.
	Special css classes to be possibly used are:
	
	- [noprint] Defined here, use this for items that should 
				not be visible when printing for example 
				interactive buttons (search), the print link etc...

-->
<xsl:template name="printHTMLHeadDefaultContent">

  	<link rel='stylesheet' media='screen' type='text/css' href='reportscreen.css'/>
  	<link rel='stylesheet' media='print' type='text/css' href='reportprint.css'/>
    <link rel='stylesheet' media='all' type='text/css' href='report.css'/>
    <!-- 
        this style is here to create a rule automatically, 
         for toggle visibility otherwise if no css, no rules 
    -->
    <style>
    .divSuccess{} .divInfo{} .divHeader{} .divDocumentationLink{}
    .noerrors{} .title{} .pre{}  .divWarning{}  .divPre{} .olToc{} .stacktrace{}
    </style>
    <style media='print'>
    .noprint
    {
    	display:none;
    }
    </style>
	<!-- script and comment same line -->
    <script><xsl:comment><xsl:text disable-output-escaping="no"><![CDATA[
	/**
	 * toggleClass by Michele Vivoda 2005-2007.
	 * Cross browser method to toggle visibility of a css class by name.
	 * Before using this method one should be sure that the 
	 * passed css class is explicitly defined in a stylesheet,
	 * otherwise there is no css rule object in the page object model.
	 * The safest way is to declare (empty) rules *in the page* as opposed
	 * to external files.
	 * @param className the css class name
	 * @param label UI - the english name of the object whose
	 *			visibility is toggled, used to display a message
	 *			in the status bar like "[lable] are now not visible"
	 * @param atag  the HTML id of the links that trigger the 
	 *			comment 
	 * Example:
	 * <a href="javascript:toggleClass('tdErrorEvent', 'Errors', 'Tg1e')" id="Tg1e">Errors</a>
	 */
	function toggleClass(className, label, atag) {
		//alert(className);
	    for (x=0; x < document.styleSheets.length;x++) {
			var mysheet=document.styleSheets[x];
			var myrules=mysheet.cssRules ? mysheet.cssRules: mysheet.rules;
			var targetrule = null;
			for (i=0; i < myrules.length; i++){
				//find rule .. not case sensitive
				if(myrules[i].selectorText.toLowerCase()==className.toLowerCase()) { 
					targetrule=myrules[i];
					break;
				}
			}
			if (targetrule==null) {
				//alert("Not found " + className + " in " + mysheet.href);
			} else {
				var currdisplay = targetrule.style.display;
				var isNotVisible = currdisplay == "none";
				targetrule.style.display = isNotVisible ? "" : "none";
				if (atag) {
				    if (document.getElementById) {
				        var el = document.getElementById(atag);
				        if (el) {
				        	if (el.length) {
				        		for (c=0;c<el.length;c++) el[c].style.color = isNotVisible ? "" : "silver";
				        	} else {
				        		el.style.color = isNotVisible ? "" : "silver";
							}
						}
				    }
				}
				window.status = label + " are now" + (isNotVisible ? "" : " not") + " visible";
			}
		}
	}
	function exportPage()
	{
		// Writes source code in a new window (IE only). Michele Vivoda 2001
		// 2006 It's absurd that (now?) also if the html of the page is written, 
		// it can't be saved using "Save As.." menu..nothing is written down.
		mywnd = window.open()
		window.setTimeout("mywnd.document.write( document.documentElement.outerHTML);",100)
	}
	window.defaultStatus = "Eleusoft InEO report";
	]]><!-- end script and comment same line --></xsl:text></xsl:comment></script>
</xsl:template >

<!--
	Prints a summary of failure and successes 
	for the contextual <container> element
-->
<xsl:template name="printFailureAndSuccessSummary">
<xsl:param name='complete' select='string("true")'/>  
    <!-- note: very important the [1] -->
    <xsl:variable name='previd' select='preceding-sibling::container[@id][1]/@id'/>
    <xsl:variable name='nextid' select='following-sibling::container[@id][1]/@id'/>
    <xsl:variable name='prevfirstid' select='preceding-sibling::container[@id][last()]/@id'/>
    <xsl:variable name='nextlastid' select='following-sibling::container[@id][last()]/@id'/>
    <xsl:variable name='firstchildid' select='children/container[@id and not(@hidden="true")]/@id'/>
    <xsl:variable name='upid' select='ancestor::container[@id][1]/@id'/>
    
    <div class='summaryDiv'>
<xsl:if test='$complete="true"'>
    
    <xsl:if test='$previd or $nextid or $upid'>
    <span class='vcr noprint'>
    &#160;|  
    <xsl:choose>
    <xsl:when test='$prevfirstid and not($prevfirstid=.)'>
    <a title='First of tests at same level.' class='noprint'>
        <xsl:attribute name='href'>#A<xsl:value-of select='$prevfirstid'/></xsl:attribute>
    &lt;&lt;  
    </a></xsl:when>
    <xsl:otherwise>&#160;&#160;</xsl:otherwise>
    </xsl:choose>
    &#160;|  
    <xsl:choose>
    <xsl:when test='$previd'>
    <a title='Previous test.' class='noprint'>
        <xsl:attribute name='href'>#A<xsl:value-of select='$previd'/></xsl:attribute>
    &lt;  
    </a></xsl:when>
    <xsl:otherwise>&#160;&#160;</xsl:otherwise>
    </xsl:choose>
    &#160;|  
    <xsl:choose>
    <xsl:when test='$upid'>
    <a title='Container test.' class='noprint'><xsl:attribute name='href'>#A<xsl:value-of select='$upid'/></xsl:attribute>
    up
    </a></xsl:when>
    <xsl:otherwise>&#160;&#160;</xsl:otherwise>
    </xsl:choose>
    &#160;| 
    <xsl:choose>
    <xsl:when test='$firstchildid'>
    <a title='First contained test.' class='noprint'><xsl:attribute name='href'>#A<xsl:value-of select='$firstchildid'/></xsl:attribute>
    fc
    </a></xsl:when>
    <xsl:otherwise>&#160;&#160;</xsl:otherwise>
    </xsl:choose>
    &#160;| 
    <xsl:choose>
    <xsl:when test='$nextid'>
    <a title='Next test.' class='noprint'><xsl:attribute name='href'>#A<xsl:value-of select='$nextid'/></xsl:attribute>&gt;
    </a></xsl:when>
    <xsl:otherwise>&#160;&#160;</xsl:otherwise>
    </xsl:choose>
    &#160;|  
    <xsl:choose>
    <xsl:when test='$nextlastid and not($nextlastid=.)'>
    <a title='Last of tests at same level.' class='noprint'>
        <xsl:attribute name='href'>#A<xsl:value-of select='$nextlastid'/></xsl:attribute>
    &gt;&gt;  
    </a></xsl:when>
    <xsl:otherwise>&#160;&#160;</xsl:otherwise>
    </xsl:choose>
     | 
    </span>   
    </xsl:if>
   
   <!-- BEGIN REPORT EVENTS -->
   
   <xsl:variable name='childTests' select='.//container[@testtype="test"]'/>
   
   <xsl:if test='$childTests and not(count($childTests)=1)'>   
    <xsl:if test='$childTests'>
         <span class='mini minitotal'><xsl:value-of select='count($childTests)'/>&#160;Tests</span><xsl:call-template name='separator'/>
    </xsl:if>    
    <xsl:if test='.//container[@testtype="test"][children/event[@type="ErrorEvent" or @type="Failure"]]'>
         <span class='mini minifail'><xsl:value-of select='count(.//container[@testtype="test"][children/event[@type="ErrorEvent" or @type="Failure"]])'/>&#160;Tests&#160;failed</span><xsl:call-template name='separator'/>
    </xsl:if>    
    <xsl:if test='.//container[@testtype="test"][children/event[@type="Warning"]]'>
         <span class='mini miniwarn'><xsl:value-of select='count(.//container[@testtype="test"][children/event[@type="Warning"]])'/>&#160;Tests&#160;with&#160;warnings&#32;</span><xsl:call-template name='separator'/>
    </xsl:if>    
    <xsl:if test='.//container[@testtype="test"][children/event[@type="Warning" or @type="ErrorEvent" or @type="Failure"]]'>
         <span class='mini'><xsl:value-of select='count(.//container[@testtype="test"][children/event[@type="Warning" or @type="ErrorEvent" or @type="Failure"]])'/>&#160;Tests&#160;failed&#160;or&#160;with&#160;warning</span><xsl:call-template name='separator'/>
    </xsl:if>    
    <xsl:if test='.//container[@testtype="test"][not(children/event[@type="Warning" or @type="ErrorEvent" or @type="Failure"])]'>
         <span class='mini minisuccess'><xsl:value-of select='count(.//container[@testtype="test"][children/event][not(children/event[@type="Warning" or @type="ErrorEvent" or @type="Failure"])])'/>&#160;Tests&#160;with&#160;no&#160;warning,&#160;failures&#160;or&#160;errors</span><xsl:call-template name='separator'/>
    </xsl:if>    
   </xsl:if>
</xsl:if> <!-- end complete version of summary -->
   
  <xsl:call-template name='miniSummary'/>  
   
   </div>
</xsl:template>

<xsl:template name='miniSummary'>
 <xsl:if test='.//event[@type="Success" or @type="ErrorEvent" or @type="Failure"]'>
        <span class='mini minitotal'><xsl:value-of select='count(.//event[@type="Success" or @type="ErrorEvent" or @type="Failure"])'/>&#160;Total&#160;Results</span><xsl:call-template name='separator'/>
   </xsl:if>    
   <xsl:if test='.//event[@type="Failure"]'>
        <span class='mini minifail'><xsl:value-of select='count(.//event[@type="Failure"])'/>&#160;Failures</span><xsl:call-template name='separator'/>
   </xsl:if>    
    <xsl:if test='.//event[@type="ErrorEvent"]'>
        <span class='mini minierr'><xsl:value-of select='count(.//event[@type="ErrorEvent"])'/>&#160;Errors</span><xsl:call-template name='separator'/>
   </xsl:if>    
   <xsl:if test='.//event[@type="Success"]'>
        <span class='mini minisuccess'><xsl:value-of select='count(.//event[@type="Success"])'/>&#160;Successes</span><xsl:call-template name='separator'/>
   </xsl:if>    
   
   <xsl:if test='.//event[@type="Warning"]'>
        <span class='mini miniwarn'><xsl:value-of select='count(.//event[@type="Warning"])'/>&#160;Warnings</span><xsl:call-template name='separator'/>
   </xsl:if>  
</xsl:template>
   


</xsl:stylesheet>
