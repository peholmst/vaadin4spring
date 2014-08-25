package org.vaadin.spring.samples.mvp.dto;

public enum CommitStatusType {

    UNAVAILABLE("Unavailable"),
    ECONOMIC("Economic"),
    EMERGENCY("Emergency"),
    MUST_RUN("Must Run");

    private final String value;

    CommitStatusType(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    public static CommitStatusType fromValue(String v) {
        for (CommitStatusType c: CommitStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
