package entities;

public class Servico {
	
	private Integer id;
	private String nome;
	private int idade;
	
	public Servico(Integer aId, String aNome, int aIdade) {
		this.id = aId;
		this.nome = aNome;
		this.idade = aIdade;
	}
	
	public Servico(String aNome, int aIdade) {
		this(null, aNome, aIdade);
	}

	public Integer getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public int getIdade() {return idade;}
	public void setIdade(int idade) {this.idade = idade;}

	@Override
	public String toString() {
		return "Animal [id=" + id + ", nome=" + nome + ", idade=" + idade + "]";
	}
	
}
