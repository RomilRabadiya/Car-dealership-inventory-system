package incubyte.tdd.BackendAPI.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    @GetMapping
    public ResponseEntity<String> getVehicles() {
        return ResponseEntity.ok("Vehicle List");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id
    ) {

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<Void> restockVehicle(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {

        return ResponseEntity.ok().build();

    }
}
