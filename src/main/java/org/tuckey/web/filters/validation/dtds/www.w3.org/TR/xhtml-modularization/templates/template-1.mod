<!-- ...................................................................... -->
<!-- MODULE Extension Template Module ....................................... -->
<!-- file: template-1.mod

     This is an extension of XHTML, a reformulation of HTML as
     a modular XML application.

     The Extensible Hypertext Markup Language (XHTML)
     Copyright 1998-2000 W3C (MIT, INRIA, Keio), All Rights Reserved.
     Revision: $Id: template-1.mod,v 3.0 2000/10/22 17:13:38 altheim Exp $ SMI

     This DTD module is identified by the PUBLIC and SYSTEM identifiers:

       PUBLIC "-//W3C//ENTITIES MODULE Extension Template 1.0//EN"
       SYSTEM "template-1.mod"

     Revisions:
     (none)
     ....................................................................... -->

<!-- Extension Template

     [This sample template module declares two extension element
     types, <front> and <back>. The parameter entity naming
     convention uses uppercase for the entity name and lowercase
     for namespace prefixes, hence this example uses 'MODULE' and
     'mod' respectively.]

     This module declares parameter entities used to provide
     namespace-qualified names for all MODULE element types,
     as well as an extensible framework for attribute-based
     namespace declarations on all element types.

     The %NS.prefixed; conditional section keyword must be
     declared as "INCLUDE" in order to allow prefixing to be used.
     By default, foreign (i.e., non-XHTML) namespace modules should
     inherit %NS.prefixed; from XHTML, but this can be overridden
     when prefixing of only the non-XHTML markup is desired.

     XHTML's default value for the 'namespace prefix' is an empty
     string. The Prefix value can be redeclared either in a DTD
     driver or in a document's internal subset as appropriate.

     NOTE: As specified in [XMLNAMES], the namespace prefix serves as
     a proxy for the URI reference, and is not in itself significant.
-->

<!-- ......................................................................  -->

<!-- 1. Declare the xmlns attributes used by MODULE dependent on whether
        MODULE's prefixing is active. This should be used on all MODULE
        element types as part of MODULE's common attributes.

        If the entire DTD is namespace-prefixed, MODULE should inherit
        %NS.decl.attrib;, otherwise it should declare %NS.decl.attrib;
        plus a default xmlns attribute on its own element types.
-->
<![%MODULE.prefixed;[
<!ENTITY % MODULE.xmlns.attrib
     "%NS.decl.attrib;"
>
]]>
<!ENTITY % MODULE.xmlns.attrib
     "xmlns        %URI.datatype;           #FIXED '%MODULE.xmlns;'"
>

<!-- now include the module's various markup declarations ........ -->

<!ENTITY % MODULE.Common.attrib
     "%MODULE.xmlns.attrib;
      id           ID                       #IMPLIED"
>

<!-- 2. In the attribute list for each element, declare the XML Namespace
        declarations that are legal in the document instance by including
        the %NamespaceDecl.attrib; parameter entity in the ATTLIST of
        each element type.
-->

<!ENTITY % MODULE.front.qname  "front" >
<!ELEMENT %MODULE.front.qname;  ( %Flow.mix; )* >
<!ATTLIST %MODULE.front.qname;
      %MODULE.Common.attrib;
>

<!ENTITY % MODULE.back.qname  "back" >
<!ELEMENT %MODULE.back.qname;  ( %Flow.mix; )* >
<!ATTLIST %MODULE.back.qname;
      %MODULE.Common.attrib;
>

<!-- 3. If the module adds attributes to elements defined in modules that
        do not share the namespace of this module, declare those attributes
        so that they use the %MODULE.pfx; prefix. For example:

        This would add an attribute to the img element of the Image Module, but
        the attribute's name will be the qualified name, including prefix, when
        prefixes are selected for a document instance.
-->
<!ENTITY % MODULE.img.myattr.qname "%MODULE.pfx;myattr" >
<!ATTLIST %img.qname;
      %MODULE.img.myattr.qname;  CDATA      #IMPLIED
>

<!-- end of template-1.mod -->
