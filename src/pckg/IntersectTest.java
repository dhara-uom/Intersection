package pckg;

import net.opengis.wps.x100.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.dhara.wps.filter.Filter;
import org.geotools.feature.FeatureCollection;
import org.n52.wps.client.WPSClientException;
import org.n52.wps.client.WPSClientSession;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class executes the Intersection process.
 */

public class IntersectTest {

    /**
     * @param args This is the main method implemented to get the inputs from standard input
     */
    public static void main(String[] args) {
        Logger.getRootLogger().setLevel(Level.OFF);

        Params.WPSURL = args[0]; // Obtains the WPSURL as parameter 1

        // WFSURL is constructed at URL Fixer having 1.geoserver url 2.geoserver wfsid. Needs to construct two WFS URL's
        Params.WFSURL1 = URLFixer.fix(args[1], args[2]);
        Params.WFSURL2 = URLFixer.fix(args[1], args[3]);
        IntersectTest client = new IntersectTest();
        client.runProcess();      //run the process

    }

    public void runProcess() {


        try {
            CapabilitiesDocument capabilitiesDocument = requestGetCapabilities(Params.WPSURL);  // capabilitiesDocument shows the all the processes in WPS
            ProcessDescriptionType describeProcessDocument = requestDescribeProcess(
                    Params.WPSURL, Params.processID);     //This is the describe process document. This can be used to check availability of the process

            HashMap<String, Object> inputs = new HashMap<String, Object>();
            // complex data by reference
            inputs.put(
                    "Polygon1", Params.WFSURL1);     //Two polygons intersected

            inputs.put("Polygon2", Params.WFSURL2);         //Inputs are obtained from Params class. If you want to change inputs please change the values there
            executeProcess(Params.WPSURL, Params.processID,           //execute with WPS URL, processId in this case id of SimpleBuffer at WPS.
                    describeProcessDocument, inputs);

        } catch (WPSClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param url
     * @return CapabilitiesDocument
     * @throws WPSClientException Obtain the capabilities document for a given URL
     */
    public CapabilitiesDocument requestGetCapabilities(String url)
            throws WPSClientException {

        WPSClientSession wpsClient = WPSClientSession.getInstance();     //get the session

        wpsClient.connect(url);  //establish connection

        CapabilitiesDocument capabilities = wpsClient.getWPSCaps(url);   //get the capabilities document

        ProcessBriefType[] processList = capabilities.getCapabilities()   //get all the processes to an array
                .getProcessOfferings().getProcessArray();


        return capabilities;
    }

    /**
     * @param url
     * @param processID
     * @return ProcessDescriptionType
     * @throws IOException get the process description
     */
    public ProcessDescriptionType requestDescribeProcess(String url,
                                                         String processID) throws IOException {

        WPSClientSession wpsClient = WPSClientSession.getInstance();      //get the instance

        ProcessDescriptionType processDescription = wpsClient         //get description document
                .getProcessDescription(url, processID);

        InputDescriptionType[] inputList = processDescription.getDataInputs()     //get description elements to an array
                .getInputArray();


        return processDescription;
    }

    /**
     * @param url
     * @param processID
     * @param processDescription
     * @param inputs
     * @return String
     * @throws Exception After obtaining the describe process document's ProcessDescriptionType execution occurs. Inputs are given as a hash map
     */
    public String executeProcess(String url, String processID,
                               ProcessDescriptionType processDescription,
                               HashMap<String, Object> inputs) throws Exception {
        org.n52.wps.client.ExecuteRequestBuilder executeBuilder = new org.n52.wps.client.ExecuteRequestBuilder(
                processDescription);

        for (InputDescriptionType input : processDescription.getDataInputs()
                .getInputArray()) {

            String inputName = input.getIdentifier().getStringValue();       //get input value
            Object inputValue = inputs.get(inputName);       //get input name

            if (input.getLiteralData() != null) {    //check nullability of literal data
                //Executebuilder builds WPS standard execution request.
                if (inputValue instanceof String) {
                    executeBuilder.addLiteralData(inputName,         //specifies to add literal data.
                            (String) inputValue);
                }
            } else if (input.getComplexData() != null) {
                // Complexdata // Complexdata by value such as GML reference set the schema
                if (inputValue instanceof FeatureCollection) {
                    IData data = new GTVectorDataBinding(
                            (FeatureCollection) inputValue);
                    executeBuilder
                            .addComplexData(
                                    inputName,
                                    data,
                                    "http://schemas.opengis.net/gml/2.1.2/feature.xsd",
                                    "UTF-8", "text/xml");
                }
                // Complexdata Reference
                if (inputValue instanceof String) {
                    executeBuilder
                            .addComplexDataReference(
                                    inputName,
                                    (String) inputValue,
                                    "http://schemas.opengis.net/gml/2.1.1/feature.xsd",
                                    null, "text/xml");
                }

                if (inputValue == null && input.getMinOccurs().intValue() > 0) {
                    throw new IOException("Property not set, but mandatory: "
                            + inputName);
                }
            }
        }

        executeBuilder.setMimeTypeForOutput("application/WFS", "intersection_result");   /* set the Mime type as application/WFS. But most documents you will
         find it is set as text/xml. Since you are dealing with */


        ExecuteDocument execute = executeBuilder.getExecute();

        execute.getExecute().setService("WPS");    //set the service as WPS
        WPSClientSession wpsClient = WPSClientSession.getInstance();      //get the client from session
        Object responseObject = wpsClient.execute(url, execute);      //execute and get the response

        if (responseObject instanceof ExecuteResponseDocument) {
            String content = responseObject.toString();

            System.out.println(Filter.getResourceId(content));    /* Execute response is a fairly complex long xml response. But only the wfs id is needed
            to pass from one process to the other hence only the Id is needed */
            return content;
        }
        else{

            return null;
        }

    }


}
