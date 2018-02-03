[![Build Status](https://travis-ci.org/mareksoluch/json-extractor.svg?branch=master)](https://travis-ci.org/mareksoluch/json-extractor)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[ ![Download](https://api.bintray.com/packages/mareksoluch/maven/json-extractor/images/download.svg) ](https://bintray.com/mareksoluch/maven/json-extractor/_latestVersion)

# json-extractor
This is a simple library that provides possibility to extract json field values basing on fields' names.
Algorithm of fields selections is either extract all fields or base on regular expressions that match to fields' names.

# Usage
Extract all fields' values:
```java
JsonNode jsonNode = loadJson();
Stream<Object> values = JsonExtractor.allFields().extract(jsonNode);
values.forEach(value -> LOG.debug("Extracted value: {}", value));
```

Extract fields' values that match patterns:
```java
JsonNode jsonNode = loadJson();
Stream<Object> values = JsonExtractor.byPattern(".*Id.*", ".*name.*").extract(jsonNode);
values.forEach(value -> LOG.debug("Extracted value: {}", value));
```
