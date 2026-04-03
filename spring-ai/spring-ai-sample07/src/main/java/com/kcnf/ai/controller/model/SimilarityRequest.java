package com.kcnf.ai.controller.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimilarityRequest {
    private String text1;
    private String text2;
}
