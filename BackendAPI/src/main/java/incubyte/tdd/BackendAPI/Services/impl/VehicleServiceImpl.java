package incubyte.tdd.BackendAPI.Services.impl;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Repository.VehicleRepository;
import incubyte.tdd.BackendAPI.Services.VehicleService;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl
        implements VehicleService {

    private final VehicleRepository repository;

    @Override
    public Vehicle addVehicle(Vehicle vehicle) {

        return repository.save(vehicle);

    }

}