package incubyte.tdd.BackendAPI.Controller;

import incubyte.tdd.BackendAPI.Dto.Request.VehicleSearchRequest;
import jakarta.validation.Valid;
import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Services.VehicleService;
import incubyte.tdd.BackendAPI.Services.InventoryService;
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
    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@Valid @RequestBody Vehicle vehicle) {

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
            @ModelAttribute VehicleSearchRequest request
    ) {

        return ResponseEntity.ok(
                service.search(request)
        );

    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<Vehicle> purchaseVehicle(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                inventoryService.purchaseVehicle(id)
        );

    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<Vehicle> restockVehicle(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {

        return ResponseEntity.ok(
                inventoryService.restockVehicle(id, quantity)
        );

    }
}
