package com.example.steps;

import com.example.models.order.post.request.Order;
import com.example.models.order.ResponseOrder;
import com.example.models.error.ErrorResponse;
import com.example.questions.placeOrder.postOrder.PostErrorResponse;
import com.example.tasks.PlaceOrder;
import com.example.questions.ResponseStatusCode;
import com.example.utils.ApiConfig;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.datatable.DataTable;

import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Map;

public class PostPlaceOrderStepDef {

    ResponseOrder responseOrder;

    @When("the {string} places an order with the following data:")
    public void the_customer_places_an_order_with_the_following_data(String actorName, DataTable table) {
        theActorCalled(actorName);
        theActorInTheSpotlight().whoCan(CallAnApi.at(ApiConfig.baseUrl()));

        Map<String, String> row = table.asMaps().get(0);
        String idRaw = row.get("id");
        String petIdRaw = row.get("petId");
        String quantityRaw = row.get("quantity");
        String shipDate = row.get("shipDate");
        String status = row.get("status");
        String completeRaw = row.get("complete");

        Order order = new Order();
        order.setId(idRaw.trim());
        order.setPetId(petIdRaw);
        order.setQuantity(quantityRaw);
        order.setShipDate(shipDate);
        order.setStatus(status);
        order.setComplete(Boolean.parseBoolean(completeRaw));

        theActorInTheSpotlight().attemptsTo(PlaceOrder.withDetails(order));
    }

    @Given("the order exists with:")
    public void the_order_exists_with(DataTable table) {
        the_customer_places_an_order_with_the_following_data("client", table);
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(Integer status) {
        Integer actualStatus = ResponseStatusCode.status().answeredBy(theActorInTheSpotlight());
        assertThat(actualStatus, is(status));
    }

    @Then("the response should contain error messages indicating invalid data types for each field")
    public void the_response_should_contain_error_messages_indicating_invalid_data_types() {
        ErrorResponse error = new PostErrorResponse().answeredBy(theActorInTheSpotlight());
        assertThat(error, is(notNullValue()));
        assertThat(error.getCode(), is(500));
        assertThat(error.getType(), is("unknown"));
        assertThat(error.getMessage(), is("something bad happened"));
    }

    @Then("the response should include error type {string}")
    public void the_response_should_include_error_type(String expectedType) {
        ErrorResponse error = new PostErrorResponse().answeredBy(theActorInTheSpotlight());
        assertThat(error.getType(), is(expectedType));
    }

    @Then("the response should include error message {string}")
    public void the_response_should_include_error_message(String expectedMessage) {
        ErrorResponse error = new PostErrorResponse().answeredBy(theActorInTheSpotlight());
        assertThat(error.getMessage(), is(expectedMessage));
    }


    @And("the order id in the response should be {int}")
    public void the_order_id_in_the_response_should_be(Integer expectedId) {
        this.responseOrder = new com.example.questions.placeOrder.postOrder.PostResponseOrder().answeredBy(theActorInTheSpotlight());
        assertThat(responseOrder.getId(), equalTo(expectedId.longValue()));
    }

    @And("the petId in the response should be {int}")
    public void the_petId_in_the_response_should_be(Integer expectedPetId) {
        assertThat(responseOrder.getPetId(), is(expectedPetId.longValue()));
    }

    @And("the quantity in the response should be {int}")
    public void the_quantity_in_the_response_should_be(Integer expectedQuantity) {
        assertThat(responseOrder.getQuantity(), is(expectedQuantity));
    }
    @And("the shipDate in the response should be {string}")
    public void the_shipDate_in_the_response_should_be(String expectedShipDate) {
        assertThat(responseOrder.getShipDate(), is(expectedShipDate));
    }
    @And("the status in the response should be {string}")
    public void the_status_in_the_response_should_be(String expectedStatus) {
        assertThat(responseOrder.getStatus(), is(expectedStatus));
    }

    @And("the complete flag in the response should be {string}")
    public void the_complete_flag_in_the_response_should_be(String expectedComplete) {
        Boolean expectedCompleteBool = Boolean.parseBoolean(expectedComplete);
        assertThat(responseOrder.getComplete(), is(expectedCompleteBool));
        }

}
