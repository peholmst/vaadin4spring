package org.vaadin.spring.samples.eventbus;

import java.util.HashSet;
import java.util.Set;

public class ABean {
    private int anInt;
    private String aString;
    private float aFloat;
    private AnotherBean anotherBean;
    private Set<ABean> linkedBeans = new HashSet<>();

    public ABean(){
    }

    public ABean(int anInt, String aString, float aFloat, AnotherBean anotherBean) {
        this.anInt = anInt;
        this.aString = aString;
        this.aFloat = aFloat;
        this.anotherBean = anotherBean;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public String getaString() {
        return aString;
    }

    public void setaString(String aString) {
        this.aString = aString;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public AnotherBean getAnotherBean() {
        return anotherBean;
    }

    public void setAnotherBean(AnotherBean anotherBean) {
        this.anotherBean = anotherBean;
    }

    public Set<ABean> getLinkedBeans() {
        return linkedBeans;
    }

    public void setLinkedBeans(Set<ABean> linkedBeans) {
        this.linkedBeans = linkedBeans;
    }
}
