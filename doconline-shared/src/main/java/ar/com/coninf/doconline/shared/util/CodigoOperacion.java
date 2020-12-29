package ar.com.coninf.doconline.shared.util;

import java.math.BigInteger;

import org.apache.commons.lang.ArrayUtils;

public class CodigoOperacion {

	private BigInteger decimal;
	private String alfanumerico;
	private static char cifras[] = {'0','1','2','3','4','5','6','7','8','9',
									'A','B','C','D','E','F','G','H','I','J',
									'K','L','M','N','O','P','Q','R','S','T',
									'U','V','W','X','Y','Z'};


	public CodigoOperacion() {
	}
	
	public CodigoOperacion(BigInteger decimal) {
		this.decimal = decimal;
		this.alfanumerico = generarAlfanumerico(this.decimal);
	}
	
	public CodigoOperacion(int decimal) {
		this.decimal = BigInteger.valueOf(decimal);
		this.alfanumerico = generarAlfanumerico(this.decimal);
	}
	
	public CodigoOperacion(String alfanumerico) {
		this.alfanumerico = alfanumerico;
		this.decimal = generarDecimal(alfanumerico);
	}
	
	public BigInteger getDecimal() {
		return this.decimal;
	}
	
	public void SetDecimal(BigInteger decimal) {
		this.decimal = decimal;
		this.alfanumerico = generarAlfanumerico(decimal);
	}
	
	public void SetDecimal(int decimal) {
		this.decimal = BigInteger.valueOf(decimal);
		this.alfanumerico = generarAlfanumerico(this.decimal);
	}
	
	public String getAlfanumerico() {
		return this.alfanumerico;
	}
	
	public void setAlfanumerico(String alfanumerico) {
		this.alfanumerico = alfanumerico;
		this.decimal = generarDecimal(alfanumerico);
	}
	
	public void getCodigo() {
		this.decimal = this.decimal.add(BigInteger.ONE);
		this.alfanumerico = generarAlfanumerico(this.decimal);
	}
	
	private String generarAlfanumerico(BigInteger decimal) {
		String texto = "";
		String cifra;
		BigInteger division[];
		BigInteger potencia = new BigInteger("36");
		int posicion=10;
		while (posicion > 0) {
			division = decimal.divideAndRemainder(potencia);
			cifra = String.valueOf(cifras[division[1].intValue()]);
			texto = new StringBuffer(texto).insert(0,cifra).toString();
			decimal = division[0];
			posicion--;
		}
		return texto;
	}
	
	private BigInteger generarDecimal(String alfanumerico) {
		BigInteger valor = BigInteger.ZERO;
		int posicion = 9;
		int cifra;
		while (posicion > -1) {
			cifra = ArrayUtils.indexOf(cifras, alfanumerico.charAt(posicion));
			valor = valor.add(BigInteger.valueOf(new Double(cifra*(Math.pow(36, (9-posicion)))).longValue()));
			posicion--;
		}
		return valor;
	}
	
	
	
}
