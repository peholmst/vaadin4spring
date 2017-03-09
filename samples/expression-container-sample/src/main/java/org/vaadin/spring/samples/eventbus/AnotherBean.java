package org.vaadin.spring.samples.eventbus;

public class AnotherBean {
    private int anotherInt;
    private String anotherString;

    public AnotherBean(){
    }

    public AnotherBean(int anotherInt, String anotherString) {
        this.anotherInt = anotherInt;
        this.anotherString = anotherString;
    }

    public int getAnotherInt() {
        return anotherInt;
    }

    public void setAnotherInt(int anotherInt) {
        this.anotherInt = anotherInt;
    }

    public String getAnotherString() {
        return anotherString;
    }

    public void setAnotherString(String anotherString) {
        this.anotherString = anotherString;
    }
}
