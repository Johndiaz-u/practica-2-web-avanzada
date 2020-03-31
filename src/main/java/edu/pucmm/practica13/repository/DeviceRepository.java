package edu.pucmm.practica13.repository;

        import edu.pucmm.practica13.data.Device;
        import org.springframework.data.jpa.repository.JpaRepository;

        import java.util.List;
        import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findAll();

    Optional<Device> getByName(String name);
}
