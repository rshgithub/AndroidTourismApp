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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
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

        if (!isOnLine()) {
            if (isConnected) {
                Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
            } else {

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


//-----------------------------start cam--------------------------------------------------
        changeUserImage();
//*-----------------------------end cam--------------------------------------------------


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

                    binding.name.setText(response.body().getData().getName());
                    binding.address.setText(response.body().getData().getAddress());
                    Log.e("image",response.body().getData().getUserAvatar());
                    Glide.with(getActivity()).load(response.body().getData().getUserAvatar()).circleCrop().into(binding.photo);
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

    //------------------------*No Internet Connection*----------------------------
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

    File file;
    Bitmap bitmap;

    // change user image

    private void changeUserImage() {
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean pick = true;
                if (pick == true) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else
                        new ImagePicker.Builder(getActivity())
                                .crop(83, 100)                    //Crop image(Optional), Check Customization for more option
                                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(233, 280)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start();

                } else {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else new ImagePicker.Builder(getActivity())
                            .crop(83, 100)                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(233, 280)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();

                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new ImagePicker.Builder(this)
                .crop(83, 100)                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(233, 280)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
    }

    private boolean checkStoragePermission() {
        boolean res2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return res2;
    }

    private boolean checkCameraPermission() {
        boolean res1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        boolean res2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return res1 && res2;
    }

    private void uploadImage() {

        binding.camera.setImageBitmap(bitmap);
        updateUserImage();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                file = FileUtil.from(getActivity(), uri);
                uploadImage();
            } catch (IOException e) {
                Log.w("TAG", "onActivityResult: CROP_IMAGE_ACTIVITY_REQUEST_CODE => ", e);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(getActivity(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
        }


        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            Uri uri = data.getData();
            //this is written from a fragment.
            CropImage.activity(uri).setAspectRatio(83, 100)
                    .setRequestedSize(233, 280)
                    .setGuidelines(CropImageView.Guidelines.ON).start(getActivity());
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    uploadImage();
                } catch (IOException e) {
                    Log.w("TAG", "onActivityResult: CROP_IMAGE_ACTIVITY_REQUEST_CODE => ", e);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.w("TAG", "onActivityResult: ", error);
            }
        }
    }

    private void updateUserImage() {
        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");

        MultipartBody.Part body = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            Call<RegisterResponse> call = service.updateUserImage("application/json", body, token);
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
        }else
            Toast.makeText(getActivity(), "some thing went wrong!", Toast.LENGTH_SHORT).show();
    }

}