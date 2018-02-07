package za.co.samtakie.baking.utilities;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jemanuels on 2017/12/09.
 * This Class has the functionality to download the data from the web.
 */

public class NetworkUtils {

    // Get the name of the current Class and assign it to the TAG constant
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // Set the url for downloading the data and assign it to constant DYNAMIC_RECIPE_URL
    private static final String DYNAMIC_RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /**
     * Build the url string and return the full url string
     * @return The URL to use to query the baking server
     */
    public static URL buildUrl(){
        Uri builtUri;
        builtUri = Uri.parse(DYNAMIC_RECIPE_URL).buildUpon().build();
        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.d(TAG, "The url string " + url);
        return url;
    }

    /***
     *
     * @param url the url string from the return buildUrl method
     * @return return the data string in this case JSON
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                String returnScanner = scanner.next();
                return returnScanner;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}