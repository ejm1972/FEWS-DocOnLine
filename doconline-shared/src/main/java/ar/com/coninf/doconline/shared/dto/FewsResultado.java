package ar.com.coninf.doconline.shared.dto;

import java.io.Serializable;

import javax.persistence.Column;

public class FewsResultado implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "_codigo_")
	private Long _codigo_;
	@Column(name = "_descripcion_")
	private String _descripcion_;
	@Column(name = "_observacion_")
	private String _observacion_;
	
	public Long get_codigo_() {
		return _codigo_;
	}
	public void set_codigo_(Long _codigo_) {
		this._codigo_ = _codigo_;
	}
	public String get_descripcion_() {
		return _descripcion_;
	}
	public void set_descripcion_(String _descripcion_) {
		this._descripcion_ = _descripcion_;
	}
	public String get_observacion_() {
		return _observacion_;
	}
	public void set_observacion_(String _observacion_) {
		this._observacion_ = _observacion_;
	}

}
