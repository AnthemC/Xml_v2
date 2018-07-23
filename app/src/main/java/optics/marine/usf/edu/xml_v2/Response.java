package optics.marine.usf.edu.xml_v2;

import java.util.ArrayList;

public class Response {
    public MyMenu myMenu;
    public Desc desc;
    public Cal cal;
    public Tabs tabs;


    public static class MyMenu{
        public ArrayList<TopMenu> topMenu;
        public static class TopMenu{
            public String topTitle;
            public ArrayList<MidMenu> midMenu;
            public static class MidMenu{
                public String midTitle;
                public ArrayList<ROI> roi;
                public static class ROI{
                    public String ROItitle;
                    public String ROIlink;
                    public String ROIname;

                }
            }
        }
    }
    public static class Desc{
     String desc;
    }
    public static class Cal{
        public CalStart calStart;
        public CalEnd calEnd;
        public static class CalStart{
            public String day;
            public String month;
            public String year;
        }
        public static class CalEnd{
            public String day;
            public String month;
            public String year;

        }
    }

    public static class Tabs{
        public ArrayList<Pass> pass;
        public static class Pass{
            public ArrayList<MyImage> myImage;
            public String sensor;
            public String hour;
            public String min;
            public static class MyImage{
                public String picType;
                public String s200;
                public String s400;
                public String sFull;
                public String contents;

            }
        }
    }
}
