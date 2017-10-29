/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculadoraconjuntos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
/**
 *
 * @author L440
 */
public class CalculadoraConjuntos {

    /**
     * @param args the command line arguments
     */
    
    private static List<conjunto> conjuntos = new ArrayList<conjunto>();
    
    public static void main(String[] args) {
        Pattern pat = Pattern.compile("rfe");
        while(true){
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);
            System.out.print("--> ");
            String holi = "ño";
            try {
                holi = br.readLine();
            } catch (IOException ex) {
                System.out.println("error leyendo");
            }
            while(true){
                int a = holi.indexOf(" ");
                if(a==0)
                    holi = holi.substring(1, holi.length());
                else
                    break;                
            }
            if(holi.equals("exit"))
                break;
            
            int aux = holi.indexOf(" ");
            if(aux<0)
                aux = holi.indexOf(";");
            if(aux>=0){
                holi = holi.toLowerCase();
                String holi2 = holi.substring(aux, holi.length());
                switch(holi.substring(0, aux)){
                    case "set":
                        set(holi2);
                        break;
                    case "sets":
                        sets(holi2);
                        break;
                    case "showsets":
                        showsets(holi2);
                        break;
                    case "showset":
                        showset(holi2);
                        break;
                    case "setunion":
                        setUnion(holi2);
                        break; 
                    case "union":
                        union(holi2);
                        break;
                    case "setintersec":
                        setIntersec(holi2);
                        break; 
                    case "intersec":
                        intersec(holi2);
                        break;
                    /*case "mod":
                        System.out.println("SI ENTRA");
                        if(holi2.indexOf("-")>=0 && holi.length()>2){
                            mod(holi2.substring(holi2.indexOf("-")+2, holi2.length()),holi2.substring(holi2.indexOf("-")+1, holi2.indexOf("-")+2));
                            
                        }
                        break;*/
                    default:
                        System.out.println("Error en la consulta.");
                        break;
                }
            }else
                System.out.println("Error en la consulta");
        }
    }
    
    private static void set(String command){
        command = command.replace(" ", "").replace(";", "");
        if(command.indexOf(":=")>0 && command.indexOf("{")>0 && command.indexOf("}")>0){            
            String nombre = command.substring(0, command.indexOf(":="));
            if(buscaName(nombre)>0){                
                System.out.println("elemento ya está registrado");
                return;
            }
            String[] contenido = command.substring(command.indexOf("{")+1, command.indexOf("}")).split(",");
            List<String> au = new ArrayList();
            /*au.addAll(Arrays.asList(contenido));
            for(int i = 0; i<contenido.length; i++){
                for(int j = 0; j<contenido.length; j++ ){
                    System.out.println("i: "+i+" j: "+j);
                    //if((contenido[i].equals(contenido[j])&& contenido[i]!=null) || !contenido[i].equals(""))
                    if(contenido[i].equals(contenido[j])){
                        au.remove(i);
                        contenido[i] = null;
                    }                        
                }
            }
            System.out.println("LONG AU: "+au.size());*/
            conjunto e = new conjunto();
            e.name = nombre;
            e.list.addAll(Arrays.asList(contenido));
            conjuntos.add(e);
            System.out.println("Se agregó correctamente.");
        } else
            System.out.println("error en la sintaxis de petición SET");
    }
    
    private static void showsets(String command){
        command = command.replace(" ", "");
        boolean band = false;
        if(command.indexOf(";")==0 && command.length()==1){
            for(conjunto conj : conjuntos){
                System.out.println(conj.name+":={"+conj.imprimeList()+"}");
                band = true;
            }
            if(!band)
                System.out.println("No hay conjuntos registrados");
        }else
            System.out.println("error en la sintaxis");
    }
    
    private static void sets(String command){
        command = command.replace(" ", "");
        boolean band = false;
        if(command.indexOf(";")==0 && command.length()==1){
            for(conjunto conj : conjuntos){
                System.out.print(conj.name+",");
                band = true;
            }
            if(!band)
                System.out.println("No hay conjuntos registrados");
        }else
            System.out.println("error en la sintaxis");
    }
    
    private static void showset(String command){
        boolean band = false;
        command = command.replace(" ", "");
        if(command.length()>0){
            for(conjunto conj : conjuntos){
                if(conj.name.equals(command)){
                    System.out.println(conj.name+":={"+conj.imprimeList()+"}");
                    band = true;
                    break;
                }                    
            }
        }
        if(!band)
            System.out.println("No se encontró conjunto con ese nombre");
    }
    
    private static void setUnion(String command){
        command = command.replace(" ", "").replace(";", "");
        if(command.indexOf(",")>0){
            String[] aux = command.split(",");
            if(aux.length!=3){
                System.out.println("Error en los argumentos");
                return;
            }
            int Iconj1 = buscaName(aux[1])-1;
            int Iconj2 = buscaName(aux[2])-1;
            if(Iconj1>=0 && Iconj2>=0){
                conjunto union = new conjunto();
                union.type = 1;
                union.name = aux[0];
                union.conjunto1 = aux[1];
                union.conjunto2 = aux[2];
                union.list = realizaUnion(conjuntos.get(Iconj1).list, conjuntos.get(Iconj2).list);
                conjuntos.add(union);
                System.out.println("Se creo un nuevo conjunto");
            }else
                System.out.println("No existe uno de los conjuntos ingresados");
        }else
            System.out.println("Error en la consulta");
    }
    
    private static void union(String command){
        command = command.replace(" ", "").replace(";", "");
        if(command.indexOf(",")>0){
            String[] aux = command.split(",");
            if(aux.length!=2){
                System.out.println("Error en los argumentos");
                return;
            }
            int Iconj1 = buscaName(aux[0])-1;
            int Iconj2 = buscaName(aux[1])-1;
            if(Iconj1>=0 && Iconj2>=0){
                conjunto union = new conjunto();
                union.list = realizaUnion(conjuntos.get(Iconj1).list, conjuntos.get(Iconj2).list);
                System.out.println("{"+union.imprimeList()+"}");
            }else
                System.out.println("No existe uno de los conjuntos ingresados");
        }else
            System.out.println("Error en la consulta");
    }
    
    private static void setIntersec(String command){
        command = command.replace(" ", "").replace(";", "");
        if(command.indexOf(",")>0){
            String[] aux = command.split(",");
            if(aux.length!=3){
                System.out.println("Error en los argumentos");
                return;
            }
            int Iconj1 = buscaName(aux[1])-1;
            int Iconj2 = buscaName(aux[2])-1;
            if(Iconj1>=0 && Iconj2>=0){
                conjunto union = new conjunto();
                union.type = 2;
                union.name = aux[0];
                union.conjunto1 = aux[1];
                union.conjunto2 = aux[2];
                union.list = realizaInterseccion(conjuntos.get(Iconj1).list, conjuntos.get(Iconj2).list);
                conjuntos.add(union);
                System.out.println("Se creo un nuevo conjunto");
            }else
                System.out.println("No existe uno de los conjuntos ingresados");
        }else
            System.out.println("Error en la consulta");
    }
    
    private static void intersec(String command){
        command = command.replace(" ", "").replace(";", "");
        if(command.indexOf(",")>0){
            String[] aux = command.split(",");
            if(aux.length!=2){
                System.out.println("Error en los argumentos");
                return;
            }
            int Iconj1 = buscaName(aux[0])-1;
            int Iconj2 = buscaName(aux[1])-1;
            if(Iconj1>=0 && Iconj2>=0){
                conjunto union = new conjunto();
                union.list = realizaInterseccion(conjuntos.get(Iconj1).list, conjuntos.get(Iconj2).list);
                System.out.println("{"+union.imprimeList()+"}");
            }else
                System.out.println("No existe uno de los conjuntos ingresados");
        }else
            System.out.println("Error en la consulta");
    }
    
    private static List<String> realizaUnion(List<String> uno, List<String> dos){
        List<String> aux = uno;
        for (int i = 0; i<dos.size(); i++){
            if(!uno.contains(dos.get(i)))
                aux.add(dos.get(i));
        }
        return aux;
    }
    
    private static List<String> realizaInterseccion(List<String> uno, List<String> dos){
        List<String> aux = new ArrayList();
        for (int i = 0; i<dos.size(); i++){
            if(uno.contains(dos.get(i))){
                aux.add(dos.get(i));
                System.out.println("inter: "+dos.get(i));
            }
                
        }
        return aux;
    }
    
    /*private static void mod(String command, String i){
        command = command.replace(" ", "").replace(";", "");
        switch(i){
            case "a":
                
                break;
            case "d":
                break;
            case "n":
                break;
        }        
    }*/
    
    private static int buscaName(String name){
        int i = 1; 
        for(conjunto conj : conjuntos){
            if(conj.name.equals(name)){
                return i;
            }  
            i++;
        }
        return 0;
    }
    
    static class conjunto{
        String name;
        List<String> list;
        int type;
        String conjunto1;
        String conjunto2;
        conjunto(){
            name = null;
            list = new ArrayList<String>();
            conjunto1 = null;
            conjunto2 = null;
            type = 0;
        }
        
        String imprimeList(){
            String aux = "";
            for (String s: list){
                aux+=s+",";
            }
            return aux.substring(0, aux.length()-1);
        }
    }
}
