/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Kimberly
 */
public class Server {

    /*multithreading service provide by Java that 
    you can run multiple process at a time*/
    int port;// numero de puerto
    ServerSocket server = null; //servidor
    Socket client = null; //cliente
    ExecutorService pool = null;//sirve para que cuando uno inicie el proceso este le ayude a empezar el servidor usando el metodo runnable de Java
    /*clase que da metodos para administrar la terminación y los métodos que 
    pueden producir un futuro para rastrear el progreso de una o más tareas asincrónicas*/
    int clientCount = 0;//contar los clientes

    public static void main(String[] args) throws IOException {
        Server serverobj = new Server(5000);// iniciando el puerto con 5000
        serverobj.startServer();
    }

    //metodo constructor
    public Server(int port) {
        this.port = port;//definiendo el puerto con el de arriba(5000)
        pool = Executors.newFixedThreadPool(5);//cuantos procesos pueden ejecutarse - solo va a permitir 5 clientes
    }

    public void startServer() throws IOException {

        server = new ServerSocket(5000);
        System.out.println("Servidor Iniciado");
        System.out.println("Cualquier cliente puede parar el servidor enviando -1");
        while (true) {
            //aqui es donde se crean los clientes multiples
            client = server.accept();//inicializando el cliente - accept mantiene el server listo para el cliente se conecte y despues crea el socket del cliente
            clientCount++;//cuando 1 cliente se conecte este va a incrementar - asi se va a diferenciar los clientes
            ServerThread runnable = new ServerThread(client, clientCount, this);//constructor de la clase ServerThread
            pool.execute(runnable);//ayuda a ejecuatar el ruunable con el execute

        }
    }

    //hilos - multithread
    private static class ServerThread implements Runnable {

        Server server = null;
        Socket client = null;
        BufferedReader cin;//es una clase de Java para leer el texto de una secuencia de entrada
        PrintStream cout;//nos permite agregar la capacidad de imprimir datos a una corriente (flujo) de datos determinada
        Scanner sc = new Scanner(System.in);//utilizada para obtener la entrada de los tipos primitivos
        int id;
        String s;

        ServerThread(Socket client, int count, Server server) throws IOException {

            this.client = client;
            this.server = server;
            this.id = count;
            System.out.println("Conexion " + id + "establecido con el cliente" + client);

            cin = new BufferedReader(new InputStreamReader(client.getInputStream()));
            cout = new PrintStream(client.getOutputStream());// diferente en cada cliente 

        }

        @Override
        public void run() {
            int x = 1;
            try {
                while (true) {
                    s = cin.readLine();//lee lo que el cliente manda y lo guarda en "s"

                    System.out.print("Cliente(" + id + ") :" + s + "\n");//aqui lee el mensaje guardado en s
                    System.out.print("Servidor : ");

                    s = sc.nextLine();
                    if (s.equalsIgnoreCase("bye")) {
                        cout.println("BYE");
                        x = 0;
                        System.out.println("Conexion terminada por el servidor");
                        break;
                    }
                    cout.println(s);
                }
                //cierra todo
                cin.close();
                client.close();
                cout.close();
                if (x == 0) {
                    System.out.println("Servidor limpiandose");
                    System.exit(0);
                }
            } catch (IOException ex) {
                System.out.println("Error: " + ex);
            }

        }
    }
}
