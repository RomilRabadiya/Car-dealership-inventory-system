package incubyte.tdd.BackendAPI.Services.impl;

import incubyte.tdd.BackendAPI.Exception.DuplicateVehicleException;
import incubyte.tdd.BackendAPI.Exception.InvalidQuantityException;
import incubyte.tdd.BackendAPI.Exception.OutOfStockException;
import incubyte.tdd.BackendAPI.Exception.VehicleNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import incubyte.tdd.BackendAPI.Entity.Vehicle;
import incubyte.tdd.BackendAPI.Repository.VehicleRepository;
import incubyte.tdd.BackendAPI.Services.VehicleService;

import java.math.BigDecimal;
import java.util.List;
import incubyte.tdd.BackendAPI.Dto.Request.VehicleSearchRequest;
import incubyte.tdd.BackendAPI.Specification.VehicleSpecification;

import incubyte.tdd.BackendAPI.common.constants.ErrorMessages;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

        return repository.findAll(
                VehicleSpecification.search(
                        request.getMake(),
                        request.getModel(),
                        request.getCategory(),
                        request.getMinPrice(),
                        request.getMaxPrice()
                )
        );

    }

    private Vehicle getVehicleById(Long id)
    {
        Vehicle existingVehicle =
                repository.findById(id)

                        .orElseThrow(
                                () ->
                                        new VehicleNotFoundException(
                                                ErrorMessages.VEHICLE_NOT_FOUND + id
                                        )
                        );
        return existingVehicle;
    }

    @Override
    public Vehicle updateVehicle(
            Long id,
            Vehicle updatedVehicle
    ) {

        Vehicle existingVehicle = getVehicleById(id);

        existingVehicle.updateDetails(updatedVehicle);

        return repository.save(existingVehicle);

    }

    @Override
    public void deleteVehicle(Long id) {

        Vehicle vehicle = getVehicleById(id);

        repository.delete(vehicle);

    }

}