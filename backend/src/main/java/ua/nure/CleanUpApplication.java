package ua.nure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CleanUpApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(CleanUpApplication.class, args);

//        ContractRepository contractRepository = applicationContext.getBean(ContractRepository.class);
//
//        Set<ContractInfoDto> allContractsByCustomerCompany = contractRepository.getAllContractsByCleaningCompany("cleaning_company@gmail.com");
//
//        for (ContractInfoDto contractInfoDto : allContractsByCustomerCompany) {
//            System.out.println(contractInfoDto.getId());
//        }
//
//        System.out.println(allContractsByCustomerCompany);


//        AdminRepository adminRepository = applicationContext.getBean(AdminRepository.class);
//        BCryptPasswordEncoder passwordEncoder = applicationContext.getBean(BCryptPasswordEncoder.class);
//
//        Admin admin = new Admin();
//
//        admin.setId(1L);
//        admin.setEmail("admin@gmail.com");
//        admin.setPassword(passwordEncoder.encode("adminadmin"));
//        admin.setFirstName("Rick");
//        admin.setLastName("Sanchez");
//        admin.setRole(new Role(1L, UserRole.ADMIN));
//
//        adminRepository.save(admin);
    }


}
