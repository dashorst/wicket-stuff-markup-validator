<!-- ....................................................................... -->
<!-- Module Qualified Names Template Module .................................. -->
<!-- file: template-qname-1.mod

     This is an extension of XHTML, a reformulation of HTML as
     a modular XML application.

     The Extensible Hypertext Markup Language (XHTML)
     Copyright 1998-2000 W3C (MIT, INRIA, Keio), All Rights Reserved.
     Revision: $Id: template-qname-1.mod,v 3.0 2000/10/22 17:13:38 altheim Exp $

     This DTD module is identified by the PUBLIC and SYSTEM identifiers:

       PUBLIC "-//W3C//ELEMENTS Module Qualified Names Template 1.0//EN"
       SYSTEM "template-qname-1.mod"

     Revisions:
     (none)
     ....................................................................... -->

<!-- NOTES:  Using the Module Qualified Names Extension Template

     This is a template module for a fictitous markup module 'Module',
     which currently declares two extension element types, <front>
     and <back>. The parameter entity naming convention uses uppercase
     for the entity name and lowercase for namespace prefixes, hence
     this example uses 'MODULE' and 'module' respectively. 

     This template scheme can accommodate the inclusion of more than
     one namespace by copying and modifying this template for each
     included namespace, and aggregating each template's namespace
     declarations via the %NS.decl.attrib; parameter entity in XHTML.

     Please note the three case variants:

         'Module'   the human-readable markup language name
         'MODULE'   used as a parameter entity name prefix
         'module'   used as the default namespace prefix

     To use this module as a template, replace the three names above
     with suitable ones for your language, modifying the template as
     necessary, then declare it as '%xhtml-qname-extra.mod;', prior
     to where it is instantiated in Section A of the XHTML Qualified
     Names module.

     The %MODULE.prefixed or the %NS.prefixed; conditional section 
	 keyword must be declared as "INCLUDE" in order to allow prefixing be 
	 used for this module.
-->

<!-- ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: -->

<!-- Module Qualified Names

     This module is contained in two parts, labeled Section 'A' and 'B':

       Section A declares parameter entities to support namespace-
       qualified names, namespace declarations, and name prefixing
       for Module.

       Section B declares parameter entities used to provide
       namespace-qualified names for all Module element types.

     The recommended step-by-step programme for creating conforming
     modules is enumerated below, and spans both the Module Qualified
     Names Template and Module Extension Template modules.
-->

<!-- Section A: Module XML Namespace Framework :::::::::::::::::::: -->

<!-- 1. Declare a %MODULE.prefixed; conditional section keyword, used
        to activate namespace prefixing. The default value should
        inherit '%NS.prefixed;' from the DTD driver, so that unless
        overridden, the default behaviour follows the overall DTD
        prefixing scheme.
-->
<!ENTITY % NS.prefixed "IGNORE" >
<!ENTITY % MODULE.prefixed "%NS.prefixed;" >

<!-- 2. Declare a parameter entity (eg., %MODULE.xmlns;) containing
        the URI reference used to identify the Module namespace:
-->
<!ENTITY % MODULE.xmlns  "http://module-namespace-uri" >

<!-- 3. Declare parameter entities (eg., %MODULE.prefix;) containing
        the default namespace prefix string(s) to use when prefixing
        is enabled. This may be overridden in the DTD driver or the
        internal subset of an document instance. If no default prefix
        is desired, this may be declared as an empty string.

     NOTE: As specified in [XMLNAMES], the namespace prefix serves
     as a proxy for the URI reference, and is not in itself significant.
-->
<!ENTITY % MODULE.prefix  "module" >

<!-- 4. Declare parameter entities (eg., %MODULE.pfx;) containing the
        colonized prefix(es) (eg., '%MODULE.prefix;:') used when
        prefixing is active, an empty string when it is not.
-->
<![%MODULE.prefixed;[
<!ENTITY % MODULE.pfx  "%MODULE.prefix;:" >
]]>
<!ENTITY % MODULE.pfx  "" >

<!-- 5. The parameter entity %MODULE.xmlns.extra.attrib; may be
        redeclared to contain any non-Module namespace declaration
        attributes for namespaces embedded in Module. When prefixing
        is active it contains the prefixed xmlns attribute and any
        namespace declarations embedded in Module, otherwise an empty
        string.
-->
<![%MODULE.prefixed;[
<!ENTITY % MODULE.xmlns.extra.attrib
      "xmlns:%MODULE.prefix; %URI.datatype;   #FIXED '%MODULE.xmlns;'" >
]]>
<!ENTITY % MODULE.xmlns.extra.attrib "" >

<!ENTITY % XHTML.xmlns.extra.attrib
      "%MODULE.xmlns.extra.attrib;"
>


<!-- Section B: Module Qualified Names ::::::::::::::::::::::::::::: -->

<!-- This section declares parameter entities used to provide
     namespace-qualified names for all Module element types.
-->

<!-- module:  template-1.mod -->
<!ENTITY % MODULE.front.qname  "%MODULE.pfx;front" >
<!ENTITY % MODULE.back.qname   "%MODULE.pfx;back" >

<!-- end of template-qname-1.mod -->
