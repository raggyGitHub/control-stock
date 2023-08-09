package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConectionFactory;
import com.alura.jdbc.modelo.Producto;
import com.alura.jdbc.dao.ProductoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public int modificar(String nombre, String descripcion,Integer cantidad, Integer id) throws SQLException {
		ConectionFactory factory = new ConectionFactory();
		final Connection con = factory.recuperaConexion();
		try(con){
			final PreparedStatement statement = con.prepareStatement("UPDATE PRODUCTO SET "
					+ " NOMBRE = ?"
					+ ", DESCRIPCION = ?"
					+ ", CANTIDAD = ?"
					+ " WHERE ID = ?" );
			try(statement){
				statement.setString(1,nombre);
				statement.setString(2,descripcion);
				statement.setInt(3,cantidad);
				statement.setInt(4,id);
				statement.execute();
				int updateCount = statement.getUpdateCount();

				return updateCount;
			}

		}

	}

	public int eliminar(Integer id) throws SQLException {
		ConectionFactory factory = new ConectionFactory();
		final Connection con = factory.recuperaConexion();
		try(con){
			final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");
			try(statement){
				statement.setInt(1,id);
				statement.execute();
				return statement.getUpdateCount();
			}
		}
	}

	public List<Map<String,String>> listar() throws SQLException {

		ConectionFactory factory = new ConectionFactory();
		final Connection con = factory.recuperaConexion();
		try(con) {
			final PreparedStatement statement = con.prepareStatement("SELECT ID,NOMBRE,DESCRIPCION, CANTIDAD FROM PRODUCTO");
			try(statement){
				statement.execute();
				ResultSet resultSet = statement.getResultSet();

				List<Map<String,String>> resultado = new ArrayList<>();

				while (resultSet.next()){
					Map<String,String> fila = new HashMap<>();
					fila.put("ID", String.valueOf(resultSet.getInt("ID")));
					fila.put("NOMBRE", resultSet.getString("NOMBRE"));
					fila.put("DESCRIPCION", resultSet.getString("DESCRIPCION"));
					fila.put("CANTIDAD", resultSet.getString("CANTIDAD"));

					resultado.add(fila);
				}
				return resultado;
			}
		}
	}

    public void guardar(Producto producto) throws SQLException {

		ProductoDAO productoDAO = new ProductoDAO(new ConectionFactory().recuperaConexion());
		productoDAO.guardar(producto);
	}

}
