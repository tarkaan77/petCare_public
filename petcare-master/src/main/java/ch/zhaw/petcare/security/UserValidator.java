package ch.zhaw.petcare.security;

import java.util.List;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import ch.zhaw.petcare.model.Person;
import ch.zhaw.petcare.repository.PersonRepository;

class UserValidator implements OAuth2TokenValidator<Jwt> {

    PersonRepository personRepository;

    public UserValidator(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        OAuth2Error error = new OAuth2Error("invalid_token", "The required email is missing", null);

        String userEmail = jwt.getClaimAsString("email");
        List<String> userRoles = jwt.getClaimAsStringList("user_roles");
        if (userEmail != null && !userEmail.equals("")) {
            Person person = personRepository.findFirstByEmail(userEmail);
            if (person == null && (userRoles == null || userRoles.isEmpty())) {
                String username = jwt.getClaimAsString("nickname");
                // Assuming that username, address, and phoneNumber are not null
                personRepository.save(new Person(username, "defaultAddress", userEmail, "defaultPhoneNumber"));
            }
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }

}
