/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/8/28 0028.
 */
public class RegexTool {

    public static List<String> getResult(String src, String regex) {
        ArrayList<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(src);
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }

}
