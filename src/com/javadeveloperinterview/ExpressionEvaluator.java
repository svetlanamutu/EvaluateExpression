package com.javadeveloperinterview;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEvaluator {
    private boolean _expression_is_valid = true;

    public boolean ValidateExpression(String expression) {
        _expression_is_valid = true;
        //to implement properly later
        return _expression_is_valid;
    }

    private double Add(double a, double b) {

        return a + b;
    }

    private double Subtract(double a, double b) {

        return a - b;
    }

    private double Multiply(double a, double b) {

        return a * b;
    }

    private double Divide(double a, double b) {

        return a / b;
    }
    private String RemoveWhiteSpaces(String expression){
        //Remove all white space in the expression
        Pattern patternWhiteSpace = Pattern.compile("\\s");
        Matcher matcher = patternWhiteSpace.matcher(expression);
        boolean matchFound = matcher.find();
        if (matchFound) {
            expression = matcher.replaceAll("");
        }
        return expression;
    }
    public Double EvaluateBinaryOperation(String expression) {
        Double leftOperand=0.0;
        Double rightOperand=0.0;
        String operation = "";
        // Remove all commas that separate groups of 3 digits in a number, if present;
        // If commas are present, a more complex REGEX is needed and comma must be removed before parsing of string into double
        expression = expression.replaceAll(",", "");
        //Remove white spaces
        expression = RemoveWhiteSpaces(expression);

        //Extract the left operand
        Pattern patternLeftOperand = Pattern.compile("(^[+-]?[\\d]+){1}(([.]?[\\d]+)?)?");
        Matcher matcher = patternLeftOperand.matcher(expression);
        boolean matchFound = matcher.find();
        if (matchFound) {
            leftOperand = Double.parseDouble(matcher.group(0));
            expression = matcher.replaceFirst("");

        }

        //Extract the operation
        Pattern patternOperation = Pattern.compile("^[+/*-]");
        matcher = patternOperation.matcher(expression);
        matchFound = matcher.find();
        if (matchFound) {
            operation = matcher.group(0).toString();
            expression = matcher.replaceFirst("");

        }
        //Extract the right operand
        Pattern patternRightOperand = Pattern.compile("(^[+-]?[\\d]+){1}(([.]?[\\d]+)?)?");
        matcher = patternRightOperand.matcher(expression);
        matchFound = matcher.find();
        if (matchFound) {
            rightOperand = Double.parseDouble(matcher.group(0));
            expression = matcher.replaceFirst("");



        }
        switch (operation){
            case "+":
                return this.Add(leftOperand, rightOperand);
            case "-":
                return this.Subtract(leftOperand, rightOperand);
            case "*":
                return this.Multiply(leftOperand, rightOperand);
            case "/":
                return this.Divide(leftOperand,rightOperand);
            default:
                throw new IllegalStateException("Unexpected value: " + operation);
        }
    }

    public String RemoveAPairOfParentheses(String expression){
        expression = RemoveWhiteSpaces(expression);

        Pattern patternUnaryOperation = Pattern.compile("[(]([-]?[\\d]+){1}(([.]?[\\d]+)?)?[)]");
        Matcher matcherUnary = patternUnaryOperation.matcher(expression);
        boolean matchFoundUnary = matcherUnary.find();

        Pattern patternBinaryOperation = Pattern.compile("[(]{1}([-]?[\\d]+){1}(([.]?[\\d]+)?)?[+/*-]([-]?[\\d]+){1}(([.]?[\\d]+)?)?[)]{1}");
        Matcher matcherBinary = patternBinaryOperation.matcher(expression);
        boolean matchFoundBinary = matcherBinary.find();

        if (matchFoundUnary) {
            String fullMatch = matcherUnary.group(0); //matcher.group(0) contains full match string
            //remove first and last characters - first is '(', last is ')'
            String expressionAfterFirstReplacement = fullMatch.replace("(", "");
            String expressionAfterSecondReplacement = expressionAfterFirstReplacement.replace(")", "");
            expression = matcherUnary.replaceFirst(expressionAfterSecondReplacement);

        }

        if (matchFoundBinary) {
            String fullMatch = matcherBinary.group(0); //matcher.group(0) contains full match string
            //remove first and last characters - first is '(', last is ')'
            String expressionAfterFirstReplacement = fullMatch.replace("(", "");
            String expressionAfterSecondReplacement = expressionAfterFirstReplacement.replace(")", "");

            //evaluate without ()
            expression = matcherBinary.replaceFirst((EvaluateBinaryOperation(expressionAfterSecondReplacement)).toString());

        }

        Pattern patternManyOperationsWithinOnePairOfParenthesis = Pattern.compile("[(]{1}([-]?[\\d]+){1}(([.]?[\\d]+)?)?[+/*-]([-]?[\\d]+){1}(([.]?[\\d]+)?)?([+/*-]([-]?[\\d]+){1}(([.]?[\\d]+)?)?){1,}[)]{1}");
        Matcher matcherMany = patternManyOperationsWithinOnePairOfParenthesis.matcher(expression);
        boolean matchFoundMany = matcherMany.find();
        if (matchFoundMany) {
            String fullMatchLong = matcherMany.group(0); //matcher.group(0) contains full match string

            Pattern patternBinaryOperationMultiplyOrDivide = Pattern.compile("([-]?[\\d]+){1}(([.]?[\\d]+)?)?[/*]([-]?[\\d]+){1}(([.]?[\\d]+)?)?");
            Matcher matcherBinaryMultiplyOrDivide;
            boolean matchFoundBinaryMultiplyOrDivide=false;
            do {

                matcherBinaryMultiplyOrDivide = patternBinaryOperationMultiplyOrDivide.matcher(fullMatchLong);
                matchFoundBinaryMultiplyOrDivide = matcherBinaryMultiplyOrDivide.find();

                if (matchFoundBinaryMultiplyOrDivide) {
                    String fullMatch = matcherBinaryMultiplyOrDivide.group(0); //matcher.group(0) contains full match string

                    Double expressionToReplaceWith = this.EvaluateBinaryOperation(fullMatch);
                    //evaluate without ()
                    fullMatchLong = matcherBinaryMultiplyOrDivide.replaceFirst(expressionToReplaceWith.toString());

                }
            }
            while (matchFoundBinaryMultiplyOrDivide == true);

            //evaluate all additions and subtractions

            Pattern patternBinaryOperationAddOrSubtract = Pattern.compile("([-]?[\\d]+){1}(([.]?[\\d]+)?)?[+-]([-]?[\\d]+){1}(([.]?[\\d]+)?)?");
            Matcher matcherBinaryAddOrSubtract;
            boolean matchFoundBinaryAddOrSubtract=false;

            do {

                matcherBinaryAddOrSubtract = patternBinaryOperationAddOrSubtract.matcher(fullMatchLong);
                matchFoundBinaryAddOrSubtract = matcherBinaryAddOrSubtract.find();

                if (matchFoundBinaryAddOrSubtract) {
                    String fullMatch = matcherBinaryAddOrSubtract.group(0); //matcher.group(0) contains full match string

                    Double expressionToReplaceWith = this.EvaluateBinaryOperation(fullMatch);
                    //evaluate without ()
                    fullMatchLong = matcherBinaryAddOrSubtract.replaceFirst(expressionToReplaceWith.toString());

                }
            }
            while (matchFoundBinaryAddOrSubtract == true);

            String expressionAfterFirstReplacement = fullMatchLong.replace("(", "");
            String expressionAfterSecondReplacement = expressionAfterFirstReplacement.replace(")", "");

            //evaluate without ()
            expression = matcherMany.replaceFirst((expressionAfterSecondReplacement).toString());



        }
    return expression;
    }

    public String Evaluate(String expression){
        expression = RemoveWhiteSpaces(expression);
        while (_expression_is_valid && expression.indexOf("(")>=0){
            expression = RemoveAPairOfParentheses(expression);
            }

        Pattern patternBinaryOperationMultiplyOrDivide = Pattern.compile("([-]?[\\d]+){1}(([.]?[\\d]+)?)?[/*]([-]?[\\d]+){1}(([.]?[\\d]+)?)?");
        Matcher matcherBinaryMultiplyOrDivide;
        boolean matchFoundBinaryMultiplyOrDivide=false;
        do {

            matcherBinaryMultiplyOrDivide = patternBinaryOperationMultiplyOrDivide.matcher(expression);
            matchFoundBinaryMultiplyOrDivide = matcherBinaryMultiplyOrDivide.find();

            if (matchFoundBinaryMultiplyOrDivide) {
                String fullMatch = matcherBinaryMultiplyOrDivide.group(0); //matcher.group(0) contains full match string

                Double expressionToReplaceWith = this.EvaluateBinaryOperation(fullMatch);
                //evaluate without ()
                expression = matcherBinaryMultiplyOrDivide.replaceFirst(expressionToReplaceWith.toString());

            }
        }
        while (matchFoundBinaryMultiplyOrDivide == true);

        //evaluate all additions and subtractions

        Pattern patternBinaryOperationAddOrSubtract = Pattern.compile("([-]?[\\d]+){1}(([.]?[\\d]+)?)?[+-]([-]?[\\d]+){1}(([.]?[\\d]+)?)?");
        Matcher matcherBinaryAddOrSubtract;
        boolean matchFoundBinaryAddOrSubtract=false;

        do {

            matcherBinaryAddOrSubtract = patternBinaryOperationAddOrSubtract.matcher(expression);
            matchFoundBinaryAddOrSubtract = matcherBinaryAddOrSubtract.find();

            if (matchFoundBinaryAddOrSubtract) {
                String fullMatch = matcherBinaryAddOrSubtract.group(0); //matcher.group(0) contains full match string

                Double expressionToReplaceWith = this.EvaluateBinaryOperation(fullMatch);
                //evaluate without ()
                expression = matcherBinaryAddOrSubtract.replaceFirst(expressionToReplaceWith.toString());

            }
        }
        while (matchFoundBinaryAddOrSubtract == true);

        return expression;
    }

}