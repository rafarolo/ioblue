package br.com.ioblue.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

	// private String id;
	private String category;
	private String doc;
	private String type;
	private Integer agency;
	private Integer  number;
	private String description;
	private boolean active;

}
