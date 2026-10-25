package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ View từ XML
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // Sự kiện bấm nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    //đã có api
                    performLogin(email, password);
                }
            }
        });

        // Sự kiện bấm chuyển sang màn hình Đăng ký
        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    // Hàm gọi API Đăng nhập
    private void performLogin(String email, String password) {
        // 1. Tạo instance ApiService từ ApiClient
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // 2. Tạo đối tượng request dữ liệu
        ApiService.LoginRequest request = new ApiService.LoginRequest(email, password);

        // 3. Thực hiện gửi request bất đồng bộ (enqueue)
        apiService.loginUser(request).enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Đăng nhập thành công từ Server Backend
                    String message = response.body().message;
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                    // TODO: Chuyển sang HomeActivity sau khi đăng nhập thành công
                    // Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    // startActivity(intent);
                    // finish();
                } else {
                    // Server trả về lỗi (Ví dụ 401: Sai email hoặc mật khẩu)
                    Toast.makeText(LoginActivity.this, "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                // Lỗi kết nối (Server Python chưa bật hoặc sai đường dẫn IP)
                Toast.makeText(LoginActivity.this, "Lỗi kết nối Server: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}