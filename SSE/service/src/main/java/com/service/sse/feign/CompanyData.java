package com.service.sse.feign;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import java.util.List;

public class CompanyData {

    private Long id;
    private String name;
    private String fancyName;
    private String code;

    @JsonIncludeProperties({ "id", "name", "fancyName" })
    private List<CompanyData> branchs;

    private Boolean blockItemMovement;
    private Boolean isHeadquarters;

}