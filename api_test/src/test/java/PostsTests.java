import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import io.restassured.response.Response;
import model.Post;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class PostsTests extends Base{

    @Test(description = "This test aims to create a new Post")
    public void createPostTest(){

        Post testPost = new Post("Harry Potter 1", "Harry Potter and the Philosopher's Stone");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .post("/v1/post")
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Post created"));
    }

    @Test(description = "This test aims to create a invalid Post")
    public void createPostInvalidTest(){

        Post testPost = new Post("Post with invalid form", "");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .post("/v1/post")
                .then()
                .log().all()
                .body("message", Matchers.equalTo("Invalid form"));
    }

    @Test(description = "This test aims to get one post")
    public void getOnePostTest(){

        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/post/" + postId);

        assertThat(response.asString(), matchesJsonSchemaInClasspath("post.schema.json"));
        assertThat(response.path("data.id"), Matchers.equalTo(postId));

    }
    @Test(description = "This test aims to get one post not Authenticated test")
    public void getOnePostNotAuthenticatedTest(){

                given()
                .when()
                .get("/v1/post/" + postId)
                .then()
                .log().all()
                .statusCode(401)
                .body("message", Matchers.equalTo("Please login first"));
    }

    @Test(description = "This test aims to get all posts")
    public void getAllPostsTest(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/posts")
                .then()
                .log().all()
                .statusCode(200)
                .body("results[0].data[0].id", Matchers.equalTo(postId));
    }

    @Test(description = "This test aims to get all invalid Posts")
    public void getAllPostsInvalidTest(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/invalidPost")
                .then()
                .log().all()
                .statusCode(404)
                .body(Matchers.containsString("Opss!! 404 again?"));
    }
    @Test(description = "This test aims to edit Post")
    public void putPostTest(){
        Post testPost = new Post("Harry Potter 2", "Harry Potter and the Philosopher's Stone");
        given().spec(RequestSpecifications.useJWTAuthentication())
               .body(testPost)
               .when()
               .put("/v1/post/" + postId)
               .then()
               .log().all()
               .statusCode(200)
               .body("message", Matchers.equalTo("Post updated"));
    }

    @Test(description = "This test aims to edit a negative id Post")
    public void putPostInvalidIdTest(){

        Post testPost = new Post("Invalid id", "This a test");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .put("/v1/post/" + -1)
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Post could not be updated"));
    }
    @Test(description = "This test aims to detele a Post")
    public void deletePostTest(){

        Post testPost = new Post("Harry Potter 3", "Harry Potter and the Prisoner of Azkaban");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .delete("/v1/post/" + postId)
                .then()
                .log().all()
                .statusCode(200)
                .body("message", Matchers.equalTo("Post deleted"));
    }

    @Test(description = "This test aims to detele a invalid Post")
    public void deletePostInvalidTest(){

        Post testPost = new Post("Harry Potter 3", "Harry Potter and the Prisoner of Azkaban");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .delete("/v1/post/invalid")
                .then()
                .log().all()
                .statusCode(404)
                .body("message", Matchers.equalTo("Invalid parameter"));
    }
}