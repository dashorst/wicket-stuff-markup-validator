<!-- ...................................................................... -->
<!-- Inventory Qname Module ................................................... -->
<!-- file: inventory-qname-1.mod

	 PUBLIC "-//MY COMPANY//ELEMENTS XHTML Inventory Qnames 1.0//EN"
	 SYSTEM "http://www.example.com/DTDs/inventory-qname-1.mod"

	 xmlns:inventory="http://www.example.com/xmlns/inventory"
     ...................................................................... -->

<!-- Declare the default value for prefixing of this module's elements -->
<!-- Note that the NS.prefixed will get overridden by the XHTML Framework or
     by a document instance. -->
<!ENTITY % NS.prefixed "IGNORE" >
<!ENTITY % Inventory.prefixed "%NS.prefixed;" >

<!-- Declare the actual namespace of this module -->
<!ENTITY % Inventory.xmlns "http://www.example.com/xmlns/inventory" >

<!-- Declare the default prefix for this module -->
<!ENTITY % Inventory.prefix "inventory" >

<!-- Declare the prefix for this module -->
<![%Inventory.prefixed;[
<!ENTITY % Inventory.pfx "%Inventory.prefix;:" >
]]>
<!ENTITY % Inventory.pfx "" >

<!-- Declare the xml namespace attribute for this module -->
<![%Inventory.prefixed;[
<!ENTITY % Inventory.xmlns.extra.attrib
	"xmlns:%Inventory.prefix;	%URI.datatype;	#FIXED	'%Inventory.xmlns;'" >
]]>
<!ENTITY % Inventory.xmlns.extra.attrib "" >

<!-- Declare the extra namespace that should be included in the XHTML
     elements -->
<!ENTITY % XHTML.xmlns.extra.attrib
	%Inventory.xmlns.extra.attrib; >

<!-- Now declare the qualified names for all of the elements in the
     module -->
<!ENTITY % Inventory.shelf.qname "%Inventory.pfx;shelf" >
<!ENTITY % Inventory.item.qname "%Inventory.pfx;item" >
<!ENTITY % Inventory.desc.qname "%Inventory.pfx;desc" >
<!ENTITY % Inventory.sku.qname "%Inventory.pfx;sku" >
<!ENTITY % Inventory.price.qname "%Inventory.pfx;price" >
