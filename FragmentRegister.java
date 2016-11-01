package com.codeshastra.coderr.provideameal;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentRegister extends Fragment {


    private Context mContext;

    public String time;
    public EditText nameEditText;
    public EditText passwordEditText;
    public EditText occupationEditText;
    public EditText contactEditText;
    public EditText emailEditText;
    public EditText addressEditText;
    public TextInputLayout nameTextInputLayout;
    public TextInputLayout passwordTextInputLayout;
    public TextInputLayout occupationTextInputLayout;
    public TextInputLayout contactTextInputLayout;
    public TextInputLayout emailTextInputLayout;
    public TextInputLayout addressTextInputLayout;

    public FragmentRegister() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mContext = getActivity();

        nameEditText = (EditText) view.findViewById(R.id.edit_name);
        passwordEditText = (EditText) view.findViewById(R.id.edit_password);
        occupationEditText = (EditText) view.findViewById(R.id.edit_occupation);
        contactEditText = (EditText) view.findViewById(R.id.edit_contact);
        emailEditText = (EditText) view.findViewById(R.id.edit_email);
        addressEditText = (EditText) view.findViewById(R.id.edit_address);

        nameEditText.addTextChangedListener(new MyTextWatcher(nameEditText));
        passwordEditText.addTextChangedListener(new MyTextWatcher(passwordEditText));
        occupationEditText.addTextChangedListener(new MyTextWatcher(occupationEditText));
        contactEditText.addTextChangedListener(new MyTextWatcher(contactEditText));
        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        addressEditText.addTextChangedListener(new MyTextWatcher(addressEditText));

        nameTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_name);
        passwordTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_password);
        occupationTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_occupation);
        contactTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_contact);
        emailTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_email);
        addressTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_address);

        Button sendButton = (Button) view.findViewById(R.id.btn_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    Log.e("FDonate", "All valid");
                    Toast.makeText(mContext, "Sent", Toast.LENGTH_SHORT).show();
                    nameEditText.setText(null);
                    nameTextInputLayout.setErrorEnabled(false);
                    passwordEditText.setText(null);
                    passwordTextInputLayout.setErrorEnabled(false);
                    occupationEditText.setText(null);
                    occupationTextInputLayout.setErrorEnabled(false);
                    contactEditText.setText(null);
                    contactTextInputLayout.setErrorEnabled(false);
                    emailEditText.setText(null);
                    emailTextInputLayout.setErrorEnabled(false);
                    addressEditText.setText(null);
                    addressTextInputLayout.setErrorEnabled(false);
                    Intent intent = new Intent(getActivity(), RegisterSummaryActivity.class);
                    getActivity().startActivity(intent);
                }

                sendToServer();
            }
        });
        return view;
    }

    private boolean isFormValid() {
        if (!isNameValid() || !isPasswordValid() || !isOccupationValid() || !isContactValid() || !isAddressValid() || !isEmailValid()) {
            if (!isNameValid()) {
                nameTextInputLayout.setErrorEnabled(true);
                nameTextInputLayout.setError("Please enter a valid name");
            } else {
                nameTextInputLayout.setError(null);
                nameTextInputLayout.setErrorEnabled(false);
            }
            if (!isPasswordValid()) {
                passwordTextInputLayout.setErrorEnabled(true);
                passwordTextInputLayout.setError("Please enter a valid password");
            } else {
                passwordTextInputLayout.setError(null);
                passwordTextInputLayout.setErrorEnabled(false);
            }
            if (!isOccupationValid()) {
                occupationTextInputLayout.setErrorEnabled(true);
                occupationTextInputLayout.setError("Please enter a valid occupation");
            } else {
                occupationTextInputLayout.setError(null);
                occupationTextInputLayout.setErrorEnabled(false);
            }
            if (!isContactValid()) {
                contactTextInputLayout.setErrorEnabled(true);
                contactTextInputLayout.setError("Please enter a valid contact number");
            } else {
                contactTextInputLayout.setError(null);
                contactTextInputLayout.setErrorEnabled(false);
            }
            if (!isEmailValid()) {
                emailTextInputLayout.setErrorEnabled(true);
                emailTextInputLayout.setError("Please enter a valid email address");
            } else {
                emailTextInputLayout.setError(null);
                emailTextInputLayout.setErrorEnabled(false);
            }
            if (!isAddressValid()) {
                addressTextInputLayout.setErrorEnabled(true);
                addressTextInputLayout.setError("Please enter a valid address");
            } else {
                addressTextInputLayout.setError(null);
                addressTextInputLayout.setErrorEnabled(false);
            }
            return false;
        } else {
            nameTextInputLayout.setErrorEnabled(false);
            passwordTextInputLayout.setErrorEnabled(false);
            occupationTextInputLayout.setErrorEnabled(false);
            contactTextInputLayout.setErrorEnabled(false);
            emailTextInputLayout.setErrorEnabled(false);
            addressTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean isNameValid() {
        return !nameEditText.getText().toString().trim().isEmpty();
    }

    public boolean isPasswordValid() {
        return !passwordEditText.getText().toString().trim().isEmpty();
    }

    public boolean isOccupationValid() {
        return !occupationEditText.getText().toString().trim().isEmpty();
    }

    public boolean isContactValid() {
        return !contactEditText.getText().toString().trim().isEmpty();
    }

    public boolean isEmailValid() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailEditText.getText().toString().matches(emailPattern);
    }

    public boolean isAddressValid() {
        return !addressEditText.getText().toString().trim().isEmpty();
    }

    private void sendToServer() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://provideameal.esy.es/pam_volunteer.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("", "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    // Check for error node in json
                    if (status.equals("success")) {
                        // user successfully logged in
                        Log.e("Registration", "successfull");
                    } else {
                        Toast.makeText(getActivity(), "Unable to send information to our server. ", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("", "json parsing error: " + e.getMessage());
                    //Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;

                Log.e("", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "There was an error connecting to the server.Please try again later ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // String id =token.substring(6,token.length()-1);
                params.put("name", nameEditText.getText().toString());
                params.put("password", passwordEditText.getText().toString());
                params.put("occupation", occupationEditText.getText().toString());
                params.put("contact", contactEditText.getText().toString());
                params.put("email", emailEditText.getText().toString());
                params.put("address", addressEditText.getText().toString());
                Log.e("", params.toString());
                return params;
            }
        };
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_name:
                    if (!isNameValid()) {
                        nameTextInputLayout.setErrorEnabled(true);
                        nameTextInputLayout.setError("Please enter a valid name");
                    } else {
                        nameTextInputLayout.setError(null);
                        nameTextInputLayout.setErrorEnabled(false);
                    }
                    break;
                case R.id.edit_password:
                    if (!isPasswordValid()) {
                        passwordTextInputLayout.setErrorEnabled(true);
                        passwordTextInputLayout.setError("Please enter a valid password");
                    } else {
                        passwordTextInputLayout.setError(null);
                        passwordTextInputLayout.setErrorEnabled(false);
                    }
                case R.id.edit_occupation:
                    if (!isOccupationValid()) {
                        occupationTextInputLayout.setErrorEnabled(true);
                        occupationTextInputLayout.setError("Please enter a valid occupation");
                    } else {
                        occupationTextInputLayout.setError(null);
                        occupationTextInputLayout.setErrorEnabled(false);
                    }
                case R.id.edit_contact:
                    if (!isContactValid()) {
                        contactTextInputLayout.setErrorEnabled(true);
                        contactTextInputLayout.setError("Please enter a valid contact number");
                    } else {
                        contactTextInputLayout.setError(null);
                        contactTextInputLayout.setErrorEnabled(false);
                    }
                case R.id.edit_email:
                    if (!isEmailValid()) {
                        emailTextInputLayout.setErrorEnabled(true);
                        emailTextInputLayout.setError("Please enter a valid email address");
                    } else {
                        emailTextInputLayout.setError(null);
                        emailTextInputLayout.setErrorEnabled(false);
                    }
                case R.id.edit_address:
                    if (!isAddressValid()) {
                        addressTextInputLayout.setErrorEnabled(true);
                        addressTextInputLayout.setError("Please enter a valid address");
                    } else {
                        addressTextInputLayout.setError(null);
                        addressTextInputLayout.setErrorEnabled(false);
                    }
            }
        }
    }

}
