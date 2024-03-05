package com.otto.mart.model.APIModel.Response.faq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TopUpInstructionsItem {
    private String instruction;
    private String title;

    public String getInstruction() {
        return instruction;
    }

    public TopUpInstructionsItem setInstruction(String instruction) {
        this.instruction = instruction;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TopUpInstructionsItem setTitle(String title) {
        this.title = title;
        return this;
    }
}
