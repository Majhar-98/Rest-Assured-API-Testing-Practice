import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import java.io.IOException;
import static io.restassured.RestAssured.given;

public class User extends Setup{

   @Test
    public void callingLoginAPI() throws ConfigurationException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .body("{\n" +
                                "    \"email\":\"salman@roadtocareer.net\",\n" +
                                "    \"password\":\"1234\"\n" +
                                "}")
                        .when( )
                        .post("/user/login")
                        .then( )
                        .assertThat( ).statusCode(200).extract( ).response( );

        JsonPath jsonpath = res.jsonPath( );
        String token = jsonpath.get("token");
        System.out.println(token);
        String message = jsonpath.get("message");

        Utils.setEnvVariable("token", token);

        System.out.println(token);
        System.out.println(message);

    }

  @Test
    public void loginWithIncorrectEmail() throws ConfigurationException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .body("{\n" +
                                "    \"email\":\"towsif.6@roadtocareer.net\",\n" +
                                "    \"password\":\"1234\"\n" +
                                "}")
                        .when( )
                        .post("/user/login")
                        .then( )
                        .assertThat( ).statusCode(404).extract( ).response( );

        JsonPath jsonpath = res.jsonPath( );
        String token = jsonpath.get("token");
        String message = jsonpath.get("message");

        Utils.setEnvVariable("token", token);

        System.out.println(token);
        System.out.println(message);

    }

   @Test
    public void loginWithIncorrectPassword() throws ConfigurationException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .body("{\n" +
                                "    \"email\":\"salman@roadtocareer.net\",\n" +
                                "    \"password\":\"1846\"\n" +
                                "}")
                        .when( )
                        .post("/user/login")
                        .then( )
                        .assertThat( ).statusCode(401).extract( ).response( );

        JsonPath jsonpath = res.jsonPath( );
        String token = jsonpath.get("token");
        String message = jsonpath.get("message");

        Utils.setEnvVariable("token", token);

        System.out.println(token);
        System.out.println(message);


    }

    @Test
    public void blankPassword() throws ConfigurationException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .body("{\n" +
                                "    \"email\":\"salman@roadtocareer.net\",\n" +
                                "    \"password\":\"\"\n" +
                                "}")
                        .when( )
                        .post("/user/login")
                        .then( )
                        .assertThat( ).statusCode(401).extract( ).response( );

        JsonPath jsonpath = res.jsonPath( );
        String token = jsonpath.get("token");
        String message = jsonpath.get("message");

        Utils.setEnvVariable("token", token);

        System.out.println(token);
        System.out.println(message);

    }

   @Test
    public void getUserList() throws IOException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                        .when( )
                        .get("/user/list")
                        .then( )
                        .assertThat( ).statusCode(200).extract( ).response( );

        JsonPath response = res.jsonPath( );
        System.out.println(response.get().toString());
        //System.out.println(res.asString());
        //System.out.println(response.get("users").toString( ));

    }

    @Test
    public void searchUserById() throws IOException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY","ROADTOSDET")
                        .when( )
                        .get("/user/search/id/1233")
                        .then( )
                        .assertThat( ).statusCode(200).extract( ).response( );

        JsonPath response = res.jsonPath( );
        //System.out.println(response.get().toString());
        System.out.println(res.asString());
        //System.out.println(response.get("users[0].id").toString( ));

    }

  @Test
    public void searchUserByInvalidId() throws IOException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY","ROADTOSDET")
                        .when( )
                        .get("/user/search/id/70000")
                        .then( )
                        .assertThat( ).statusCode(404).extract( ).response( );

        JsonPath response = res.jsonPath( );
        System.out.println(response.get().toString());
        //System.out.println(res.asString());
        //System.out.println(response.get("users[7].id").toString( ));

    }

    @Test
    public void searchUserByInvalidPhoneNumber() throws IOException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .header("Authorization", props.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY","ROADTOSDET")
                        .when( )
                        .get("/user/search/01844995980")
                        .then( )
                        .assertThat( ).statusCode(200).extract( ).response( );

        JsonPath response = res.jsonPath( );
        System.out.println(response.get().toString());
        //System.out.println(res.asString());
        //System.out.println(response.get("users[0].id").toString( ));

    }

   @Test
    public void getUserListForIncorrectAuth() throws IOException {

        RestAssured.baseURI = props.getProperty("base_url");
        Response res =
                given( )
                        .contentType("application/json")
                        .header("Authorization", "incorrect token")
                        .when( )
                        .get("/user/list")
                        .then( )
                        .assertThat( ).statusCode(403).extract( ).response( );

        //System.out.println(res.asString());
        //JsonPath response = res.jsonPath();
        JsonPath response = res.jsonPath( );
        System.out.println(response.get("error.message").toString( ));

    }

    @Test
    public void getUserListForBlankAuthorizationToken() throws IOException {

        RestAssured.baseURI = props.getProperty("base_url");

        Response res =
                given( )
                        .contentType("application/json")
                        .header("Authorization", "")
                        .when( )
                        .get("/user/list")
                        .then( )
                        .assertThat( ).statusCode(401).extract( ).response( );

        //System.out.println(res.asString());
        //JsonPath response = res.jsonPath();
        JsonPath response = res.jsonPath( );
        System.out.println(response.get("error.message").toString( ));
    }
    

}
