package com.vulsoftinc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateCardStatusRequest {
    @SerializedName("status")
    private String status;

    public UpdateCardStatusRequest(String status) {
        this.status = status;
    }
}
