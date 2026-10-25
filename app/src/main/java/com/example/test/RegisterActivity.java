package com.example.test;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterName, etRegisterEmail, etRegisterPassword;
    private Button btnRegister, btnGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ View từ XML
        etRegisterName = findViewById(R.id.etRegisterName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        // Sự kiện bấm nút Tạo tài khoản
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etRegisterName.getText().toString().trim();
                String email = etRegisterEmail.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Gọi hàm gửi API Đăng ký xuống Server Python
                    performRegister(name, email, password);
                }
            }
        });

        // Bấm quay lại màn hình Login
        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity đăng ký để quay lại Activity Login
            }
        });
    }
    // Hàm gọi API Đăng ký qua Retrofit
    private void performRegister(String fullName, String email, String password) {
        // 1. Khởi tạo ApiService từ ApiClient
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // 2. Tạo đối tượng request chứa dữ liệu đăng ký
        ApiService.RegisterRequest request = new ApiService.RegisterRequest(fullName, email, password);

        // 3. Thực hiện gửi request bất đồng bộ (enqueue)
        apiService.registerUser(request).enqueue(new Callback<ApiService.ApiResponse>() {
            @Override
            public void onResponse(Call<ApiService.ApiResponse> call, Response<ApiService.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Đăng ký thành công trên Backend
                    String message = response.body().message;
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                    // Đóng màn hình đăng ký để quay lại màn hình đăng nhập
                    finish();
                } else {
                    // Server trả về lỗi (Ví dụ 400: Email đã tồn tại)
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: Email đã được sử dụng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiService.ApiResponse> call, Throwable t) {
                // Lỗi kết nối (Server Python chưa bật hoặc sai địa chỉ IP)
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối Server: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}