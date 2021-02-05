package br.com.ioblue.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum Category {

	BASIC("Basic", "Plano Básico com menor tarifa e benefícios essenciais para sua transações"), 
	CLASSIC("Classic", "Plano para quem quer conforme e não abre mão da melhor relação custo-benefício"),
	PERSONNALITE("Personnalité", "Plano para quem busca sempre o melhor");

	String title;
	String description;

}
