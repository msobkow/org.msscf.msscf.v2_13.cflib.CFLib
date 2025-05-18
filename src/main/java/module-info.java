open module org.msscf.msscf.v2_13.cflib.CFLib {
    requires transitive java.desktop;
    requires transitive org.apache.commons.codec;
    // requires org.apache.xerces;
    requires transitive jakarta.persistence;
    // requires jdk.xml.dom;
    // Ensure Xerces is properly integrated
    exports org.msscf.msscf.v2_13.cflib.CFLib;
    exports org.msscf.msscf.v2_13.cflib.CFLib.xml;
    exports org.msscf.msscf.v2_13.cflib.CFLib.dbutil;
    exports org.msscf.msscf.v2_13.cflib.CFLib.Tip;
    
    // Verify that the Xerces version matches your project requirements
}
