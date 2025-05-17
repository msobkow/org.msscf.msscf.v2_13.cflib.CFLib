open module org.msscf.msscf.v2_13.cflib.CFLib {
    requires java.desktop;
    requires org.apache.commons.codec;
    requires xercesImpl;
    requires jakarta.persistence;
    
    // Ensure Xerces is properly integrated
    exports org.msscf.msscf.v2_13.cflib.CFLib.xml;
    exports org.msscf.msscf.v2_13.cflib.CFLib.dbutil;
    exports org.msscf.msscf.v2_13.cflib.CFLib.Tip;
    
    // Verify that the Xerces version matches your project requirements
}
