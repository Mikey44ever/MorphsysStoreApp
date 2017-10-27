package pos.store.morphsys.com.morphsysstoreapp.pojo.registration;

/**
 * Created by MorphsysLaptop on 25/10/2017.
 */

public class RegistrationPOJOBuilder {

    private RegistrationPOJO registration;

    public RegistrationPOJOBuilder(){
        registration = new RegistrationPOJO();
    }

    public RegistrationPOJO build(){
        return registration;
    }

    public RegistrationPOJOBuilder setFirstName(final String firstName){
        registration.setFirstName(firstName);
        return this;
    }

    public RegistrationPOJOBuilder setMiddleName(final String middleName){
        registration.setMiddleName(middleName);
        return this;
    }

    public RegistrationPOJOBuilder setLastName(final String lastName){
        registration.setLastName(lastName);
        return this;
    }

    public RegistrationPOJOBuilder setUsername(final String username){
        registration.setUserName(username);
        return this;
    }

    public RegistrationPOJOBuilder setPassword(final String password){
        registration.setPassword(password);
        return this;
    }

    public RegistrationPOJOBuilder setEmail(final String email){
        registration.setEmail(email);
        return this;
    }

    public RegistrationPOJOBuilder setMobileNumber(final String mobileNumber){
        registration.setMobileNumber(mobileNumber);
        return this;
    }

    public RegistrationPOJOBuilder setBirthDate(final String date){
        registration.setBirthDate(date);
        return this;
    }

    public RegistrationPOJOBuilder setBranch(final String branch){
        registration.setBranch(branch);
        return this;
    }
}
