package com.alura.jdbc.dao;

import com.alura.jdbc.modelo.Producto;

import java.sql.*;

public class ProductoDAO {
    final private Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

    public void guardar(Producto producto) throws SQLException {

        try (con) {
            con.setAutoCommit(false);

            final PreparedStatement statement = con.prepareStatement(
                    "INSERT INTO PRODUCTO" +
                            "(nombre,descripcion,cantidad)" +
                            "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            try (statement) {
                ejecutaRegistro(statement, producto);
                con.commit();
            }
        } catch (Exception e) {
            con.rollback();
            System.out.println("ROLLBACK TRANSACTION");
        }
    }

    private static void ejecutaRegistro(PreparedStatement statement, Producto producto) throws SQLException {

//		if (cantidad < 50) {
//			throw new RuntimeException("Ocurrio un error!!");
//		}
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());

        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();
        try (resultSet) {
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado producto de ID %s",producto));
            }

        }
    }
}
