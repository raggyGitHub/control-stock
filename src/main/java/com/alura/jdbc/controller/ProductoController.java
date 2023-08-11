package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConectionFactory;
import com.alura.jdbc.modelo.Producto;
import com.alura.jdbc.dao.ProductoDAO;

import java.sql.*;
import java.util.List;

public class ProductoController {

	public  ProductoDAO productoDao;

	public ProductoController() {
		this.productoDao = new ProductoDAO(new ConectionFactory().recuperaConexion());;
	}

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id)  {
		return productoDao.modificar(nombre, descripcion, cantidad, id);
	}

	public int eliminar(Integer id) throws SQLException {
		return productoDao.eliminar(id);
	}

	public List<Producto> listar()  {
		return productoDao.listar();
	}

    public void guardar(Producto producto, Integer categoriaId) {
		producto.setCategoriaId(categoriaId);
		productoDao.guardar(producto);
	}

}
