package incubyte.tdd.BackendAPI.Repository;

import incubyte.tdd.BackendAPI.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository
        extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    boolean existsByMakeAndModel(
            String make,
            String model
    );

}
