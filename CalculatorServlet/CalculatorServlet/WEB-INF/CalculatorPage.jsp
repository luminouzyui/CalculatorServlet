<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Calculator</title>
<script>
var b = "";
function addToText(sym){
	b = b + sym;
	document.getElementById("val").value = b;
}
</script>
<style>
input[type="button"]{
	width:40px;
}
</style>
</head>
<body>
	<form action="/CalculatorServlet/Calculate" method="post">
		<table>
			<tr><td colspan=6><input type='text' value='<%
			if(request.getAttribute("val")!=null) out.print(request.getAttribute("val"));
			%>' name='val' id='val' style="width:260px;text-align:right;background:lightgrey;foreground:black" readonly></td></tr>
			<tr>
				<td><input type='button' onclick='addToText("7")' value='7'></td>
				<td><input type='button' onclick='addToText("8")' value='8'></td>
				<td><input type='button' onclick='addToText("9")' value='9'></td>
				<td><input type='button' onclick='addToText("+")' value='+'></td>
				<td><input type='button' onclick='addToText("Sin")' value='Sin'></td>
				<td><input type='button' onclick='addToText("Sqrt")' value='Sqrt'></td>
			</tr>
			<tr>
				<td><input type='button' onclick='addToText("4")' value='4'></td>
				<td><input type='button' onclick='addToText("5")' value='5'></td>
				<td><input type='button' onclick='addToText("6")' value='6'></td>
				<td><input type='button' onclick='addToText("-")' value='-'></td>
				<td><input type='button' onclick='addToText("Cos")' value='Cos'></td>
				<td></td>
			</tr>
			<tr>
				<td><input type='button' onclick='addToText("1")' value='1'></td>
				<td><input type='button' onclick='addToText("2")' value='2'></td>
				<td><input type='button' onclick='addToText("3")' value='3'></td>
				<td><input type='button' onclick='addToText("*")' value='x'></td>
				<td><input type='button' onclick='addToText("Tan")' value='Tan'></td>
				<td></td>
			</tr>
			<tr>
				<td></td>
				<td><input type='button' onclick='addToText("0")' value='0'></td>
				<td></td>
				<td><input type='button' onclick='addToText("/")' value='/'></td>
				<td><input type='button' onclick='addToText("^")' value='X^y'></td>
				<td><input type='submit' value='='></td>
			</tr>
		</table>
	</form>
</body>
</html>