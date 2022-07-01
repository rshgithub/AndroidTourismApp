package com.example.pablo.interfaces;

import com.example.pablo.model.amenities.Amenities;
import com.example.pablo.model.bookingInfo.CartExample;
import com.example.pablo.model.churches.AllChurches;
import com.example.pablo.model.churches.TopChurches;
import com.example.pablo.model.edit.EditExample;
import com.example.pablo.model.edit_order.EditOrderDetails;
import com.example.pablo.model.hotel.Hotels;
import com.example.pablo.model.hotel.HotelsData;
import com.example.pablo.model.hotel.SearchHotel;
import com.example.pablo.model.hotels.HotelsExample;
import com.example.pablo.model.logout.LogOutExample;
import com.example.pablo.model.RegisterRequest;
import com.example.pablo.model.RegisterResponse;
import com.example.pablo.model.LoginRequest;
import com.example.pablo.model.login.ExampleLogin;
import com.example.pablo.model.RestaurantsExam;
import com.example.pablo.model.mosquedetails.MosqueDetailsExample;
import com.example.pablo.model.mosques.MosqueExample;
import com.example.pablo.model.mosques.TopMosque;
import com.example.pablo.model.notification.Notification;
import com.example.pablo.model.order_details.OrderDetailsExample;
import com.example.pablo.model.orders.OrdersExample;
import com.example.pablo.model.payment.Payment;
import com.example.pablo.model.register.Example;
import com.example.pablo.model.rooms.RoomsExample;
import com.example.pablo.model.users.UsersExample;
import com.example.pablo.model.reservations.Datum;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    //login
    @POST("login")
    Call<ExampleLogin> login(@Body LoginRequest loginRequest);

    //register
    @POST("register")
    Call<Example> register(@Header("Content-Type") String type
            , @Body RegisterRequest registerRequest);

    //logout
    @POST("logout")
    Call<LogOutExample> logOutUser(@Header("Authorization") String token);

    //get user photo
    @Multipart
    @POST("register")
    Call<RegisterResponse> UserImage(
            @Header("Accept") String accept
            , @Part MultipartBody.Part image);

//***************************************************************************


    @GET("getTopHotels")
    Call<List<HotelsData>> getPopularHotels(@Header("Authorization") String token);

    @GET("hotels")
    Call<Hotels> getHotels(@Header("Authorization") String token, @Query("limit") int limit, @Query("page") int page);

    //hotels details
    @GET("hotels/{id}")
    Call<HotelsExample> getHotelsDetails(@Path("id") Long id, @Header("Authorization") String token);

    //room
    @GET("hotel_rooms")
    Call<List<com.example.pablo.model.rooms.Data>> getRoom(@Header("Authorization") String token);

    //room
    @GET("hotel_rooms/{id}")
    Call<RoomsExample> getRoomDetails(@Path("id") Long id,@Header("Authorization") String token);

    //booking info
    @FormUrlEncoded
    @POST("addNewOrderItem")
    Call<CartExample> bookInfo(
            @Field("check_in") String check_in,
            @Field("check_out") String check_out,
            @Field("room_count") String room_count,
            @Field("room_id") String room_id,
            @Field("order_id") String order_id,
            @Header("Authorization") String token
    );


    //room_reservations
    @GET("getAuthOrderItems")
    Call<com.example.pablo.model.cart.CartExample> getCart(@Header("Authorization") String token);

    //delete item from cart
    @DELETE("orders/{id}")
    Call<Datum> deleteItem(@Path("id") Long itemId, @Header("Authorization") String token);

    //edit item from cart
    @FormUrlEncoded
    @POST("orders/{id}")
    Call<EditExample> editItem(
            @Path("id") Long roomId,
            @Field("check_in") String check_in,
            @Field("check_out") String check_out,
            @Field("room_count") String room_count,
            @Field("order_id") Long order_id,
            @Field("_method") String _method,
            @Header("Authorization") String token);

    //clear all cart
    @DELETE("deleteAuthReservations")
    Call<Datum> clearCart(@Header("Authorization") String token);

    //get hotels orders
    @GET("getAuthHotelOrders")
    Call<OrdersExample> getHotelOrders(@Header("Authorization") String token);

    //delete item from cart
    @GET("hotelOrders/{id}")
    Call<OrderDetailsExample> getHotelOrdersDetails(@Path("id") Long itemId,
                                                    @Header("Authorization") String token);

    //get edit details

    @GET("orders/{id}")
    Call<EditOrderDetails> getEditOrdersDetails(@Path("id") Long id,  @Header("Authorization") String token);

    //Payment
    @FormUrlEncoded
    @POST("buyAllOrderItems")
    Call<Payment> Payment(
            @Field("number") Long number,
            @Field("name") String name,
            @Field("exp_month") Long exp_month,
            @Field("exp_year") Long exp_year,
            @Field("cvc") Long cvc,
            @Header("Authorization") String token
             );


    @GET("searchHotelByNameDesc")
    Call<SearchHotel> search(@Header("Authorization") String token, @Query("data") String name);


    @GET("searchChurchByNameDesc")
    Call<AllChurches> ChurchesSearch(@Header("Authorization") String token, @Query("data") String name);


    @GET("searchMosqueByNameDesc")
    Call<MosqueExample> mosqueSearch(@Header("Authorization") String token, @Query("data") String name);

    @Multipart
    @POST("updateAuthAvatar")
    Call<RegisterResponse> updateUserAvatar(
            @Header("Accept") String accept
            , @Part MultipartBody.Part image
            , @Header("Authorization") String token);

    //**************************************************************************
    //mosque
    @GET("mosques")
    Call<MosqueExample> getMosques(@Header("Authorization") String token);

    //Top mosque
    @GET("getTopMosques")
    Call<List<TopMosque>> getTopMosques(@Header("Authorization") String token);

    //mosque details
    @GET("mosques/{id}")
    Call<MosqueDetailsExample> getMosqueDetails(@Path("id") int id, @Header("Authorization") String token);

    //churches
    @GET("churches")
    Call<AllChurches> getChurches(@Header("Authorization") String token);

    //Top churches
    @GET("getTopChurches")
    Call<List<TopChurches>> getTopChurches(@Header("Authorization") String token);

    //churches details
    @GET("churches/{id}")
    Call<MosqueDetailsExample> getChurchesDetails(@Path("id") int id, @Header("Authorization") String token);

    //******************************************************************************
    //get user details from
    @GET("users/{id}")
    Call<UsersExample> getUserDetails(@Path("id") Long id, @Header("Authorization") String token);

    //******************************************************************************
    //get orders test
    @GET("Orders/{id}")
    Call<List<RegisterResponse>> getOrders(@Path("userId") int id);

    //getNotification
    @GET("AllAuthNotifications")
    Call<Notification> getNotification(@Header("Authorization") String token);

    //get AmenitiesData
    @GET("hotel_advantages")
    Call<List<Amenities>> getAmenitiesData(@Header("Authorization") String token);

    //getFavourites
    @GET("favourite")
    Call<List<RestaurantsExam>> getRestaurantFavourite();



    class ApiClient {

        private static final String BASE_URL = "http://54.235.12.20/api/";

//        static Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();

        public static Service getRetrofitInstance() {

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .writeTimeout(10000, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Accept", "application/json")
                                    .addHeader("Content-Type", "multipart/form-data")
                                    .addHeader("X-Requested-With", "XMLHttpRequest")
                                    // .addHeader("Authorization", "Bearer" + Token)
                                    .addHeader("Accept-Language", "en")

                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })

                    .readTimeout(10000, TimeUnit.SECONDS).addInterceptor(interceptor).addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Accept", "application/json")
                                    .addHeader("Content-Type", "multipart/form-data")
                                    .addHeader("X-Requested-With", "XMLHttpRequest")
                                    // .addHeader("Authorization", "Bearer" + Token)
                                    .addHeader("Accept-Language", "en")

                                    .build();
                            return chain.proceed(newRequest);
                        }
                    })
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();


            return retrofit.create(Service.class);
        }
    }

}

