# Webcomponents java-Proxy-utils 
The java-proxy-utils are usefull to implement a Java (1.7) based proxy for the Webcomponent Frontend.
You can simply use it in your Java Servlet for different usecases:

- route the incoming HttpServletRequests to the FACTFinder REST interface and automatically redirect the response to the HTTPServletResponse.
- just route the incoming HttpServletRequests to the FACTFinder REST interface and use the Abstracted Response as you wish.
- modify the request to the FACTFinder REST interface and work with the response.
- manually send a request to the FACTFinder Service


## Simplified Authentication
In every request to the FACTFinder REST interface the SDK will inject your authentication properties into the security mechanism so you don't have to bother with that.
Just set your credentials in the settings an you are reay to go.

## How to use
```java
	private ProxyUtils			utils;
	private FACTFinderSettings	settings;

	@Override
	public void init() throws ServletException {
		settings = new FACTFinderSettings();
		settings.setAccount("admin");
		settings.setPassword("myPassowrd");
		// url to your FACTFinder instance for example:
		settings.setUrl("http://my-factfinder-isntacne.de/FACT-Finder-7.2");
		utils = new ProxyUtils(settings);
		super.init();
	}  
```

Now you can use he SDK to interact with FACTFinder:

```java
	// 1. just redirect
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				utils.redirectGET(req, resp);
	};

	// 2. Manually send request
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		utils.copyHeaders(req, resp);
		String json = utils.sendRequest(req).getData();
		utils.writeResponse(resp, json);
	};
```

For Webcomponents compatibility you just need to redirect the HTTP OPTIONS call  
 
```java
	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		utils.redirectOPTIONS(req, resp);
	}  
```

## Java Domain Model
To modify the Response from the FACTFinder Search engine you can create a Object Model with a variety of different JSON Parser available.
We provide a Basic Object Model parsed with [Gson](https://github.com/google/gson) in the following project: [java-factfinder-domain-model](https://github.com/FACT-Finder-Web-Components/java-factfinder-domain-model)



 

