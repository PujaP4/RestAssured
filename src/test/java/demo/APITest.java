package demo;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class APITest {
	
	public class secondTest {
		@Test
		public void test() {
			
			Response response = get("https://api.restful-api.dev/objects");
			baseURI = "https://api.restful-api.dev";
			given().get("/objects").then().statusCode(200);
		}
		
		@Test
		public void Get() {
			baseURI = "https://api.restful-api.dev/";
			given().
				get("objects").
			then().
				contentType("application/json"). //we can write in multiple and singlle line 'below line'
				body("id[2]", equalTo("3"),"name[2]", is("Apple iPhone 12 Pro Max"), "data[2].color", equalTo("Cloudy White")).
				body("data[3].price", is(389.99f)). //to get double or floating number end wit 'f' or 'd'
				body("data[3].color", equalTo("Purple"));
		}
		
//		public void convertJsonFloatDoubleNumberToBigDecimal() {
//			baseURI = "https://api.restful-api.dev/";
//			given().
//			config(RestAssured.config(jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAl)))).
//			when().
//			get("objects").
//			then().
//			body("data[3]", is(new BigDecimal(389.99)));
//		}
		
		@Test
		public String post() {
			baseURI = "https://api.restful-api.dev/";
			//fisrt way
//			Map<String, Object> map = new HashMap<String, Object>();
//			map("name", "Apple MacBook Pro 16");
//			map("year", 2019);
//			
//			//another way using request
//			
//			JSONObject print = new JSONObject(map);
//			System.out.println(print.toJSONString());
//			
			
			//now actual way to do this
			//line number 56 57 will be there but remove map from 56
			
			JSONObject data = new JSONObject();
			
			JSONObject request = new JSONObject(); 
			
			request.put("name", "Apple MacBook Pro 16");
			data.put("year", 2019);
			data.put("price", 1849.99);
			data.put("CPU model", "Intel Core i9");
			data.put("Hard disk size", "1 TB");
			
			request.put("data", data);
			
			System.out.println(request.toJSONString());
			///Post Request 	
			
			String id = given().
			contentType("application/json").
			body(request.toJSONString()).
			when().
			post("objects"). then().statusCode(200)
			.extract().path("id"); //to exract any element or key's value of json body
			
			Response response = RestAssured.get("objects");
			System.out.println(response.getBody().asString());
			
			return id;
			
		}
		
		@Test
		public void put() {
			baseURI = "https://api.restful-api.dev/";
			String id = post();
			
			//create object

			
//			Map<String, Object> data = new HashMap<String, Object>();
			
			JSONObject data = new JSONObject();
			JSONObject request = new JSONObject();
			request.put("name", "Apple MacBook Pro 16");
			data.put("year", 2019);
			data.put("price", 2049.99);
			data.put("CPU model", "Intel Core i9");
			data.put("Hard disk size", "1 TB");
			data.put("color", "silver");
			
			request.put("data", data);
			
			System.out.println(request.toJSONString());
			
			given().
			contentType("application/json").
			body(request.toJSONString()).
			when().
			put("objects/" + id).then().
			statusCode(200);
		}
		
		@Test
		public void patch()
		{
			baseURI = "https://api.restful-api.dev/";
			String id = post();
			JSONObject request = new JSONObject();
			request.put("name", "Apple MacBook Pro 16 (Updated Name)");
			JSONObject data = new JSONObject();
			data.put("year", 2019);
			
			request.put("data", data);
			
			Float price = given().contentType("application/json").body(request.toJSONString()).
			when().patch("objects/" +id).then().statusCode(200).extract().path("data.price");
			System.out.println("The price of this item, is " +price);
			
		}
		
		@Test
		public void Delete() {
			baseURI = "https://api.restful-api.dev/";
			String id = post();
			
			JSONObject request = new JSONObject();
			request.put("message", "Object with id = 6, has been deleted.");
			
			given().contentType("application/json").body(request.toJSONString()).
			when().delete("objects/" +id).then().statusCode(200);
		
		}
		
		//Authentication
		
		//1. Basic auth --> Username passsword
		
		public void basicAuth() {
			
			baseURI = "";
			given().auth().basic("username", "Password").
					when().get("").then().statusCode(200);
		}
		
		//2 premitive basic auth
		
		public void PreAuth()
		{
			given().baseUri(baseURI).auth().preemptive().basic("username", "Pasword").
			when().get("").then().statusCode(200);
		}
		
		//3 bearer token
		
		public String bearerToken()
		
		{
			JSONObject request = new JSONObject();
			request.put("username", "username");
			request.put("passowrd", "paswrd");
			
			String token =
			given().baseUri("url/").
					contentType(ContentType.JSON).  //we can writhe this line like this as well
					body(request.toJSONString()).
					when().
					post("/url"). //when we are using post put patch request then only use contentType
					then().
					statusCode(200).
					extract().
					path("token");
			
			return token;
		}
		
		// ReuseBaerer token
		
		public void secondTest() {
			String token = bearerToken();
			RequestSpecification requestSpec = 
					given().
					baseUri("").
					header("Authentication", "Bearer"+ token).
					contentType(ContentType.JSON);
			
		}
		
		//API key in header
		
		public void apiKeyHeader() {
			baseURI = "";
			given().
			header("x-api-Key", "your API key").
			when().
			get("").then().
			statusCode(200);
		}
		
		//API key in Query param
		
		public void queryParamAPIKey()
		{
			baseURI = "";
			
			given().
			queryParam("api_key", "your API Key").
			when().
			get("").then().
			statusCode(200);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
