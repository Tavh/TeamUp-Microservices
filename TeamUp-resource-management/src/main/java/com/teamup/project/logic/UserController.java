package com.teamup.project.logic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.teamup.project.entities.UserEntity;
import com.teamup.project.enums.ErrorType;
import com.teamup.project.exceptions.ApplicationException;
import com.teamup.project.repository.UserRepos;
import com.teamup.project.utils.InputValidationUtils;

@Controller
public class UserController {
	
	@Autowired
	private UserRepos userRepos;
	
	// ------------------------------------Creates a user-------------------------------------

	public long createUser (UserEntity user) throws ApplicationException {

		if (!InputValidationUtils.isNicknameValid(user.getNickname())) {
			throw new ApplicationException (ErrorType.WORNG_INPUT, "The nickname you inserted is invalid");
		}

		if (!InputValidationUtils.isEmailValid(user.getEmail())) {
			throw new ApplicationException (ErrorType.WORNG_INPUT, "The email you inserted is invalid");
		}

		if (!InputValidationUtils.isPasswordValid(user.getPassword())) {
			throw new ApplicationException (ErrorType.WORNG_INPUT, "The password you inserted is invalid");
		}

		if (userRepos.getUser(user.getEmail()) != null) {
			throw new ApplicationException (ErrorType.EMAIL_ALREADY_EXISTS, ErrorType.EMAIL_ALREADY_EXISTS.getInternalMessage());
		}

		Integer passwordHash = user.getPassword().hashCode();

		user.setPassword(passwordHash.toString());

		long autoGeneratedId = userRepos.createUser(user);

		return autoGeneratedId;
	}

	// ---------------------------------- Remove -------------------------------------

	public void removeUser(long id) throws ApplicationException {

		UserEntity user = userRepos.getUser(id);
		
		if (user == null) {
			throw new ApplicationException (ErrorType.DATA_NOT_FOUND, "The user you're trying to remove does not exist");
		}
		
		userRepos.removeUser(id);
	}

	// ------------------------------------Getters-------------------------------------

	public UserEntity getUser(long id) throws ApplicationException {

		return userRepos.getUser(id);
	}

	public UserEntity getUser(String email) throws ApplicationException {

		return userRepos.getUser(email);
	}

	// ------------------------------------Authenticate user-------------------------------------

	public boolean isUserLegitimate(String email, String password) throws ApplicationException {

		UserEntity user = userRepos.getUser(email);

		if (user == null) {
			throw new ApplicationException (ErrorType.INVALID_USER, "The account you're trying to authenticate doesn't exist");
		}

		Integer hashedPasswordInteger = password.hashCode();
		String hashedPasswordString = hashedPasswordInteger.toString();

		if (hashedPasswordString.equals(user.getPassword())) {
			return true;
		}

		throw new ApplicationException (ErrorType.WORNG_INPUT, "The password you inserted doesn't match the account's password");
	}


}
