<!-- ...................................................................... -->
<!-- My Elements Module ................................................... -->
<!-- file: myml-elements-1_0.mod

	 PUBLIC "-//MY COMPANY//ELEMENTS XHTML MyML Elements 1.0//EN"
	 SYSTEM "http://example.com/DTDs/myml-elements-1_0.mod"

	 xmlns:myml="http://example.com/DTDs/myml-1_0.dtd"
     ...................................................................... -->

<!-- My Elements Module

     myelement
     myotherelement

     This module has no purpose other than to provide structure for some
	 PCDATA content.
-->

<!ELEMENT %MyML.myelement.qname;
     ( #PCDATA | %MyML.myotherelement.qname; )* >
<!ATTLIST %MyML.myelement.qname;
     myattribute	CDATA	#IMPLIED
     %MyML.xmlns.attrib;
>

<!ELEMENT %MyML.myotherelement.qname; EMPTY >
<!ATTLIST %MyML.myotherelement.qname;
      %MyML.xmlns.attrib;
>

<!ENTITY % MyML.img.myattr.qname "%MyML.pfx;myattr" >
<!ATTLIST %img.qname;
      %MyML.img.myattr.qname;   CDATA  #IMPLIED
      %MyML.xmlns.attrib;
>

<!-- end of myml-elements-1_0.mod -->
