package com.fow.util;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class CreateSegFile {

    public char[] createHeader(char[] inspectionLine4,char[] volumeNumber4,
                             char[] trackNumber2,char[] auxiliaryNumber2,
                             char[] formatCode2,char[] cmpNumber2,
                             char[] measurementSystem2) throws Exception {
        if(inspectionLine4.length!=4)
            throw new Exception("检查线字节数不为4");
        if(volumeNumber4.length!=4)
            throw new Exception("卷号字节数不为4");
        if(trackNumber2.length!=2)
            throw new Exception("每个记录的道数字节数不为2");
        if(auxiliaryNumber2.length!=2)
            throw new Exception("每个记录的辅助道数字节数不为2");
        if(formatCode2.length!=2)
            throw new Exception("数据采样格式码字节数不为2");
        if(cmpNumber2.length!=2)
            throw new Exception("cmp覆盖次数字节数不为2");
        if(measurementSystem2.length!=2)
            throw new Exception("检测系统字节数不为2");
        char[] result=new char[400];
        int i=0;
        for(;i<4;i++)
            result[i]=random();
        for(;i<8;i++)
            result[i]=inspectionLine4[i-4];
        for(;i<12;i++)
            result[i]=volumeNumber4[i-8];
        for (;i<14;i++)
            result[i]=trackNumber2[i-12];
        for(;i<16;i++)
            result[i]=auxiliaryNumber2[i-14];
        for(;i<24;i++)
            result[i]=0;
        for(;i<26;i++)
            result[i]=formatCode2[i-24];
        for(;i<28;i++)
            result[i]=cmpNumber2[i-26];
        for(;i<54;i++)
            result[i]=0;
        for(;i<56;i++)
            result[i]=measurementSystem2[i-54];
        for(;i<400;i++)
            result[i]=0;
        return result;

    }
    private char random()
    {
        return (char)(Math.random()*256);
    }
    public  static void findindex(File file)
    {
        try {
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
            byte[] header=new byte[240];
            int local=0;
            int len=0;
            byte start=3;
            int tmp=0;
            Queue<Byte> queue=new LinkedList();
            queue.offer((byte)99);
            queue.offer((byte)99);
            queue.offer((byte)99);
            queue.offer((byte)99);
            while ((len=bis.read(header))>0)
            {
                for(byte b:header)
                {
                    queue.poll();
                    queue.offer(b);
                    if(((LinkedList<Byte>) queue).get(0)==start&&((LinkedList<Byte>) queue).get(1)==0&&((LinkedList<Byte>) queue).get(2)==0&&((LinkedList<Byte>) queue).get(3)==0)
                    {
                        System.out.println("索引："+(local-10)+"\t当前start值:"+start+"\t与上一次的差："+(local-tmp));
                        tmp=local;
                        start++;
                    }
                    local++;
                }

                if(start>100) break;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void showHeader(File file)
    {
        try {
            BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
            byte[] header=new byte[240];
            bis.skip(3600);
            int dump3200=0;
            while (dump3200!=0)
            {
                bis.skip(3200);
                dump3200--;
            }
            for(int i=0;i<10;i++)
            {
                bis.read(header);
                for(byte b:header)
                    System.out.print(String.format("%02x",b)+"\t");
                System.out.println();
                long skiped=6004;
                while (skiped!=0)
                    skiped-=bis.skip(skiped);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读文件
    public static byte[] read(File file,int off,int len) throws IOException {
        BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
        long skiped=off;
        while (skiped!=0)
            skiped-=bis.skip(skiped);
        byte[] data=new byte[len];
        bis.read(data);
        return data;
    }
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
    static long getUnsignedInterger(int data)
    {
        return data&0x0FFFFFFFF;
    }
    static float IEEEtoIBM(Double d) throws Exception
    {
        String data=d.toString();

        return 0.1f;
    }
    public static void main(String[] args) {
//        File fileHeader = new File("E:/SGY插值/seg文件构建数据/header3600.txt");
////        File tranceHeader=new File("E:/SGY插值/seg文件构建数据/tranceheader240.txt");
        File bmpimage = new File("E:/SGY插值/seg文件构建数据/结果.bmp");
        File color = new File("E:/SGY插值/色标/白灰黑32-10.bmp");
        byte[] colorCode=new byte[256*3];
        int[] bmpHeader={0x42,0x4d,0,0xe0,0x06,0,0,0,0,0,0x36,0,0,0,0x28, 0,
                0,0,0xdd,0x05,0,0,0x2c,0x01,0,0,1,0,0x20,0,0,0,
                0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
                0,0,0,0,0,0};
        ReadXLSX read=new ReadXLSX();
        File excelFile = new File("E:/SGY插值/50结果.xlsx");
        try {
            BufferedInputStream colorbis=new BufferedInputStream(new FileInputStream(color));
            byte[] colorpoint=new byte[4];
            long skiped=54;
            while (skiped!=0)
                skiped-=colorbis.skip(skiped);
            for(int i=0;i<256;i++)
            {
                colorbis.read(colorpoint);
                int index=i*3;
                colorCode[index]=colorpoint[0];
                colorCode[index+1]=colorpoint[1];
                colorCode[index+2]=colorpoint[2];
                skiped=36;
                while (skiped!=0)
                    skiped-=colorbis.skip(skiped);
            }
            colorbis.close();
            List<List<String>> oneSheet=read.readOneSheet(excelFile,0);
            if(!bmpimage.exists())
            {
                bmpimage.createNewFile();
            }
            FileOutputStream fos=new FileOutputStream(bmpimage);
            byte[] tmp=new byte[bmpHeader.length];
            for(int i=0;i<bmpHeader.length;i++)
            {
                tmp[i]=(byte)bmpHeader[i];
            }
            fos.write(tmp);
            for(List<String> list:oneSheet)
            {
                byte[] datatmp=new byte[list.size()*4];
                int i=0;
                for(String s:list)
                {
                    Double a=Double.parseDouble(s);
                    //byte point=0;
                    double bili=a/20000.0*128+127;
                    int pointtmp=(int)bili;
                    pointtmp=pointtmp>0?pointtmp:-pointtmp;
                    //point=(byte)pointtmp;
                    datatmp[i]=colorCode[pointtmp*3];
                    i++;
                    datatmp[i]=colorCode[pointtmp*3+1];
                    i++;
                    datatmp[i]=colorCode[pointtmp*3+2];
                    i++;
                    int b=0xff;
                    datatmp[i]=(byte)b;
                    i++;
                }
                fos.write(datatmp);
            }
            System.out.println("完成");
            if(fos!=null)
                fos.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
