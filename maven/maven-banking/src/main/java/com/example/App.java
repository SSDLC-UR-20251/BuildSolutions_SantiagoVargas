package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;


public class App {

    // 1. Leer el archivo JSON desde un .txt
    public static String leerArchivo(String rutaArchivo) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/resources/" + rutaArchivo)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 2. Obtener transacciones de un usuario específico
    public static List<JSONObject> obtenerTransacciones(String jsonData, String usuario) {
        List<JSONObject> transaccionesUsuario = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
    
            // Verifica si el usuario está en el JSON
            if (!jsonObject.has(usuario)) {
                System.out.println("El usuario no tiene transacciones registradas.");
                return transaccionesUsuario; // Retorna lista vacía
            }
    
            // Obtiene el array de transacciones del usuario
            JSONArray transacciones = jsonObject.getJSONArray(usuario);
    
            // Agrega cada transacción a la lista
            for (int i = 0; i < transacciones.length(); i++) {
                transaccionesUsuario.add(transacciones.getJSONObject(i));
            }
        } catch (JSONException e) {
            System.err.println("Error al parsear JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return transaccionesUsuario;
    }
    

    // 3. Generar extracto bancario en un archivo .txt
    public static void generarExtracto(String usuario, List<JSONObject> transacciones) {
        String nombreArchivo = "extracto_" + usuario.replace("@", "_").replace(".", "_") + ".txt";
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write("Extracto Bancario - " + usuario + "\n");
            writer.write("====================================\n");
    
            for (JSONObject transaccion : transacciones) {
                String tipo = transaccion.getString("type");
                String monto = transaccion.getString("balance");
                String fecha = transaccion.getString("timestamp");
    
                writer.write("Fecha: " + fecha + "\n");
                writer.write("Tipo: " + tipo + "\n");
                writer.write("Monto: " + monto + "\n");
                writer.write("------------------------------------\n");
            }
    
            System.out.println("Extracto generado correctamente en: " + nombreArchivo);
    
        } catch (IOException e) {
            System.err.println("Error al generar el extracto: " + e.getMessage());
        }
    }
    
    

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su correo electrónico: ");
        String usuario = scanner.nextLine();

        String jsonData = leerArchivo("transactions.txt");
        if (jsonData == null) {
            System.out.println("No se pudo leer el archivo de transacciones.");
            return;
        }

        List<JSONObject> transacciones = obtenerTransacciones(jsonData, usuario);
        if (transacciones.isEmpty()) {
            System.out.println("No se encontraron transacciones para este usuario.");
        } else {
            generarExtracto(usuario, transacciones);
        }
    }
}