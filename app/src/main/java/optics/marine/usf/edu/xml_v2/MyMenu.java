package optics.marine.usf.edu.xml_v2;

import java.util.ArrayList;

public class MyMenu {
    public String topTitle;
    public ArrayList<MyRegion> myRegions;

    public static class MyRegion{
        public ArrayList<ROI> roi;
        public String regionTitle;

        public static class ROI{
            public String ROItitle;
            public String ROIlink;
        }
    }
}
