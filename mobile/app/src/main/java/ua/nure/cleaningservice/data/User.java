package ua.nure.cleaningservice.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("userRole")
    @Expose
    private String userRole;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("address")
    @Expose
    private Address address;

    private static User instance;

    private User() {}

    public User(String id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return instance;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return instance;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return instance;
    }

    public String getCompanyId() {
        return companyId;
    }

    public User setCompanyId(String companyId) {
        this.companyId = companyId;
        return instance;
    }

    public String getToken() {
        return token;
    }

    public User setToken(String token) {
        this.token = token;
        return instance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return instance;
    }

    public String getUserRole() {
        return userRole;
    }

    public User setUserRole(String userRole) {
        this.userRole = userRole;
        return instance;
    }


    public Address getAddress() {
        return address;
    }

    public User setAddress(Address address) {
        this.address = address;
        return instance;
    }
}
