import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceholderTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    @Test
    public void getPostById_shouldReturnValidPost() {
        RestAssured.given().
                baseUri(BASE_URL).
                when().
                get("/posts/1").
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("id", Matchers.equalTo(1)).
                body("userId", Matchers.equalTo(1)).
                body("title", Matchers.not(Matchers.emptyOrNullString()));
    }

    @Test
    public void getAllPosts_shouldReturn100Posts() {
        RestAssured.given().
                baseUri(BASE_URL).
                when().
                get("/posts").
                then().
                statusCode(200).
                body("", Matchers.hasSize(100));
    }

    @Test
    public void getPostByInvalidId_shouldReturn404() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get("/posts/9999");

        assertEquals(404, response.getStatusCode(), "Expected 404 Not Found");
        assertEquals("{}", response.getBody().asString(), "Expected empty JSON object as response");
    }

    @Test
    public void getFromInvalidEndpoint_shouldReturn404() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get("/invalid-endpoint");

        assertEquals(404, response.getStatusCode(), "Expected 404 from invalid endpoint");
    }
    @Test
    public void getCommentsForPost_shouldReturnValidComments() {
        RestAssured.given().
                baseUri(BASE_URL).
                when().
                get("/posts/1/comments").
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("size()", Matchers.greaterThan(0)).
                body("[0].postId", Matchers.equalTo(1)).
                body("[0].email", Matchers.containsString("@"));
    }

    @Test
    public void getCommentsForInvalidPost_shouldReturnEmptyList() {
        RestAssured.given().
                baseUri(BASE_URL).
                when().
                get("/posts/9999/comments").
                then().
                statusCode(200).
                body("size()", Matchers.equalTo(0));
    }
    @Test
    public void getPostById_assertionsStyle() {
        Response response = RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get("/posts/1");

        assertEquals(200, response.getStatusCode(), "Status code should be 200");

        int userId = response.jsonPath().getInt("userId");
        String title = response.jsonPath().getString("title");

        assertEquals(1, userId, "User ID should be 1");
        assertNotNull(title, "Title should not be null");
        assertFalse(title.isEmpty(), "Title should not be empty");
    }
}
