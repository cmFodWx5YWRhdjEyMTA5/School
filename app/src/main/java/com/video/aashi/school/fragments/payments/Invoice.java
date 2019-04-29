package com.video.aashi.school.fragments.payments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.video.aashi.school.APIUrl;
import com.video.aashi.school.MainActivity;
import com.video.aashi.school.Navigation;
import com.video.aashi.school.Payment;
import com.video.aashi.school.PinLogin;
import com.video.aashi.school.R;
import com.video.aashi.school.adapters.Expired;
import com.video.aashi.school.adapters.Interfaces.MyInterface;
import com.video.aashi.school.adapters.arrar_adapterd.Invoice_array;
import com.video.aashi.school.adapters.post_class.Invoic;
import com.video.aashi.school.adapters.post_class.Ivoices;
import com.video.aashi.school.fragments.HomePage;
import com.video.aashi.school.fragments.MainInvoice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
public class Invoice extends Fragment {

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
    Paidadapter paidadapter;
  public  static   List<Invoice_array> invoice_arrays= new ArrayList<>();
    String studentid,classid,locationid;
    Retrofit retrofit;
    MyInterface myInterface;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice, container, false);
        studentid = Navigation.student_id;
        classid = Navigation.class_id;
        locationid = Navigation.location_id;
        recyclerView=(RecyclerView) view.findViewById(R.id.allinvoice);
        invoice_arrays.clear();
        UnPaid.invoice_arrays.clear();
        OkHttpClient defaulthttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor()
                        {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException
                            {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Content-Type", "application/json").build();
                                return chain.proceed(request);
                            }
                        }).build();
        retrofit =   new Retrofit.Builder().baseUrl(HomePage.url).addConverterFactory
                (GsonConverterFactory.create())
                .client(defaulthttpClient)
                .build();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        myInterface = retrofit.create(MyInterface.class);
        new getPaid().execute();
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }
    class getPaid extends AsyncTask
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(true);
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            Call<ResponseBody> call  = myInterface.getIvoices(new Ivoices(studentid,classid,locationid,Navigation.loginId,Navigation.session));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String bodyString = null;
                    if (response.isSuccessful())
                    {
                        try
                        {
                            bodyString  = response.body().string();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        Log.i("Tag","MyPaids"+ classid + call.request().url() + bodyString );
                        {
                            try {
                                JSONObject object = new JSONObject(bodyString);
                                String   failure = object.getString("status");
                                String  errorMessage = object.getString("errorMessage");
                                if  (failure.contains("failure")) {
                                    String finalErrorMessage = errorMessage;
                                    Expired expired = new Expired(getActivity(), finalErrorMessage);
                                    expired.setTitle(finalErrorMessage);
                                    expired.setCancelable(false);

                                    expired.setPositiveButton("OK", (dialog1, which) -> {
                                        expired.getSharedPreferences();
                                        Intent i = new Intent(getActivity(), PinLogin.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    });
                                    expired.show();
                                }
                                JSONArray list = object.getJSONArray("Student Invoice Details");
                                for(int i=0;i<list.length();i++)
                                {
                                    if (list != null) {
                                        JSONObject data = list.getJSONObject(i);
                                        paid = data.getString("paid");
                                        accountName = data.getString("accountName");
                                        bankName = data.getString("bankName");
                                        basicAmount = data.getString("basicAmount");
                                        chequePayment = data.getString("chequePayment");
                                        invoiceDtDisp = data.getString("invoiceDtDisp");
                                        invoiceHdrName = data.getString("invoiceHdrName");
                                        invoiceStatusDisp = data.getString("invoiceStatusDisp");
                                        paidAmount = data.getString("paidAmount");
                                        //itemName  = data.getString("itemName");
                                        invoice_arrays.add(new Invoice_array(accountName,bankName,basicAmount,chequePayment,invoiceDtDisp,invoiceHdrName,
                                                invoiceStatusDisp,paidAmount,paid));
                                        if (paid.contains("N"))
                                        {
                                            UnPaid.invoice_arrays.add(new Invoice_array(accountName,bankName,basicAmount,chequePayment,invoiceDtDisp,invoiceHdrName,
                                                    invoiceStatusDisp,paidAmount,paid));
                                            UnPaid.paidadapter = new UnPaid.Paidadapterr(UnPaid. invoice_arrays,
                                                    getActivity());

                                            UnPaid.recyclerView .setAdapter(UnPaid.paidadapter);

                                        }
                                        paidadapter = new Paidadapter(invoice_arrays,paidadapter);
                                        recyclerView.setAdapter(paidadapter);
                                        progressDialog.dismiss();

                                    }
                                    else
                                    {
                                    }
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Something went wrong..!!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }


                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t)
                {



                }
            });

            return null;
        }





    }


    class Paidadapter extends RecyclerView.Adapter<Viewholder> {


        List<Invoice_array> list;
       Paidadapter paidadapter;


        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.all_fees, viewGroup, false);
            return new Viewholder(view);
        }

        public Paidadapter(List<Invoice_array> adapters,Paidadapter paidadapter)
        {
            this.paidadapter  = paidadapter;
            this.list = adapters;
        }

        @Override
        public void onBindViewHolder(@NonNull Viewholder viewholder, int i)
        {
            viewholder.date.setText(list.get(i).getInvoiceDtDisp()  );
            viewholder.amount.setText(list.get(i).getPaidAmount());
            String paids = list.get(i).getPaid();
            viewholder.feename.setText(list.get(i).getInvoiceHdrName());
            if (paids.contains("Y"))
            {
                viewholder.paidlayout.setVisibility(View.VISIBLE);
                viewholder.unpaidlayout.setVisibility(View.GONE);
            }
            else
            {
                viewholder.paidlayout.setVisibility(View.GONE);
                viewholder.unpaidlayout.setVisibility(View.VISIBLE);
            }
            viewholder.paynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(),Payment.class));
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

