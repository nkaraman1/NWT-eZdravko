package ba.unsa.etf.nwt.UserManagementService.controllers;

import ba.unsa.etf.nwt.UserManagementService.model.Role;
import ba.unsa.etf.nwt.UserManagementService.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value="/roles")
    public List<Role> getRoles() {
        return roleService.getAllRoles();
    }
    @GetMapping(value = "/roles/id/{id}")
    public ResponseEntity<?> getRoleByID(@PathVariable("id") Long id) {
        return roleService.getRoleByID(id);
    }
    @GetMapping(value = "/roles/name/{nazivRole}")
    public ResponseEntity<?> getRoleByName(@PathVariable String nazivRole) {
        return roleService.getRoleByName(nazivRole);
    }

    @PostMapping(value = "/roles/create/{nazivRole}/flag-kod/{potrebanKod}")
    public ResponseEntity<?> createRole(@PathVariable String nazivRole, @PathVariable boolean potrebanKod) {
        return roleService.createRole(nazivRole, potrebanKod);
    }

    @DeleteMapping(value = "/roles/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @PatchMapping(value = "/roles/update-code/{id}")
    public ResponseEntity<?> updateRoleCode(@PathVariable Long id) {
        return roleService.changeRoleCode(id);
    }
}
