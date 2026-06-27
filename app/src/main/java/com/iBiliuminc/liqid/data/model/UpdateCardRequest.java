package com.iBiliuminc.liqid.data.model;

import com.google.gson.annotations.SerializedName;

public class UpdateCardRequest {
    @SerializedName("frozen")
    private boolean frozen;
    @SerializedName("online_enabled")
    private boolean onlineEnabled;
    @SerializedName("contactless_enabled")
    private boolean contactlessEnabled;
    @SerializedName("atm_enabled")
    private boolean atmEnabled;

    public UpdateCardRequest(boolean frozen, boolean onlineEnabled, boolean contactlessEnabled, boolean atmEnabled) {
        this.frozen = frozen;
        this.onlineEnabled = onlineEnabled;
        this.contactlessEnabled = contactlessEnabled;
        this.atmEnabled = atmEnabled;
    }
}
