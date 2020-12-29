package ar.com.boldt.monedero.shared.dto;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.com.coninf.doconline.shared.anotacion.InsertOptional;

@Entity
@Table(name = "NUM_CODIGO_OPERACION")
public class CodigoOperacionMovimientoDto implements Serializable{

	private static final long serialVersionUID = -3066647015237207878L;

	@Id
	@InsertOptional
	@Column(name = "NUMERO", unique = true, nullable = false)
	private BigInteger numero;
	
	@Column(name = "VALOR")
	private int valor;

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public BigInteger getNumero() {
		return numero;
	}

	public void setNumero(BigInteger numero) {
		this.numero = numero;
	}
		
	
}
