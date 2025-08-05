//package com.example.foodie;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class JavaServer {
//    public static void main(String[] args) {
//        try(ServerSocket serverSocket = new ServerSocket(5000)){
//            System.out.println("Server is started and  waiting for client");
//
//            while (true) {
//                Socket client = serverSocket.accept();
//                System.out.println("Client connected");
//
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                String jsonString = bufferedReader.readLine();
//                System.out.println("Received JSON: " + jsonString);
//
//                client.close();
//            }
//


//
//
//
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
