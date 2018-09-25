package cn.leo.store.utils;

import android.support.v4.util.SimpleArrayMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * create by : Jarry Leo
 * date : 2018/8/15 16:47
 */
public class IdCardValidate {

    /**
     * 检查身份证号码是否合法
     *
     * @param id 身份证号码
     * @return 检查结果
     */
    public static boolean check(String id) {
        return checkLength(id) &&
                checkNumber(id) &&
                checkLastCode(id) &&
                checkDate(id) &&
                checkAreaCode(id);
    }

    //检查长度
    private static boolean checkLength(String id) {
        return id.length() == 18;
    }

    //检查数字
    private static boolean checkNumber(String id) {
        String reg = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
        return id.matches(reg);
    }

    //检查校验码
    private static boolean checkLastCode(String id) {
        String[] valCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] wi = {"7", "9", "10", "5", "8", "4", "2", "1",
                "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        int sum = 0;
        for (int i = 0; i < wi.length; i++) {
            sum = sum + Integer.parseInt(String.valueOf(id.charAt(i)))
                    * Integer.parseInt(wi[i]);
        }
        int modValue = sum % 11;
        return valCodeArr[modValue].equals(id.substring(17));
    }

    //检查地区码
    private static boolean checkAreaCode(String id) {
        SimpleArrayMap<String, String> map = new SimpleArrayMap<>();
        map.put("11", "北京");
        map.put("12", "天津");
        map.put("13", "河北");
        map.put("14", "山西");
        map.put("15", "内蒙古");
        map.put("21", "辽宁");
        map.put("22", "吉林");
        map.put("23", "黑龙江");
        map.put("31", "上海");
        map.put("32", "江苏");
        map.put("33", "浙江");
        map.put("34", "安徽");
        map.put("35", "福建");
        map.put("36", "江西");
        map.put("37", "山东");
        map.put("41", "河南");
        map.put("42", "湖北");
        map.put("43", "湖南");
        map.put("44", "广东");
        map.put("45", "广西");
        map.put("46", "海南");
        map.put("50", "重庆");
        map.put("51", "四川");
        map.put("52", "贵州");
        map.put("53", "云南");
        map.put("54", "西藏");
        map.put("61", "陕西");
        map.put("62", "甘肃");
        map.put("63", "青海");
        map.put("64", "宁夏");
        map.put("65", "新疆");
        map.put("71", "台湾");
        map.put("81", "香港");
        map.put("82", "澳门");
        map.put("91", "国外");
        return map.containsKey(id.substring(0, 2));
    }

    //检查日期
    private static boolean checkDate(String id) {
        String strYear = id.substring(6, 10);// 年份
        String strMonth = id.substring(10, 12);// 月份
        String strDay = id.substring(12, 14);// 日期
        String date = strYear + "-" + strMonth + "-" + strDay;
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            Date parse = simpleDateFormat.parse(date);
            boolean after = calendar.getTime().after(parse);
            calendar.add(Calendar.YEAR, -150);
            boolean before = calendar.getTime().before(parse);
            return after && before;
        } catch (ParseException e) {
            return false;
        }
    }
}
