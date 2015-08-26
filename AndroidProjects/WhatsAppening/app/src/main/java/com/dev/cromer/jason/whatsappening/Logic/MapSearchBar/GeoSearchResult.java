package com.dev.cromer.jason.whatsappening.Logic.MapSearchBar;

import android.location.Address;

public class GeoSearchResult {

    private Address address;

    //contructor
    public GeoSearchResult(Address address) {
        this.address = address;
    }

    public String getAddress() {
        String displayAddress = "";
        displayAddress += address.getAddressLine(0) + "\n";

        for(int i = 1; i < address.getMaxAddressLineIndex(); i++) {
            displayAddress += address.getAddressLine(i) + ", ";
        }

        displayAddress = displayAddress.substring(0, displayAddress.length() -2);

        return displayAddress;
    }

    public String toString() {
        String displayAddress = "";

        if(address.getFeatureName() != null) {
            displayAddress += address + ", ";
        }

        for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            displayAddress += address.getAddressLine(i);
        }

        return displayAddress;
    }
}
