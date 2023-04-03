package ch.uzh.ifi.hase.soprafs23.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs23.config.JwtProfileDetailsService;
import ch.uzh.ifi.hase.soprafs23.config.JwtRequest;
import ch.uzh.ifi.hase.soprafs23.config.JwtResponse;
import ch.uzh.ifi.hase.soprafs23.config.JwtTokenUtil;
import ch.uzh.ifi.hase.soprafs23.entity.Profile;
import ch.uzh.ifi.hase.soprafs23.rest.dto.RegisterProfileDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.ProfileService;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
@CrossOrigin
public class AuthenticationController {

	private final ProfileService profileService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	AuthenticationController(ProfileService profileService) {
		this.profileService = profileService;
	}

	@PostMapping("/registration")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity<?> createUser(@RequestBody RegisterProfileDTO registerProfileDTO) {
		Profile profileInput = DTOMapper.INSTANCE.convertRegisterProfileDTOtoEntity(registerProfileDTO);
		profileService.createUser(profileInput);

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(null);
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
		final Profile profile = profileService
				.getProfileBySigninCredentials(authenticationRequest);

		final String token = jwtTokenUtil.generateToken(profile);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
