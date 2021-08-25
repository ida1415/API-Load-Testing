import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import io.restassured.response.Response;
import model.Comment;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommentsTests extends Base{

    @Test(description = "This test aims to create a new Comment")
    public void createCommentTest(){
        Comment testCreateComment = new Comment ("Luis", "This a test");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testCreateComment)
                .when()
                .post("/v1/comment/"+ postId)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Comment created"));
    }

    @Test(description = "This test aims to create a invalid Comment")
    public void createCommentInvalidTest(){

        Comment testCreateComment = new Comment ("Ana", "This a test");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testCreateComment)
                .when()
                .post("/v1/comment/"+commentId)
                .then()
                .log().all()
                .body("message", Matchers.equalTo("Comment could not be created"));
    }

    @Test(description = "This test aims to get one comment",groups = "useComment")
    public void getOneCommentTest(){

        Response response = given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .get("/v1/comment/" + postId +"/"+ commentId);

        assertThat(response.asString(), matchesJsonSchemaInClasspath("comment.schema.json"));
        assertThat(response.path("data.id"), Matchers.equalTo(commentId));

    }
    @Test(description = "This test aims to get one invalid comment")
    public void getOneCommentInvalidTest(){

        given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .get("/v1/comment/" +postId +"/"+ commentId)
                .then()
                .log().all()
                .statusCode(404)
                .body("Message", Matchers.equalTo("Comment not found"));
    }
    @Test(description = "This test aims to get all comments",groups = "useComment")
    public void getAllCommentsTest(){

        given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .get("/v1/comments/"+postId )
                .then()
                .log().all()
                .statusCode(200)
                .body("results[0].data[0].id", Matchers.equalTo(commentId));
    }
    @Test(description = "This test aims to get all invalid comments")
    public void getAllCommentInvalidTest(){

        given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .get("/v1/comments/10000000000000")
                .then()
                .log().all()
                .statusCode(406)
                .body("Message", Matchers.equalTo("Could not get comments"));
    }

    @Test(description = "This test aims to edit Comment",groups = "useComment")
    public void putCommentTest() {

        Comment testCreateComment = new Comment("Hermione", "This a test");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testCreateComment)
                .when()
                .put("/v1/comment/" + postId +"/"+commentId)
                .then()
                .log().all()
                .statusCode(200)
                .body("message", Matchers.equalTo("Comment updated"));
    }

    @Test(description = "This test aims to not edit Comment")
    public void putCommentInvalidTest() {

       Comment testCreateComment = new Comment("Hermione", "This a test");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testCreateComment)
                .when()
                .put("/v1/comment/" + postId +"/"+commentId)
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Comment could not be updated"));
    }
    @Test(description = "This test aims to detele a Comment",groups = "useComment")
    public void deleteCommentTest(){

        Comment testCreateComment = new Comment("Hermione", "This a test");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testCreateComment)
                .when()
                .delete("/v1/comment/" + postId + "/" + commentId)
                .then()
                .log().all()
                .statusCode(200)
                .body("message", Matchers.equalTo("Comment deleted"));
    }
    @Test(description = "This test aims to detele invalid Comment")
    public void deleteCommentInvalidTest(){

        Comment testCreateComment = new Comment("Hermione", "This a test");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testCreateComment)
                .when()
                .delete("/v1/comment/" + postId + "/" + commentId)
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Comment could not be deleted"));
    }
}