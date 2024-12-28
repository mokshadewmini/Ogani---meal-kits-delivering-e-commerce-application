
async function signUp(){
    
    const User_DTO = {
        fname:document.getElementById("firstName").value,
        lname:document.getElementById("lastName").value,
        email:document.getElementById("email").value,
        password:document.getElementById("password").value,
        
    };
    
    const response = await fetch(
            "SignUp",
    {
        method:"POST",
        body: JSON.stringify(User_DTO),
        headers:{
            "Content-Type":"application/json"
        }
    }
            );
    
    if(response.ok){
        const json = await response.json();
        if(json.success){ 
            window.location = "verify-account.html" 
        }else{
            document.getElementById("message").innerHTML = json.content;
        }
    }else{
         document.getElementById("message").innerHTML = "Please try again later!";
    } 
}




