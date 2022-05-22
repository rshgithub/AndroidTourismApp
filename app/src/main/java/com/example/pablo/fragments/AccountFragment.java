package com.example.pablo.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pablo.FileUtil;
import com.example.pablo.activity.Login;
import com.example.pablo.interfaces.Service;
import com.example.pablo.databinding.FragmentAccountBinding;
import com.example.pablo.model.RegisterResponse;
import com.example.pablo.model.logout.LogOutExample;
import com.example.pablo.model.users.UsersExample;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pablo.activity.Login.PREF_NAME;
import static com.example.pablo.activity.Login.parseError;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    FragmentAccountBinding binding;
    Service service;
    int AccountId;


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
                                getUserImage(file);
                            }
                        }
                    });


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        service = Service.ApiClient.getRetrofitInstance();
        getAccountData();
        getLogout();

        //Permissions
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//-----------------------------start cam--------------------------------------------------
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ActivityResultLauncher<Intent> someActivityResultLauncher =
//                        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                                new ActivityResultCallback<ActivityResult>() {
//                                    @Override
//                                    public void onActivityResult(ActivityResult result) {
//                                        if (result.getResultCode() == Activity.RESULT_OK) { // There are no request codes
//                                            Intent data = result.getData();
//                                            Log.e("data", data.getDataString() + "");
//                                            File file = null;
//                                            try {
//                                                file = FileUtil.from(getActivity(), data.getData());
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                            getUserImage(file);
//                                        }
//                                    }
//                                });

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

        service.getUserDetails(AccountId, "Bearer " + token).enqueue(new Callback<UsersExample>() {
            @Override
            public void onResponse(Call<UsersExample> call, Response<UsersExample> response) {
                Log.e("response code", response.code() + "");

                if (response.body() != null) {
                    binding.name.setText(response.body().getData().getName());
                    binding.address.setText(response.body().getData().getAddress());
                    Glide.with(getActivity()).load(response.body().getData().getUserAvatar()).circleCrop().into(binding.photo);
                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<UsersExample> call, Throwable t) {
                t.printStackTrace();
            }

        });


    }

    private void getLogout() {

        Login.SP = getActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = Login.SP.getString(Login.TokenKey, "");//"No name defined" is the default value.

        service.logOutUser("Bearer " + token).enqueue(new Callback<LogOutExample>() {
            @Override
            public void onResponse(Call<LogOutExample> call, Response<LogOutExample> response) {
                Log.e("response code", response.code() + "");

                if (response.body() != null) {

                    binding.Out.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                            Toast.makeText(getActivity(), "Logged out Successfully", Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    String errorMessage = parseError(response);
                    Log.e("errorMessage", errorMessage + "");
                }
            }

            @Override
            public void onFailure(Call<LogOutExample> call, Throwable t) {
                t.printStackTrace();
            }

        });


    }
}