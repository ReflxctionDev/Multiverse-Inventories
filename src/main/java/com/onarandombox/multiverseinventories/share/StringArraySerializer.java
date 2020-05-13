package com.onarandombox.multiverseinventories.share;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringArraySerializer implements SharableSerializer<String[]> {
    @Override
    public String[] deserialize(Object obj) {
        List<?> list = (List<?>) obj;
        List<String> resultList = new ArrayList<>(list.size());
        for (Object o : list) {
            if (o instanceof String) {
                resultList.add((String) o);
            }
        }
        return resultList.toArray(new String[resultList.size()]);
    }

    @Override
    public Object serialize(String[] strings) {
        return Arrays.asList(strings);
    }
}
