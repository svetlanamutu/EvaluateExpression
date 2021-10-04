package com.javadeveloperinterview;

public class Main {

    public static void main(String[] args) {
        ExpressionEvaluator exp = new ExpressionEvaluator();
        System.out.print("2+2*2+2+1=");
        System.out.println(exp.Evaluate("2+2*2+2+1"));

        System.out.print("2+2*(2+2*1)=");
        System.out.println(exp.Evaluate("2+2*(2+2*1)"));

        System.out.print("-2+(2*(2+(2*1)))=");
        System.out.println(exp.Evaluate("-2+(2*(2+(2*1)))"));


    }
}

