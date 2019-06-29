package com.erommel.rest.response;

import java.util.Collection;

public class CollectionResponse {

    public Collection result;

    public CollectionResponse() {
        this(null);
    }

    public CollectionResponse(Collection result) {
        this.result = result;
    }

    public Collection getResult() {
        return result;
    }

    public void setResult(Collection result) {
        this.result = result;
    }
}
