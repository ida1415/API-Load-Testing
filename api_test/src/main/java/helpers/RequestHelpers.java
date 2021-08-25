package helpers;

import Specififactions.RequestSpecifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Post;
import model.Comment;
import model.User;
import static io.restassured.RestAssured.given;

public class RequestHelpers {
    public static String TOKEN = "";

    //   get a json token from the login request
    public static String getAuthToken () {

        User testUser = new User(
                "Pablo Juan",
                "Alexie_Robel@yahoo.com",
                "pasword");

        Response response = given().body(testUser).post("/v1/user/login");
        JsonPath jsonPath = response.jsonPath();
        TOKEN = jsonPath.get("token.access_token");
        System.out.println("New token fetched" + TOKEN);
        return TOKEN;
    }

    public static int createRandomPostAndGetID () {
        Post randomPost = new Post("randome", "randome");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(randomPost)
                .when()
                .log().all()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get("id");
    }

    public static void cleanUpPost (int id) {
        given().spec(RequestSpecifications.useJWTAuthentication())
                .delete("/v1/post/"+ id);
    }

    public static int createRandomCommentAndGetID (int postId) {
        Comment randomComment = new Comment("randome", "randome");
        Response response = given().spec(RequestSpecifications.userBasicAuthentication())
                .body(randomComment)
                .when()
                .log().all()
                .post("/v1/comment/" + postId);

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get("id");
    }
    public static void cleanUpComment (int commentId, int postId) {
        given().spec(RequestSpecifications.userBasicAuthentication())
                .delete("/v1/comment/"+ postId +"/"+commentId);
    }
}