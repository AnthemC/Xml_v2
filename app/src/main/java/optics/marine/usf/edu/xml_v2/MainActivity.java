package optics.marine.usf.edu.xml_v2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.res.Resources;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity {

    TextView display;
    private Resources resources;
    private String output;
    ProgressBar mProgressBar;
    TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.info);


        XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("xmlv2");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.i("inMethod", "in parseXML");
        ArrayList items = null;
        int eventType = parser.getEventType();
        Response response = null;
        Integer iTop = 0, iMid = -1;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    items = new ArrayList();
                    Log.i("case startdoc", "In the start of XML");
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    //Log.i("name", name);
                    if (name.equals("response")) {
                        response = new Response();
                        Log.i("name", name);
                    } else if (response != null) {
                        if (name.equals("menu")) {
                            response.myMenu = new Response.MyMenu();
                            Log.i("menu", name);
                        } else if (response.myMenu != null) {
                            if (name.equals("top")) {
                                response.myMenu.topMenu = new ArrayList<>();
                                Response.MyMenu.TopMenu top = new Response.MyMenu.TopMenu();
                                top.topTitle = parser.getAttributeValue(null, "title");
                                response.myMenu.topMenu.add(top);
                                Log.i("top", top.topTitle);
                            } else if (response.myMenu.topMenu.get(iTop) != null) {
                                if (name.equals("mid")) {
                                    if (response.myMenu.topMenu.get(iTop).midMenu == null) {
                                        response.myMenu.topMenu.get(iTop).midMenu = new ArrayList<>();
                                    }
                                    Response.MyMenu.TopMenu.MidMenu midMenu = new Response.MyMenu.TopMenu.MidMenu();
                                    midMenu.midTitle = parser.getAttributeValue(null, "title");
                                    response.myMenu.topMenu.get(iTop).midMenu.add(midMenu);
                                    Log.i("mid", midMenu.midTitle);
                                    Log.i("iTop", iTop.toString());
                                    Log.i("iMid", iMid.toString());
                                    iMid++;
                                } else if (response.myMenu.topMenu.get(iTop).midMenu.get(iMid) != null) {
                                    if (name.equals("roi")) {
                                        response.myMenu.topMenu.get(iTop).midMenu.get(iMid).roi = new ArrayList<>();
                                        Response.MyMenu.TopMenu.MidMenu.ROI roi = new Response.MyMenu.TopMenu.MidMenu.ROI();
                                        roi.ROIname = parser.getAttributeValue(null, "name");
                                        roi.ROIlink = parser.getAttributeValue(null, "link");
                                        roi.ROItitle = parser.nextText();
                                        response.myMenu.topMenu.get(iTop).midMenu.get(iMid).roi.add(roi);
                                        Log.i("roi", roi.ROItitle);
                                        //Log.i("MidArraySize", ((Integer)(response.myMenu.topMenu.get(iTop).midMenu.size())).toString());
                                    }
                                    break;
                                }
                            }
                        }
                        if (name.equals("description")) {
                            Log.i("description", "inside description");
                        }
                        if (name.equals("calendar")) {
                            Log.i("calandar", "inside calandar");
                        }
                        if (name.equals("tabs")) {
                            Log.i("tabs", "inside tabs");
                        }
                    }
                    break;
                /*case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("product") && currentItem != null) {
                        items.add(currentItem);
                    }*/
            }
            eventType = parser.next();
            //Log.i("while loop", "inside while loop");
        }

        Log.i("end", "ending of parseXML");
        //   printProducts(products);
    }

}