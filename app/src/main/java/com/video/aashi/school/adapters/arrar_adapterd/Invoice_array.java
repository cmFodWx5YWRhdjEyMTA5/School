package com.video.aashi.school.adapters.arrar_adapterd;

public class Invoice_array {

    String accountName;
    String bankName;
    String basicAmount;
    String chequePayment;
    String invoiceDtDisp;
    String invoiceHdrName;
    String invoiceStatusDisp;
    String paidAmount;
    String paid;



    public Invoice_array(String accountName,String bankName,String basicAmount,String chequePayment,String invoiceDtDisp,String invoiceHdrName,
                         String invoiceStatusDisp,String paidAmount,String paid)
    {
        this.accountName =accountName;
        this.bankName = bankName;
        this.basicAmount = basicAmount;
        this.chequePayment = chequePayment;
        this.invoiceDtDisp = invoiceDtDisp;
        this.invoiceHdrName = invoiceHdrName;
        this.invoiceStatusDisp = invoiceStatusDisp;
        this.paidAmount = paidAmount;
        this.paid = paid;

    }


    public String getAccountName() {
        return accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBasicAmount() {
        return basicAmount;
    }

    public String getChequePayment() {
        return chequePayment;
    }

    public String getInvoiceDtDisp() {
        return invoiceDtDisp;
    }

    public String getInvoiceHdrName() {
        return invoiceHdrName;
    }

    public String getInvoiceStatusDisp() {
        return invoiceStatusDisp;
    }

    public String getPaid() {
        return paid;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


}
