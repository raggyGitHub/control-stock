package com.alura.jdbc.pruebas;

import com.alura.jdbc.factory.ConectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaPoolDeConexiones {
    public static void main(String[] args) throws SQLException {
        ConectionFactory conectionFactory = new ConectionFactory();

        for (int i = 0; i < 20; i++) {
            Connection conexion = conectionFactory.recuperaConexion();

            System.out.println("Abriendo la conexión número :"+(i+1));

        }
    }
}
