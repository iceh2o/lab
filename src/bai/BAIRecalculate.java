/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author u000783
 */
public class BAIRecalculate {

    int cursor;
    Properties baiProperties;
    List<String> memFile = new ArrayList<>();

    public BAIRecalculate(Properties baiProperties) throws IOException {
        if (baiProperties == null) {
            this.baiProperties = new Properties();
            this.baiProperties.load(new FileInputStream("config/bai.properties"));
        } else {
            this.baiProperties = baiProperties;
        }
    }

    private BAIRecalculate() throws IOException {
        this(null);
    }

    /**
     * Read BAI File into memory.
     *
     * @return file converted to list of String
     * @throws java.io.FileNotFoundException
     */
    public String[] readBAI() throws FileNotFoundException, IOException {
        File file = new File(baiProperties.getProperty("InputFile"));
        FileReader fileReader;
        fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuffer = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }
        fileReader.close();
        return stringBuffer.toString().split("\n");
    }

    /**
     * Read 01 Record
     *
     * @param record
     * @param i
     * @return
     */
    public FileHeader get01(String[] record, int i) {
        String[] fh = record[i].split(",");
        return new FileHeader(fh[1], fh[2], fh[3], fh[4], fh[5], fh[6], fh[7], fh[8]);
    }

    /**
     * Read 02 Record
     *
     * @param record
     * @param i
     * @return
     */
    public GroupHeader get02(String[] record, int i) {
        String[] gh = record[i].split(",");
        return new GroupHeader(gh[1], gh[2], gh[3], gh[4], gh[5], gh[6], gh[7]);
    }

    /**
     * Read 03 Record
     *
     * @param record
     * @param i
     * @return
     */
    public long get03(String[] record, int i) {
        long total = 0;
        StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append(record[i]);
        memFile.add(record[i]);
        for (int j = i + 1; j < record.length; j++) {
            if (record[j].startsWith("88,")) {
                stringBuffer.append(record[j]);
                memFile.add(record[j]);
            } else {
                break;
            }
        }
        String[] fields = stringBuffer.toString().split(",");
        for (int k = 4; k < fields.length; k = k + 4) {
            try {
                total += Long.parseLong(fields[k]);
            } catch (Exception e) {
                // Ignore if fields[k] is empty or not a number
                // System.out.println("---" + k);
            }
        }
        return total;
    }

    /**
     * Read 16 Record
     *
     * @param record
     * @param i
     * @return
     */
    public long get16(String[] record, int i) {
        long total = 0;
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(record[i]);
        memFile.add(record[i]);
        for (int j = i + 1; j < record.length; j++) {
            if (record[j].startsWith("88,")) {
                stringBuffer.append(record[j]);
                memFile.add(record[j]);
            } else {
                break;
            }
        }

        String[] fields = stringBuffer.toString().split(",");
        total += Long.parseLong(fields[2]);
        return total;
    }

    /**
     * Helper to iterate a list and get the total
     *
     * @param list
     * @return
     */
    public long toTotal(List<Long> list) {
        Iterator<Long> it = list.iterator();
        Long total = (long) 0;
        while (it.hasNext()) {
            total += it.next();
        }

        return total;
    }

    public void calculate() throws IOException {
        String[] contentOfFile = readBAI();
        List<Long> accountTrailers99 = new ArrayList<>();
        List<Long> accountTrailers98 = new ArrayList<>();
        Long accountTrailerTotal = (long) 0;
        int type03LineCount = 0;
        int type02LineCount = 0;
        int previous98Count = 0;
        for (int i = 0; i < contentOfFile.length; i++) {
            if (contentOfFile[i].startsWith("01,")) {
                memFile.add(get01(contentOfFile, i).toString());
            } else if (contentOfFile[i].startsWith("02,")) {
                type02LineCount++;
                memFile.add(get02(contentOfFile, i).toString());
            } else if (contentOfFile[i].startsWith("03,")) {
                type03LineCount = i;
                accountTrailerTotal = accountTrailerTotal + get03(contentOfFile, i);
            } else if (contentOfFile[i].startsWith("16,")) {
                accountTrailerTotal = accountTrailerTotal + get16(contentOfFile, i);
            } else if (contentOfFile[i].startsWith("49,")) {
                memFile.add("49," + accountTrailerTotal + "," + (i - type03LineCount + 1) + "/");
                accountTrailers98.add(accountTrailerTotal);
                accountTrailers99.add(accountTrailerTotal);
                accountTrailerTotal = new Long(0);
            } else if (contentOfFile[i].startsWith("98,")) {
                memFile.add("98," + toTotal(accountTrailers98) + "," + accountTrailers98.size() + "," + (i - previous98Count) + "/");
                previous98Count = i;
                accountTrailers98 = new ArrayList<>();
            } else if (contentOfFile[i].startsWith("99,")) {
                memFile.add("99," + toTotal(accountTrailers99) + "," + type02LineCount + "," + (i + 1) + "/");
            }
        }
    }

    public void saveToFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(baiProperties.getProperty("OutputFile", "OutputBAI.txt")));
        Iterator<String> it = memFile.iterator();
        while (it.hasNext()) {
            writer.write(it.next());
            writer.newLine();
        }
        writer.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BAIRecalculate bai = new BAIRecalculate();
        bai.calculate();
        bai.saveToFile();
//        String[] contentOfFile = bai.readBAI();
//        List<Long> accountTrailers99 = new ArrayList<>();
//        List<Long> accountTrailers98 = new ArrayList<>();
//        Long accountTrailerTotal = (long) 0;
//        int type03LineCount = 0;
//        int type02LineCount = 0;
//        int previous98Count = 0;
//        for (int i = 0; i < contentOfFile.length; i++) {
//            if (contentOfFile[i].startsWith("01,")) {
//                System.out.println(bai.get01(contentOfFile, i));
//            } else if (contentOfFile[i].startsWith("02,")) {
//                type02LineCount++;
//                System.out.println(bai.get02(contentOfFile, i));
//            } else if (contentOfFile[i].startsWith("03,")) {
//                type03LineCount = i;
//                accountTrailerTotal = accountTrailerTotal + bai.get03(contentOfFile, i);
//            } else if (contentOfFile[i].startsWith("16,")) {
//                accountTrailerTotal = accountTrailerTotal + bai.get16(contentOfFile, i);
//            } else if (contentOfFile[i].startsWith("49,")) {
//                System.out.println("49," + accountTrailerTotal + "," + (i - type03LineCount + 1) + "/");
//                accountTrailers98.add(accountTrailerTotal);
//                accountTrailers99.add(accountTrailerTotal);
//                accountTrailerTotal = new Long(0);
//            } else if (contentOfFile[i].startsWith("98,")) {
//                System.out.println("98," + bai.toTotal(accountTrailers98) + "," + accountTrailers98.size() + "," + (i - previous98Count) + "/");
//                previous98Count = i;
//                accountTrailers98 = new ArrayList<>();
//            } else if (contentOfFile[i].startsWith("99,")) {
//                System.out.println("99," + bai.toTotal(accountTrailers99) + "," + type02LineCount + "," + (i + 1) + "/");
//            }
//        }
    }
}
