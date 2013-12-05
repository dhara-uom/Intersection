package test;

import net.opengis.wps.x100.CapabilitiesDocument;
import net.opengis.wps.x100.ProcessDescriptionType;
import org.dhara.wps.filter.Filter;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.n52.wps.client.WPSClientException;
import pckg.IntersectTest;
import pckg.Params;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * This is the test class Which tests whether existence of capabilities doc
 * describeprocess doc
 * geoserver existence
 * testFilter
 */

@RunWith(JUnit4.class)
public class TestProcess {

    private static IntersectTest main;
    private CapabilitiesDocument capabilitiesDocument;
    private ProcessDescriptionType describeProcessDocument;
    private String url = "http://localhost:8094/geoserver/Dhara/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=Dhara:LS_Bulathsinhala&maxFeatures=50&outputFormat=GML2";


    /**
     * SetUp only the main class.
     */

    @BeforeClass
    public static void setUp() {

        main = new IntersectTest();

    }

    /**
     * Test existence of capabilities document
     */

    @Test

    public void capabilitiesDocExist() {
        try {
            capabilitiesDocument = main.requestGetCapabilities(Params.WPSURL);
            Assert.assertNotNull("Capabilities Document not available", capabilitiesDocument);

        } catch (WPSClientException e) {
            Assert.fail("WPSClientException thrown");
        }
    }

    /**
     * Test describe process
     */
    @Test
    public void describeProcessExist() {
        try {
            describeProcessDocument = main.requestDescribeProcess(
                    Params.WPSURL, Params.processID);
            Assert.assertNotNull("Capabilities Document Not available", describeProcessDocument);

        } catch (IOException e) {
            Assert.fail("IO exception thrown at describe process ");
        }
    }

    /**
     * Test Execution
     */

    @Test
    public void testExecute() {
        try {

            describeProcessDocument = main.requestDescribeProcess(
                    Params.WPSURL, Params.processID);

            System.out.println(describeProcessDocument);
            HashMap<String, Object> inputs = new HashMap<String, Object>();
            // complex data by reference
            inputs.put(
                    "Polygon1", Params.WFSURL1);     //Two polygons intersected

            inputs.put("Polygon2", Params.WFSURL2);         //Inputs are obtained from Params class. If you want to change inputs please change the values there

            String output = main.executeProcess(Params.WPSURL, Params.processID,
                    describeProcessDocument, inputs);
            Assert.assertNotNull("Output Not available", output);
            System.out.println(Filter.getResourceId(output));

        } catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test whether Geoserver started
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGeoserver() throws IOException {
        String USER_AGENT = "Mozilla/5.0";

        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        //read response
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Assert.assertNotNull("GeoServer response arriving", response.toString());

    }

    /**
     * Test whether filtering process ok using DummyResponse
     */
    @Test
    public void testFilter() {

        String id = Filter.getResourceId(DummyResponse.response);

        Assert.assertEquals("N52:Shape_d7081c5b-c07a-4adb-9003-47592e4346d78175463493701042351", id);

    }


}
