package ua.nure.cleaningservice.network;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ua.nure.cleaningservice.data.Company;
import ua.nure.cleaningservice.data.Contract;
import ua.nure.cleaningservice.data.Placement;
import ua.nure.cleaningservice.data.Service;

public interface JSONPlaceHolderApi {

    @POST("/auth/login")
    Call<Company> login(@Body Company company);

    @GET("/cleaning-providers/{email}")
    Call<Company> getCleaningProviderData(@Header("Authorization") String token,
            @Path("email") String email);

    @GET("/placement-owners/{email}")
    Call<Company> getPlacementOwnerData(@Header("Authorization") String token,
            @Path("email") String email);

    @GET("/cleaning-providers/{email}/services")
    Call<ArrayList<Service>> getServiceData(
            @Header("Authorization") String token, @Path("email") String email);

    @GET("/placement-owners/{email}/placements")
    Call<ArrayList<Placement>> getPlacementData(
            @Header("Authorization") String token, @Path("email") String email);

    @GET("/contracts/placement-owner/{email}")
    Call<ArrayList<Contract>> getPlacementOwnerContracts(
            @Header("Authorization") String token, @Path("email") String email);

    @GET("/contracts/cleaning-provider/{email}")
    Call<ArrayList<Contract>> getCleaningProviderContracts(
            @Header("Authorization") String token, @Path("email") String email);

    @PUT("/cleaning-providers/{email}/services")
    Call<Service> updateService(@Header("Authorization") String token,
            @Path("email") String email, @Body Service service);

    @PUT("/cleaning-providers")
    Call<Company> updateCleaningProviderProfile(@Header("Authorization") String token,
            @Body Company cleaningProvider);

    @PUT("/placement-owners")
    Call<Company> updatePlacementOwnerProfile(@Header("Authorization") String token,
            @Body Company placementOwner);

    @GET("/cleaning-providers/services/{id}")
    Call<Service> getService(@Header("Authorization") String token,
            @Path("id") Integer id);

    @DELETE("/cleaning-providers/services/{id}")
    Call<Service> deleteService(@Header("Authorization") String token,
            @Path("id") Integer id);

    @PUT("/placement-owners/{email}/placements")
    Call<Placement> updatePlacement(@Header("Authorization") String token,
            @Path("email") String email, @Body Placement placement);

    @GET("/placement-owners/placements/{id}")
    Call<Placement> getPlacement(@Header("Authorization") String token,
            @Path("id") Integer id);

    @DELETE("/placement-owners/placements/{id}")
    Call<Placement> deletePlacement(@Header("Authorization") String token,
            @Path("id") Integer id);

    @POST("/placement-owners/{email}/placements")
    Call<Placement> addPlacement(@Header("Authorization") String token,
            @Path("email") String email, @Body Placement placement);

    @POST("/cleaning-providers/{email}/services")
    Call<Service> addService(@Header("Authorization") String token,
            @Path("email") String email, @Body Service service);

    @GET("/cleaning-providers")
    Call<ArrayList<Company>> getCleaningProviders(@Header("Authorization") String token);

    @POST("/auth/register/cleaning-provider")
    Call<Company> cleaningProviderSignUp(@Body Company cleaningProvider);

    @POST("/auth/register/placement-owner")
    Call<Company> placementOwnerSignUp(@Body Company placementOwner);

    @POST("/contracts")
    Call<Contract> signContract(@Header("Authorization") String token,
            @Body Contract contract);

}
