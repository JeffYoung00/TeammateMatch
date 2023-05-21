package com.jeff.teammate.model.request;

import lombok.Data;

@Data
public class TagRequest {
    private int gameId;
    private String groupName;
    private String tagName;
}
