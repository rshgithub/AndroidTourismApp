package com.example.pablo.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pablo.activity.ActivityNotification;
import com.example.pablo.FileUtil;
import com.example.pablo.activity.ActivityAbout;
import com.example.pablo.activity.NoInternetConnection;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.FragmentAccountBinding;
import com.example.pablo.model.RegisterResponse;
import com.example.pablo.model.logout.LogOutExample;
import com.example.pablo.model.users.UsersExample;
import com.google.gson.Gson;
import com.victor.loading.newton.NewtonCradleLoading;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    Service service;
    NewtonCradleLoading newtonCradleLoading;
    boolean isConnected = false;
    ConnectivityManager connectivityManager;

    ActivityResultLauncher<Intent> someActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) { // There are no request codes
                                Intent data = result.getData();
                                Log.e("data", data.getDataString() + "");
                                File file = null;
                                try {
                                    file = FileUtil.from(getActivity(), data.getData());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //getUserImage(file);
                            }
                        }
                    });


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityAbout.class);
                startActivity(intent);
            }
        });

        binding.Notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNotification.class);
                startActivity(intent);
            }
        });

        binding.Support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityAbout.class);
                startActivity(intent);
            }
        });

        if (!isOnLine()){
            if (isConnected){
                Toast.makeText(getActivity(),"Connected",Toast.LENGTH_SHORT).show();
            }else{

                Intent i = new Intent(getActivity(), NoInternetConnection.class);
                startActivity(i);
                getActivity().finish();
            }
        }

        service = Service.ApiClient.getRetrofitInstance();
        getAccountData();
        binding.Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLogout();
            }
        });


        //   Permissions
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//-----------------------------start cam--------------------------------------------------
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityResultLauncher<Intent> someActivityResultLauncher =
                        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                                new ActivityResultCallback<ActivityResult>() {
                                    @Override
                                    public void onActivityResult(ActivityResult result) {
                                        if (result.getResultCode() == Activity.RESULT_OK) { // There are no request codes
                                            Intent data = result.getData();
                                            Log.e("data", data.getDataString() + "");
                                            File file = null;
                                            try {
                                                file = FileUtil.from(getActivity(), data.getData());
                                               // changeUserImage();

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            getUserImage(file);
                                        }
                                    }
                                });

                binding.photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        someActivityResultLauncher.launch(intent);
                    }
                });
            }
        });
//*-----------------------------end cam--------------------------------------------------


        return view;
    }

    private void getUserImage(File resourceFile) {
        MultipartBody.Part body = null;
        if (resourceFile != null) {
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"),
                            resourceFile);
            body = MultipartBody.Part.createFormData(
                    // key in postman
                    "file", resourceFile.getName(),
                    requestFile);
        }


        Call<RegisterResponse> call = service.UserImage(
                "application/json"
                , body
        );
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse>
                                           call, Response<RegisterResponse> response) {
                Log.d("response code", response.code() +
                        "");
                if (response.isSuccessful() ||
                        response.code() == 200) {
                    Log.d("response_inside",
                            response.code() + "");
                    Log.d("Success", new
                            Gson().toJson(response.body()));
                }else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.message()+"", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<RegisterResponse>
                                          call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
            }
        });
    }

    private void getAccountData() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.
        Long AccountId = Login.SP.getLong(Login.USERKey, -1);

        service.getUserDetails(AccountId, token).enqueue(new Callback<UsersExample>() {
            @Override
            public void onResponse(Call<UsersExample> call, Response<UsersExample> response) {
                Log.e("response code", response.code() + "");

                if (response.isSuccessful()) {

                    binding.name.setText(response.body().getData().getName());
                    binding.address.setText(response.body().getData().getAddress());
                    Glide.with(getActivity()).load(response.body().getData().getUserAvatar()).circleCrop().into(binding.photo);
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.message()+"", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UsersExample> call, Throwable t) {
                t.printStackTrace();
              Toast.makeText(getContext(), t.getMessage()+"", Toast.LENGTH_LONG).show();

            }

        });


    }

    private void getLogout() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.logOutUser(token).enqueue(new Callback<LogOutExample>() {
            @Override
            public void onResponse(Call<LogOutExample> call, Response<LogOutExample> response) {
                Log.e("response code", response.code() + "");

                if (response.isSuccessful()) {
                   Toast.makeText(getActivity(), response.body().getMessage()+"", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();


                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.message()+"", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LogOutExample> call, Throwable t) {
                t.printStackTrace();
               Toast.makeText(getActivity(), t.getMessage()+"", Toast.LENGTH_LONG).show();


            }

        });


    }

    //------------------------*No Internet Connection*----------------------------
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkCallback(){


        try {

            connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){

                @Override
                public void onAvailable(@NonNull Network network) {
                    isConnected = true;
                }

                @Override
                public void onLost(@NonNull Network network) {
                    isConnected = false;
                }
            });




        }catch (Exception e){

            isConnected = false;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        registerNetworkCallback();
    }

    public boolean isOnLine(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isAvailable() || !networkInfo.isConnected()){
            return false;
        }
        return true;
    }
    File file;


    private void updateUserImage() {
        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");

        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }
        Call<RegisterResponse> call = service.updateUserImage("application/json", body,token);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d("response code", response.code() + "");
                if (response.isSuccessful()) {
                    Log.d("success", "success");

                } else {
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("onFailure", t.getMessage() + "");
                call.cancel();
            }
        });
    }

}