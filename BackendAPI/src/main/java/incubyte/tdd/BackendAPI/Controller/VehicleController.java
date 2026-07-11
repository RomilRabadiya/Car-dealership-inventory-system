package incubyte.tdd.BackendAPI.Controller;

import incubyte.tdd.BackendAPI.Dto.Request.VehicleSearchRequest;
import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Services.VehicleService;
import incubyte.tdd.BackendAPI.Dto.Request.VehicleSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService service;


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

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {

        return ResponseEntity.ok(
                service.getAllVehicles()
        );

    }

    @GetMapping("/search")
    public ResponseEntity<List<Vehicle>> searchVehicles(

            @RequestParam String make

    ) {

        return ResponseEntity.ok(
                service.search(VehicleSearchRequest.builder().make(make).build())
        );

    }
}
