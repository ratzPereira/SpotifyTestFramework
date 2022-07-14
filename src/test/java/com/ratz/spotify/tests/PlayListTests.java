package com.ratz.spotify.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlayListTests {


  RequestSpecification requestSpecification;
  ResponseSpecification responseSpecification;

  String access_token = "BQBYMOQL8ZSfqOHlslIC7Qc1fdWDq-Mm5p6JeXOIS11kj1QXwzdgi9rxEDFa-THExiD1VmcSNGGAwvFoG5nVkKznPU2wj1p0uoRwcqpYK1GjhU8zjic6nWp7pEnAQmse8sU8Rbrfga3zwDziGSWNFO-eTwNPFXgF_K-rg7nGnDNWCNuca5J4NihkYpvNWWfPWUkNMApICrkWddcaHPRczQBIRNp7svJxQz0BuaeO8fxvF7cggR0E97ZEfvB3LPvCuEQG1vz8YNDVN9k2";


  @BeforeClass
  public void beforeClass(){

    requestSpecification = new RequestSpecBuilder()
        .setBaseUri("https://api.spotify.com")
        .setBasePath("/v1")
        .addHeader("Authorization","Bearer " + access_token)
        .setContentType(ContentType.JSON)
        .log(LogDetail.ALL).build();

    responseSpecification = new ResponseSpecBuilder()
        .log(LogDetail.ALL).build();

  }

  @Test
  public void shouldBeAbleToCreatePlaylist(){

    String payload = "{\n" +
        "  \"name\": \"New Playlist\",\n" +
        "  \"description\": \"New playlist description\",\n" +
        "  \"public\": false\n" +
        "}";

    given(requestSpecification)
        .body(payload)
    .when()
        .post("/users/317ncaqmbq6rrm4ajbbx7hxemm4m/playlists")
    .then()
        .spec(responseSpecification)
        .statusCode(201).contentType(ContentType.JSON)
        .body("name", equalTo("New Playlist"),
            "description", equalTo("New playlist description"),
            "public", equalTo(false));
  }

  @Test
  public void shouldBeAbleToGetAPlaylist(){
    given(requestSpecification)
        .when().get("/playlists/2TygZyLWSz4NsoDmc1vqjD")
        .then().spec(responseSpecification).statusCode(200).contentType(ContentType.JSON)
          .body("id",equalTo("2TygZyLWSz4NsoDmc1vqjD"), "type",equalTo("playlist"));
  }

  @Test
  public void shouldBeAbleToUpdatePlaylist(){

    String payload = "{\n" +
        "  \"name\": \"Lets update Playlist Name\",\n" +
        "  \"description\": \"Lets update playlist description\",\n" +
        "  \"public\": false\n" +
        "}";

    given(requestSpecification)
        .body(payload)
        .when()
        .put("/playlists/2TygZyLWSz4NsoDmc1vqjD")
        .then()
        .spec(responseSpecification)
        .statusCode(200);
  }

  @Test
  public void shouldNotBeAbleToCreatePlaylist(){

    String payload = "{\n" +
        "  \"name\": \"\",\n" +
        "  \"description\": \"New playlist description\",\n" +
        "  \"public\": false\n" +
        "}";

    given(requestSpecification)
        .body(payload)
        .when()
        .post("/users/317ncaqmbq6rrm4ajbbx7hxemm4m/playlists")
        .then()
        .spec(responseSpecification)
        .statusCode(400).contentType(ContentType.JSON)
        .body("error.message", equalTo("Missing required field: name"));
  }
}
