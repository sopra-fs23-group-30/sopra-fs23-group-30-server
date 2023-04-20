package ch.uzh.ifi.hase.soprafs23.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.config.JwtRequest;
import ch.uzh.ifi.hase.soprafs23.config.JwtResponse;
import ch.uzh.ifi.hase.soprafs23.config.JwtTokenUtil;
import ch.uzh.ifi.hase.soprafs23.entity.ProfileEntity;
import ch.uzh.ifi.hase.soprafs23.rest.dto.profile.RegisterPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

@RestController
@CrossOrigin
public class AuthenticationController {

	private final ProfileService profileService;

	private AuthenticationManager authenticationManager;

	private JwtTokenUtil jwtTokenUtil;

	AuthenticationController(ProfileService profileService, AuthenticationManager authenticationManager,
			JwtTokenUtil jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.profileService = profileService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PostMapping("/registration")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<Object> createUser(@RequestBody RegisterPostDTO registerProfileDTO) {
		ProfileEntity profileInput = DTOMapper.INSTANCE.convertRegisterProfileDTOtoEntity(registerProfileDTO);
		profileService.createUser(profileInput);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(null);
	}

	@PostMapping("/login")
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		final ProfileEntity profile = profileService
				.getProfileBySigninCredentials(authenticationRequest);

		final String token = jwtTokenUtil.generateToken(profile);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws DisabledException, BadCredentialsException {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("INVALID_CREDENTIALS", e);
		}
	}
}
