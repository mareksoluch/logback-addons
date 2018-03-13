package pl.marko.logback.composite.loggingevent;

import java.util.Collection;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class ArrayValuePrinter {

    public Object printValue(String key, String value, Collection<String> arrayFieldNames, String arraySeparator) {
        return arrayFieldNames.contains(key)
                ? valueToArray(value, arraySeparator)
                : value;
    }

    private Collection<String> valueToArray(String value, String arraySeparator) {
        if (value != null && value.contains(arraySeparator)) {
            return asList(value.split(arraySeparator));
        }
        return singletonList(value);
    }
}
