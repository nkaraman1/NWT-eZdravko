package ba.unsa.etf.nwt.SurveyService.feign;

import ba.unsa.etf.nwt.UserManagementService.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("UserManagementService")
public interface UserInterface {
    @GetMapping(value="/users/id/{ID}")
    public ResponseEntity<?> getUserByID(@PathVariable Long ID);
}
