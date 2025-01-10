package tn.iit.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.validation.Valid;
import tn.iit.DAO.ClientRepository;
import tn.iit.dto.LoginDTO;
import tn.iit.dto.RegisterDTO;
import tn.iit.entite.Client;
import tn.iit.service.ClientService;

@RestController
@RequestMapping("/account")
@CrossOrigin(origins = "*")
public class ClientController {
	@Value("${secrurity.jwt.secret-key}")
	private String jwtSecretKey;
	@Value("${security.jwt.issuer}")
	private String jwtIssuer;

	@Autowired
	private ClientRepository repo;
	@Autowired
	private ClientService clientService;
	// Injectez-le via le constructeur ou utilisez une méthode @Bean

	/*
	 * private String createJwtToken(AppUser appUser) { Instant now = Instant.now();
	 * JwtClaimsSet claims = JwtClaimsSet.builder() .issuer(jwtIssuer)
	 * .issuedAt(now) .expiresAt(now.plusSeconds(24 * 3600))
	 * .subject(appUser.getUsername()) .claim("role", appUser.getRole()) .build();
	 * var encoder = new NimbusJwtDecoder( new
	 * ImmutableSecret<>(jwtSecretKey.getBytes())); var params =
	 * JwtEncoderParameters.from(
	 * JwsHeader.with((MacAlgorithm.HS256).build().claims); return
	 * encoder.encode(params).getTokenValue(); }
	 */

	@GetMapping("/all")
	public List<Client> getAll() {
		return clientService.getAllAccounts();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Client> getById(@PathVariable Integer id) {
		return clientService.findById(id).map(client -> ResponseEntity.ok(client))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/login")
	// @CrossOrigin(origins = "*")
	public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDto, BindingResult result) {
		if (result.hasErrors()) {
			var errorList = result.getAllErrors();
			var errorMap = new HashMap<String, String>();
			for (int i = 0; i < errorList.size(); i++) {
				var error = (FieldError) errorList.get(i);
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errorMap);
		}

		try {
			Client client = repo.findByUsername(loginDto.getUsername());
			if (client == null) {
				client = repo.findByEmail(loginDto.getUsername()); // Si c'est un email, essayez aussi
				if (client == null) {
					return ResponseEntity.status(401).body("Invalid username or email.");
				}
			}

			// Vérifiez le mot de passe
			var bCryptEncoder = new BCryptPasswordEncoder();
			if (!bCryptEncoder.matches(loginDto.getPassword(), client.getPassword())) {
				return ResponseEntity.status(401).body("Invalid password.");
			}

			// Générer le token JWT
			String jwtToken = createJwtToken(client);

			// Créez une réponse avec le token et les informations utilisateur
			var response = new HashMap<String, Object>();
			response.put("token", jwtToken);
			response.put("client", client);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("An error occurred while processing your request.");
		}
	}

	@PostMapping("/register")
	@CrossOrigin(origins = "*")

	public ResponseEntity<Object> register(@Valid @RequestBody RegisterDTO registerDto, BindingResult result) {
		if (result.hasErrors()) {
			var errorList = result.getAllErrors();
			;
			var errorMap = new HashMap<String, String>();
			for (int i = 0; i < errorList.size(); i++) {
				var error = (FieldError) errorList.get(i);
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errorMap);

		}
		var bCryptEncoder = new BCryptPasswordEncoder();
		Client client = new Client();
		client.setEmail(registerDto.getEmail());
		client.setFirstName(registerDto.getFirstName());
		client.setUsername(registerDto.getUsername());
		client.setLastName(registerDto.getLastName());
		client.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
		client.setRole(registerDto.getRole());

		try {
			var otherUser = repo.findByUsername(registerDto.getUsername());
			if (otherUser != null) {
				return ResponseEntity.badRequest().body("username already used");
			}
			otherUser = repo.findByEmail(registerDto.getEmail());
			if (otherUser != null) {
				return ResponseEntity.badRequest().body("email already used");
			}
			repo.save(client);
			String jwtToken = createJwtToken(client);
			var response = new HashMap<String, Object>();
			response.put("token", jwtToken);
			response.put("user", client);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			System.out.println("ther is an exception");
			e.printStackTrace();
			return ResponseEntity.status(500).body("An error occurred while processing your request.");

		}

	}

	@GetMapping("/profile")
	public ResponseEntity<Object> profile(Authentication auth) {
		var response = new HashMap<String, Object>();
		response.put("username", auth.getName());
		response.put("authorities", auth.getAuthorities());
		var appUser = repo.findByUsername(auth.getName());
		return ResponseEntity.ok(response);
	}

	private String createJwtToken(Client client) {
		Instant now = Instant.now();

		JwtClaimsSet claims = JwtClaimsSet.builder().issuer(jwtIssuer) // The token issuer
				.issuedAt(now) // Token issuance date
				.expiresAt(now.plusSeconds(24 * 3600)) // Token expiration time
				.subject(client.getUsername()) // User identity
				.claim("role", client.getRole()) // Custom claim for user role
				.build();

		// JWT encoder with ImmutableSecret
		var encoder = new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey.getBytes()));

		// JWS Header and Encoding
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
		JwtEncoderParameters params = JwtEncoderParameters.from(header, claims);

		return encoder.encode(params).getTokenValue();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
		try {
			clientService.deleteById(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Client> updateArticle(@PathVariable Integer id, @RequestBody Client updatedClient) {
		try {
			Client client = clientService.updateClient(id, updatedClient);
			return ResponseEntity.ok(client);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
