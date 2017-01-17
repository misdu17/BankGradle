package com.syful.framework.web.platform.utilities;

import org.joda.time.DateTime;

import java.util.*;


public class DateTimeFormatter {

    /// <summary>
    /// Returns formatted time
    /// </summary>
    /// <param name="time"></param>
    /// <returns></returns>
    public String secondsTimeFormatter(long time) {
        float formattedTime = time;

        String ss = "";
        Map<String, Float> map = new HashMap<>();
        map.put("m", (float) 60000);
        map.put("s", (float) 1000);
        Map<String, Float> sortByValueMap = sortByValue(map);

        for (Map.Entry<String, Float> mp : sortByValueMap.entrySet()) {
            float newTime = formattedTime / mp.getValue();
            int integral = (int) newTime;
            if (integral > 0) {
                ss += integral + mp.getKey() + " ";
                formattedTime = formattedTime - integral * mp.getValue();
            }
        }
        System.out.println("Test execution time is equal to " + ss);
        return ss.trim();
    }

    private Map sortByValue(Map unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static String getJsonDateTime(DateTime dateTime){
        DateTime time;
        long timespan = dateTime.getMinuteOfDay();
        long remainder = timespan % 5;
        if (remainder != 0) {
            if (remainder >= 2.5f) {
                int s = (int) (5 - remainder);
                time = dateTime.plusMinutes(s);
                time.minusMillis((int) dateTime.getMillis());
            } else {
                time = dateTime.plusMinutes((int) -remainder);
            }
        } else {
            time = dateTime;
        }

        return "/Date(" + time.getMillis() + "-0400)/";
    }

    public static DateTime getPartibleDateTime(DateTime dateTime, int divisor){
        DateTime time;
        long timespan = dateTime.getMinuteOfDay();
        long remainder = timespan % divisor;
        if (remainder != 0) {
            if (remainder >= 2.5f) {
                int s = (int) (5 - remainder);
                time = dateTime.plusMinutes(s);
                time.minusMillis((int) dateTime.getMillis());
            } else {
                time = dateTime.plusMinutes((int) -remainder);
            }
        } else {
            time = dateTime;
        }

        return time;
    }

}
