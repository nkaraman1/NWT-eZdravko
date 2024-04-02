package ba.unsa.etf.nwt.UserManagementService.exceptions;

import java.util.HashMap;

public class RoleErrorHandler {
    public enum RoleErrorCode {
        ID_NOT_FOUND, ROLENAME_NOT_FOUND, ROLENAME_REQUIRED, ROLE_ALREADY_EXISTS, USERS_EXIST_WITH_ROLE, WRONG_CODE
    }

    private final HashMap<RoleErrorCode, String> errorMap;

    public RoleErrorHandler() {
        errorMap = new HashMap<>();
        this.errorMap.put(RoleErrorCode.ID_NOT_FOUND, "Ne postoji rola sa tim ID-om!");
        this.errorMap.put(RoleErrorCode.ROLENAME_NOT_FOUND, "Ne postoji rola sa tim nazivom!");
        this.errorMap.put(RoleErrorCode.ROLENAME_REQUIRED, "Naziv role je obavezan");
        this.errorMap.put(RoleErrorCode.ROLE_ALREADY_EXISTS, "Postoji već rola sa tim nazivom!");
        this.errorMap.put(RoleErrorCode.USERS_EXIST_WITH_ROLE, "Postoje korisnici sa datom rolom!");
        this.errorMap.put(RoleErrorCode.WRONG_CODE, "Neispravan aktivacijski kod za rolu!");
    }

    public String getError(RoleErrorCode roleErrorCode) {
        return errorMap.getOrDefault(roleErrorCode, "Greška nepoznata!");
    }
}
