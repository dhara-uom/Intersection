package pckg;

/**
 * Created with IntelliJ IDEA.
 * User: newair
 * Date: 10/20/13
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class URLFixer {

    private static String GeoServerHost = "http://localhost:8094/geoserver/Dhara/ows";
    private static String service = "WFS";
    private static String version = "1.0.0";
    private static String request = "GetFeature";
    private static String typeName = "Dhara:agalawatta2";     // this is default
    private static String maxFeatures = "50";
    private static String outputFormat = "GML2";


    public static void main(String[] args) {

        System.out.println(fix(GeoServerHost, typeName));
    }

    public static String fix(String host, String name) {
        //  http://localhost:8094/geoserver
        String repo = name.split(":")[0];

        String completeURL = host + "/" + repo + "/ows?" + "service=" + service + "&version=" + version + "&request=" + request + "&typeName=" + name + "&maxFeatures=" + maxFeatures + "&outputFormat=" + outputFormat;


        return completeURL;

    }


}
