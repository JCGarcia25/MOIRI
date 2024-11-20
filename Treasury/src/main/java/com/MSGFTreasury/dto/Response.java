package com.MSGFTreasury.dto;

import java.util.Map;

public class Response {
    private String type;
    private Map<String, Integer> value;
    private Map<String, String> valueInfo;

    // Getters y setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Integer> getValue() {
        return value;
    }

    public void setValue(Map<String, Integer> value) {
        this.value = value;
    }

    public Map<String, String> getValueInfo() {
        return valueInfo;
    }

    public void setValueInfo(Map<String, String> valueInfo) {
        this.valueInfo = valueInfo;
    }
}
