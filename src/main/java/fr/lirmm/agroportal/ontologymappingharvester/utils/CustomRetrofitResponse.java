package fr.lirmm.agroportal.ontologymappingharvester.utils;

import retrofit2.Response;

public class CustomRetrofitResponse {

    private Response<String> response;
    private String errorMessage;


    public Response<String> getResponse() {
        return response;
    }

    public void setResponse(Response<String> response) {
        this.response = response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
