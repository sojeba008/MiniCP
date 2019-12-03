package minicp.examples;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Preprocess {
    public static void main(String[] args) {
        String data="10 25 14 25 15\n25 15 11 14\n56 24 25 15 12 11 15 10 25 25 25";
        System.out.println("-------------------");

//        System.out.println(itemlist(data));

        System.out.println("-------------------");

        ArrayList[] dataInList = new ArrayList[3];
        ArrayList temp = new ArrayList();
        for (int i = 0; i < data.split("\n").length; i++) {
            for (String symb : data.split("\n")[i].split(" ")) {
                temp.add(symb);
            }
            dataInList[i]= (ArrayList) temp.clone();
            temp.clear();
        }
        System.out.println("-------------------");

//        System.out.println(dataInList[1]);
//        System.out.println(getUnfrequentItemList(dataInList,itemlist(data),2));

        System.out.println("-------------------");
//        System.out.println(dataformatOnArray(dataInList));
//        System.out.println((dataInList[1].get(0)));


        System.out.println("Le support est "+ getSupport(dataInList,"25 15"));
//
//
//        for(int i=0;i<14;i++){
//            for(int k=0;k<4;k++){
//                System.out.print(dataformatOnArray(data)[i][k]);
//                System.out.print(" ");
//            }
//            System.out.println("");
//        }
//        for(int i=0;i<14;i++){
//                System.out.print(dataformatOnString(data)[i]);
//                System.out.println("");
//            }
    }


    public static int getSupport(ArrayList[] dataInList,String pattern){
//        if(String.valueOf(dataInList[0].get(1)).equals(seq.split(" ")[1])) {
//            System.out.println("voilà ce que je veux " + String.valueOf(dataInList[0].get(1)) + " " + seq.split(" ")[1]);
//        }

//        System.out.println(dataInList[2].get(1));
        for (int j=0;j<dataInList.length;j++) {
//            System.out.println("It "+j);

            for (int i=0;i<dataInList[j].size();i++) {
                int tokeep=0;
                for (String s : pattern.split(" ")) {
//                        System.out.println("kana "+j+"et"+i+": "+s+ " et "+dataInList[j].get(i));
                    if (String.valueOf(dataInList[j].get(i)).equals(s)) {
                        tokeep=-1;
//                     System.out.println(s+" trouvé à la position " +i);
                        break;
                    }
                }
//                System.out.println("tkepp "+tokeep);
                if(tokeep!=-1) {
//                    System.out.println("yakana " + i);
                    dataInList[j].remove(i);
                    i=i-1;
                    tokeep = 0;
                }


            }
        }
        int support=0;
        pattern=" "+pattern+" ";
        for(int k=0;k<dataInList.length;k++) {
            String stemp = " ";
            for (int i = 0; i < dataInList[k].size(); i++) {
                stemp += String.valueOf(dataInList[2].get(i)) + " ";
            }
            if(stemp.contains(pattern))
            {
                support+=1;
            }
        }
        return support;
    }



    public static ArrayList dataformatOnArray(ArrayList[] data){
        ArrayList processed_data = new ArrayList();
        int j=-1;
        ArrayList temp=new ArrayList();
        for(int i=0;i<data.length;i++){
//            System.out.println(data.split("\n")[i]);
            int pos=-1;
            for(int k=0;k<data[i].size();k++){
                j+=1;
                temp.add(i+1);
                temp.add(pos+2);
                temp.add(1);
                temp.add(Integer.parseInt((String) data[i].get(k)));
                System.out.println(temp);
                processed_data.add(temp.clone());
                temp.clear();
                pos+=1;
            }
        }
        return processed_data;
    }

    public static ArrayList dataformatOnString(ArrayList[] data){
        ArrayList processed_data = new ArrayList();
        String temp="";
        for(int i=0;i<data.length;i++){
//            System.out.println(data.split("\n")[i]);
            int pos=-1;
            for(int k=0;k<data[i].size();k++){
                temp+=String.valueOf(i+1)+" "+String.valueOf(pos+2)+" 1 "+String.valueOf(data[i].get(k));
                processed_data.add(temp);
                temp="";
                pos+=1;
            }
        }

        return processed_data;
    }



    public static ArrayList getUnfrequentItemList(ArrayList[] data,ArrayList itemlist,int treshold){
        int counter=0;
        ArrayList unfrequentItem=new ArrayList();

        for (int i=0;i<itemlist.size();i++){
//            System.out.println(itemlist.get(i));
            for(int j=0;j<data.length;j++){

                if(data[j].contains(itemlist.get(i))){
                    counter+=1;
                }
            }
            if(counter<treshold){
//                System.out.println(counter);
                unfrequentItem.add(itemlist.get(i));
                counter=0;
            }
        }
        return unfrequentItem;
    }



    public static ArrayList itemlist(String data) {
        ArrayList AlreadyAdd = new ArrayList();
        ArrayList itemlist = new ArrayList();
        for (int i = 0; i < data.split("\n").length; i++) {
            for (String symb : data.split("\n")[i].split(" ")) {
//                System.out.println(symb);
                if(!AlreadyAdd.contains(symb)){
                    itemlist.add(symb);
                    AlreadyAdd.add(symb);
                }
            }

        }
        return itemlist;
    }




}
