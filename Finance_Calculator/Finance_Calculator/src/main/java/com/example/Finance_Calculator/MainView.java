package com.example.Finance_Calculator;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView  extends VerticalLayout {
    // monthly payments
    private double mAmount = 0.0;
    //total payment
    private double Tpayment = 0.0;
    //amount string text
    //storing amounts in text so button event and call them
    private String monthlytext,totaltext;


    public MainView(){

        setSizeFull(); // Makes layout take the full screen
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // Horizontal center
        // top heading
        H1 heading1 = new H1("Smart Finance Calculator");
        heading1.getStyle()
                        .set("text-align", "center")
                        .setPaddingTop("20")
                        .setWidth("100%");
        add(heading1);
// button navigation row
        //loan button
        Button btnLoan = new Button("Loan");
        btnLoan.getStyle()
                .setBackgroundColor("#3FC7F4")
                .set("color", "white")
                .setBorderRadius("8");
        // investment button
        Button btnInvestment = new Button("Investment");
        btnInvestment.getStyle()
                .setBackgroundColor("#3FC7F4")
                .set("color", "white")
                .setBorderRadius("8");
        btnLoan.setEnabled(false);// disable button
        btnInvestment.addClickListener(e->getUI().ifPresent(
                ui->ui.navigate("Investment")));

        //navigation button layout
        add(new HorizontalLayout(btnLoan,btnInvestment));

        //loan heading
        H2 heading2 = new H2("Loan");
        heading1.getStyle()
                .set("text-align", "center")
                .setPaddingTop("50")
                .setFontSize("20")
                .setWidth("100%");
        add(heading2);

        NumberField LAmount = new NumberField("Loan Amount");
        LAmount.getStyle()
                        .set("color","black");
        NumberField IntRate = new NumberField("Interest Rate (%)");
        IntRate.getStyle()
                .set("color","black");
        //loan term input box
        IntegerField Lterm = new IntegerField("Loan Term (Years)");
        Lterm.setMin(1);
        //Lterm.setMax(20);
        Lterm.getStyle()
                .set("color","black");
        ComboBox Currency = new ComboBox("Currency");
        Currency.setItems("$","R","€","£","¥");
        // set default value
        //Currency.setValue("$");
        Currency.getStyle()
                .set("color","black");

        // layout for the inputs
        add(new HorizontalLayout( LAmount, IntRate));
        add(new HorizontalLayout(Lterm,Currency));
        Button calLoan = new Button("Calculate Loan");
        calLoan.getStyle()
                .setBackgroundColor("#3FC7F4")
                .set("color","white");
        add(calLoan);

        //Loan Repayment Details heading
        H2 Heading3 = new H2("Loan Repayment Details");
        Heading3.getStyle()
                        .setPaddingTop("50")
                        .setFontSize("20");
        add(Heading3);

        //monthly payments label heading
        H3 monthlyInstallment = new H3("Monthly payment");
        monthlyInstallment.getStyle()
                .set("color","#D9D9D9")
                .setFontSize("19")
                .setMarginLeft("24")
                .setFontWeight("semi-Bold");

        //total payments label heading
        H3 totalPayments = new H3("Total payment");
        totalPayments.getStyle()
                .set("color","#D9D9D9")
                .setFontSize("19")
                .setMarginLeft("24")
                //.setPaddingLeft("20")
                .setFontWeight("semi-Bold");
        //layout of the headings for repayment details
        //HorizontalLayout titles = new HorizontalLayout(monthlyInstallment,totalPayments);
        //add(titles);
        // the money placement in the first row
        //String currency = Currency.getValue().toString();// change it to a string
        // convert the amount into a string
        String currency;




        //String Amount1 = String.valueOf(mAmount);
        // label for monthly payment amount
        Span MAmount = new Span();
        MAmount.getStyle()
                .set("color","black")
                .setFontSize("17");


        // label for Total payment amount
        Span TAmount = new Span();
        //style the text
        TAmount.getStyle()
                .set("color","black")
                .setMarginLeft("324")
                .setPadding("44")
                .setFontSize("17");
        //add(TAmount);
        //add(new HorizontalLayout(MAmount,TAmount));

        Currency.addValueChangeListener(e -> {
            //currency symbol
            String selectedCurrency = e.getValue().toString();
            //monthly payments the currency and amount
            String MamountStr = String.format("%.2f", mAmount);// amount format and change to string
            //MAmount.setText(selectedCurrency + " " + MamountStr);
            monthlytext = selectedCurrency+ " "+ MamountStr;
            // Total payments the currency and amount
            String TamountStr = String.format("%.2f", Tpayment);// amount format and change to string
            //TAmount.setText(selectedCurrency + " " + TamountStr);

            totaltext = selectedCurrency + " "+ TamountStr;
        });

        calLoan.addClickListener(e->{

            double monthlypayment =calcLoan(LAmount,IntRate,Lterm);
            mAmount =monthlypayment;
            //monthly payment text
            MAmount.setText(monthlytext);
            //total payment text
            double totalpayment = caltotal(Lterm,monthlypayment);
            Tpayment = totalpayment;
            TAmount.setText(totaltext);

        });

        //monthly column
        VerticalLayout monthColumn = new VerticalLayout(monthlyInstallment,MAmount);
        monthColumn.setPadding(false);
        //monthColumn.setSpacing(false);
        monthColumn.setAlignItems(Alignment.START);
        monthColumn.setAlignItems(Alignment.CENTER);

        //total payment column
        VerticalLayout totalpayColumn = new VerticalLayout(totalPayments,TAmount);
        totalpayColumn.setPadding(false);
        //totalpayColumn.setSpacing(false);
        totalpayColumn.setAlignItems(Alignment.START);
        totalpayColumn.setAlignItems(Alignment.CENTER);

        // combine the  monthly pay and total pay columns
        VerticalLayout PaymentsRow1 = new VerticalLayout(monthColumn,totalpayColumn);
        PaymentsRow1.setSpacing(true);
        PaymentsRow1.setPadding(true);
        PaymentsRow1.setAlignItems(Alignment.CENTER); // Vertically center columns
        PaymentsRow1.setJustifyContentMode(JustifyContentMode.CENTER); // Horizontally center
        PaymentsRow1.setWidthFull();

        add(PaymentsRow1);
    }

    //calculate the loan
    public double calcLoan(NumberField LAmount,NumberField interestRate, IntegerField years){

        //loan amount (present value)
        double p = LAmount.getValue();
        //r	Monthly interest rate = annualRate ÷ 12 ÷ 100
        double r = interestRate.getValue() /12/100;
        //Total number of months = loan term
        int n = years.getValue()*12;
        //Monthly payment (the answer/result)
        double MonthlyPayment =0.0;
        double numirator;
        double denomirator;

        numirator = p*r*Math.pow(1 + r, n);
        denomirator = Math.pow(1+r,n)-1;
        MonthlyPayment = numirator/denomirator;
        return MonthlyPayment;

    }
    // calculate  total payment
    public double caltotal(IntegerField years, double monthlypay){
        int n = years.getValue()*12;
        double total =0 ;
        // loop until end of the loan term in months
        for (int i = 1; i < n; i++) {
            total = total +monthlypay;

        }
        return  total;
    }

}
