---
title: Kotam AOM - API Dokümantasyonu v1.0
language_tabs:
  - shell: Shell
  - http: HTTP
  - javascript: JavaScript
  - ruby: Ruby
  - python: Python
  - php: PHP
  - java: Java
  - go: Go
toc_footers: []
includes: []
search: true
highlight_theme: darkula
headingLevel: 2

---

<!-- Generator: Widdershins v4.0.1 -->

<h1 id="kotam-aom-api-dok-mantasyonu">Kotam AOM - API Dokümantasyonu v1.0</h1>

> Scroll down for code samples, example requests and responses. Select a language for code samples from the tabs above or the mobile navigation menu.

Kotam AOM projesi için geliştirilen REST API dökümantasyonu.

Base URLs:

* <a href="http://localhost:8080">http://localhost:8080</a>

Email: <a href="mailto:zelihapolat111@gmail.com">GitHub Proje Linki</a> Web: <a href="https://github.com/furkannayvz/KotamProject">GitHub Proje Linki</a> 

<h1 id="kotam-aom-api-dok-mantasyonu-m-teri-i-lemleri">Müşteri İşlemleri</h1>

Müşteri kaydı, giriş ve sorgulama işlemleri

## register

<a id="opIdregister"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:8080/api/customer/register?msisdn=string&name=string&surname=string&email=string&password=string&nationalId=string \
  -H 'Accept: */*'

```

```http
POST http://localhost:8080/api/customer/register?msisdn=string&name=string&surname=string&email=string&password=string&nationalId=string HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/customer/register?msisdn=string&name=string&surname=string&email=string&password=string&nationalId=string',
{
  method: 'POST',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:8080/api/customer/register',
  params: {
  'msisdn' => 'string',
'name' => 'string',
'surname' => 'string',
'email' => 'string',
'password' => 'string',
'nationalId' => 'string'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.post('http://localhost:8080/api/customer/register', params={
  'msisdn': 'string',  'name': 'string',  'surname': 'string',  'email': 'string',  'password': 'string',  'nationalId': 'string'
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:8080/api/customer/register', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/customer/register?msisdn=string&name=string&surname=string&email=string&password=string&nationalId=string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:8080/api/customer/register", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/customer/register`

<h3 id="register-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|msisdn|query|string|true|none|
|name|query|string|true|none|
|surname|query|string|true|none|
|email|query|string|true|none|
|password|query|string|true|none|
|nationalId|query|string|true|none|

> Example responses

> 200 Response

<h3 id="register-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|string|

<aside class="success">
This operation does not require authentication
</aside>

## login

<a id="opIdlogin"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:8080/api/customer/login \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:8080/api/customer/login HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "msisdn": 5551234567,
  "password": "1234abcDEF!"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/customer/login',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:8080/api/customer/login',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:8080/api/customer/login', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:8080/api/customer/login', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/customer/login");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:8080/api/customer/login", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/customer/login`

> Body parameter

```json
{
  "msisdn": 5551234567,
  "password": "1234abcDEF!"
}
```

<h3 id="login-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[LoginRequestDTO](#schemaloginrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="login-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[LoginResponseDTO](#schemaloginresponsedto)|

<aside class="success">
This operation does not require authentication
</aside>

## getCustomerByMsisdn

<a id="opIdgetCustomerByMsisdn"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/api/customer/{msisdn} \
  -H 'Accept: */*'

```

```http
GET http://localhost:8080/api/customer/{msisdn} HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/customer/{msisdn}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:8080/api/customer/{msisdn}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:8080/api/customer/{msisdn}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8080/api/customer/{msisdn}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/customer/{msisdn}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8080/api/customer/{msisdn}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/customer/{msisdn}`

<h3 id="getcustomerbymsisdn-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|msisdn|path|string|true|none|

> Example responses

> 200 Response

<h3 id="getcustomerbymsisdn-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[Customer](#schemacustomer)|

<aside class="success">
This operation does not require authentication
</aside>

## checkExists

<a id="opIdcheckExists"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/api/customer/exists?email=string&nationalId=string \
  -H 'Accept: */*'

```

```http
GET http://localhost:8080/api/customer/exists?email=string&nationalId=string HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/customer/exists?email=string&nationalId=string',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:8080/api/customer/exists',
  params: {
  'email' => 'string',
'nationalId' => 'string'
}, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:8080/api/customer/exists', params={
  'email': 'string',  'nationalId': 'string'
}, headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8080/api/customer/exists', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/customer/exists?email=string&nationalId=string");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8080/api/customer/exists", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/customer/exists`

<h3 id="checkexists-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|email|query|string|true|none|
|nationalId|query|string|true|none|

> Example responses

> 200 Response

<h3 id="checkexists-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|boolean|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="kotam-aom-api-dok-mantasyonu--ifre-kurtarma">Şifre Kurtarma</h1>

Şifre sıfırlama, kod gönderme ve doğrulama işlemleri

## verifyCode

<a id="opIdverifyCode"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:8080/api/auth/verify-code \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:8080/api/auth/verify-code HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "email": "zeliha@example.com",
  "code": 864312
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/auth/verify-code',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:8080/api/auth/verify-code',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:8080/api/auth/verify-code', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:8080/api/auth/verify-code', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/auth/verify-code");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:8080/api/auth/verify-code", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/auth/verify-code`

> Body parameter

```json
{
  "email": "zeliha@example.com",
  "code": 864312
}
```

<h3 id="verifycode-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[VerificationRequestDTO](#schemaverificationrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="verifycode-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|string|

<aside class="success">
This operation does not require authentication
</aside>

## resetPassword

<a id="opIdresetPassword"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:8080/api/auth/reset-password HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "email": "zeliha@example.com",
  "code": 864312,
  "newPassword": "YeniSifre123!",
  "nationalId": 12345678901,
  "confirmPassword": "YeniSifre123!"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/auth/reset-password',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:8080/api/auth/reset-password',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:8080/api/auth/reset-password', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:8080/api/auth/reset-password', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/auth/reset-password");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:8080/api/auth/reset-password", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/auth/reset-password`

> Body parameter

```json
{
  "email": "zeliha@example.com",
  "code": 864312,
  "newPassword": "YeniSifre123!",
  "nationalId": 12345678901,
  "confirmPassword": "YeniSifre123!"
}
```

<h3 id="resetpassword-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[ResetPasswordRequestDTO](#schemaresetpasswordrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="resetpassword-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|string|

<aside class="success">
This operation does not require authentication
</aside>

## forgotPassword

<a id="opIdforgotPassword"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:8080/api/auth/forgot-password HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "nationalId": 12345678901,
  "email": "zeliha@example.com"
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/auth/forgot-password',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:8080/api/auth/forgot-password',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:8080/api/auth/forgot-password', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:8080/api/auth/forgot-password', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/auth/forgot-password");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:8080/api/auth/forgot-password", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/auth/forgot-password`

> Body parameter

```json
{
  "nationalId": 12345678901,
  "email": "zeliha@example.com"
}
```

<h3 id="forgotpassword-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[ForgotPasswordRequestDTO](#schemaforgotpasswordrequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="forgotpassword-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|string|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="kotam-aom-api-dok-mantasyonu-paket-i-lemleri">Paket İşlemleri</h1>

Paket listeleme ve detay sorgulama işlemleri

## getPackageByName

<a id="opIdgetPackageByName"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/api/packages/name/{name} \
  -H 'Accept: */*'

```

```http
GET http://localhost:8080/api/packages/name/{name} HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/packages/name/{name}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:8080/api/packages/name/{name}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:8080/api/packages/name/{name}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8080/api/packages/name/{name}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/packages/name/{name}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8080/api/packages/name/{name}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/packages/name/{name}`

<h3 id="getpackagebyname-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|name|path|string|true|none|

> Example responses

> 200 Response

<h3 id="getpackagebyname-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[PackageEntity](#schemapackageentity)|

<aside class="success">
This operation does not require authentication
</aside>

## getPackageById

<a id="opIdgetPackageById"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/api/packages/id/{id} \
  -H 'Accept: */*'

```

```http
GET http://localhost:8080/api/packages/id/{id} HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/packages/id/{id}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:8080/api/packages/id/{id}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:8080/api/packages/id/{id}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8080/api/packages/id/{id}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/packages/id/{id}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8080/api/packages/id/{id}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/packages/id/{id}`

<h3 id="getpackagebyid-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|id|path|integer(int64)|true|none|

> Example responses

> 200 Response

<h3 id="getpackagebyid-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[PackageEntity](#schemapackageentity)|

<aside class="success">
This operation does not require authentication
</aside>

## getPackageIdByName

<a id="opIdgetPackageIdByName"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/api/packages/id-by-name/{name} \
  -H 'Accept: */*'

```

```http
GET http://localhost:8080/api/packages/id-by-name/{name} HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/packages/id-by-name/{name}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:8080/api/packages/id-by-name/{name}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:8080/api/packages/id-by-name/{name}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8080/api/packages/id-by-name/{name}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/packages/id-by-name/{name}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8080/api/packages/id-by-name/{name}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/packages/id-by-name/{name}`

<h3 id="getpackageidbyname-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|name|path|string|true|none|

> Example responses

> 200 Response

<h3 id="getpackageidbyname-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|integer|

<aside class="success">
This operation does not require authentication
</aside>

## getAllPackages

<a id="opIdgetAllPackages"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/api/packages/all \
  -H 'Accept: */*'

```

```http
GET http://localhost:8080/api/packages/all HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/packages/all',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:8080/api/packages/all',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:8080/api/packages/all', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8080/api/packages/all', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/packages/all");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8080/api/packages/all", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/packages/all`

> Example responses

> 200 Response

<h3 id="getallpackages-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|Inline|

<h3 id="getallpackages-responseschema">Response Schema</h3>

Status Code **200**

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|*anonymous*|[[PackageEntity](#schemapackageentity)]|false|none|[Paket veritabanı varlığını temsil eder]|
|» id|integer(int64)|false|none|Paketin benzersiz kimliği|
|» name|string|false|none|Paket adı|
|» dataQuota|integer(int64)|false|none|Veri kotası (MB)|
|» smsQuota|integer(int64)|false|none|SMS kotası|
|» minutesQuota|integer(int64)|false|none|Dakika kotası|
|» price|number(double)|false|none|Paket fiyatı (₺)|
|» period|integer(int32)|false|none|Paket geçerlilik süresi (gün)|
|» packageName|string|false|none|none|

<aside class="success">
This operation does not require authentication
</aside>

<h1 id="kotam-aom-api-dok-mantasyonu-bakiye-i-lemleri">Bakiye İşlemleri</h1>

Bakiye ekleme ve kontrol işlemleri

## insertBalance

<a id="opIdinsertBalance"></a>

> Code samples

```shell
# You can also use wget
curl -X POST http://localhost:8080/api/balances/balances \
  -H 'Content-Type: application/json' \
  -H 'Accept: */*'

```

```http
POST http://localhost:8080/api/balances/balances HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: */*

```

```javascript
const inputBody = '{
  "MSISDN": 5551234567,
  "PACKAGE_ID": 3
}';
const headers = {
  'Content-Type':'application/json',
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/balances/balances',
{
  method: 'POST',
  body: inputBody,
  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Content-Type' => 'application/json',
  'Accept' => '*/*'
}

result = RestClient.post 'http://localhost:8080/api/balances/balances',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Content-Type': 'application/json',
  'Accept': '*/*'
}

r = requests.post('http://localhost:8080/api/balances/balances', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Content-Type' => 'application/json',
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('POST','http://localhost:8080/api/balances/balances', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/balances/balances");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("POST");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Content-Type": []string{"application/json"},
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("POST", "http://localhost:8080/api/balances/balances", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`POST /api/balances/balances`

> Body parameter

```json
{
  "MSISDN": 5551234567,
  "PACKAGE_ID": 3
}
```

<h3 id="insertbalance-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|body|body|[BalanceRequestDTO](#schemabalancerequestdto)|true|none|

> Example responses

> 200 Response

<h3 id="insertbalance-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|string|

<aside class="success">
This operation does not require authentication
</aside>

## getBalanceByMsisdn

<a id="opIdgetBalanceByMsisdn"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/api/balances/{msisdn} \
  -H 'Accept: */*'

```

```http
GET http://localhost:8080/api/balances/{msisdn} HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/balances/{msisdn}',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:8080/api/balances/{msisdn}',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:8080/api/balances/{msisdn}', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8080/api/balances/{msisdn}', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/balances/{msisdn}");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8080/api/balances/{msisdn}", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/balances/{msisdn}`

<h3 id="getbalancebymsisdn-parameters">Parameters</h3>

|Name|In|Type|Required|Description|
|---|---|---|---|---|
|msisdn|path|string|true|none|

> Example responses

> 200 Response

<h3 id="getbalancebymsisdn-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|[BalanceDTO](#schemabalancedto)|

<aside class="success">
This operation does not require authentication
</aside>

## getMaxBalanceId

<a id="opIdgetMaxBalanceId"></a>

> Code samples

```shell
# You can also use wget
curl -X GET http://localhost:8080/api/balances/max-id \
  -H 'Accept: */*'

```

```http
GET http://localhost:8080/api/balances/max-id HTTP/1.1
Host: localhost:8080
Accept: */*

```

```javascript

const headers = {
  'Accept':'*/*'
};

fetch('http://localhost:8080/api/balances/max-id',
{
  method: 'GET',

  headers: headers
})
.then(function(res) {
    return res.json();
}).then(function(body) {
    console.log(body);
});

```

```ruby
require 'rest-client'
require 'json'

headers = {
  'Accept' => '*/*'
}

result = RestClient.get 'http://localhost:8080/api/balances/max-id',
  params: {
  }, headers: headers

p JSON.parse(result)

```

```python
import requests
headers = {
  'Accept': '*/*'
}

r = requests.get('http://localhost:8080/api/balances/max-id', headers = headers)

print(r.json())

```

```php
<?php

require 'vendor/autoload.php';

$headers = array(
    'Accept' => '*/*',
);

$client = new \GuzzleHttp\Client();

// Define array of request body.
$request_body = array();

try {
    $response = $client->request('GET','http://localhost:8080/api/balances/max-id', array(
        'headers' => $headers,
        'json' => $request_body,
       )
    );
    print_r($response->getBody()->getContents());
 }
 catch (\GuzzleHttp\Exception\BadResponseException $e) {
    // handle exception or api errors.
    print_r($e->getMessage());
 }

 // ...

```

```java
URL obj = new URL("http://localhost:8080/api/balances/max-id");
HttpURLConnection con = (HttpURLConnection) obj.openConnection();
con.setRequestMethod("GET");
int responseCode = con.getResponseCode();
BufferedReader in = new BufferedReader(
    new InputStreamReader(con.getInputStream()));
String inputLine;
StringBuffer response = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    response.append(inputLine);
}
in.close();
System.out.println(response.toString());

```

```go
package main

import (
       "bytes"
       "net/http"
)

func main() {

    headers := map[string][]string{
        "Accept": []string{"*/*"},
    }

    data := bytes.NewBuffer([]byte{jsonReq})
    req, err := http.NewRequest("GET", "http://localhost:8080/api/balances/max-id", data)
    req.Header = headers

    client := &http.Client{}
    resp, err := client.Do(req)
    // ...
}

```

`GET /api/balances/max-id`

> Example responses

> 200 Response

<h3 id="getmaxbalanceid-responses">Responses</h3>

|Status|Meaning|Description|Schema|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|OK|integer|

<aside class="success">
This operation does not require authentication
</aside>

# Schemas

<h2 id="tocS_LoginRequestDTO">LoginRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemaloginrequestdto"></a>
<a id="schema_LoginRequestDTO"></a>
<a id="tocSloginrequestdto"></a>
<a id="tocsloginrequestdto"></a>

```json
{
  "msisdn": 5551234567,
  "password": "1234abcDEF!"
}

```

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|msisdn|string|false|none|Müşterinin telefon numarası|
|password|string|false|none|Müşterinin şifresi|

<h2 id="tocS_LoginResponseDTO">LoginResponseDTO</h2>
<!-- backwards compatibility -->
<a id="schemaloginresponsedto"></a>
<a id="schema_LoginResponseDTO"></a>
<a id="tocSloginresponsedto"></a>
<a id="tocsloginresponsedto"></a>

```json
{
  "msisdn": 5551234567,
  "name": "Zeliha",
  "surname": "Polat",
  "email": "zeliha@example.com",
  "nationalId": 12345678901,
  "packageEntity": {
    "id": 1,
    "name": "Sosyal Paket",
    "dataQuota": 5000,
    "smsQuota": 1000,
    "minutesQuota": 500,
    "price": 39.9,
    "period": 30,
    "packageName": "string"
  },
  "sdate": "2019-08-24T14:15:22Z"
}

```

Kullanıcının giriş işlemi sonrası dönen bilgiler

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|msisdn|string|false|none|MSISDN (telefon numarası)|
|name|string|false|none|Müşteri adı|
|surname|string|false|none|Müşteri soyadı|
|email|string|false|none|Müşteri e-posta adresi|
|nationalId|string|false|none|Müşteri T.C. kimlik numarası|
|packageEntity|[PackageEntity](#schemapackageentity)|false|none|Seçilen paketin bilgileri|
|sdate|string(date-time)|false|none|none|

<h2 id="tocS_PackageEntity">PackageEntity</h2>
<!-- backwards compatibility -->
<a id="schemapackageentity"></a>
<a id="schema_PackageEntity"></a>
<a id="tocSpackageentity"></a>
<a id="tocspackageentity"></a>

```json
{
  "id": 1,
  "name": "Sosyal Paket",
  "dataQuota": 5000,
  "smsQuota": 1000,
  "minutesQuota": 500,
  "price": 39.9,
  "period": 30,
  "packageName": "string"
}

```

Paket veritabanı varlığını temsil eder

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|Paketin benzersiz kimliği|
|name|string|false|none|Paket adı|
|dataQuota|integer(int64)|false|none|Veri kotası (MB)|
|smsQuota|integer(int64)|false|none|SMS kotası|
|minutesQuota|integer(int64)|false|none|Dakika kotası|
|price|number(double)|false|none|Paket fiyatı (₺)|
|period|integer(int32)|false|none|Paket geçerlilik süresi (gün)|
|packageName|string|false|none|none|

<h2 id="tocS_BalanceRequestDTO">BalanceRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemabalancerequestdto"></a>
<a id="schema_BalanceRequestDTO"></a>
<a id="tocSbalancerequestdto"></a>
<a id="tocsbalancerequestdto"></a>

```json
{
  "MSISDN": 5551234567,
  "PACKAGE_ID": 3
}

```

Bakiye oluşturmak için gerekli istek verilerini içerir

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|MSISDN|string|false|none|MSISDN (telefon numarası)|
|PACKAGE_ID|integer(int64)|false|none|Seçilen paket ID’si|

<h2 id="tocS_VerificationRequestDTO">VerificationRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemaverificationrequestdto"></a>
<a id="schema_VerificationRequestDTO"></a>
<a id="tocSverificationrequestdto"></a>
<a id="tocsverificationrequestdto"></a>

```json
{
  "email": "zeliha@example.com",
  "code": 864312
}

```

Kullanıcının doğrulama kodunu kontrol etmek için gönderdiği bilgiler

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|email|string|false|none|Kullanıcının e-posta adresi|
|code|string|false|none|Doğrulama kodu|

<h2 id="tocS_ResetPasswordRequestDTO">ResetPasswordRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemaresetpasswordrequestdto"></a>
<a id="schema_ResetPasswordRequestDTO"></a>
<a id="tocSresetpasswordrequestdto"></a>
<a id="tocsresetpasswordrequestdto"></a>

```json
{
  "email": "zeliha@example.com",
  "code": 864312,
  "newPassword": "YeniSifre123!",
  "nationalId": 12345678901,
  "confirmPassword": "YeniSifre123!"
}

```

Şifre sıfırlama işlemi için gönderilen bilgiler

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|email|string|false|none|Kullanıcının e-posta adresi|
|code|string|false|none|E-posta ile gönderilen doğrulama kodu|
|newPassword|string|false|none|Yeni şifre|
|nationalId|string|false|none|T.C. kimlik numarası|
|confirmPassword|string|false|none|Yeni şifrenin tekrarı|

<h2 id="tocS_ForgotPasswordRequestDTO">ForgotPasswordRequestDTO</h2>
<!-- backwards compatibility -->
<a id="schemaforgotpasswordrequestdto"></a>
<a id="schema_ForgotPasswordRequestDTO"></a>
<a id="tocSforgotpasswordrequestdto"></a>
<a id="tocsforgotpasswordrequestdto"></a>

```json
{
  "nationalId": 12345678901,
  "email": "zeliha@example.com"
}

```

Şifremi unuttum talebi için kullanıcı bilgileri

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|nationalId|string|false|none|T.C. kimlik numarası|
|email|string|false|none|Kullanıcının e-posta adresi|

<h2 id="tocS_Customer">Customer</h2>
<!-- backwards compatibility -->
<a id="schemacustomer"></a>
<a id="schema_Customer"></a>
<a id="tocScustomer"></a>
<a id="tocscustomer"></a>

```json
{
  "msisdn": 5551234567,
  "name": "Zeliha",
  "surname": "Polat",
  "email": "zeliha@example.com",
  "getsDate": "2024-07-26T12:00:00Z",
  "nationalId": 12345678901,
  "packageEntity": {
    "id": 1,
    "name": "Sosyal Paket",
    "dataQuota": 5000,
    "smsQuota": 1000,
    "minutesQuota": 500,
    "price": 39.9,
    "period": 30,
    "packageName": "string"
  }
}

```

Müşteri bilgilerini temsil eder

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|msisdn|string|false|none|Müşterinin telefon numarası|
|name|string|false|none|Müşteri adı|
|surname|string|false|none|Müşteri soyadı|
|email|string|false|none|E-posta adresi|
|getsDate|string(date-time)|false|none|Kayıt tarihi|
|nationalId|string|false|none|T.C. kimlik numarası|
|packageEntity|[PackageEntity](#schemapackageentity)|false|none|Paket veritabanı varlığını temsil eder|

<h2 id="tocS_BalanceDTO">BalanceDTO</h2>
<!-- backwards compatibility -->
<a id="schemabalancedto"></a>
<a id="schema_BalanceDTO"></a>
<a id="tocSbalancedto"></a>
<a id="tocsbalancedto"></a>

```json
{
  "balanceId": 101,
  "msisdn": 5551234567,
  "leftData": 2500,
  "leftSms": 500,
  "leftMinutes": 400,
  "getsDate": "2024-07-26T10:30:00Z",
  "packageEntity": {
    "id": 2,
    "dataQuota": 5000,
    "smsQuota": 1000,
    "minutesQuota": 500,
    "price": 39.9,
    "period": 30,
    "packageName": "Sosyal Paket"
  }
}

```

Kullanıcının mevcut bakiye durumunu temsil eder

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|balanceId|integer(int64)|false|none|Bakiye ID|
|msisdn|string|false|none|MSISDN (telefon numarası)|
|leftData|integer(int64)|false|none|Kalan veri (MB)|
|leftSms|integer(int64)|false|none|Kalan SMS|
|leftMinutes|integer(int64)|false|none|Kalan dakika|
|getsDate|string(date-time)|false|none|Paketin alındığı tarih|
|packageEntity|[PackageDTO](#schemapackagedto)|false|none|Kullanıcının aktif paketi|

<h2 id="tocS_PackageDTO">PackageDTO</h2>
<!-- backwards compatibility -->
<a id="schemapackagedto"></a>
<a id="schema_PackageDTO"></a>
<a id="tocSpackagedto"></a>
<a id="tocspackagedto"></a>

```json
{
  "id": 2,
  "dataQuota": 5000,
  "smsQuota": 1000,
  "minutesQuota": 500,
  "price": 39.9,
  "period": 30,
  "packageName": "Sosyal Paket"
}

```

API'de kullanılan paket bilgilerinin DTO modeli

### Properties

|Name|Type|Required|Restrictions|Description|
|---|---|---|---|---|
|id|integer(int64)|false|none|Paket ID|
|dataQuota|integer(int32)|false|none|Veri kotası (MB)|
|smsQuota|integer(int32)|false|none|SMS kotası|
|minutesQuota|integer(int32)|false|none|Dakika kotası|
|price|number(double)|false|none|Paket fiyatı (₺)|
|period|integer(int32)|false|none|Paket süresi (gün)|
|packageName|string|false|none|Paket adı|

