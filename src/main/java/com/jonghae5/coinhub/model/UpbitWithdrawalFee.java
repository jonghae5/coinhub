package com.jonghae5.coinhub.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpbitWithdrawalFee {
    private boolean success;
    private String data;

    private static ObjectMapper mapper = new ObjectMapper();
    public List<UpbitEachWithdrawalFee> getData() throws JsonProcessingException {
        return mapper.readValue(data, new TypeReference<>() {});

    }
}
