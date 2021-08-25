import helpers.RequestHelpers;
import io.restassured.RestAssured;
import org.testng.annotations.*;

public class Base {

    protected int postId;
    protected int commentId;

    @Parameters("host")
    @BeforeSuite(alwaysRun = true)
    public void setup(@Optional("https://api-coffee-testing.herokuapp.com") String host) {

        System.out.println(String.format("Test Host: %s", host));

        RestAssured.baseURI = host;
    }

    // before groups

    @BeforeMethod(alwaysRun = true )
    void createPost(){
        postId = RequestHelpers.createRandomPostAndGetID();
    }

    @AfterMethod(alwaysRun = true)
    void deletePost(){
        RequestHelpers.cleanUpPost(postId);
    }


    @BeforeMethod(onlyForGroups = "useComment",dependsOnMethods = "createPost")
    void createComment(){
        commentId = RequestHelpers.createRandomCommentAndGetID(postId);
    }

    @AfterMethod(onlyForGroups = "useComment")
    void deleteComment(){
        RequestHelpers.cleanUpComment(commentId,postId);
    }

}