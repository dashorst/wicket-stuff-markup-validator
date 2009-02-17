<!-- file: myml-qname-1.mod -->

<!-- Bring in the datatypes - we use the URI.datatype PE for declaring the
     xmlns attributes. -->
<!ENTITY % MyML-datatypes.mod
         PUBLIC "-//W3C//ENTITIES XHTML Datatypes 1.0//EN"
		 "http://www.w3.org/TR/xhtml-modularization/DTD/xhtml-datatypes-1.mod" >
%MyML-datatypes.mod;

<!-- By default, disable prefixing of this module -->
<!ENTITY % NS.prefixed "IGNORE" >
<!ENTITY % MyML.prefixed "%NS.prefixed;" >

<!-- Declare the actual namespace of this module -->
<!ENTITY % MyML.xmlns "http://www.example.com/xmlns/myml" >

<!-- Declare the default prefix for this module -->
<!ENTITY % MyML.prefix "myml" >

<!-- If this module's namespace is prefixed -->
<![%MyML.prefixed;[
  <!ENTITY % MyML.pfx  "%MyML.prefix;:" >
]]>
<!ENTITY % MyML.pfx  "" >

<!-- Declare a Parameter Entity (PE) that defines any external namespaces 
     that are used by this module -->
<!ENTITY % MyML.xmlns.extra.attrib "" >

<!-- Declare a PE that defines the xmlns attributes for use by MyML. -->
<![%MyML.prefixed;[
<!ENTITY % MyML.xmlns.attrib
   "xmlns:%MyML.prefix;  %URI.datatype;  #FIXED '%MyML.xmlns;'
   %MyML.xmlns.extra.attrib;"
>
]]>
<!ENTITY % MyML.xmlns.attrib
   "xmlns	%URI.datatype;	#FIXED '%MyML.xmlns;'
   	%MyML.xmlns.extra.attrib;"
>

<!-- Make sure that the MyML namespace attributes are included on the XHTML
     attribute set -->
<![%NS.prefixed;[
<!ENTITY % XHTML.xmlns.extra.attrib
	"%MyML.xmlns.attrib;" >
]]>
<!ENTITY % XHTML.xmlns.extra.attrib
	""
>
<!-- Now declare the element names -->

<!ENTITY % MyML.myelement.qname "%MyML.pfx;myelement" >
<!ENTITY % MyML.myotherelement.qname "%MyML.pfx;myotherelement" >
