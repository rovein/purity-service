package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.user.SmartDevice;

@Repository
public interface SmartDeviceRepository extends CrudRepository<SmartDevice, Long> {

}
