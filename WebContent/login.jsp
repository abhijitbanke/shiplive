
<%@ page language="java"  %>

<html>
  <head>
    <title>Login</title>
  </head>
<body>
  <h1>SAMPLE LOGIN</h1>


<%

// CONSTANT VALUES


String mac1="00:1C:C0:1A:96:59";
String mac2="00:1E:C9:4E:F2:1A";
String mac3="00:24:8C:AC:39:CA";
String mac4="00:50:56:C0:00:08";
String userId="admin";
String pass="admin";

String []macs={mac1,mac2,mac3,mac4};




// Read the form values
String 	user  = request.getParameter("user");
String password = request.getParameter("password");
String macValues = request.getParameter("macvalues").toUpperCase().replace('-',':');
//String [] macAddresses=macValues.split(";");
/*




int status=0;
for(int i=0;i<macAddresses.length;i++){

	
	if(macAddresses[i].equals(mac1) || macAddresses[i].equals(mac2)){
		status=10;
		break;
	}
}

if(status=10){
	if(user.equals(userId) && password.equals(pass))
		status=10;
	else
		status=5;
}
*/
boolean bMacAuthorized=false;
out.println("list of obtainted mac values "+macValues+"<br/><hr/>");

for(String mac: macs)
{
	out.println("comparing with "+mac+"<br/>");	
	if(macValues.contains(mac))
	{
		bMacAuthorized=true;
		out.println("<b>matched</b>");
	}
}

String messageText="";

if(!bMacAuthorized)
	messageText="Your system is not authorized. contact admin";
else if(!(user.equals(userId) && password.equals(pass)))
	messageText="Invalid username or password";
else
	messageText="Welcome!!!";



/*
String messageText="";
switch (status) {
  case 0:
    messageText = "You have no authorization !! ";
    break;
  case 5:
    messageText = "You entered the wrong username or password. ";
    break;
  case 10:
	messageText = "You have logged in successfully ";	    
    break;
  
}

}

*/
%>


<h3>
<%=messageText %>
</h3>
</body>
</html>