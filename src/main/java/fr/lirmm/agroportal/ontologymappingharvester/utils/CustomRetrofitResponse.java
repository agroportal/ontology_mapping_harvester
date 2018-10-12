package fr.lirmm.agroportal.ontologymappingharvester.utils;

import fr.lirmm.agroportal.ontologymappingharvester.entities.postmappingapi.PostApiResponse;
import retrofit2.Response;

public class CustomRetrofitResponse {

    private Response<PostApiResponse> response;
    private Response<String> responseDelete;
    private String errorMessage;

    public Response<String> getResponseDelete() {
        return responseDelete;
    }

    public void setResponseDelete(Response<String> responseDelete) {
        this.responseDelete = responseDelete;
    }

    public Response<PostApiResponse> getResponse() {
        return response;
    }

    public void setResponse(Response<PostApiResponse> response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
