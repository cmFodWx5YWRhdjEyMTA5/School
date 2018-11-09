package com.video.aashi.school.fragments.payments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.Payment;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.Invoice_array;
import com.video.aashi.school.adapters.post_class.Invoic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnPaid extends Fragment {


    String accountName;
    String bankName;
    String basicAmount;
    String chequePayment;
    String invoiceDtDisp;
    String invoiceHdrName;
    String invoiceStatusDisp;
    String paidAmount;
    String paid;
    String itemName;
  public  static   Paidadapterr paidadapter;
    public static List<Invoice_array> invoice_arrays= new ArrayList<>();
    String studentid,classid,locationid;
    Retrofit retrofit;
    MyInterface myInterface;
   public static RecyclerView recyclerView;
    RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_un_paid, container, false);

        studentid = Navigation.student_id;
        classid = Navigation.class_id;
        locationid = Navigation.location_id;
        recyclerView=(RecyclerView) view.findViewById(R.id.unpaid_recycle);
        invoice_arrays.clear();
        OkHttpClient defaulthttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException
                            {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Content-Type", "application/json").build();
                                return chain.proceed(request);

                            }
                        }).build();
        retrofit =   new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        myInterface = retrofit.create(MyInterface.class);
       // new getPaid().execute();
   //     paidadapter = new Paidadapterr(invoice_arrays,paidadapter);
      //  recyclerView.setAdapter(paidadapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


/*    class getPaid extends AsyncTask
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects)
        {
            HashMap<String, String> params = new HashMap<String, String>();
            requestQueue = Volley.newRequestQueue(getActivity());
            params.put("studentId",studentid);
            params.put("classId",classid);
            params.put("locId",locationid);
            final String URL = APIUrl.BASE_URL +"getStudentInvoicesForParentLogin";
            JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                        Log.i("Tag","Hellopaid"+ response );

                            try {

                                JSONObject object = new JSONObject(response.toString());
                                JSONArray list = object.getJSONArray("Student Invoice Details");
                                for(int i=0;i<list.length();i++)
                                {
                                    if (list.length() != 0)
                                    {
                                        JSONObject data = list.getJSONObject(i);
                                        paid = data.getString("paid");
                                        if (paid.contains("N"))
                                        {
                                            accountName = data.getString("accountName");
                                            bankName = data.getString("bankName");
                                            basicAmount = data.getString("basicAmount");
                                            chequePayment = data.getString("chequePayment");
                                            invoiceDtDisp = data.getString("invoiceDtDisp");
                                            invoiceHdrName = data.getString("invoiceHdrName");
                                            invoiceStatusDisp = data.getString("invoiceStatusDisp");
                                            paidAmount = data.getString("paidAmount");
                                            //  itemName  = data.getString("itemName");
                                            invoice_arrays.add(new Invoice_array(accountName,bankName,basicAmount,chequePayment,invoiceDtDisp,invoiceHdrName,
                                                    invoiceStatusDisp,paidAmount,paid));
                                    }
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            progressDialog.dismiss();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                            Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                  requestQueue.add(request_json);

            return null;
        }






    }

*/
static class Paidadapterr extends RecyclerView.Adapter<Viewholder> {


        List<Invoice_array> list;
        Context context;
        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_fees, viewGroup, false);
            return new Viewholder(view);
        }
        public Paidadapterr(List<Invoice_array> adapters, Context context)
        {

            this.context = context;
            this.list = adapters;

        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {

            viewholder.date.setText(list.get(i).getInvoiceDtDisp()  );
            viewholder.amount.setText(list.get(i).getPaidAmount());
            String paids = list.get(i).getPaid();
            viewholder.feename.setText(list.get(i).getInvoiceHdrName());
            Log.i("Tag","Hellopaid"+ paids );
            if (paids.contains("N"))
            {
                viewholder.paidlayout.setVisibility(View.GONE);
                viewholder.unpaidlayout.setVisibility(View.VISIBLE);
            }
            else
            {
                viewholder.paidlayout.setVisibility(View.VISIBLE);
                viewholder.unpaidlayout.setVisibility(View.GONE);

            }
            viewholder.paynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   context. startActivity(new Intent(context,Payment.class));
                }
            });
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public static class Viewholder extends RecyclerView.ViewHolder
    {
        TextView date;
        TextView amount,feename;
        LinearLayout paidlayout,unpaidlayout;
        CardView paynow;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            date = (TextView)itemView.findViewById(R.id.due_date);
            amount =(TextView)itemView.findViewById(R.id.due_amount);
            paidlayout =(LinearLayout)itemView.findViewById(R.id.paid);
            unpaidlayout =(LinearLayout)itemView.findViewById(R.id.unpaid);
            paynow =(CardView) itemView.findViewById(R.id.paynow);
            feename=(TextView)itemView.findViewById(R.id.feename);
        }
    }

}

