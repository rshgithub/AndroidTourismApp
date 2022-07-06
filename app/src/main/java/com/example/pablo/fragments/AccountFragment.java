package com.example.pablo.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pablo.R;
import com.example.pablo.activity.ActivityNotification;
import com.example.pablo.FileUtil;
import com.example.pablo.activity.ActivityAbout;
import com.example.pablo.activity.ActivityPrivacy;
import com.example.pablo.activity.ActivitySupport;
import com.example.pablo.activity.NoInternetConnection;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.FragmentAccountBinding;
import com.example.pablo.model.RegisterResponse;
import com.example.pablo.model.logout.LogOutExample;
import com.example.pablo.model.users.UsersExample;
import com.victor.loading.newton.NewtonCradleLoading;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Signup.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    Service service;
    NewtonCradleLoading newtonCradleLoading;
    boolean isConnected = false;
    ConnectivityManager connectivityManager;
    File file;
    Bitmap bitmap;
    int SELECT_PHOTO=1;
    Uri uri;
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

        startShimmer();
        getRetrofitInstance();
        ifIsOnLine();
        about();
        logOutButton();
        permission();
        swipe();
        getAccountData();
        changeUserImage();
        notification();
        support();
        privacy();
        return view;

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
                    stopShimmer();
                    binding.name.setText(response.body().getData().getName());
                    binding.address.setText(response.body().getData().getAddress());
                    Log.e("image",response.body().getData().getUserAvatar());
                    Glide.with(getActivity()).load(response.body().getData().getUserAvatar()).error(R.drawable.ic_baseline_person_24).into(binding.photo);
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UsersExample> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), t.getMessage() + "", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(getActivity(), response.body().getMessage() + "", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(), Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_LONG).show();


                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                    Toast.makeText(getActivity(), response.message() + "", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LogOutExample> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), t.getMessage() + "", Toast.LENGTH_LONG).show();


            }

        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerNetworkCallback() {


        try {

            connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(@NonNull Network network) {
                    isConnected = true;
                }

                @Override
                public void onLost(@NonNull Network network) {
                    isConnected = false;
                }
            });


        } catch (Exception e) {

            isConnected = false;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        registerNetworkCallback();
    }

    public boolean isOnLine() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    private void changeUserImage() {
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                file = FileUtil.from(getActivity(), uri);
                binding.photo.setImageBitmap(bitmap);
                updateUserImage();
            } catch (IOException e) {
                Log.w("tag", e);
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "RESULT_CANCELED", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "cancel image"+ resultCode, Toast.LENGTH_SHORT).show();
        }


        if (requestCode == SELECT_PHOTO&&requestCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Glide.with(getActivity()).asBitmap().load(bitmap).into(binding.photo);
//                    .apply(new RequestOptions().transform(new RoundedCorners(100))
//                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE))

            binding.photo.setImageBitmap(bitmap);
        }

    }

    private void updateUserImage(){
        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");

        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            Call<RegisterResponse> call = service.updateUserAvatar("application/json", body, token);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    Log.d("response code", response.code() + "");
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT).show();
                        Log.d("success", response.message());
                    }else {
                        Toast.makeText(getActivity(), "else", Toast.LENGTH_SHORT).show();

                    }
                }
                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.d("error", t.getMessage() + "");
                }
            });
        }
    }

    private void permission(){
        ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    private void swipe(){
        SwipeRefreshLayout swipeRefreshLayout = binding.swipe;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                service = Service.ApiClient.getRetrofitInstance();
                startShimmer();
                getAccountData();
            }, 1000);
        });

    }

    private void logOutButton(){
        binding.Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLogout();
            }
        });

        binding.textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLogout();
            }
        });
    }

    private void ifIsOnLine(){
        if (!isOnLine()) {
            if (isConnected) {
                Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
            } else {

                Intent i = new Intent(getActivity(), NoInternetConnection.class);
                startActivity(i);
                getActivity().finish();
            }
        }
    }

    private void about(){

        binding.About.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityAbout.class);
                startActivity(intent);
            }
        });

        binding.textView50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityAbout.class);
                startActivity(intent);
            }
        });
    }

    private void notification(){

        binding.Notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNotification.class);
                startActivity(intent);
            }
        });

        binding.textView46.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityNotification.class);
                startActivity(intent);
            }
        });

    }

    private void support(){

        binding.Support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySupport.class);
                startActivity(intent);
            }
        });

        binding.textView47.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivitySupport.class);
                startActivity(intent);
            }
        });

    }


    private void privacy(){

        binding.privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityPrivacy.class);
                startActivity(intent);
            }
        });

        binding.Privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ActivityPrivacy.class);
                startActivity(intent);
            }
        });

    }

    private void getRetrofitInstance(){
        service = Service.ApiClient.getRetrofitInstance();

    }


    private void startShimmer() {

        binding.shimmerLayout.startShimmer();
    //    binding.shimmerLayout.setVisibility(View.GONE);
    }

    private void stopShimmer() {
        binding.shimmerLayout.stopShimmer();
        binding.shimmerLayout.setVisibility(View.GONE);
        binding.container.setVisibility(View.VISIBLE);
    }
}