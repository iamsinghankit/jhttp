
# JHTTP - Java HTTP Server 🚀

A lightweight, extensible, HTTP server built from scratch using Java `Socket` . Designed to demonstrate core HTTP concepts, support advance routing, filters (auth, CORS, IP blacklist).

---

## 🔧 Features

| Feature                   | Description                                                   |
|---------------------------|---------------------------------------------------------------|
| ✅ HTTP                    | Supports plain connections                                   |
| ✅ GET, POST, PUT, DELETE  | Handles all common HTTP methods                              |
| ✅ Routing with Parameters | Simple path-based routing with query/path param parsing      |
| ✅ Filters (Middleware)    | CORS, Auth, IP Whitelist, Rate Limiting                      |
| ✅ Basic Auth              | Basic support for `Authorization: Basic` token header        |
| ✅ CORS Support            | Adds proper headers and handles `OPTIONS` preflight          |
| ✅ Body Parsers            | Handles JSON, form-data, file uploads, and octet streams     |
| ✅ Modular Architecture    | Easily plug in routes, filters, and response logic           |

---

---

## 🚀 Getting Started

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

| Filter                   | Purpose                     |
|--------------------------|-----------------------------|
| `CorsInterceptor`        | Adds CORS headers + preflight |
| `BasicAuthInterceptor`   | Verifies bearer token       |
| `IPBlackListInterceptor` | Block specified IPs     |
| `RateLimitInterceptor`   | Basic per-IP request limiting |


## 📜 License

Apache 2. Contributions welcome!


