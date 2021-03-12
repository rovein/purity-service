package ua.nure.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.nure.entity.user.Address;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

    List<Address> findAllByCountry(String country);

    List<Address> findAllByCountryAndCity(String country, String city);

    List<Address> findAllByCountryAndCityAndStreet(String country, String city, String address);

}
