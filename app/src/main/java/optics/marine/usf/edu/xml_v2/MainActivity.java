package optics.marine.usf.edu.xml_v2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.res.Resources;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class MainActivity extends AppCompatActivity {

    TextView display;
    TextView mTextView;
    String server_url = "http://optics.marine.usf.edu/cgi-bin/md?roi=CWFL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = (TextView) findViewById(R.id.info);
        mTextView = (TextView) findViewById(R.id.text);



        final RequestQueue mRequestQueue = Volley.newRequestQueue(MainActivity.this);
        /*RequestQueue mRequestQueue;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        Network network = new BasicNetwork(new HurlStack());
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Log.e("file", response);
                            //display.setText(response);
                            //display.setMovementMethod(new ScrollingMovementMethod());
                            XmlPullParserFactory xmlParser = XmlPullParserFactory.newInstance();
                            XmlPullParser parser = xmlParser.newPullParser();
                            InputStream inputStream = new
                                    ByteArrayInputStream(response.getBytes("UTF-8"));
                            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                            parser.setInput(inputStream, null);

                            parseXML(parser);
                            //requestQueue.stop();
                        } catch (IOException | XmlPullParserException e){
                            e.printStackTrace();
                            mRequestQueue.stop();
                            /*mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
                                @Override
                                public boolean apply(Request<?> request) {
                                    return true;
                                }
                            });*/
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("Something went wrong");
                error.printStackTrace();
                mRequestQueue.stop();
            }
        });
        mRequestQueue.add(stringRequest);



        /*XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("xmlv2");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }*/
    }

    /*private String convertStreamToString(InputStream is){
        try{
            return new Scanner(is).next();
        }catch (NoSuchElementException e){
            return "";
        }
    }*/

    private void parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.i("inMethod", "in parseXML");
        int eventType = parser.getEventType();
        Response response = null;
        Integer iTop = -1, iMid = -1, iPass = -1;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    Log.i("case startdoc", "In the start of XML");
                    //Log.i("doc", parser.nextText());
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    Log.i("StartTag", name);
                    if (name.equals("response")) {
                        response = new Response();
                        Log.i("name", name);
                    } else if (response != null) {
                        //Log.i("doc", parser.nextText());
                        if (name.equals("menu")) {
                            response.myMenu = new Response.MyMenu();
                            Log.i("menu", name);
                        } else if (response.myMenu != null) {
                            if (name.equals("top_menu")) {
                                if (response.myMenu.topMenu == null){
                                    response.myMenu.topMenu = new ArrayList<>();
                                }
                                Response.MyMenu.TopMenu top = new Response.MyMenu.TopMenu();
                                top.topTitle = parser.getAttributeValue(null, "title");
                                response.myMenu.topMenu.add(top);
                                iTop++;
                                Log.i("top", top.topTitle);
                            } else if (response.myMenu.topMenu.get(iTop) != null) {
                                if (name.equals("mid_menu")) {
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
                                    if (name.equals("roi_menu")) {
                                        if (response.myMenu.topMenu.get(iTop).midMenu.get(iMid).roi == null){
                                            response.myMenu.topMenu.get(iTop).midMenu.get(iMid).roi = new ArrayList<>();
                                        }
                                        Response.MyMenu.TopMenu.MidMenu.ROI roi = new Response.MyMenu.TopMenu.MidMenu.ROI();
                                        roi.ROIname = parser.getAttributeValue(null, "name");
                                        roi.ROIlink = parser.getAttributeValue(null, "link");
                                        roi.ROItitle = parser.nextText();
                                        response.myMenu.topMenu.get(iTop).midMenu.get(iMid).roi.add(roi);
                                        Log.i("roi", roi.ROItitle);
                                        //Log.i("MidArraySize", ((Integer)(response.myMenu.topMenu.get(iTop).midMenu.size())).toString());
                                    }
                                }
                            }
                        }
                        if (name.equals("description")) {
                            response.desc = new Response.Desc();
                            response.desc.desc = parser.nextText();
                            Log.i("desc", response.desc.desc);
                        }

                        if (name.equals("calendar")) {
                            response.cal = new Response.Cal();
                        }else if(response.cal != null){
                            if (name.equals("calendar_start")){
                                response.cal.calStart = new Response.Cal.CalStart();
                                response.cal.calStart.day = parser.getAttributeValue(null, "day");
                                response.cal.calStart.month = parser.getAttributeValue(null, "month");
                                response.cal.calStart.year = parser.getAttributeValue(null, "year");
                                Log.i("calS", response.cal.calStart.day);
                            }else if(name.equals("calendar_end")){
                                response.cal.calEnd = new Response.Cal.CalEnd();
                                response.cal.calEnd.day = parser.getAttributeValue(null, "day");
                                response.cal.calEnd.month = parser.getAttributeValue(null, "month");
                                response.cal.calEnd.year = parser.getAttributeValue(null, "year");
                                Log.i("calE", response.cal.calEnd.day);
                            }
                        }

                        if (name.equals("tabs")) {
                            //Log.i("tab", "in name = tab");
                            response.tabs = new Response.Tabs();
                        } else if (response.tabs != null) {
                            if (name.equals("pass")){
                                //Log.i("pass", "in name = pass");
                                if (response.tabs.pass == null){
                                    response.tabs.pass = new ArrayList<>();
                                }
                                Response.Tabs.Pass pass = new Response.Tabs.Pass();
                                pass.sensor = parser.getAttributeValue(null, "sensor");
                                pass.hour = parser.getAttributeValue(null, "hour");
                                pass.min = parser.getAttributeValue(null, "min");
                                response.tabs.pass.add(pass);
                                iPass++;
                            }else if(response.tabs.pass.get(iPass) != null){
                                if (name.equals("image")){
                                    //Log.i("images", "in name = image");
                                    if (response.tabs.pass.get(iPass).myImage == null){
                                        response.tabs.pass.get(iPass).myImage = new ArrayList<>();
                                    }
                                    Response.Tabs.Pass.MyImage image = new Response.Tabs.Pass.MyImage();
                                    image.picType = parser.getAttributeValue(null, "type");
                                    image.s200 = parser.getAttributeValue(null, "image_200_link");
                                    image.s400 = parser.getAttributeValue(null, "image_400_link");
                                    image.sFull = parser.getAttributeValue(null, "image_full_link");
                                    image.contents = parser.nextText();
                                    response.tabs.pass.get(iPass).myImage.add(image);
                                    Log.i("image", image.contents);
                                }
                            }
                        }
                    }
                    break;

                case XmlPullParser.END_TAG:
                    Log.i("EndTag", parser.getName());//((Integer)XmlPullParser.END_TAG).toString());
                    name = parser.getName();
                    //if (name.equalsIgnoreCase("product") && currentItem != null) {
                    //   items.add(currentItem);
                    //}
            }
            eventType = parser.next();
            //Log.i("while loop", "inside while loop");
        }

        Log.i("end", "ending of parseXML");
        //   printProducts(products);
    }




}