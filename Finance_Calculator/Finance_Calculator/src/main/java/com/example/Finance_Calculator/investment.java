package com.example.Finance_Calculator;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;

@Route("Investment")
public class investment extends VerticalLayout {
    private  String CurrSymbol;
    private final double intRate = 0.08;
    //private String finalAmount;


    public investment() {

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
        btnInvestment.setEnabled(false);// disable button


        //navigation button layout
        add(new HorizontalLayout(btnLoan,btnInvestment));

        //navigate to the loan calculator page
        btnLoan.addClickListener(e->getUI().ifPresent(
                ui->ui.navigate(" ")));



        //Investment heading
        H2 heading2 = new H2("Investment");
        heading2.getStyle()
                .set("text-align", "center")
                .setPaddingTop("50")
                .setFontSize("20")
                .setWidth("100%");
        add(heading2);



        //A= P(1+I)^N - compound interest
        //amount
        NumberField Amount = new NumberField("Investment amount");
        ComboBox Currency = new ComboBox("Currency");
        Currency.setItems("$","R","€","£","¥");
        Currency.setValue("$");
        CurrSymbol = Currency.getValue().toString();//  initial symbol after setting the default value
        Currency.getStyle()
                .set("color","black");
        Currency.setValue("$");
        IntegerField years = new IntegerField("Investment Duration (Years)");

        // money label
        Span finalAmount = new Span();
        //style the text
        finalAmount.getStyle()
                .set("color","black")
                .setMarginLeft("324")
                .setPadding("44")
                .setFontSize("17");


        //layout of the first row of inputs
        add(new HorizontalLayout(Currency,Amount, years));
        // dialog message box that shows
        Dialog IntMessage = new Dialog("Interest rate ");
        VerticalLayout message1 = new VerticalLayout(
                new Span("All investments are  calculated at an 8% interest rate. "),//message
                new Button("close", e -> IntMessage.close()) // close button
        );
        IntMessage.add(message1);

        //button to calulate the interest
        Button calInvestment = new Button("Calculate Investment");
        add(calInvestment);
        calInvestment.addClickListener(e->{

            //call a function to calculate the amount if the values are entered in the text boxes
            if (Amount.getValue() == null || years.getValue() == null) {
                Notification.show("Please enter values in the input boxes ");
            }
            else{
                // show a pop up message that tells the customer that the intrest is set at 8%
                IntMessage.open();// show the dialog message when clicked
                //calling the function to calculate the  future amount
            double accumulatedMoney =calculateCompoundInterest(Amount.getValue(),intRate,years.getValue());

                String formatted = String.format("%.2f", accumulatedMoney);
                finalAmount.setText(CurrSymbol + " " + formatted);
            }


        });
        // get the value in the combox the currency symbol

        Currency.addValueChangeListener(e-> {
            CurrSymbol = e.getValue().toString();
            System.out.println(CurrSymbol);

        });



        //Accumulated Investment Value heading
        H2 heading3 = new H2("Accumulated Investment Value");
        heading3.getStyle()
                .set("text-align", "center")
                .setPaddingTop("50")
                .setFontSize("20")
                .setWidth("100%");
        //add(heading3);


        //layout of the heading and the amount centered under each other
        VerticalLayout AmountLayout = new VerticalLayout(heading3,finalAmount);
        AmountLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(AmountLayout);




    }

    public double calculateCompoundInterest(double p, double rate, int years) {
        // used the compund int method and is compounded monthly
        double montlyrate = rate / 12;
        int months = years * 12;
        double amount = p * Math.pow((1 + montlyrate), months);
        System.out.println(amount);
        return amount;
    }

}
