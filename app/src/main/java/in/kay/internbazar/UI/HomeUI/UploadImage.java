package in.kay.internbazar.UI.HomeUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import in.kay.internbazar.Api.RetrofitClient;
import in.kay.internbazar.R;
import in.kay.internbazar.Utils.Preference;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImage extends AppCompatActivity {
    Button btnChoose, btnUpload;
    ImageView imageView;
    int IMG_CODE=1016;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        btnChoose=findViewById(R.id.btn_choose);
        btnUpload=findViewById(R.id.btn_upload);
        imageView=findViewById(R.id.imageView);
        btnUpload.setEnabled(false);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,IMG_CODE);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoUpload();
            }
        });
    }

    private void DoUpload() {
        String uid = Preference.getSharedPreferenceString(this, "uid", "");
        String token = Preference.getSharedPreferenceString(this, "token", "");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", uid);
        jsonObject.addProperty("userType", "student");
        JsonObject data = new JsonObject();
        data.addProperty("image", imagetoString());
        jsonObject.add("data", data);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().edit(jsonObject, "Bearer " + token);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMax(100);
        pd.setMessage("Uploading...");
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();
        pd.setCancelable(false);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200)
                {
                    Toast.makeText(UploadImage.this, "Successfully uploaded image.", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    finish();
                }
                else {
                    try {
                        String s=response.errorBody().string();
                        Toast.makeText(UploadImage.this, "Failed to uploaded image. "+s, Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UploadImage.this, "Failed to uploaded image. "+t, Toast.LENGTH_SHORT).show();
                pd.dismiss();
                finish();
            }
        });
    }

    private String imagetoString() {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream);
        byte[] imgByte=byteArrayOutputStream.toByteArray();
        return "data:image/jpeg;base64,"+in.kay.internbazar.Utils.Base64.encode(imgByte);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_CODE && resultCode==RESULT_OK&&data!=null)
        {
            Uri path=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                imageView.setImageBitmap(bitmap);
                btnUpload.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}