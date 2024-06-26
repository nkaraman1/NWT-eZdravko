package ba.unsa.etf.nwt.NewsService.feign;

import ba.unsa.etf.nwt.NewsService.DTO.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient("NewsService")
public interface NotificationInterface {
    @PostMapping(value="/notifications")
    public ResponseEntity<?> createNotification(@RequestBody NotificationDTO notificationDTO);
}