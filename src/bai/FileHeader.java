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
public class FileHeader {

    String recordCode = "01";
    String senderIdentification;
    String receiverIdentification;
    String fileCreationDate;
    String fileCreationTime;    
    String fileIdentificatioNumber;
    String physicalRecordLength;
    String blockSize;
    String versionNumber;

    public FileHeader(String senderIdentification, String receiverIdentification, String fileCreationDate, String fileCreationTime, String fileIdentificatioNumber, String physicalRecordLength, String blockSize, String versionNumber) {
        this.senderIdentification = senderIdentification;
        this.receiverIdentification = receiverIdentification;
        this.fileCreationDate = fileCreationDate;
        this.fileCreationTime = fileCreationTime;
        this.fileIdentificatioNumber = fileIdentificatioNumber;
        this.physicalRecordLength = physicalRecordLength;
        this.blockSize = blockSize;
        this.versionNumber = versionNumber;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public String getSenderIdentification() {
        return senderIdentification;
    }

    public String getReceiverIdentification() {
        return receiverIdentification;
    }

    public String getFileCreationDate() {
        return fileCreationDate;
    }

    public String getFileCreationTime() {
        return fileCreationTime;
    }

    public String getFileIdentificatioNumber() {
        return fileIdentificatioNumber;
    }

    public String getPhysicalRecordLength() {
        return physicalRecordLength;
    }

    public String getBlockSize() {
        return blockSize;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String toString() {
        return recordCode
                + "," + senderIdentification
                + "," + receiverIdentification
                + "," + fileCreationDate
                + "," + fileCreationTime
                + "," + fileIdentificatioNumber
                + "," + physicalRecordLength
                + "," + blockSize
                + "," + versionNumber;
    }
}
