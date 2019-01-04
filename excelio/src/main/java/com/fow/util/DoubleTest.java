package com.fow.util;

import org.apache.poi.util.StringUtil;

import java.io.*;
import java.util.Scanner;

public class DoubleTest {
    static Scanner scan = new Scanner(System.in);

    //把输入的浮点数分成整数部分和小数部分
    static String douToStr(String s){
        int n = s.indexOf(".");
        if(n<0){
            s += ".0";
            n = s.indexOf(".");
        }
        String s1 = s.substring(0,n);
        String s2 = "0" + s.substring(n);

        String s3 = intToStr(Integer.parseInt(s1));
        String s4 = dToStr(Double.parseDouble(s2));
        return  (s3.length()==0? 0 : s3) + "." + s4;
    }

    //把整数部分转成二进制
    static String intToStr(int n){
        if(n==0) return "";
        int a = n % 2;
        int b = n / 2;
        return intToStr(b) + a;
    }

    //把小数部分转成二进制
    static String dToStr(double d){
        if(d-(int)d<0.01) return "" + (int)d;
        int n = (int)(d * 2);
        double a = d * 2 - n;
        return "" + n + dToStr(a);
    }
    public static String reverse1(String str) {
        return new StringBuilder(str).reverse().toString();
    }
    public static String getBinaryString(int[] datas)
    {
        StringBuilder sb=new StringBuilder();
        for(int i:datas)
        {
            String tmp=Integer.toBinaryString(i);
            int print0=8-tmp.length();
            while (print0!=0)
            {
                sb.append("0");
                print0--;
            }
            sb.append(tmp);
            //System.out.print(tmp);
        }
        return sb.toString();
    }
    public static String getBinaryString(int data)
    {
            if(data<0) data+=256;
            StringBuilder sb=new StringBuilder();
            String tmp=Integer.toBinaryString(data);
            int print0=8-tmp.length();
            while (print0!=0)
            {
                sb.append("0");
                print0--;
            }
            sb.append(tmp);
            return sb.toString();
    }
    public static byte[] read(BufferedInputStream bis,int off,int len) throws IOException {
        long skiped=off;
        while (skiped!=0)
            skiped-=bis.skip(skiped);
        byte[] data=new byte[len];
        bis.read(data);
        return data;
    }
    public static int pipei(String s,BufferedInputStream bis)
    {
        int index=0;
        try {
            int ssize=s.length();
            byte[] header=new byte[ssize];
            byte[] data=read(bis,0,ssize*100);
            StringBuilder sb=new StringBuilder();
            for(byte b:header)
            {
                sb.append(getBinaryString((int)b));
            }
            for(byte b:data)
            {

                sb.append(getBinaryString((int)b));
            }

            return sb.toString().indexOf(s);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        //String s1="0100001110100100011110110111010101000011100010100000111000001001100001101101011110110011110011101000011010011111011001010101011100001100110101000011011011001001000011000111110100100110110110100001011010000111100100111101011000010100011010000010101000010";
//        String s="0100001110100100011110110111010101000011100010100000111000001001010000110110101111011001111001110100001101001111101100101010101101000011001101010000110110110010010000110001111101001001101101100100001011010000111100100111101011000010100011010000010101000010";
//        String tmp="01000101110100100011110111000010";
//
//        System.out.println("反转前：");
//        int a=s.indexOf(tmp);
//        //int a=s.lastIndexOf(tmp);
//        System.out.println(a);
//        System.out.println("反转后：");
//        tmp=reverse1(tmp);
//        a=s.indexOf(tmp);
//        System.out.println(a);
//        try {
//            File file=new File("E:/SGY插值/地震数据/original.sgy");
//            File segheaderfile=new File("E:/SGY插值/地震数据/tranceheader3600.txt");
//            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
////            String data="";
////            int index=0;
//            //int result=pipei("00011101",bis,3840,1024);
//            //System.out.println(result);
//            if(!segheaderfile.exists()) {
//                segheaderfile.createNewFile();
//            }
//            byte[] headerdata=read(bis,3600,240);
//            FileOutputStream os=new FileOutputStream(segheaderfile);
//            os.write(headerdata);
//            if(bis!=null)
//                bis.close();
//            if(os!=null)
//                os.close();
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
        int a=(int)Double.parseDouble("-23.458");
        System.out.println(Double.parseDouble("-23.458"));
    }
}
