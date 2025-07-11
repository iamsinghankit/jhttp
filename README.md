
# JHTTP - Java HTTP Server 🚀

A lightweight, extensible, high-performance HTTP server built from scratch using Java `VirtualThread` . Designed to demonstrate core HTTP concepts, support advance routing, interceptors (auth, CORS, IP blocklist, JSON logging).

---

## 🔧 Features

| Feature                     | Description                                              |
|-----------------------------|----------------------------------------------------------|
| ✅ HTTP                      | Supports plain connections                               |
| ✅ GET, POST, PUT, DELETE    | Handles all common HTTP methods                          |
| ✅ Routing with Parameters   | Simple path-based routing with query/path param parsing  |
| ✅ Interceptors (Middleware) | CORS, Auth, IP Whitelist, Rate Limiting, Json logging    |
| ✅ Basic Auth                | Basic support for `Authorization: Basic` token header    |
| ✅ CORS Support              | Adds proper headers and handles `OPTIONS` preflight      |
| ✅ Body Parsers              | Handles JSON, form-data, file uploads, and octet streams |
| ✅ Modular Architecture      | Easily plug in routes, interceptors, and response logic  |

---

---

## 🚀 Getting Started

### 🏗️ Build from Scratch

### 🔹  Clone the Repository

```bash
git clone https://github.com/iamsinghankit/jhttp.git
cd jhttp

mvn clean install
```
## Adding JHttp to your build

To add a dependency on JHttp using Maven, use the following:
```xml
<dependency>
    <groupId>com.singhankit</groupId>
    <artifactId>jhttp</artifactId>
    <version>0.0.1</version>
</dependency>
 ``` 

### Register a Route

```java
var getHelloMapping = RequestMapping.GET()
                                     .path("/hello")
                                     .handler(req -> new HttpResponse(req.headers(),HttpStatus.OK, null))
                                     .build();

JHttp jHttp = JHttp.defaults()
                           .addMapping(getHelloMapping)      
                           .build();
      jHttp.start();
```

---

### Sample `curl` Request

```bash
curl http://localhost:9090/hello
```
---

## 🔐 Basic Authentication

Add this interceptor:

```java
JHttp jHttp = JHttp.defaults()
                   .addMapping(getHelloMapping)
                   .addRequestInterceptor(new BasicAuthInterceptor("user", "pass"))
                   .build();
      jHttp.start();
```

Request example:

```http
Authorization: Basic dXNlcjpwYXNz
```

---

## 🔄 CORS Support

Enable with:

```java
.addResponseInterceptor(new CorsInterceptor(CorsHeader.allowAll()))
```

Handles:

* `OPTIONS`
* `Access-Control-Allow-*` headers


## 🧪 Supported Interceptors

| Interceptor              | Purpose                        |
|--------------------------|--------------------------------|
| `CorsInterceptor`        | Adds CORS headers              |
| `BasicAuthInterceptor`   | Verifies basic token           |
| `IPBlockListInterceptor` | Block specified IPs            |
| `RateLimitInterceptor`   | Basic per-IP request limiting  |
| `JsonLoggingInterceptor` | Request, Response json logging | 




## 📜 License

This project is licensed under the [Apache License 2.0](LICENSE).  
Feel free to use, modify, and distribute it with attribution.


## 👤 Author

Ankit Singh  
🔗 [GitHub](https://github.com/iamsinghankit) • 📨 [Email](mailto:iamsinghankit@gmail.com)


