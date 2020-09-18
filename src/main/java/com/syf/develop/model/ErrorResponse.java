package com.syf.develop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private String trackingId;
    private String documentationUrl;
    private String remediationInfo;
    List<Errors> errors;
    List<DebugInfo> debugInfo;
    @JsonProperty("transient")
    public boolean transientType;
}
