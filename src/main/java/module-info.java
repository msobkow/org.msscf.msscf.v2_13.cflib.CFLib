open module org.msscf.msscf.v2_13.cflib.CFLib {
    requires transitive java.desktop;
    requires transitive org.apache.commons.codec;
    requires transitive jakarta.persistence;
    exports org.msscf.msscf.v2_13.cflib.CFLib;
    exports org.msscf.msscf.v2_13.cflib.CFLib.xml;
    exports org.msscf.msscf.v2_13.cflib.CFLib.dbutil;
    exports org.msscf.msscf.v2_13.cflib.CFLib.Tip;
}
