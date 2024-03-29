package servidor;

import interfaz.*;
import clases.*;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;




public class Servidor {
	private static final int PUERTO = 5000; //Si cambias aquí el puerto, recuerda cambiarlo en el cliente
	private static int slotsporPelicula = 0; 
	private static String 	IPServidor = "192.168.1.4";
	private static int max= 3;

	
	private static final Pelicula[] listaPeliculas = {
			new Pelicula("Pelicula 1",slotsporPelicula)
			,new Pelicula("Pelicula 2",slotsporPelicula)
			,new Pelicula("Pelicula 3",slotsporPelicula)};
	
	
	
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		
		
		System.setProperty("java.rmi.server.hostname", IPServidor);
        Remote remote = UnicastRemoteObject.exportObject(new Interfaz(){
        	/*
				Sobrescribir opcionalmente los métodos que escribimos en la interfaz
        	*/
        	       	      	
                                  
            @Override
            public String listarPeliculas() throws RemoteException{
            	
            	int indice=0;
            	String  cadenaPeliculas= "";
            	for (Pelicula pelicula : listaPeliculas) {
            		
            		cadenaPeliculas+= indice +"->"+ pelicula.getTitulo() +"\n";
            		indice++;
				}
            	return cadenaPeliculas + "\nIngrese el número de pelicula que desea ver...\n";
            };
            
            @Override
            public String seleccionPelicula (int indice) throws RemoteException{
            	
            	String salida="";
            	          	
            	Pelicula film = listaPeliculas[indice];
            	
            	salida+="SELECCION: "+ film.getTitulo() ;
            	 	
            	return salida;
            
            	
            };
            
            
           
            @Override
            public void salirPelicula(int indice) throws RemoteException{
            	
            	listaPeliculas[indice].disminuirClientesPelicula();	
            	           	
            };
            
            @Override
            public String avisarDisponibilidad(int indice) throws RemoteException{
            	String salida = "";
            	if(confirmacionPelicula(indice)){
            		//salida+="Puede ver la pelicula" + listaPeliculas[indice].getTitulo();
            		salida+="Puede ver la pelicula";
            		
            	}else {
            		salida = "No esta disponible por el momento, hay muchos clientes en la pelicula";
            	}
            	return salida;
            };
            
            
            
            @Override
            public boolean confirmacionPelicula(int indice) throws RemoteException{
            	boolean bandera = false;
        		if(listaPeliculas[indice].getNumeroClientesPelicula() < max) {
        			bandera = true;
        		}else {
        			System.out.println("Esta copado con la pelicula: "+ listaPeliculas[indice].getTitulo());
        			bandera = false;
        		}
        		return bandera;
            			
            };
            @Override
            public String transmision(int indice) throws RemoteException{
            	String cadena = ""; 
            	listaPeliculas[indice].setNumeroClientesPelicula();
            	
            	cadena += "Se esta reproduciendo la pelicula: "+ listaPeliculas[indice].getTitulo() 
            			+"\n Nº de usuarios en linea:"+ listaPeliculas[indice].getNumeroClientesPelicula();
            	
            	System.out.println("Se esta transmitiendo:"+ listaPeliculas[indice].getTitulo()  );
            	return cadena;
            }
  
        }, 0);
        
        
        
        Registry registry = LocateRegistry.createRegistry(PUERTO);
        System.out.println("Servidor escuchando en el puerto " + String.valueOf(PUERTO));
        registry.bind("Sistema de Video", remote); // Registrar Peliculas
    }

	
	
	
	
}

