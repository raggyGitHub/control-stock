package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public int modificar(String nombre, String descripcion,Integer cantidad, Integer id) throws SQLException {
		Connection con = new ConectionFactory().recuperaConexion();
		PreparedStatement statement = con.prepareStatement("UPDATE PRODUCTO SET "
				+ " NOMBRE = ?"
				+ ", DESCRIPCION = ?"
				+ ", CANTIDAD = ?"
				+ " WHERE ID = ?" );
		statement.setString(1,nombre);
		statement.setString(2,descripcion);
		statement.setInt(3,cantidad);
		statement.setInt(4,id);
		statement.execute();
		int updateCount = statement.getUpdateCount();

		con.close();

		return updateCount;
	}

	public int eliminar(Integer id) throws SQLException {
		Connection con = new ConectionFactory().recuperaConexion();
		PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");
		statement.setInt(1,id);
		statement.execute();
		return statement.getUpdateCount();
	}

	public List<Map<String,String>> listar() throws SQLException {

		Connection con = new ConectionFactory().recuperaConexion();
		PreparedStatement statement = con.prepareStatement("SELECT ID,NOMBRE,DESCRIPCION, CANTIDAD FROM PRODUCTO");

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

		//System.out.println(result);
		con.close();

		return resultado;
	}

    public void guardar(Map<String,String> producto) throws SQLException {

		String nombre = producto.get("NOMBRE");
		String descripcion = producto.get("DESCRIPCION");
		Integer cantidad = Integer.valueOf(producto.get("CANTIDAD"));
		Integer maximoCantidad = 50;

		Connection con = new ConectionFactory().recuperaConexion();
		con.setAutoCommit(false);

		PreparedStatement statement = con.prepareStatement(
				"INSERT INTO PRODUCTO" +
				"(nombre,descripcion,cantidad)"+
				"VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS);
		try{
			do {
				int cantidadParaGuardar = Math.min(cantidad,maximoCantidad);
				ejecutaRegistro(statement, nombre, descripcion, cantidadParaGuardar);
				cantidad -= maximoCantidad;
			}while (cantidad>0);
			con.commit();
			System.out.println("COMMIT TRANSACTION");
		}
		catch (Exception e){
			con.rollback();
			System.out.println("ROLLBACK TRANSACTION");
		}
		statement.close();
		con.close();
	}

	private static void ejecutaRegistro(PreparedStatement statement, String nombre, String descripcion, Integer cantidad) throws SQLException {

		if (cantidad<50){
			throw new RuntimeException("Ocurrio un error!!");
		}

		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);
//		String sqlInsert ="INSERT INTO PRODUCTO(nombre,descripcion,cantidad)"
//				+"VALUES('"+producto.get("NOMBRE") +"','"
//				+producto.get("DESCRIPCION")+"',"
//				+producto.get("CANTIDAD")+")";
//
//		System.out.println(sqlInsert);
		//statement.execute(sqlInsert,Statement.RETURN_GENERATED_KEYS);
		statement.execute();
		ResultSet resultSet = statement.getGeneratedKeys();

		while (resultSet.next()){
			System.out.println(
					String.format("Fue insertado producto de ID %d",
							resultSet.getInt(1))
			);

		}
		resultSet.close();
	}

}
