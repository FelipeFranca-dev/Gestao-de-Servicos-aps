package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Servico {

	public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private Integer id;
	private String cliente;
	private String tipo;
	private double preco;
	private String status;
	
	public Servico(Integer id, String cliente, String tipo, double preco, String status) {
		this.id = id;
		this.cliente = cliente;
		this.tipo = tipo;
		this.preco = preco;
		this.status = status;
	}

	public Servico(String cliente, String tipo, double preco, String status) {
		this(null, cliente,tipo, preco,status);
	}

	public Integer getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	public String getCliente() {return cliente;}
	public void setCliente(String cliente) {this.cliente = cliente;}
	
	public String getTipo() {return tipo;}
	public void setTipo(String tipo) {this.tipo = tipo;}

	public double getPreco() {return preco;}
	public void setPreco(double preco) {this.preco = preco;}

	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}

	@Override
	public String toString() {
		return "Servico [id=" + id + ", cliente=" + cliente
				+ ", tipo=" + tipo + ", preco=" + preco
				+ ", status=" + status + "]";
	}
}
