package edu.pucmm.practica13.repository;

        import edu.pucmm.practica13.data.Device;
        import edu.pucmm.practica13.data.DeviceMessage;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;

        import java.sql.Timestamp;
        import java.time.LocalDate;
        import java.util.List;
public interface DeviceMessageRepository extends JpaRepository<DeviceMessage, Long> {

    List<DeviceMessage> findAll();
    List<DeviceMessage> findAllByDeviceId(Long id);

    @Query(value = "SELECT * FROM device_message d WHERE d.device_id = ?1 AND date BETWEEN ?2 AND ?3 offset(?4) limit(?5)",nativeQuery = true)
    List<DeviceMessage> findAllPaginated(Long deviceId, LocalDate start, LocalDate end, int offset, int limit);

    @Query(value = "SELECT TOP 1 * FROM device_message d WHERE d.device_id = ?1 ORDER BY date DESC",nativeQuery = true)
    List<DeviceMessage> findLastByDeviceId(Long deviceId);

    long count();
    List<DeviceMessage> findAllByDevice(Long id);
}
