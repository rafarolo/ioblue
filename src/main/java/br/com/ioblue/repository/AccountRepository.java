package br.com.ioblue.repository;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import br.com.ioblue.model.Account;

@EnableScan
public interface AccountRepository extends CrudRepository<Account, String> {

	List<Account> findByDoc(String doc);

	List<Account> findByActive(boolean active);

}