package com.raysofthesun.poswebjava.core.configuration.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.raysofthesun.poswebjava.apply.insureds.enums.InsuredRole;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class PosDocumentRequirement {
    private String type;
    private String group;
    private int minimumUploads;
    private int maximumUploads;
    @JsonIgnore
    private List<InsuredRole> forRoles = Arrays.asList(InsuredRole.values());
}
