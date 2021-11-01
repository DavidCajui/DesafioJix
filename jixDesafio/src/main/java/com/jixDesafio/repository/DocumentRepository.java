package com.jixDesafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.jixDesafio.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

	Document findByid(long id);
	public static Document findByLinkFixo(String linkfixo) {
		return  DocumentRepository.findByLinkFixo(linkfixo);
	}
}
