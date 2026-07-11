package incubyte.tdd.BackendAPI.Services.impl;

import incubyte.tdd.BackendAPI.Exception.DuplicateVehicleException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Repository.VehicleRepository;
import incubyte.tdd.BackendAPI.Services.VehicleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl
        implements VehicleService {

    private final VehicleRepository repository;

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {

        if (repository.existsByMakeAndModel(
                vehicle.getMake(),
                vehicle.getModel()
        )) {

            throw new DuplicateVehicleException(
                    "Vehicle already exists."
            );

        }

        return repository.save(vehicle);

    }

    @Override
    public List<Vehicle> getAllVehicles() {

        return repository.findAll();

    }

    @Override
    public List<Vehicle> searchByMake(String make) {

        return repository.findByMakeIgnoreCase(make);

    }

}