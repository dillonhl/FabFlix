package edu.uci.ics.dillonhl.service.gateway.threadpool;

import edu.uci.ics.dillonhl.service.gateway.models.base.RequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.base.ResponseModel;
import edu.uci.ics.dillonhl.service.gateway.models.request.movies.ThumbnailRequestModel;

import javax.ws.rs.core.MultivaluedMap;

public class ClientRequest {
    private String email;
    private String session_id;
    private String transaction_id;
    private String URI;
    private String endpoint;

    //TODO Add request model data members
    private RequestModel requestModel;
    private ThumbnailRequestModel thumbnailRequestModel;
    private ResponseModel responseModel;
    private boolean getRequest;
    private MultivaluedMap<String,String> map;

    public ClientRequest() {

    }

    public ResponseModel getResponseModel() {
        return responseModel;
    }

    public void setResponseModel(ResponseModel responseModel) {
        this.responseModel = responseModel;
    }

    public String getEmail() {
        return email;
    }

    public String getSession_id() {
        return session_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getURI() {
        return URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public RequestModel getRequestModel() {
        return requestModel;
    }

    public void setRequestModel(RequestModel requestModel) {
        this.requestModel = requestModel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }


    public void setThumbnailRequestModel(ThumbnailRequestModel thumbnailRequestModel) {
        this.thumbnailRequestModel = thumbnailRequestModel;
    }

    public ThumbnailRequestModel getThumbnailRequestModel() {
        return thumbnailRequestModel;
    }

    public boolean isGetRequest() {
        return getRequest;
    }

    public void setGetRequest(boolean getRequest) {
        this.getRequest = getRequest;
    }

    public MultivaluedMap<String, String> getMap() {
        return map;
    }

    public void setMap(MultivaluedMap<String, String> map) {
        this.map = map;
    }
}


