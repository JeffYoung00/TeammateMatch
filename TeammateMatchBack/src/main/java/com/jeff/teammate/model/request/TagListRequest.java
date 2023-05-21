package com.jeff.teammate.model.request;

import lombok.Data;

import java.util.List;

@Data
public class TagListRequest {
    private List<Integer> tagIdList;
}
