package userDefinedFunctions;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

public class UserFunctions {
	
	private double[] _xValues;
	private int _n;
	private String _function;
	private String[] _knownFunctionsAndOperators = {"LN","LOG","SIN","COS","TAN","SQRT","+","-","*","/","^","PI","E"};
	private String[] _knownFunctions = {"LN","LOG","SIN","COS","TAN","SQRT"};
	private String[] _knownOperators = {"+","-","*","/","^"};
	private Exception _syntaxError;
	private Exception _semanticError;
	private Exception _otherError;
	private Stack<String> _stackOfStrings;
	
	public UserFunctions(double[] xValues, int n, String fFunction) throws Exception{
		// TODO Auto-generated constructor stub
		
		_xValues = xValues;
		_n = n;
		_function = fFunction;
		_syntaxError = null;
		_semanticError = null;
		_otherError = null;
		_stackOfStrings = new Stack<String>();
		
		//replacing all useless characters
		_function = _function.replaceAll("\\s+", "");
		_function = _function.replaceAll("\\[", "(");
		_function = _function.replaceAll("\\{", "(");
		_function = _function.replaceAll("\\]", ")");
		_function = _function.replaceAll("\\}", ")");
		_function = _function.toUpperCase();
		
		try{
			checkBasicSyntax();
		}
		catch (Exception e) {
			// TODO: handle exception
			_syntaxError = new Exception(e.getMessage());
			throw _syntaxError ;
		}
		
	}
	
	private void checkBasicSyntax() throws Exception{
		
		int _parenthesesOpen = 0;
		int _paranthesesClose = 0;
		Pattern _pattern = Pattern.compile("\\(");
		Matcher _matcher = _pattern.matcher(_function);
		
		while (_matcher.find()){
			_parenthesesOpen++;
		}
		
		_pattern = Pattern.compile("\\)");
		_matcher = _pattern.matcher(_function);
		
		while (_matcher.find()){
			_paranthesesClose++;
		}
		
		if (_parenthesesOpen!=_paranthesesClose){
			
			_syntaxError = new Exception("The user-function is not well paranthesed!");
			throw _syntaxError;
			
		}
		
		
		_pattern = Pattern.compile("\\(\\)");
		_matcher = _pattern.matcher(_function);
		
		while (_matcher.find()){
			_syntaxError = new Exception("The user-function has at least one empty paranthese, which is prohibited!");
			throw _syntaxError;
		}
		
		String _tempFunction = _function;
		_tempFunction = _tempFunction.replaceAll("\\+", " + ");
		_tempFunction = _tempFunction.replaceAll("\\^", " ^ ");
		_tempFunction = _tempFunction.replaceAll("\\*", " * ");
		_tempFunction = _tempFunction.replaceAll("\\/", " / ");
		_tempFunction = _tempFunction.replaceAll("\\-", " - ");
		_tempFunction = _tempFunction.replaceAll("\\(", " ( ");
		_tempFunction = _tempFunction.replaceAll("\\)", " ) ");
		_tempFunction = _tempFunction.replaceAll("\\s{2,}", " ").trim();
		
		String[] _tempFunctionArray = _tempFunction.split(" ");
		
		for (int i=0; i<_tempFunctionArray.length; i++){
			
			if (!_tempFunctionArray[i].startsWith("X") && !_tempFunctionArray[i].equals("(") &&!_tempFunctionArray[i].equals(")") && !partOf(_knownFunctionsAndOperators, _tempFunctionArray[i])){
			
				_syntaxError = new Exception("Unknown characters/function:" + _tempFunctionArray[i]);
				throw _syntaxError;
			
			}
			
		}
		
		for (int i=0; i<_tempFunctionArray.length; i++){
			
			if (_tempFunctionArray[i].startsWith("X")){
				
				int _number;
				
				try{
					_number = Integer.parseInt(_tempFunctionArray[i].substring(1));
				}
				catch(Exception e){
					_syntaxError = new Exception("There are some issues with the X/x variable!The number behind can not be recognized: " + _tempFunctionArray[i]);
					throw _syntaxError;
				}
				
				if (_number>_n || _number<0){
				
					_syntaxError = new Exception("The given X/x variable's index too damn high(or low): " + _tempFunctionArray[i]);
					throw _syntaxError;
					
				}
				
			}
		}
		
		_function = _tempFunction;
	
	}
	

	public double evaluateFunction() throws Exception{
		
		String _helpFunction = _function;
		
		
		for (int i=0; i<_n; i++){
			String _xValue = "X" + (i+1);
			
			_helpFunction = _helpFunction.replaceAll(_xValue, Double.toString(_xValues[i]));
			
		}
		
		_helpFunction = _helpFunction.replaceAll("PI", Double.toString(java.lang.Math.PI));
		_helpFunction = _helpFunction.replaceAll("E", Double.toString(java.lang.Math.E));
		
		_helpFunction = "( " + _helpFunction + " )"; //some good twist
		
		String[] _functionArray = _helpFunction.split(" ");
		
		int i = _functionArray.length - 1;
		
		while (i>-1){
			
			if (!_functionArray[i].equals("(")){
				_stackOfStrings.push(new String(_functionArray[i]));
				i--;
			}
			else{
				
				String[] _basicFunctions = new String[100];
				int _basicLength = 0;
				
				while (!_stackOfStrings.peek().equals(")")){
					
					String _helper;
					_helper = (String)_stackOfStrings.pop();
					
					_basicFunctions[_basicLength] = _helper;
					_basicLength++;
					
				}
				
				_stackOfStrings.pop();
				
				double _tempResult = 0;
				
				try{
					_tempResult = this.evaluateFunction(_basicLength, _basicFunctions);
				}
				catch (Exception e) {
					// TODO: handle exception
					throw e;
				}
				
				
				if ( i-1>-1 && partOf(_knownFunctions, _functionArray[i-1])){
					
					try{
						_tempResult = this.evaluateFunction(_tempResult, _functionArray[i-1]);
					}
					catch (Exception e) {
						// TODO: handle exception
						throw e;
					}
					
					i--;
				}
				
				_stackOfStrings.push(new String(Double.toString(_tempResult)));
				
				i--;
				
			}
			
		}
		
		double _result = Double.parseDouble(_stackOfStrings.pop());
		
		if (!_stackOfStrings.empty()){
			
			_otherError = new Exception("Something went wrog at calculation: stack error!");
			throw _otherError;
		}
		
		return _result;
	}
	
	
	public double evaluateFunction(int _basicLength, String[] _basicFunctions) throws Exception{
		
		for (int i=0; i< _basicLength; i++){
			
			if (i % 2 == 0){
				
				try{
					Double.parseDouble(_basicFunctions[i]);
				}
				catch (Exception e) {
					// TODO: handle exception
					_semanticError = new Exception("The basic rule of value followed up by an operator is violated!");
					throw _semanticError;
				}
			}
			else{
				
				if (!partOf(_knownOperators, _basicFunctions[i])){
					_semanticError = new Exception("The basic rule of value followed up by an operator is violated!");
					throw _semanticError;
				}
				
			}
			
		}
		
		
		int _bLength = _basicLength;
		String[] _bFunctions = _basicFunctions;
		
		int i = 0;
		
		
		//first step should be the "^" operator
		while (i < _bLength){
			
			if (_bFunctions[i].equals("^")){
				
				double _number1 = Double.parseDouble(_bFunctions[i-1]);
				double _number2 = Double.parseDouble(_bFunctions[i+1]);
				double _result = java.lang.Math.pow(_number1, _number2);
				
				_bFunctions[i-1] = Double.toString(_result);
				
				if ((i + 2) < _bLength ){
					for (int j= i+2; j<_bLength; j++){
						_bFunctions[j-2] = _bFunctions[j];	
					}
				}
				
				_bLength = _bLength - 2;
				i--;
				
			}
			
			i++;
			
		}
		
		
		i = 0;
		
		//second step should be the "*" and "/" operators
		while (i < _bLength){
			
			if (_bFunctions[i].equals("*")){
				
				double _number1 = Double.parseDouble(_bFunctions[i-1]);
				double _number2 = Double.parseDouble(_bFunctions[i+1]);
				double _result = _number1 * _number2;
				
				_bFunctions[i-1] = Double.toString(_result);
				
				if ((i + 2) < _bLength ){
					for (int j= i+2; j<_bLength; j++){
						_bFunctions[j-2] = _bFunctions[j];	
					}
				}
				
				_bLength = _bLength - 2;
				i--;
				
			}
			
			if (_bFunctions[i].equals("/")){
				
				double _number1 = Double.parseDouble(_bFunctions[i-1]);
				double _number2 = Double.parseDouble(_bFunctions[i+1]);
				double _result = _number1 / _number2;
				
				_bFunctions[i-1] = Double.toString(_result);
				
				if ((i + 2) < _bLength ){
					for (int j= i+2; j<_bLength; j++){
						_bFunctions[j-2] = _bFunctions[j];	
					}
				}
				
				_bLength = _bLength - 2;
				i--;
				
			}
			
			
			i++;
			
		}
		
		
		i = 0;
		
		//last step should be the "+" and "-" operators
		while (i < _bLength){
			
			if (_bFunctions[i].equals("+")){
				
				double _number1 = Double.parseDouble(_bFunctions[i-1]);
				double _number2 = Double.parseDouble(_bFunctions[i+1]);
				double _result = _number1 + _number2;
				
				_bFunctions[i-1] = Double.toString(_result);
				
				if ((i + 2) < _bLength ){
					for (int j= i+2; j<_bLength; j++){
						_bFunctions[j-2] = _bFunctions[j];	
					}
				}
				
				_bLength = _bLength - 2;
				i--;
				
			}
			
			if (_bFunctions[i].equals("-")){
				
				double _number1 = Double.parseDouble(_bFunctions[i-1]);
				double _number2 = Double.parseDouble(_bFunctions[i+1]);
				double _result = _number1 - _number2;
				
				_bFunctions[i-1] = Double.toString(_result);
				
				if ((i + 2) < _bLength ){
					for (int j= i+2; j<_bLength; j++){
						_bFunctions[j-2] = _bFunctions[j];	
					}
				}
				
				_bLength = _bLength - 2;
				i--;
				
			}
			
			
			i++;
			
		}
		
		if (_bLength != 1){
			_syntaxError = new Exception("Something went wrong at operators and calculations!");
			throw _syntaxError;
		}
		
		double _result = Double.parseDouble(_bFunctions[0]);
		
		return _result;
		
	}
	
	
	public double evaluateFunction(double _number, String _evalFunction) throws Exception{
			
		double _result = 0;
		
		
		//LN
		if (_evalFunction.equals("LN") && _number<= 0){	
			_otherError = new Exception("Can not evaluate a natural logarithm for a negativ number!");
			throw _otherError;
		}
		else if (_evalFunction.equals("LN") && _number> 0){
			_result = java.lang.Math.log(_number);
		}
		
		
		//LOG
		if (_evalFunction.equals("LOG") && _number<= 0){	
			_otherError = new Exception("Can not evaluate the base 10 logarithm for a negativ number!");
			throw _otherError;
		}
		else if (_evalFunction.equals("LOG") && _number> 0){
			_result = java.lang.Math.log10(_number);
		}
		
		
		//SIN
		if (_evalFunction.equals("SIN")){	
			_result = java.lang.Math.sin(_number);
		}
		
		
		//COS
		if (_evalFunction.equals("COS")){	
			_result = java.lang.Math.cos(_number);
		}
		
		
		//TAN
		if ( _evalFunction.equals("TAN") && ( _number==(java.lang.Math.PI/2) || _number==-(java.lang.Math.PI/2)) ){	
			_otherError = new Exception("Can not evaluate the tangent for PI / 2!");
			throw _otherError;
		}
		else if ( _evalFunction.equals("TAN") && _number!=(java.lang.Math.PI/2) && _number!=-(java.lang.Math.PI/2) ){
			_result = java.lang.Math.tan(_number);
		}
		
		
		//SQRT
		if ( _evalFunction.equals("SQRT") && _number<0 ){	
			_otherError = new Exception("Can not evaluate the square root for a negativ number!");
			throw _otherError;
		}
		else if ( _evalFunction.equals("SQRT") && _number>=0 ){
			_result = java.lang.Math.sqrt(_number);
		}
		
		return _result;
		
	}
	
	
	private boolean partOf(String[] _givenArray, String _value){
		
		boolean _returnValue = false;
		
		for (int i=0; i<_givenArray.length; i++){
			if (_givenArray[i].equals(_value)){
				_returnValue = true;
			}
		}
		
		return _returnValue;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[] _testArray = {2, 3, 4};
		int _n = 3;
		String _tempFunction = "ln(     x1) + (ln( x2   ) *      cos(x3))";
//		UserFunctions _userFunction = null;
//		
//		//ln(     x1) + (ln( x2   ) *      cos(x3))
//		//     x1 +  x2    *      x3
//		
//		try {
//			_userFunction = new UserFunctions(_testArray, _n, _tempFunction);
//			System.out.println("It was ok!");
//		} catch (Exception e) {
//	   		// TODO Auto-generated catch block
//			System.out.println(e.getMessage());
//		}
		
//		
//		_tempFunction = _tempFunction.replaceAll("\\s+", "");
//		_tempFunction = _tempFunction.replaceAll("\\[", "(");
//		_tempFunction = _tempFunction.replaceAll("\\{", "(");
//		_tempFunction = _tempFunction.replaceAll("\\]", ")");
//		_tempFunction = _tempFunction.replaceAll("\\}", ")");
//		_tempFunction = _tempFunction.toUpperCase();
//		
//		System.out.println(_tempFunction);
//		
//		
//		_tempFunction = _tempFunction.replaceAll("\\+", " + ");
//		_tempFunction = _tempFunction.replaceAll("\\^", " ^ ");
//		_tempFunction = _tempFunction.replaceAll("\\*", " * ");
//		_tempFunction = _tempFunction.replaceAll("\\/", " / ");
//		_tempFunction = _tempFunction.replaceAll("\\-", " - ");
//		_tempFunction = _tempFunction.replaceAll("\\(", " ( ");
//		_tempFunction = _tempFunction.replaceAll("\\)", " ) ");
//		_tempFunction = _tempFunction.replaceAll("\\s{2,}", " ").trim();
		
//		_tempFunction = _tempFunction.replaceAll("\\s+\\([^\\s]+", "( ");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\(\\s+", " (");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\([^\\s]+", " ( ");
//		_tempFunction = _tempFunction.replaceAll("\\s+\\)[^\\s]+", ") ");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\)\\s+", " )");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\)[^\\s]+", " )");
//		_tempFunction = _tempFunction.replaceAll("\\s+\\+[^\\s]+", "+ ");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\+\\s+", " +");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\+[^\\s]+", " + ");
//		_tempFunction = _tempFunction.replaceAll("\\s+\\-[^\\s]+", "- ");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\-\\s+", " -");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\-[^\\s]+", " - ");
//		_tempFunction = _tempFunction.replaceAll("\\s+\\*[^\\s]+", "* ");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\*\\s+", " *");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\*[^\\s]+", " * ");
//		_tempFunction = _tempFunction.replaceAll("\\s+\\/[^\\s]+", "/ ");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\/\\s+", " /");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\/[^\\s]+", " / ");
//		_tempFunction = _tempFunction.replaceAll("\\s+\\^[^\\s]+", "^ ");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\^\\s+", " ^");
//		_tempFunction = _tempFunction.replaceAll("[^\\s]+\\^[^\\s]+", " ^ ");
//		System.out.println(_tempFunction);
		
		
//		double kk = 0;
//		try{
//			kk = _userFunction.evaluateFunction();
//			
//			System.out.println(kk);
//		}
//		catch (Exception e) {
//			// TODO: handle exception
//		
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//			
//		}

		
		System.out.println(java.lang.Math.log(2) + " " + java.lang.Math.log(3) + " " + java.lang.Math.cos(4));
		
		//System.out.println(java.lang.Math.pow(-2.00,3.00)); 
		
	}

}
