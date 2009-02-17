<!-- File: example.mod -->

<!-- Declare a Parameter Entity (PE) that defines any external namespaces 
     that are used by this module -->

<!ENTITY % MODULE.ns.extras.attrib "" >

<!-- By default, disable prefixing of this module -->

<!ENTITY % MODULE.prefixed "IGNORE" >

<!-- If this module's namespace is prefixed -->

<[%MODULE.prefixed;[
  <!ENTITY % MODULE.pfx  "M:" >
  <!ENTITY % MODULE.sfx  ":M" >
  <!ENTITY % MODULE.ns.prefixed.attrib 
     "xmlns%MODULE.sfx; %URI.datatype; #FIXED 'MODULEns'" >
  <!ENTITY % MODULE.ns.noprefix.attrib '' >
]]>

<!-- If this module's namespace is not prefixed, set the PEs as needed -->

<!ENTITY % MODULE.pfx  "" >
<!ENTITY % MODULE.sfx  "" >
<!ENTITY % MODULE.ns.prefixed.attrib '' >
<!ENTITY % MODULE.ns.noprefix.attrib 
         "xmlns  %URI.datatype; #FIXED 'MODULEns'" >

<!-- Set a value for NS.prefixed.attrib. This is usually overridden by the
     DTD driver.  This will contain the external namespaces used in this
     module and the module namespace declaration if it is prefixed. -->

<!ENTITY % NS.prefixed.attrib
  "%MODULE.ns.prefixed.attrib;
   %MODULE.ns.extras.attrib;"
>

<!-- Set the PE that is used in every ATTLIST in this module -->

<!ENTITY % MODULE.ns.attrib
  "%NS.prefixed.attrib;
   %MODULE.ns.noprefix.attrib;"
>
