package test;

/**
 * This is the dummy response created to test the filtering process
 */
public class DummyResponse {

    public static String response=
            "<ns:ExecuteResponse xmlns:ns=\"http://www.opengis.net/wps/1.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" service=\"WPS\" serviceInstance=\"http://localhost:8090/52n-wps-webapp-3.3.0-SNAPSHOT/WebProcessingService?REQUEST=GetCapabilities&amp;SERVICE=WPS\" version=\"1.0.0\" xml:lang=\"en-US\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsExecute_response.xsd\">\n" +
            "   <ns:Process ns:processVersion=\"1.1.0\">\n" +
            "      <ns1:Identifier xmlns:ns1=\"http://www.opengis.net/ows/1.1\">org.n52.wps.server.algorithm.SimpleBufferAlgorithm</ns1:Identifier>\n" +
            "      <ns1:Title xmlns:ns1=\"http://www.opengis.net/ows/1.1\">org.n52.wps.server.algorithm.SimpleBufferAlgorithm</ns1:Title>\n" +
            "   </ns:Process>\n" +
            "   <ns:Status creationTime=\"2013-11-10T15:06:43.246+05:30\">\n" +
            "      <ns:ProcessSucceeded>Process successful</ns:ProcessSucceeded>\n" +
            "   </ns:Status>\n" +
            "   <ns:ProcessOutputs>\n" +
            "      <ns:Output>\n" +
            "         <ns1:Identifier xmlns:ns1=\"http://www.opengis.net/ows/1.1\">result</ns1:Identifier>\n" +
            "         <ns1:Title xmlns:ns1=\"http://www.opengis.net/ows/1.1\">result</ns1:Title>\n" +
            "         <ns:Data>\n" +
            "            <ns:ComplexData mimeType=\"application/WFS\"><![CDATA[<OWSResponse type=\"WFS\"><ResourceID>N52:Shape_d7081c5b-c07a-4adb-9003-47592e4346d78175463493701042351</ResourceID><GetCapabilitiesLink>http://localhost:8094/geoserver/wfs?Service=WFS&amp;Request=GetCapabilities&amp;Version=1.1.0</GetCapabilitiesLink></OWSResponse>]]></ns:ComplexData>\n" +
            "         </ns:Data>\n" +
            "      </ns:Output>\n" +
            "   </ns:ProcessOutputs>\n" +
            "</ns:ExecuteResponse>";




}
