[![Build Status](https://travis-ci.org/mareksoluch/logback-addons.svg?branch=master)](https://travis-ci.org/mareksoluch/logback-addons)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
 [ ![Download](https://api.bintray.com/packages/mareksoluch/maven/logback-addons/images/download.svg?version=0.2) ](https://bintray.com/mareksoluch/maven/logback-addons/0.2/link)

# json-logback-providers
This is a pack of json providers providing some additional features to logtash logback encoder.

# MdcJsonArrayProvider
Converts MDC values to array. Values are split with  `,` separator. Only values of implicitly defined keys (via `arrayFieldName`) are transformed.

All keys not matching regex `[a-zA-Z0-9\.$%&#@!+-=]+` will be ignored.

Sample config:
```xml
<provider class="org.solo.logback.composite.loggingevent.MdcJsonArrayProvider">
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
<provider class="org.solo.logback.composite.loggingevent.MdcJsonNestedObjectProvider"/>
```

# MdcJsonNestedArrayProvider
Provider combining functionalities of both providers described above. It enables to print array values of nested properties.
For MDC map:
```
a.b.c : value1
a.d : v,a,l,u,e,2
```
Following json will be written:
```json
{
  "a" : {
    "b" : {
      "c" : "value1"
    },
    "d" : ["v","a","l","u","e","2"]    
  }
}
```
All keys not matching regex `[a-zA-Z0-9\.$%&#@!+-=]+` will be ignored.

Sample config:
```xml
<provider class="org.solo.logback.composite.loggingevent.MdcJsonNestedArrayProvider">
    <arrayFieldName>a.d</arrayFieldName>
</provider>
```
