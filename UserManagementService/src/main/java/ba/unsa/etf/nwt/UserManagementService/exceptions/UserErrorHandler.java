package ba.unsa.etf.nwt.UserManagementService.exceptions;

import java.util.HashMap;

public class UserErrorHandler {
    public enum UserErrorCode {
        NAME_BLANK, SURNAME_BLANK, DATE_IN_FUTURE, PHONE_NOT_NUMERIC, EMAIL_INVALID, PASSWORD_TOO_SHORT, ROLE_NULL,
        USER_NOT_FOUND
    }

    private final HashMap<UserErrorHandler.UserErrorCode, String> errorMap;

    public UserErrorHandler() {
        errorMap = new HashMap<>();
        this.errorMap.put(UserErrorCode.NAME_BLANK, "Ime ne smije biti prazno!");
        this.errorMap.put(UserErrorCode.SURNAME_BLANK, "Prezime ne smije biti prazno!");
        this.errorMap.put(UserErrorCode.DATE_IN_FUTURE, "Datum rođenja ne smije biti u budućnosti!");
        this.errorMap.put(UserErrorCode.PHONE_NOT_NUMERIC, "Broj telefona smije imati samo brojčane vrijednosti!");
        this.errorMap.put(UserErrorCode.EMAIL_INVALID, "Mail nije ispravan!");
        this.errorMap.put(UserErrorCode.PASSWORD_TOO_SHORT, "Password mora imati barem 8 znakova!");
        this.errorMap.put(UserErrorCode.ROLE_NULL, "Korisnik mora imati rolu!");
        this.errorMap.put(UserErrorCode.USER_NOT_FOUND, "Traženi korisnik nije pronađen!");
    }

    public String getError(UserErrorHandler.UserErrorCode userErrorCode) {
        return errorMap.getOrDefault(userErrorCode, "Greška nepoznata!");
    }
}
