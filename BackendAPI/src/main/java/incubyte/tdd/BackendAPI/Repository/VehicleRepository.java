package incubyte.tdd.BackendAPI.Repository;

import incubyte.tdd.BackendAPI.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository
        extends JpaRepository<Vehicle, Long> {

    boolean existsByMakeAndModel(
            String make,
            String model
    );

    List<Vehicle> findByMakeIgnoreCase(String make);

    List<Vehicle> findByModelIgnoreCase(String model);

}
