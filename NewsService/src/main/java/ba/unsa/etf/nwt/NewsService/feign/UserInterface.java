package ba.unsa.etf.nwt.NewsService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("UserManagementService")
public interface UserInterface {
    @GetMapping(value="/users/id/{ID}")
    public ResponseEntity<?> getUserByID(@PathVariable Long ID);

    @GetMapping(value="/users/uid/{UID}")
    public ResponseEntity<?> getUserByUID(@PathVariable String UID);
}