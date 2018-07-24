/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bai;

/**
 *
 * @author u000783
 */
public class GroupHeader {

    String recordCode = "02";
    String ultimateReceiverIdentification;
    String originatorIdentification;
    String groupStatus;
    String asOfDate;
    String asOfTime;
    String currencyCode;
    String asOfDateModifier;

    public GroupHeader(String ultimateReceiverIdentification, String originatorIdentification, String groupStatus, String asOfDate, String asOfTime, String currencyCode, String asOfDateModifier) {
        this.ultimateReceiverIdentification = ultimateReceiverIdentification;
        this.originatorIdentification = originatorIdentification;
        this.groupStatus = groupStatus;
        this.asOfDate = asOfDate;
        this.asOfTime = asOfTime;
        this.currencyCode = currencyCode;
        this.asOfDateModifier = asOfDateModifier;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public String getUltimateReceiverIdentification() {
        return ultimateReceiverIdentification;
    }

    public String getOriginatorIdentification() {
        return originatorIdentification;
    }

    public String getGroupStatus() {
        return groupStatus;
    }

    public String getAsOfDate() {
        return asOfDate;
    }

    public String getAsOfTime() {
        return asOfTime;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getAsOfDateModifier() {
        return asOfDateModifier;
    }

    @Override
    public String toString() {
        return recordCode
                + "," + ultimateReceiverIdentification
                + "," + originatorIdentification
                + "," + groupStatus
                + "," + asOfDate
                + "," + asOfTime
                + "," + currencyCode
                + "," + asOfDateModifier;
    }

}
