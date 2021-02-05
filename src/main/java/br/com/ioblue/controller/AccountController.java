package br.com.ioblue.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.ioblue.model.Account;
import br.com.ioblue.model.AccountDTO;
import br.com.ioblue.repository.AccountRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class AccountController {

	@Autowired
	AccountRepository accountRepository;

	@GetMapping("/accounts")
	public ResponseEntity<List<Account>> getAllAccountsByDoc(@RequestParam(required = false) String doc) {
		try {
			List<Account> accounts = new ArrayList<>();
			if (doc == null) {
				accountRepository.findAll().forEach(accounts::add);
			} else {
				accountRepository.findByDoc(doc).forEach(accounts::add);
			}
			if (accounts.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(accounts);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/accounts/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable("id") String id) {
		Optional<Account> accountData = accountRepository.findById(id);
		if (accountData.isPresent()) {
			return ResponseEntity.ok(accountData.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/accounts")
	public ResponseEntity<Account> newAccount(@RequestBody AccountDTO accountDTO) {
		try {
			// SecureRandom random = new SecureRandom();
			// @formatter:off
			Account account = Account.builder()
					//.id(new UID().toString())
					.category(accountDTO.getCategory())
					.number(accountDTO.getNumber())
					.agency(accountDTO.getAgency())
					.doc(accountDTO.getDoc())
					.type(accountDTO.getType())
					.active(true)
					.build();
			// @formatter:on
			Account accountNew = accountRepository.save(account);
			if (accountNew.getId() != null) {
				return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri())
						.build();
			} else {
				return ResponseEntity.badRequest().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/accounts/{id}")
	public ResponseEntity<Account> updateAccount(@PathVariable("id") String id, @RequestBody AccountDTO accountDTO) {
		Optional<Account> accountData = accountRepository.findById(id);
		if (accountData.isPresent()) {
			Account accountUpdate = accountData.get();
			accountUpdate.setCategory(accountDTO.getCategory());
			accountUpdate.setDoc(accountDTO.getDoc());
			accountUpdate.setType(accountDTO.getType());
			accountUpdate.setDescription(accountDTO.getDescription());
			accountUpdate.setActive(accountDTO.isActive());
			return ResponseEntity.ok(accountRepository.save(accountUpdate));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<HttpStatus> deleteAccount(@PathVariable("id") String id) {
		try {
			accountRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/accounts")
	public ResponseEntity<HttpStatus> deleteAllAccounts() {
		try {
			accountRepository.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/accounts/active")
	public ResponseEntity<List<Account>> findAccountsActive() {
		try {
			List<Account> account = accountRepository.findByActive(true);
			if (account.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return ResponseEntity.ok(account);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
