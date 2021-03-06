/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectofinal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author PC
 */
public class ProyectoFinal {

    /**
     * @param args the command line arguments
     */
    private static final String FILENAME = "test.txt";
    
    public static int findTag(List<Partition> partList,char state){
        int i=0; boolean found=false;
        while(i<partList.size()&& !found){
            if(partList.get(i).getNames().contains(String.valueOf(state))){
                found = true;
            }else{
                i++;
            }
            
        }
        return i;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        BufferedReader br = null;
	FileReader fr = null;
        int iteration=1;
        
        Automaton automaton = new Automaton();
        List<Partition> partitions = new ArrayList<Partition>();
        List<Partition> partitionsAux = new ArrayList<Partition>();

	try {

            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);

            String sCurrentLine;

            br = new BufferedReader(new FileReader(FILENAME));

            while ((sCurrentLine = br.readLine()) != null) {
                    //System.out.println(sCurrentLine);
                    String[] split=sCurrentLine.split("\t");
                    automaton.getStates().add(new State(split[0].charAt(0),Integer.parseInt(split[1]),split[2].charAt(0),Integer.parseInt(split[3])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        
        boolean added = false;
        for(int i=0;i<automaton.getStates().size();i++){
            if(partitions.isEmpty()){
                partitions.add(new Partition().addState(automaton.getStates().get(i)));
                partitions.get(0).setTag1(automaton.getStates().get(i).getType0());
                partitions.get(0).setTag2(automaton.getStates().get(i).getType1());
                //partitions.get(0).setTag(1);
            }else{
                for(int j=0;j<partitions.size();j++){
                    if(partitions.get(j).getTag1()==automaton.getStates().get(i).getType0() 
                            && partitions.get(j).getTag2()==automaton.getStates().get(i).getType1()){
                        partitions.get(j).addState(automaton.getStates().get(i));
                        // partitions.get(partitions.size()-1).setTag(partitions.size());
                        added=true;
                    }
                }
                if(added==false){
                    partitions.add(new Partition().addState(automaton.getStates().get(i)));
                    partitions.get(partitions.size()-1).setTag1(automaton.getStates().get(i).getType0());
                    partitions.get(partitions.size()-1).setTag2(automaton.getStates().get(i).getType1());
                    //partitions.get(partitions.size()-1).setTag(partitions.size());
                }
                added=false;
            }          
        }

//        for(int i=0;i<automaton.getStates().size();i++){
//            System.out.println(automaton.getStates().get(i).toString());
//        }
////
        System.out.println("Iteration: "+iteration);
        iteration++;
        for(int i=0;i<partitions.size();i++){
            System.out.println("----");
            for(int j=0;j<partitions.get(i).getNames().size();j++){
                System.out.println(partitions.get(i).getNames().get(j));
            }
        }
        
//        System.out.println(partitions.size());
//        for(int i=0;i<partitions.size();i++){
//            System.out.println(partitions.get(i).getTag1());
//            System.out.println(partitions.get(i).getTag2());
//            System.out.println(partitions.get(i).getStates().size());
//        }


        
        while(partitions.size()!=partitionsAux.size()){
            
        
            added=false;
             for(int i=0;i<partitions.size();i++){//Itera sobre todos las particiones
                 int j =0;
                 while(j<partitions.get(i).getStates().size()){//Itera sobre los estados de la particion i
                     if(partitionsAux.isEmpty()){//si  particionsaux está vacía crea una partición y la inserta
                         partitionsAux.add(new Partition().addState(partitions.get(i).getStates().get(j)));
                         //agrega los tags a la particion
                         partitionsAux.get(0).setTag1(findTag(partitions,partitions.get(i).getStates().get(j).getTransition0()));
                         partitionsAux.get(0).setTag2(findTag(partitions,partitions.get(i).getStates().get(j).getTransition1()));
                     }
                     else{//sino va a revisar a que parrtición pertenece
                         for(int k=0;k<partitionsAux.size();k++){
                             //si                          
                             if(partitionsAux.get(k).getTag1()==findTag(partitions,partitions.get(i).getStates().get(j).getTransition0())
                                     && partitionsAux.get(k).getTag2()==findTag(partitions,partitions.get(i).getStates().get(j).getTransition1())){
                                 partitionsAux.get(k).addState(partitions.get(i).getStates().get(j));
                                 added=true;                             
                             }
                         }
                         if(!added){
                         //crear nueva particion en partitionsAux y agregar tags
                         partitionsAux.add(new Partition().addState(partitions.get(i).getStates().get(j)));
                         //agrega tags a particion
                         partitionsAux.get(partitionsAux.size()-1).setTag1(findTag(partitions,partitions.get(i).getStates().get(j).getTransition0()));
                         partitionsAux.get(partitionsAux.size()-1).setTag2(findTag(partitions,partitions.get(i).getStates().get(j).getTransition1()));
                        }
                     }
                     added=false;
                    j++;
                 }     
             }
             
             if(partitions.size()==partitionsAux.size()){
                 break;
             }else{
                    partitions.clear();
                    //System.out.println(partitions.size());
                    partitions.addAll(partitionsAux);
                    //System.out.println(partitions.size());
                    partitionsAux.clear();
                   // System.out.println(partitionsAux.isEmpty()); 
             }
            System.out.println("Iteration: "+iteration);
            for(int i=0;i<partitions.size();i++){
                System.out.println("----");
                for(int j=0;j<partitions.get(i).getNames().size();j++){
                    System.out.println(partitions.get(i).getNames().get(j));
                }
            }
            iteration++;
        }
        

    
         

                    
//        System.out.println(partitionsAux.size());
//        for(int i=0;i<partitionsAux.size();i++){
//            System.out.println("------");
//            System.out.println(partitionsAux.get(i).getTag1());
//            System.out.println(partitionsAux.get(i).getTag2());
//            System.out.println(partitionsAux.get(i).getStates().size());
//        }
//
//
//        for(int i=0;i<partitions.size();i++){
//            partitions.get(i).setTag(i);
//        }
        
//        added=false;
//         for(int i=0;i<partitions.size();i++){//Itera sobre todos las particiones
//             int j =0;
//             while(j<partitions.get(i).getStates().size()){//Itera sobre los estados de la particion i
//                 if(partitionsAux.isEmpty()){//si  particionsaux está vacía crea una partición y la inserta
//                     partitionsAux.add(new Partition().addState(partitions.get(i).getStates().get(j)));
//                     //agrega los tags a la particion
//                     partitionsAux.get(0).setTag1(findTag(partitions,partitions.get(i).getStates().get(j).getTransition0()));
//                     partitionsAux.get(0).setTag2(findTag(partitions,partitions.get(i).getStates().get(j).getTransition1()));
//                 }
//                 else{//sino va a revisar a que parrtición pertenece
//                     for(int k=0;k<partitionsAux.size();k++){
//                         //si                          
//                         if(partitionsAux.get(k).getTag1()==findTag(partitions,partitions.get(i).getStates().get(j).getTransition0())
//                                 && partitionsAux.get(k).getTag2()==findTag(partitions,partitions.get(i).getStates().get(j).getTransition1())){
//                             partitionsAux.get(k).addState(partitions.get(i).getStates().get(j));
//                             added=true;                             
//                         }
//                     }
//                     if(!added){
//                     //crear nueva particion en partitionsAux y agregar tags
//                     partitionsAux.add(new Partition().addState(partitions.get(i).getStates().get(j)));
//                     //agrega tags a particion
//                     partitionsAux.get(partitionsAux.size()-1).setTag1(findTag(partitions,partitions.get(i).getStates().get(j).getTransition0()));
//                     partitionsAux.get(partitionsAux.size()-1).setTag2(findTag(partitions,partitions.get(i).getStates().get(j).getTransition1()));
//                    }
//                 }
//                 added=false;
//                j++;
//             }     
//         }
//        for(int i=0;i<partitionsAux.size();i++){
//            System.out.println("------");
//            for(int j=0;j<partitionsAux.get(i).getNames().size();j++){
//                System.out.println(partitionsAux.get(i).getNames().get(j));
//            }
//        }
         
    }
    
}
