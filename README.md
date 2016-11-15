# url-parser
#### Parse URL based on the provided template

[![Build Status](https://travis-ci.org/avirup-nandi/url-parser.svg?branch=master)](https://travis-ci.org/avirup-nandi/url-parser)

Url Parser is a light-weight project module which does not use any external library in production code.
Template should include only the Url path, if you add query string in template, that would be ignored.
Query params are represented as key value params, i.e. by providing key to `getQueryParamValue` method, you can fetch the value of query params.

**Java Compiler: `1.7`**
###### How to use
```
UrlParser urlParser = UrlParser.createParser("/path/{pathName}");
if (urlParser.parse("/path/myOwnPath?query=myOwnQuery")) {
    System.out.println("Param pathName: " + urlParser.getPathParamValue("templateName"));
    System.out.println("Param query: " + urlParser.getQueryParamValue("query").get(0));
}
```
Output:
``` 
Param pathName: myOwnPath
Param query: myOwnQuery
```
In above example you see `parse` method returns `boolean`. If provided Url matches the template, it would return true, otherwise false. **Note that,** It is recomended to check if `parse` returns true, or you can get un-necessary exception while accessing other methods, like: `getPathParamValue, getQueryParamValue etc`

###### Javadocs

- `createParser(String template)` creates the UrlParser object with the provided template
- `parse(String url)` takes input url to be parsed and returns boolean
- `parse(String url, String decoder)` takes input url to be parsed, decoder e.g. 'UTF-8' and returns boolean
- `parse(URL url)` takes input url to be parsed and returns boolean
- `getAllPathParams()` returns all path params as form of `Map<String, String>`
- `getPathParamValue(String templateVar)` takes templateVar as input and returns path param value
- `getAllQueryParams()` returns all query params as form of `Map<String, List<String>>`
- `getQueryParamValue(String queryParamKey)` takes queryParamKey as input and returns `List<String>`

###### url-parser supports multiple query params with same key like `?x=1&x=2&x=3&y=1`, e.g.
```
UrlParser urlParser = UrlParser.createParser("/path/{pathName}");
if (urlParser.parse("/path/myOwnPath?query=myOwnQuery&query=myPetsQuery&otherQuery=brothersQuery")) {
    System.out.println("My param query: " + urlParser.getQueryParamValue("query"));
    System.out.println("Others param query: " + urlParser.getQueryParamValue("otherQuery"));
}
```
Output:
``` 
My param query: List("myOwnQuery", "myPetsQuery")
Others param query: List("brothersQuery")
```

##### More examples in [test-cases](https://github.com/avirup-nandi/url-parser/tree/master/src/test/java/org/avirup/common/urlparser)

##### TODO List
- [ ] Support for Java 8
- [ ] Support type casting to the expected type (e.g. Integer, Double etc.)
- [ ] Publish this project in public repo
