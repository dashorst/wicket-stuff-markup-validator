<!-- File: simpleml-model-1.mod -->

<!-- Declare a Parameter Entity (PE) that defines any external namespaces 
     that are used by this module -->

<!-- Set the PE that is used in every ATTLIST in this module 
     NS.prefixed.attrib is initialized in the xhtml-qname module, and
	 SimpleML.ns.noprefix.attrib is initialized in the SimpleML DTD driver 
	 file.-->
<!ENTITY % SimpleML.xmlns.attrib
  "%NS.decl.attrib;"
>

<!ENTITY % SimpleML.Common.attrib
  "%SimpleML.xmlns.attrib;
   id           ID           #IMPLIED"
>

<!ENTITY % SimpleML.element.qname "%SimpleML.pfx;element" >
<!ENTITY % SimpleML.otherelement.qname "%SimpleML.pfx;otherelement" >

<!ELEMENT %SimpleML.element.qname;
          ( #PCDATA | %SimpleML.otherelement.qname; )* >
<!ATTLIST %SimpleML.element.qname;
          myattribute   CDATA  #IMPLIED
          %SimpleML.Common.attrib;
>
<!ELEMENT %SimpleML.otherelement.qname; EMPTY >
<!ATTLIST %SimpleML.otherelement.qname;
          %SimpleML.Common.attrib;
>

<!ENTITY % SimpleML.img.myattr.qname "%SimpleML.pfx;myattr" >
<!ATTLIST %img.qname;
          %SimpleML.img.myattr.qname;  CDATA  #IMPLIED
>

<!-- Add our elements to the XHTML content model -->
<!ENTITY % Misc.class
     "| %SimpleML.element.qname;" >

<!-- Now bring in the XHTML Basic content model -->
<!ENTITY % xhtml-basic-model.mod
     PUBLIC "-//W3C//ENTITIES XHTML Basic 1.0 Document Model 1.0//EN"
	        "http://www.w3.org/TR/xhtml-basic/xhtml-basic10-model-1.mod" >
%xhtml-basic-model.mod;
