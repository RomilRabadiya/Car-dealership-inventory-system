package incubyte.tdd.BackendAPI.Controller;

import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;

    @GetMapping
    public ResponseEntity<String> getVehicles() {
        return ResponseEntity.ok("Vehicle List");
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<Void> restockVehicle(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {

        return ResponseEntity.ok().build();

    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addVehicle(vehicle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable Long id,
            @RequestBody Vehicle vehicle) {

        return ResponseEntity.ok(
                service.updateVehicle(id, vehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id) {

        service.deleteVehicle(id);

        return ResponseEntity.noContent().build();
    }

    
}
