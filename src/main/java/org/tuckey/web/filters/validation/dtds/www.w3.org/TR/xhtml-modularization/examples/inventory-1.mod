<!-- ...................................................................... -->
<!-- Inventory Elements Module ................................................... -->
<!-- file: inventory-1.mod

	 PUBLIC "-//MY COMPANY//ELEMENTS XHTML Inventory Elements 1.0//EN"
	 SYSTEM "http://www.example.com/DTDs/inventory-1.mod"

	 xmlns:inventory="http://www.example.com/xmlns/inventory"
     ...................................................................... -->

<!-- Inventory Module

     shelf
        item
	   sku
	   desc
	   price

     This module defines a simple inventory item structure
-->

<!-- Define the global namespace attributes -->
<![%Inventory.prefixed;[
<!ENTITY % Inventory.xmlns.attrib
    "%NS.decl.attrib;"
>
]]>
<!ENTITY % Inventory.xmlns.attrib
	 "xmlns	%URI.datatype;	#FIXED '%Inventory.xmlns;'"
>

<!-- Define a common set of attributes for all module elements -->
<!ENTITY % Inventory.Common.attrib
         "%Inventory.xmlns.attrib;
	  id               ID                   #IMPLIED
>

<!-- Define the elements and attributes of the module -->
<!ELEMENT %Inventory.shelf.qname;
     ( %Inventory.item.qname; )* >
<!ATTLIST %Inventory.shelf.qname;
     location	CDATA	#IMPLIED
     %Inventory.Common.attrib;
>
<!ELEMENT %Inventory.item.qname;
     ( %Inventory.desc.qname;, %Inventory.sku.qname;, %Inventory.price.qname;) >
<!ATTLIST %Inventory.item.qname;
     location	CDATA	#IMPLIED
     %Inventory.Common.attrib;
>

<!ELEMENT %Inventory.desc.qname; ( #PCDATA ) >
<!ATTLIST %Inventory.desc.qname;
     %Inventory.Common.attrib;
>

<!ELEMENT %Inventory.sku.qname; ( #PCDATA ) >
<!ATTLIST %Inventory.sku.qname;
     %Inventory.Common.attrib;
>

<!ELEMENT %Inventory.price.qname; ( #PCDATA ) >
<!ATTLIST %Inventory.price.qname;
     %Inventory.Common.attrib;
>

<!-- end of inventory-1.mod -->
