# FACTFinder Webcomponents Helper SDK
The Helper SDK is usefull to implement a Java based Proxy for the Webcomponent Frontend.
You can simply use it in your Java Servlet for different usecases:

- route the incoming HttpServletRequests to the FACTFinder REST interface and automaticaly redirect the response to the HTTPServletResponse.
- just route the incoming HttpServletRequests to the FACTFinder REST interface and use the Abstracted Response as you whish.
- Modify the request to the FACTFinder REST interface and work with the response.


## Simplyfied Authenthification
In every request to the FACTFinder REST interface the SDK will inject your authentication properties into the Security Mechanism so you don't have to bother with that.
Just set your credentials in the settings an you are reay to go.

## How to use
```java
	private HelperSDK				sdk;
	private FACTFinderSettings	settings;

	@Override
	public void init() throws ServletException {
		settings = new FACTFinderSettings();
		settings.setAccount("admin");
		settings.setPassword("myPassowrd");
		// url to your FACTFinder instance for example:
		settings.setUrl("http://web-components.fact-finder.de/FACT-Finder-7.2");
		sdk = new HelperSDK(settings);
		super.init();
	}  
```
Now you can use he SDK to interact with FACTFinder:
```java
	// 1. just redirect
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				sdk.redirectGET(req, resp);
	};

	// 2. Manually send request
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sdk.copyHeaders(req, resp);
		String json = sdk.sendRequest(req).getData();
		sdk.writeResponse(resp, json);
	};
```
For Webcomponents compatibility You just need to redirect the HTTP OPTIONS call   
```java
	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		sdk.redirectOPTIONS(req, resp);
	}  
```

## Java Object Model
To modify the Response from the FACTFinder Search engine you can create a Object Model with a variety  of different JSON Parser available.
We provide a Basic Object Model parsed with [Gson](https://github.com/google/gson) in the following project:   https://github.com/FACT-Finder/ff-json-gson-parser



 

