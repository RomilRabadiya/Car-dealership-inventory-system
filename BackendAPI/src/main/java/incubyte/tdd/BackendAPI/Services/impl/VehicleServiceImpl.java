package incubyte.tdd.BackendAPI.Services.impl;

import incubyte.tdd.BackendAPI.Exception.DuplicateVehicleException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Repository.VehicleRepository;
import incubyte.tdd.BackendAPI.Services.VehicleService;

import java.math.BigDecimal;
import java.util.List;
import incubyte.tdd.BackendAPI.Dto.Request.VehicleSearchRequest;

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
    public List<Vehicle> search(VehicleSearchRequest request) {

        return repository.searchVehicles(
                request.getMake(),
                request.getModel(),
                request.getCategory(),
                request.getMinPrice(),
                request.getMaxPrice()
        );

    }

}