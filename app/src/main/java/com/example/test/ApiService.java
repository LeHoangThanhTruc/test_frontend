package com.example.test;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    // Class mô tả dữ liệu gửi lên khi Đăng ký
    class RegisterRequest {
        String full_name;
        String email;
        String password;

        public RegisterRequest(String full_name, String email, String password) {
            this.full_name = full_name;
            this.email = email;
            this.password = password;
        }
    }

    // Class mô tả dữ liệu gửi lên khi Đăng nhập
    class LoginRequest {
        String email;
        String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    // Class nhận phản hồi từ Server Backend
    class ApiResponse {
        public String status;
        public String message;
    }

    // Định nghĩa API Đăng ký
    @POST("api/auth/register")
    Call<ApiResponse> registerUser(@Body RegisterRequest request);

    // Định nghĩa API Đăng nhập
    @POST("api/auth/login")
    Call<ApiResponse> loginUser(@Body LoginRequest request);
}