package edu.uci.ics.dillonhl.service.gateway.models.request;

import edu.uci.ics.dillonhl.service.gateway.models.base.RequestModel;
import edu.uci.ics.dillonhl.service.gateway.models.response.GeneralResponseModel;

public class GeneralRequestModel extends RequestModel {
    public GeneralRequestModel() {
        this.setEmail("");
    }
}
