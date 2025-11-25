package dsa.upc.edu.listapp.github;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EETACBROSSystemService {
    @POST("eetacbros/user/register")
    Call<User> registerUser(@Body RegisterRequest request);
    
    @POST("eetacbros/user/login")
    Call<User> loginUser(@Body LoginRequest request);
    @POST("eetacbros/shop/buy")
    Call<BuyResponse> buyItems(@Body BuyRequest request);

    @GET("eetacbros/shop/items")
    Call<List<Item>> getItems();

}

