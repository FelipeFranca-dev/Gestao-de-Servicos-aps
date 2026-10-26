package entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Servico {

	public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private Integer id;
	private String cliente;
	private String tipo;
	private String endereco;
	private LocalDate data;
	private String status;
	private String observacoes;

	public Servico(Integer id, String cliente, String tipo, String endereco,LocalDate data, String status, String observacoes) {
		this.id = id;
		this.cliente = cliente;
		this.tipo = tipo;
		this.endereco = endereco;
		this.data = data;
		this.status = status;
		this.observacoes = observacoes;
	}

	public Servico(String cliente, String tipo, String endereco,LocalDate data, String status, String observacoes) {
		this(null, cliente, tipo, endereco, data, status, observacoes);
	}

	public Integer getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	public String getCliente() {return cliente;}
	public void setCliente(String cliente) {this.cliente = cliente;}
	
	public String getTipo() {return tipo;}
	public void setTipo(String tipo) {this.tipo = tipo;}

	public String getEndereco() {return endereco;}
	public void setEndereco(String endereco) {this.endereco = endereco;}

	public LocalDate getData() {return data;}
	public void setData(LocalDate data) {this.data = data;}

	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}

	public String getObservacoes() {return observacoes;}
	public void setObservacoes(String observacoes) {this.observacoes = observacoes;}

	@Override
	public String toString() {
		return "Servico [id=" + id + ", cliente=" + cliente
				+ ", tipo=" + tipo + ", endereco=" + endereco
				+ ", data=" + data + ", status=" + status
				+ ", observacoes=" + observacoes + "]";
	}
}