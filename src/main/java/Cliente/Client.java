/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Kimberly
 */
public class Client {

    public static void main(String args[]) throws Exception {

        Socket sk = new Socket("127.0.0.1", 5000);//host y puerto
        BufferedReader sin = new BufferedReader(new InputStreamReader(sk.getInputStream()));//pasa los datos de socket a socket
        PrintStream sout = new PrintStream(sk.getOutputStream());
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));//tomar los inputs del usuario - scanner clase
        String s;
        while (true) {
            System.out.println("Cliente: ");
            s = stdin.readLine();//lee lo que escribe y lo  guarda en "s"
            sout.println(s);//pasa el mensaje al servidor
            if (s.equalsIgnoreCase("BYE")) {
                System.out.println("Conexion terminada por el cliente");
                break;
            }
            s = sin.readLine();
            System.out.print("Servidor: " + s + "\n");
        }
        sk.close();
        sin.close();
        sout.close();
        stdin.close();
    }

}
