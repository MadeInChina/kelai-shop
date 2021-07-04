package cn.com.kelaikewang.core.report.dto;

import java.io.Serializable;

public class StatisticItemDTO<T> implements Serializable {
    private String name;
    private String label;
    private T value;

    public StatisticItemDTO(String name, String label, T value) {
        this.name = name;
        this.label = label;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
