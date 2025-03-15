package com.example;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void testLeerArchivo() {
        String contenido = App.leerArchivo("transactions.txt");
        assertNotNull(contenido, "El archivo no debe ser nulo");
        assertFalse(contenido.isEmpty(), "El archivo no debe estar vacío");
    }

    @Test
    public void testObtenerTransacciones() {
        String jsonData = "{ \"test@example.com\": [ " +
            "{\"balance\": \"100.50\", \"type\": \"Deposit\", \"timestamp\": \"2025-03-15\"} ] }";

        List<JSONObject> transacciones = App.obtenerTransacciones(jsonData, "test@example.com");
        assertNotNull(transacciones, "Las transacciones no deben ser nulas");
        assertEquals(1, transacciones.size(), "Debe haber exactamente una transacción");
        assertEquals("100.50", transacciones.get(0).getString("balance"), "El balance debe coincidir");
    }


    @Test
    public void testGenerarExtracto() throws IOException {
        List<JSONObject> transacciones = List.of(
            new JSONObject()
                .put("balance", "100.50")
                .put("type", "Deposit")
                .put("timestamp", "2025-03-15 14:00:00")
        );

        String usuario = "test@example.com";
        App.generarExtracto(usuario, transacciones);

        String nombreArchivo = "extracto_" + usuario.replace("@", "_").replace(".", "_") + ".txt";
        File archivo = new File(nombreArchivo);
        
        assertTrue(archivo.exists(), "El archivo de extracto debe existir");

        // Leer el contenido del archivo y verificar su formato
        List<String> lineas = Files.readAllLines(Path.of(nombreArchivo));
        assertEquals("Extracto Bancario - test@example.com", lineas.get(0));
        assertEquals("====================================", lineas.get(1));
        assertEquals("Fecha: 2025-03-15 14:00:00", lineas.get(2));
        assertEquals("Tipo: Deposit", lineas.get(3));
        assertEquals("Monto: 100.50", lineas.get(4));
        assertEquals("------------------------------------", lineas.get(5));
    }



}
