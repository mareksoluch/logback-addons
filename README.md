# json-logback-providers
This is a pack of json providers providing some additional features to logtash logback encoder.

# MdcJsonArrayProvider
Converts MDC values to array. Values are split with  `,` separator. Only values of implicitly defined keys (via `arrayFieldName`) are transformed.

All keys not matching regex `[a-zA-Z0-9\.$%&#@!+-=]+` will be ignored.

Sample config:
```xml
<provider class="pl.marko.logback.composite.loggingevent.MdcJsonArrayProvider">
    <arrayFieldName>mdcKey</arrayFieldName>
</provider>
```

# MdcJsonNestedObjectProvider
Convers MDC map to nested object. Path of a nested object is encoded in MDC property keys. Each path element is separated with `.`.

For MDC map:
```
a.b.c : value1
a.d : value2
e.f : value3
```
Following json will be written:
```json
{
  "a" : {
    "b" : {
      "c" : "value1"
    },
    "d" : "value2"    
  },
  "e" : {
    "f" : "value3"
  }
}
```

All keys not matching regex `[a-zA-Z0-9\.$%&#@!+-=]+` will be ignored.

Sample config:
```xml
<provider class="pl.marko.logback.composite.loggingevent.MdcJsonNestedObjectProvider"/>
```